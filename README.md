# 🎒 校园闲置物品互助交换平台

基于 SpringBoot 3 + MyBatis + MySQL 8 + Vue 3 开发的校园闲置物品物物交换平台（不做金钱交易）。

**核心特色：** 用户/管理员双账号体系、管理员独立登录入口、物品发布审核机制、图片文件上传、用户实时聊天、交易时间地点双方确认、消息通知、评价/举报/收藏。

## ✅ 技术栈

| 类别 | 技术 |
|------|------|
| 后端框架 | SpringBoot 3.2.5 |
| 持久层 | MyBatis + XML Mapper |
| 数据库 | MySQL 8 |
| 鉴权 | JWT (jjwt 0.11.5) + BCrypt 密码加密 |
| 前端 | Vue 3（本地引入 js/vue.global.js）+ 原生 HTML/CSS |
| AI | DeepSeek API（支持开关降级） |
| JDK | JDK 17 |

## � 安全设计

- **密码加密**：BCrypt 存储，注册/登录/重置全部走 `BCryptPasswordEncoder`
- **JWT 密钥外置**：通过环境变量 `JWT_SECRET` 注入，不硬编码
- **敏感信息环境变量化**：`DB_PASSWORD`、`DEEPSEEK_API_KEY` 等
- **双登录入口**：普通用户 `/login.html`、管理员 `/admin-login.html`，管理员接口独立鉴权（AdminAuthInterceptor）
- **标准鉴权头**：`Authorization: Bearer <token>`

## �📁 项目结构

```
barter/
├── pom.xml                          # Maven 依赖
├── sql/
│   ├── init.sql                     # 数据库初始化脚本（10张表）
│   └── migration_*.sql              # 增量迁移脚本（聊天/交易确认等）
├── src/main/java/com/barter/
│   ├── BarterApplication.java       # 启动类
│   ├── common/                      # Result / JwtUtil / BusinessException / GlobalExceptionHandler
│   ├── config/                      # JwtInterceptor / AdminAuthInterceptor / WebMvcConfig /
│   │                                # PasswordEncoderConfig / RestTemplateConfig / DataInitializer
│   ├── controller/                  # 控制层（11个模块）
│   ├── service/ + service/impl/     # 服务层
│   ├── mapper/                      # MyBatis 接口
│   ├── entity/                      # 实体类
│   ├── dto/                         # 请求 DTO
│   └── vo/                          # 视图 VO（IdleItemVO / ExchangeApplyVO）
├── src/main/resources/
│   ├── application.yml              # 应用配置
│   ├── mapper/*.xml                 # MyBatis XML 映射
│   └── static/                      # 前端静态页面
│       ├── css/style.css
│       ├── js/common.js             # 请求封装(Authorization Bearer) + 工具函数
│       ├── js/vue.global.js         # Vue 3 本地文件
│       ├── login.html               # 用户登录/注册/忘记密码
│       ├── admin-login.html         # 管理员独立登录页
│       ├── index.html               # 首页（搜索/分类/成色/校区筛选、收藏、举报）
│       ├── publish.html             # 发布闲置（图片选择上传、成色、校区）
│       ├── my.html                  # 个人中心（物品/申请/交易确认/聊天/收藏/AI）
│       └── admin.html               # 管理后台（用户/物品/审核/申请/举报/统计）
```

## 🚀 启动步骤

### 1. 初始化数据库

打开 MySQL 客户端，**按顺序**执行：

```sql
source sql/init.sql;                                   -- 1. 建库建表
source sql/migration_20260915_chat_and_trade.sql;      -- 2. 聊天 + 交易时间地点
source sql/migration_20260915_dual_confirm.sql;        -- 3. 双方确认完成
source sql/migration_20260915_trade_confirm.sql;       -- 4. 交易信息确认制
```

init.sql 会自动创建数据库 `barter_db`、10 张表、分类初始化数据和测试物品。**测试账号由应用首次启动时自动创建**（DataInitializer，BCrypt 加密）：
- 普通用户：`test / 123456`
- 管理员：`admin / admin123`

