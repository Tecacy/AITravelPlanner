# AI Travel Planner

面向语音交互与 AI 行程生成的 Web 版旅行规划师。项目采用 **Spring Boot + Vue 3 + MySQL** 的前后端分离架构，并整合语音识别、地图服务与大语言模型以实现智能出行建议与预算管理。

## 仓库结构

```text
AITravelPlanner/
├── backend/      # Spring Boot 服务端，REST API / AI 调用 / 数据持久化
├── frontend/     # Vue 3 + Vite Web 客户端，地图与行程交互界面
└── README.md     # 项目说明
```

### 后端（backend）
- `config/`：安全配置、CORS 设置等。
- `controller/`：REST 接口入口。
- `dto/`：通用响应包装。
- `exception/`：全局异常处理。
- `resources/application.yml`：数据库、CORS、OpenAPI 配置占位。

### 前端（frontend）
- `src/router/`：路由配置，包含首页、行程规划、预算管理页面。
- `src/stores/`：Pinia 状态管理，示例用户/行程/预算 store。
- `src/components/`：地图、语音输入、行程摘要等 UI 模块。
- `src/services/`：Axios 实例、高德地图加载封装。
- `src/styles/`：全局样式与布局基准。

## 快速开始

### 1. 后端
```bash
cd backend
./mvnw spring-boot:run
```
> Windows 请使用 `mvnw.cmd`。

运行前请在 `application.yml` 中设置 MySQL 连接，或通过环境变量覆盖：

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `CORS_ALLOWED_ORIGINS`

### 2. 前端
```bash
cd frontend
npm install
npm run dev
```

需要 Node.js 18+ 环境。若需要地图功能，请在运行前设置：

```
VITE_AMAP_KEY=你的高德 Web Key
VITE_AMAP_SECRET=安全码（若已启用安全设置）
```

## 下一步计划
- 接入科大讯飞或其他语音识别 API，实现真实语音转写。
- 集成大语言模型生成行程（Function Calling，结构化输出）。
- 结合高德/百度地图服务获取 POI、路线、导航能力。
- 与 Supabase/Firebase 认证、云同步打通，实现多终端共享。
- 丰富 UI/UX，并增加测试、CI/CD 流程。

欢迎继续扩展或指定模块深化，我们将协助完善。*** End Patch