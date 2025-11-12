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
```powershell
cd backend
.\mvnw.cmd spring-boot:run
```
> 默认使用内存数据库 H2，可通过 `http://localhost:8080/h2-console` 查看数据。
> 若已在系统中安装 Maven，也可改用 `mvn spring-boot:run`。

如需切换到 MySQL，请在 `application.yml` 中改写 `spring.datasource` 配置，并提供实际的连接参数。

### AI 与语音占位实现
- `POST /api/ai/trip-plans`：使用占位的大语言模型服务，根据用户输入生成示例行程结构。
- `POST /api/voice/transcribe`：语音转写接口，当前返回模拟文本，后续可接入科大讯飞等服务。

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
VITE_BMAP_AK=你的百度地图AK
```

> 也可在前端页面右上角的「API Key 设置」中输入百度地图 AK（及 LLM、语音等 Key），密钥会保存在浏览器本地。可选地，复制 `frontend/public/config/api-keys.example.json` 为 `api-keys.json`，在文件中填入 Key 以便默认加载。

## API Key 配置方式
- **前端设置面板（推荐）**：运行后点击页面右上角「API Key 设置」，填写百度地图 / LLM / 语音等 Key，数据仅保存在浏览器 `localStorage`，刷新后生效。
- **环境变量**：在 `frontend/.env.local`（已被 `.gitignore` 忽略）中写入 `VITE_BMAP_AK=xxx` 等变量；也可通过命令行 `export` / `set` 临时设置。
- **配置文件**：复制 `frontend/public/config/api-keys.example.json` 为 `frontend/public/config/api-keys.json`，填入密钥后重新刷新页面，系统会在首次加载时读取该文件，并仅在本地使用。

## 使用 Docker 运行

- 构建镜像：
  - `docker build -t ai-travel-planner:latest .`
- 以 H2 内存库运行（默认）：
  - `docker run --name ai-travel-planner -p 8080:8080 ai-travel-planner:latest`
- 访问：
  - Web 前端 `http://localhost:8080`
  - API 文档 `http://localhost:8080/docs`
  - H2 Console `http://localhost:8080/h2-console`

### 切换到 MySQL
- 运行时通过环境变量覆盖后端配置：
  - `docker run -p 8080:8080 \`
    `-e DB_URL="jdbc:mysql://<host>:3306/travel?useSSL=false&serverTimezone=UTC" \`
    `-e DB_USERNAME="root" -e DB_PASSWORD="secret" \`
    `ai-travel-planner:latest`

### 导出为可直接下载运行的镜像文件（.tar）
- 导出镜像：
  - `docker save -o ai-travel-planner-image.tar ai-travel-planner:latest`
- 在另一台机器加载并运行：
  - `docker load -i ai-travel-planner-image.tar`
  - `docker run --name ai-travel-planner -p 8080:8080 ai-travel-planner:latest`


欢迎继续扩展或指定模块深化，我们将协助完善。
