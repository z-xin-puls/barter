package com.barter.service.impl;

import com.barter.config.AiConfig;
import com.barter.entity.AiChatRecord;
import com.barter.mapper.AiChatRecordMapper;
import com.barter.service.AiChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Pattern;

/**
 * AI对话服务实现
 */
@Slf4j
@Service
public class AiChatServiceImpl implements AiChatService {

    @Autowired
    private AiConfig aiConfig;

    @Autowired
    private AiChatRecordMapper recordMapper;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 系统提示词（固定，需求文档要求）
    private static final String SYSTEM_PROMPT = "你是校园闲置物品交换平台助手，只回答物品发布、交换相关问题，可以帮助润色闲置物品描述，拒绝回答无关话题。";

    // 关键词：平台相关（降级模式用）
    private static final String[] RELATED_KEYWORDS = {
            "物品", "闲置", "交换", "发布", "分类", "申请", "下架",
            "教材", "数码", "运动", "生活", "希望", "留言",
            "怎么", "如何", "帮忙", "润色", "描述",
            "注册", "登录", "使用", "操作", "平台"
    };

    @Override
    public String chat(Long userId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("请输入您的问题");
        }

        String aiContent;
        if (aiConfig.isEnable()) {
            // 真实 AI 模式 —— 调用 DeepSeek V4 Flash
            try {
                aiContent = callDeepSeek(content);
            } catch (Exception e) {
                log.error("调用 DeepSeek 失败，降级返回模拟回答", e);
                aiContent = generateMockReply(content);
            }
        } else {
            // 模拟 AI 模式（降级）
            aiContent = generateMockReply(content);
        }

        // 保存对话记录
        AiChatRecord record = new AiChatRecord();
        record.setUserId(userId);
        record.setUserContent(content);
        record.setAiContent(aiContent);
        recordMapper.insert(record);

        return aiContent;
    }

    @Override
    public List<AiChatRecord> history(Long userId) {
        return recordMapper.selectByUserId(userId);
    }

    /**
     * 调用 DeepSeek V4 Flash API（OpenAI Chat Completions 兼容格式）
     */
    private String callDeepSeek(String userContent) throws Exception {
        AiConfig.Deepseek ds = aiConfig.getDeepseek();
        String url = ds.getBaseUrl() + "/v1/chat/completions";

        // 1. 构造请求体
        Map<String, Object> body = new HashMap<>();
        body.put("model", ds.getModel());
        body.put("max_tokens", 1024);

        // messages 数组：system + user
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> sysMsg = new HashMap<>();
        sysMsg.put("role", "system");
        sysMsg.put("content", SYSTEM_PROMPT);
        messages.add(sysMsg);

        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userContent);
        messages.add(userMsg);

        body.put("messages", messages);

        // 2. 构造请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + ds.getApiKey());

        // 3. 发送请求
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        // 4. 解析响应 JSON
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode choices = root.get("choices");
            if (choices != null && choices.isArray() && !choices.isEmpty()) {
                JsonNode contentNode = choices.get(0).get("message").get("content");
                if (contentNode != null) {
                    String reply = contentNode.asText().trim();
                    if (!reply.isEmpty()) {
                        return reply;
                    }
                }
            }
        }

        throw new RuntimeException("DeepSeek 返回内容为空");
    }

    // ============ 以下为本地模拟回答（降级模式使用） ============

    /**
     * 生成模拟回答
     */
    private String generateMockReply(String userContent) {
        // 判断是否为平台相关问题
        boolean isRelated = false;
        for (String keyword : RELATED_KEYWORDS) {
            if (userContent.contains(keyword)) {
                isRelated = true;
                break;
            }
        }

        if (!isRelated) {
            return "抱歉，我是校园闲置物品交换平台助手，只能回答物品发布、交换相关问题，无法解答您这个无关问题哦~";
        }

        // 物品描述润色
        if (userContent.contains("润色") || userContent.contains("描述") || userContent.contains("优化")) {
            return generatePolishReply(userContent);
        }

        // 发布物品相关
        if (userContent.contains("发布") || userContent.contains("怎么发")) {
            return "发布闲置物品步骤：\n1. 登录后进入「发布闲置」页面\n2. 选择物品分类（教材书籍/数码产品/运动器材/生活用品）\n3. 填写物品名称、详细描述（成色、使用情况等）\n4. 填写期望交换的物品\n5. 点击提交即可发布";
        }

        // 交换申请相关
        if (userContent.contains("交换") || userContent.contains("申请")) {
            return "发起交换申请步骤：\n1. 在首页浏览到心仪的闲置物品\n2. 点击进入物品详情\n3. 填写留言告诉物品发布者你想交换什么\n4. 提交申请后等待发布者同意或拒绝\n5. 同意后物品状态变为「已交换完成」";
        }

        // 分类相关
        if (userContent.contains("分类")) {
            return "平台目前有4个物品分类：\n1. 教材书籍 —— 课本、教辅、笔记等\n2. 数码产品 —— 手机、电脑、耳机等\n3. 运动器材 —— 球拍、球鞋、健身器材等\n4. 生活用品 —— 宿舍好物、小家电等";
        }

        // 下架相关
        if (userContent.contains("下架")) {
            return "下架自己发布的物品：\n进入「个人中心 → 我的发布」，找到想要下架的物品，点击「下架」按钮即可。下架后其他用户将无法看到该物品。";
        }

        // 默认回复
        return "您好！我是校园闲置物品交换平台助手，可以帮您：\n• 润色闲置物品描述\n• 解答物品发布、交换相关问题\n• 介绍平台功能和使用方法\n\n请问有什么可以帮您的？";
    }

    /**
     * 润色描述回复
     */
    private String generatePolishReply(String content) {
        String descPart = content;
        Pattern p = Pattern.compile("[\"「:：](.+?)[\"」]");
        var matcher = p.matcher(content);
        if (matcher.find()) {
            descPart = matcher.group(1);
        }

        return "好的！以下是为您润色后的物品描述，更加吸引人哦~\n\n【原版】" + descPart + "\n\n【润色版】\n📚 物品名称：（请填写）\n✨ 成色描述：保存完好/轻微使用痕迹\n🔧 使用情况：功能正常/配件齐全\n📦 期望交换：（请填写希望交换的物品）\n\n您可以根据实际情况调整以上描述！";
    }
}