### 2. 配置（可选）

`application.yml` 已内置开发默认值，生产环境建议通过环境变量覆盖：

| 环境变量 | 说明 | 默认值 |
|---------|------|--------|
| `DB_PASSWORD` | MySQL 密码 | root |
| `JWT_SECRET` | JWT 签名密钥 | 内置开发密钥 |
| `DEEPSEEK_API_KEY` | DeepSeek API Key | 空（AI 降级为模拟回答） |
| `AI_ENABLED` | AI 开关 true/false | true |
| `UPLOAD_PATH` | 图片上传目录 | ./uploads |

### 3. 启动项目

**方式一：IDEA 启动** —— 运行 `BarterApplication.java`

**方式二：命令行启动**
```bash
mvn spring-boot:run
```

### 4. 访问（端口 8081）

| 页面 | 地址 | 说明 |
|------|------|------|
| 首页 | http://localhost:8081/index.html | 浏览物品、关键词/分类/成色/校区筛选 |
| 用户登录 | http://localhost:8081/login.html | 登录/注册/忘记密码（含管理员入口链接） |
| 管理员登录 | http://localhost:8081/admin-login.html | 管理员专用登录 |
| 发布 | http://localhost:8081/publish.html | 发布闲置（图片文件上传） |
| 个人中心 | http://localhost:8081/my.html | 我的发布/申请/聊天/收藏/AI |
| 管理后台 | http://localhost:8081/admin.html | 用户/物品/审核/申请/举报管理 |

## 🔄 核心业务流程

### 物品发布审核流
```
用户发布（audit_status=0 待审核，市场不可见）
  → 管理员后台审核（通过=1 上架展示 / 驳回=2 附备注）
  → 用户可在"我的发布"查看审核状态
```

### 交换申请与双方确认流
```
申请人发起申请(0待处理)
  → 发布者 同意(1) / 拒绝(2)
  → 双方约定交易时间地点：
      一方填写 → trade_status=1 待对方确认
      对方 同意 → trade_status=2 双方已确认（锁定）
      对方 反对 → 填写自己的方案，回到对方确认（可循环）
  → 双方各自点击"我确认完成"（apply_confirmed / owner_confirmed）
  → 双方都确认后 → 申请状态=3 已完成，物品状态=2 已交换，双方可互评
```

### 消息通知
交换申请、同意/拒绝、交易信息待确认、确认完成等关键节点自动推送站内通知。

## 📡 API 接口清单

> 鉴权接口请求头携带 `Authorization: Bearer <token>`

### 用户模块
| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | /api/user/register | 用户注册（含学号/院系） | ❌ |
| POST | /api/user/login | 用户登录返回 token | ❌ |
| POST | /api/user/resetPassword | 忘记密码（重置为 123456） | ❌ |

### 分类 / 物品模块
| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | /api/category/list | 查询全部分类 | ❌ |
| POST | /api/item/add | 发布闲置物品（待审核） | ✅ |
| GET | /api/item/page | 分页查询（keyword/categoryId/itemCondition/campus） | ❌ |
| GET | /api/item/detail/{id} | 物品详情（浏览量+1） | ❌ |
| PUT | /api/item/off/{id} | 下架自己的物品 | ✅ |
| GET | /api/item/my | 我的发布 | ✅ |

### 交换申请模块
| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | /api/exchange/apply | 发起交换申请 | ✅ |
| GET | /api/exchange/my | 我发起的申请 | ✅ |
| GET | /api/exchange/received | 我收到的申请 | ✅ |
| PUT | /api/exchange/handle | 发布者处理申请(同意/拒绝) | ✅ |
| PUT | /api/exchange/confirm/{id} | 双方各自确认交换完成 | ✅ |
| PUT | /api/exchange/trade/{id} | 填写/反提议交易时间地点 | ✅ |
| PUT | /api/exchange/trade/confirm/{id} | 确认对方提出的交易信息 | ✅ |

