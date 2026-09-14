# 🎒 校园闲置物品互助交换平台

基于 SpringBoot 3 + MyBatis + MySQL 8 + Vue 3 开发的校园闲置物品物物交换平台（不做金钱交易）。

## ✅ 技术栈

| 类别 | 技术 |
|------|------|
| 后端框架 | SpringBoot 3.2.5 |
| 持久层 | MyBatis + XML Mapper |
| 数据库 | MySQL 8 |
| 鉴权 | JWT (jjwt 0.11.5) |
| 前端 | Vue 3 (CDN引入) + 原生 HTML/CSS |
| JDK | JDK 17 |

## 📁 项目结构

```
barter/
├── pom.xml                          # Maven 依赖
├── sql/
│   └── init.sql                     # 数据库初始化脚本
├── src/main/java/com/barter/
│   ├── BarterApplication.java       # 启动类
│   ├── common/                      # 通用类
│   │   ├── Result.java              # 统一返回封装
│   │   ├── JwtUtil.java             # JWT 工具
│   │   └── GlobalExceptionHandler.java  # 全局异常处理
│   ├── config/                      # 配置类
│   │   ├── CorsConfig.java          # 跨域配置
│   │   ├── JwtInterceptor.java       # JWT 拦截器
│   │   ├── WebMvcConfig.java        # 拦截器注册
│   │   └── AiConfig.java            # AI 开关配置
│   ├── controller/                  # 控制层 (5个模块)
│   ├── service/                     # 服务层接口
│   ├── service/impl/                # 服务层实现
│   ├── mapper/                      # MyBatis 接口
│   ├── entity/                      # 实体类
│   └── dto/                         # 请求 DTO
├── src/main/resources/
│   ├── application.yml              # 应用配置
│   ├── mapper/*.xml                 # MyBatis XML 映射
│   └── static/                      # 前端静态页面
│       ├── css/style.css
│       ├── js/common.js
│       ├── login.html               # 登录/注册
│       ├── index.html               # 首页(物品列表)
│       ├── publish.html             # 发布闲置
│       └── my.html                  # 个人中心
```

## 🚀 启动步骤

### 1. 初始化数据库

打开 MySQL 客户端（Navicat / MySQL Workbench / 命令行），执行：

```sql
source E:/CodeSolution/Java/PracticalTraining/barter/sql/init.sql;
```

或直接运行 `sql/init.sql` 中的全部 SQL。这会自动：
- 创建数据库 `barter_db`
- 创建 5 张数据表
- 插入 4 条分类初始化数据
- 插入测试账号 `test / 123456`
- 插入 4 件测试闲置物品

### 2. 修改数据库密码

打开 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/barter_db?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=Asia/Shanghai
    username: root
    password: 你的MySQL密码
```

### 3. 启动项目

**方式一：IDEA 启动**
- 用 IDEA 打开项目，点击 `BarterApplication.java` 的绿色运行按钮

**方式二：命令行启动**
```bash
cd barter
mvn spring-boot:run
```

### 4. 访问

浏览器打开: http://localhost:8080/

| 页面 | 地址 | 说明 |
|------|------|------|
| 首页 | http://localhost:8080/index.html | 浏览物品、分类筛选 |
| 登录 | http://localhost:8080/login.html | 登录注册入口 |
| 发布 | http://localhost:8080/publish.html | 发布闲置物品 |
| 个人中心 | http://localhost:8080/my.html | 我的发布/申请/AI历史 |

**测试账号：**
- 账号: `test`
- 密码: `123456`

## 🤖 AI 助手开关

在 `application.yml` 中配置：

```yaml
ai:
  enable: false   # false=返回模拟回答，不依赖外部API
                  # true=真实调用（预留通义千问接口，当前同样返回模拟）
```

AI 系统提示词固定：
> 你是校园闲置物品交换平台助手，只回答物品发布、交换相关问题，可以帮助润色闲置物品描述，拒绝回答无关话题。

## 📡 API 接口清单

### 用户模块
| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | /api/user/register | 用户注册 | ❌ |
| POST | /api/user/login | 用户登录返回 token | ❌ |

### 分类模块
| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | /api/category/list | 查询全部分类 | ❌ |

### 闲置物品模块
| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | /api/item/add | 发布闲置物品 | ✅ |
| GET | /api/item/page | 分页查询（支持categoryId） | ❌ |
| PUT | /api/item/off/{id} | 下架自己的物品 | ✅ |
| GET | /api/item/my | 我的发布 | ✅ |

### 交换申请模块
| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | /api/exchange/apply | 发起交换申请 | ✅ |
| GET | /api/exchange/my | 我发起的申请 | ✅ |
| GET | /api/exchange/received | 我收到的申请 | ✅ |
| PUT | /api/exchange/handle | 处理申请(同意/拒绝) | ✅ |

### AI 助手模块
| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | /api/ai/chat | AI 对话 | ✅ |
| GET | /api/ai/record/list | AI 历史对话记录 | ✅ |

**需鉴权的接口**：请求头携带 `token: xxxxx`

## 🗃️ 数据库表

| 表名 | 说明 |
|------|------|
| sys_user | 用户表 |
| item_category | 物品分类表（4条初始化数据） |
| idle_item | 闲置物品表 |
| exchange_apply | 交换申请表 |
| ai_chat_record | AI 对话记录表 |

## ❌ 已排除功能（不实现）

- ❌ 图片上传
- ❌ 收藏、点赞
- ❌ 评论、留言板
- ❌ 支付、交易、金额
- ❌ 管理员前端页面
- ❌ 复杂流式 AI 输出

## ⚠️ 注意事项

1. 密码明文存储（课堂实训项目，生产环境请加密）
2. JWT 密钥硬编码（生产环境请外置配置）
3. AI 真实调用接口已预留（`AiChatServiceImpl.callRealAi`），后续接入 Spring-AI 或 HTTP 调用通义千问即可
4. 拦截器已配置部分接口放行（注册/登录/浏览分类和物品列表无需登录）

## 🛠️ Postman 测试示例

**登录：**
```
POST http://localhost:8080/api/user/login
Content-Type: application/json

{ "username": "test", "password": "123456" }
```
返回：`{ "code": 200, "data": { "token": "eyJhbG..." } }`

**后续请求携带 token：**
```
GET http://localhost:8080/api/item/page
Headers: token: eyJhbG...
```
