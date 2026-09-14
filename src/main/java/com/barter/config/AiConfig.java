package com.barter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AI 配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AiConfig {

    /** 是否启用真实AI（true=调用 DeepSeek，false=本地模拟） */
    private boolean enable = true;

    /** DeepSeek 配置 */
    private Deepseek deepseek = new Deepseek();

    @Data
    public static class Deepseek {
        /** API 基础地址 */
        private String baseUrl = "https://api.deepseek.com";
        /** API Key */
        private String apiKey;
        /** 模型名：deepseek-v4-flash / deepseek-v4-pro */
        private String model = "deepseek-v4-flash";
    }
}