### 用户聊天模块
| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | /api/chat/send | 发送消息 | ✅ |
| GET | /api/chat/conversation/{otherUserId} | 双向聊天记录（自动已读） | ✅ |
| GET | /api/chat/unread/count | 未读消息数 | ✅ |

### 通知 / 评价 / 举报 / 收藏
| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | /api/notification/list | 我的通知列表 | ✅ |
| GET | /api/notification/unread-count | 未读通知数 | ✅ |
| PUT | /api/notification/read/{id} | 标记已读 | ✅ |
| PUT | /api/notification/read-all | 全部已读 | ✅ |
| POST | /api/review/add | 交换完成后互评 | ✅ |
| GET | /api/review/user/{userId} | 查某用户的评价 | ❌ |
| GET | /api/review/rating/{userId} | 某用户平均分 | ❌ |
| POST | /api/report/add | 举报物品/用户 | ✅ |
| POST | /api/favorite/add/{itemId} | 收藏物品 | ✅ |
| DELETE | /api/favorite/remove/{itemId} | 取消收藏 | ✅ |
| GET | /api/favorite/my | 我的收藏列表 | ✅ |

### 文件上传 / AI 助手
| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | /api/upload/image | 上传图片（≤5MB，jpg/png/gif/webp） | ✅ |
| POST | /api/ai/chat | AI 对话 | ✅ |
| GET | /api/ai/record/list | AI 历史对话记录 | ✅ |

### 管理端（独立鉴权，userType=admin）
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/admin/login | 管理员登录 |
| GET | /api/admin/user/list | 用户列表 |
| DELETE | /api/admin/user/delete/{id} | 删除用户 |
| PUT | /api/admin/user/reset-pwd | 重置用户密码 |
| PUT | /api/admin/user/status/{id}/{status} | 封禁/解封用户 |
| GET | /api/admin/item/list | 物品列表 |
| PUT | /api/admin/item/status | 上下架物品 |
| PUT | /api/admin/item/audit/{id}/{status} | **审核物品**(通过/驳回) |
| DELETE | /api/admin/item/delete/{id} | 删除物品 |
| GET | /api/admin/apply/list | 交换申请列表 |
| GET | /api/admin/report/list | 举报列表 |
| PUT | /api/admin/report/handle/{id}/{status} | 处理举报 |
| GET | /api/admin/stats | 数据统计（COUNT 查询） |

## �️ 数据库表

| 表名 | 说明 |
|------|------|
| sys_user | 用户表（学号/院系/头像/状态，无 role 字段） |
| admin_user | 管理员表（独立于用户表） |
| item_category | 物品分类表（icon/parentId） |
| idle_item | 闲置物品表（images/itemCondition/campus/viewCount/auditStatus） |
| exchange_apply | 交换申请表（ownerUserId/tradeTime/tradeLocation/tradeStatus/applyConfirmed/ownerConfirmed） |
| chat_message | 用户聊天消息表 |
| notification | 站内通知表 |
| review | 评价表（1-5星） |
| report | 举报表 |
| favorite | 收藏表 |
| ai_chat_record | AI 对话记录表 |

## 🤖 AI 助手

```yaml
ai:
  enable: true            # false=返回本地模拟回答，不依赖外部API
  deepseek:
    api-key: ${DEEPSEEK_API_KEY:}   # 环境变量注入
    model: deepseek-chat
```

AI 系统提示词固定：
> 你是校园闲置物品交换平台助手，只回答物品发布、交换相关问题，可以帮助润色闲置物品描述，拒绝回答无关话题。

## ⚠️ 注意事项

1. 数据库更新**只加增量迁移文件**（`sql/migration_日期_xxx.sql`），不修改 init.sql 已发布内容
2. 图片上传后存储在 `./uploads/yyyyMMdd/` 目录，重启不丢失，可通过 `UPLOAD_PATH` 迁移
3. 生产环境务必更换 `JWT_SECRET`，并设置强 `DB_PASSWORD`
4. 忘记密码为课堂简化实现（直接重置为 123456），生产环境应改为邮件/短信验证
