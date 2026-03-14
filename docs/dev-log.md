# LinkPet 项目开发日志

> 记录 LinkPet 宠物领养平台从项目搭建到功能迭代的完整开发过程。
> 开发者：武赟 / 武振川
> 技术栈：Spring Boot 3 + MyBatis-Plus + Spring Security + JWT + MinIO + Vue 3 + Vite + Axios

---

## 2026-03-11

### 🏗️ 项目初始化与基础架构搭建

> Commit: `baafcec` — minio下载完成

项目从零搭建，一次性提交了完整的多模块 Maven 工程和全部基础代码，共 **100 个文件，4143 行新增代码**。

- 📄 涉及文件：`pom.xml`（父工程及 5 个子模块）、全部 Entity / DTO / VO / Mapper / Service / Controller 类

- 🛠️ 工程结构：
  - `linkpet-common` — 公共模块：`JwtUtil`（JWT 签发与解析）、`MinioUtil`（文件上传至 MinIO）、`BaseContext`（ThreadLocal 存储当前用户 ID）、`Result` / `PageResult`（统一响应封装）、`GlobalExceptionHandler`（全局异常拦截）
  - `linkpet-model` — 数据模型：6 个实体类（`User`、`Pet`、`PetType`、`Post`、`Comment`、`AdoptionApply`、`UserAction`），配套 DTO 和 VO
  - `linkpet-mapper` — 持久层：7 个 MyBatis-Plus Mapper 接口 + XML 映射文件，`PetMapper.xml` 中编写了 `selectPetPage` 分页查询 SQL（LEFT JOIN `pet_type` 和 `user` 表获取 `typeName`、`userNickname`）
  - `linkpet-service` — 业务层：`PetService`、`PostService`、`UserService`、`AdoptionService`、`CommentService`、`PetTypeService` 及其完整实现
  - `linkpet-web-app` — 用户端 API（端口 8080）：包含 `AuthController`（登录注册）、`PetController`（宠物 CRUD）、`PostController`（帖子 CRUD + 点赞）、`CommentController`（评论）、`AdoptionController`（领养申请）、`PetTypeController`（品种列表）、`UploadController`（图片上传）
  - `linkpet-web-admin` — 管理端 API（独立端口）：`AdminPetController`、`AdminPostController`、`AdminPetTypeController`、`AdminUserController`、`AdminAdoptionController`、`AdminAuthController`

- 🧠 业务逻辑：
  - **认证体系**：用户端和管理端各自独立的 `SecurityConfig` + `JwtAuthenticationFilter`，拦截请求头中的 `Bearer` Token，解析后注入 `SecurityContextHolder`。管理端登录 `AdminAuthController.login()` 额外校验 `role == ROLE_ADMIN`
  - **宠物发布流程**：`PetServiceImpl.add()` 通过 `BaseContext.getCurrentId()` 获取当前用户，设置 `status = PET_AVAILABLE(1)` 后 `petMapper.insert()`
  - **领养申请**：`AdoptionServiceImpl.apply()` 先查重（同用户不能重复申请同一宠物），再插入 `adoption_apply` 记录，状态初始为 `ADOPTION_PENDING(0)`
  - **帖子点赞**：`PostServiceImpl.toggleLike()` 查询 `user_action` 表判断是否已点赞，已赞则 `deleteById` + `likeCount - 1`，未赞则 `insert` + `likeCount + 1`，整体使用 `@Transactional` 保证原子性

---

### 🔧 修复项目启动问题

> Commit: `0e5358a` — 项目可以运行

- 📄 涉及文件：`pom.xml`、`Pet.java`、`Post.java`、`PetDTO.java`、`PostDTO.java`、`PetVO.java`、`PostVO.java`、`PetMapper.xml`、`JwtUtil.java`、`AppApplication.java`、`AdminApplication.java`

- 🛠️ 方法与接口：
  - `JwtUtil` 中 `var builder` 改为显式类型 `JwtBuilder builder`，解决编译兼容问题
  - 启动类添加 `@MapperScan("com.wzc.linkpet.mapper")`，修复 Mapper 无法注入的问题
  - MyBatis-Plus 依赖从 `mybatis-plus-boot-starter` 改为 `mybatis-plus-spring-boot3-starter`，适配 Spring Boot 3

- 🧠 业务逻辑：
  - **图片存储结构调整**：`Pet.images` 和 `Post.images` 字段从 `String`（逗号分隔）改为 `List<String>`（JSON 数组），实体上添加 `@TableField(typeHandler = JacksonTypeHandler.class)`，表注解改为 `@TableName(value = "pet", autoResultMap = true)` 以支持自动 JSON 反序列化
  - `PetMapper.xml` 中 `images` 列添加 `typeHandler="com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler"` 对应处理
  - `Pet` 实体新增 `location` 字段（发现地点，适用于流浪动物），`PetDTO` 同步新增该字段
  - 删除根模块下无用的 `LinkPetApplication.java` 和 `application.properties`

---

## 2026-03-13

### 📦 前端工程与管理端认证上线

> Commit: `143683d` — 上传前端
> Commit: `136f30c` — add linkpet-frontend, docs, db.sql and other untracked files

- 📄 涉及文件：`linkpet-frontend/` 整个目录（34 个文件，4649 行）、`db.sql`、`docs/issues.md`、`AdminAuthController.java`

- 🛠️ 方法与接口：
  - 前端 API 层：`pets.js`（`getPets` / `getPetById` / `createPet` / `updatePet` / `deletePet`）、`posts.js`（`getPosts` / `createPost` / `deletePost` / `toggleLike`）、`adoptions.js`（`applyAdoption` / `getMyAdoptions`）、`auth.js`（`login` / `register`）、`petTypes.js`（`getPetTypes`）、`upload.js`（`uploadImage`）
  - HTTP 客户端 `http.js`：Axios 实例，请求拦截器自动注入 `Bearer Token`，响应拦截器解包 `{ code, message, data }` 结构，401 时自动清除 `localStorage`
  - 路由 `router/index.js`：初始仅 `/` 首页路由
  - 状态管理 `store/user.js`：基于 `reactive` + `localStorage` 的用户状态管理
  - 数据格式化 `format.js`：`formatPet()` 将后端 `PetVO` 转为 `PetCard` 组件 props（月龄转可读文本、首张图片拼接 MinIO 地址、性格描述拆分为 tags）；`formatPost()` 将 `PostVO` 转为社区卡片格式
  - `AdminAuthController.login()` — 管理端登录接口，校验 `ROLE_ADMIN` 角色后签发 JWT

- 🧠 业务逻辑：
  - **前端首页**为单页应用，包含 `AppNavbar`（导航栏 + 登录/注册弹窗）、`HeroSection`（Hero 区 + 统计栏）、`AdoptionSection`（品种筛选 + 宠物卡片网格）、`CommunitySection`（社区帖子 + 大图特色卡片布局）、`RescueBanner`（救助指南）、`AppFooter`
  - `AdoptionSection` 在 `onMounted` 时先调 `getPetTypes()` 构建筛选 tab，再调 `getPets()` 加载宠物列表，API 失败时 fallback 到 `mockData.js` 中的模拟数据
  - `AppNavbar` 实现了完整的登录/注册弹窗，登录成功后将 `token` 和 `userInfo` 写入 `localStorage`，并显示用户头像和昵称
  - `db.sql` 包含完整的建表语句（`user`、`pet_type`、`pet`、`post`、`comment`、`user_action`、`adoption_apply`），并插入默认品种分类

---

### ⚙️ 项目配置优化

> Commit: `c375821` — update gitignore and settings
> Commit: `bb5d5f2` — Merge pull request #1

- 📄 涉及文件：`.gitignore`、`.claude/settings.json`、`package-lock.json`
- 🧠 业务逻辑：补充 `.gitignore` 规则，合并 Claude 辅助开发分支 `claude/nervous-cohen` 的首次 PR

---

## 2026-03-14

### 🎨 首页 UI 精细化调整（系列优化）

#### 缩小区块间距与宠物卡片尺寸

> Commit: `f36b50e` — 缩小首页各区块间距及宠物卡片尺寸，适配数据字段

- 📄 涉及文件：`variables.css`、`HeroSection.vue`、`AdoptionSection.vue`、`PetCard.vue`、`format.js`

- 🛠️ 方法与接口：
  - `formatPet()` 中 `tags` 数据源从 `pet.personalityDesc` 改为 `pet.description`，`desc` 从 `healthDesc || personalityDesc` 改为 `description`，适配后端字段合并

- 🧠 业务逻辑：
  - `.section` 全局 padding 从 `6rem` 缩减至 `3.5rem`，header margin 从 `3.5rem` 缩至 `2rem`
  - `HeroSection` 的 `min-height` 从 `100vh` 改为 `85vh`，上下 padding 缩小
  - 宠物卡片图片高度从 `220px` 缩至 `120px`，卡片圆角、badge、收藏按钮尺寸同步缩小
  - 宠物网格 `grid-template-columns` 最小列宽从 `260px` 缩至 `200px`，间距从 `2rem` 缩至 `1rem`

#### 简化宠物卡片为图片+名称

> Commit: `dfeb423` — 简化宠物卡片为图片+名称，删除浏览所有宠物按钮

- 📄 涉及文件：`PetCard.vue`、`AdoptionSection.vue`

- 🧠 业务逻辑：
  - `PetCard` 卡片体从完整信息（名称、年龄、地点、tags、描述、View Details 按钮）**大幅精简为仅显示名称**，移除约 100 行模板和样式代码
  - 卡片添加 `cursor: pointer`，暗示可点击交互
  - 图片高度回调至 `140px`，hover 效果从旋转变为纯平移 `translateY(-6px)`
  - 名称添加 `text-overflow: ellipsis` 单行截断
  - 删除 `AdoptionSection` 底部的 "Browse All Available Pets" 按钮

#### 拆分单页为独立路由页面

> Commit: `fca875b` — 拆分单页为独立路由页面，取消滚动浏览

- 📄 涉及文件：`App.vue`、`router/index.js`、`home/index.vue`、`AppNavbar.vue`、`HeroSection.vue`、`AdoptionSection.vue`、`CommunitySection.vue`、`RescueBanner.vue`，新建 `adoption/index.vue`、`community/index.vue`、`guide/index.vue`

- 🛠️ 方法与接口：
  - `router/index.js` 新增三条路由：`/adoption`（领养中心）、`/community`（宠物社区）、`/guide`（救助指南）
  - `App.vue` 重构：将 `AppNavbar` 和 `AppFooter` 提升到 `App.vue` 全局层级，`<RouterView />` 包裹在 `<main class="page-main">` 中

- 🧠 业务逻辑：
  - **架构转型**：从单页滚动浏览模式拆分为多个独立路由页面，每个页面复用原有 Section 组件
  - `AppNavbar` 中所有 `<a href="#section">` 锚点链接改为 `<router-link to="/path">`，支持 `active-class="nav-link--active"` 高亮
  - `home/index.vue` 精简为仅渲染 `HeroSection`（其他内容各自在独立路由中）
  - 首页 Hero 的 `min-height` 改为 `calc(100vh - 70px)` 填满视口
  - 各 Section 移除 `id` 属性和 `brush-divider` 分隔线（不再需要锚点定位）
  - `RescueBanner` 新增 `min-height: calc(100vh - 70px)` 使其填满页面

#### Footer 仅在救助指南页显示，社区卡片简化

> Commit: `3873344` — Footer仅在救助指南页显示，社区卡片简化为网格小卡片

- 📄 涉及文件：`App.vue`、`CommunitySection.vue`

- 🛠️ 方法与接口：
  - `App.vue` 中引入 `useRoute()` + `computed`，通过 `showFooter = computed(() => route.path === '/guide')` 条件渲染 `AppFooter`
  - `CommunitySection` 完全重构：移除 `StoryCard` 组件引用

- 🧠 业务逻辑：
  - 社区页面从 **大图特色 + 侧边卡片列表** 的复杂双栏布局，重构为与领养中心一致的 **均匀网格小卡片** 布局（`grid-template-columns: repeat(auto-fill, minmax(200px, 1fr))`）
  - 每张帖子卡片仅显示封面图 + 标题 + 作者头像和昵称，移除摘要、标签、阅读按钮、轮播点
  - 加载数量从 `pageSize: 4` 改为 `pageSize: 8`
  - `AppFooter` 仅在 `/guide` 页面显示

#### 首页禁止滚动，Hero 内容压缩至一屏

> Commit: `f4f1b9b` — 首页禁止滚动，Hero内容压缩至一屏显示

- 📄 涉及文件：`App.vue`、`HeroSection.vue`

- 🛠️ 方法与接口：
  - `App.vue` 新增 `noScroll` 计算属性，当 `route.path === '/'` 时给 `.page-main` 添加 `--no-scroll` 修饰类
  - `.page-main--no-scroll` 设置 `height: calc(100vh - 70px); overflow: hidden`

- 🧠 业务逻辑：
  - 首页 Hero 强制一屏显示，禁止滚动溢出
  - Hero 内各元素间距系统性缩小（eyebrow `margin-bottom: 0.8rem`，标题 `0.8rem`，描述 `margin: 0 auto 1.5rem`，CTA 按钮 `margin-bottom: 1.5rem`）

#### 修复首页统计栏被截断和底部背景色不一致

> Commit: `20a901a` — 修复首页统计栏被截断和底部背景色不一致

- 📄 涉及文件：`App.vue`、`HeroSection.vue`

- 🧠 业务逻辑：
  - 统计栏 `.hero__stats` 的 `gap` 从 `2rem` 缩至 `1.5rem`，`padding` 从 `1.2rem 2.5rem` 缩至 `0.8rem 2rem`
  - 数字字号从 `1.5rem` 缩至 `1.3rem`，分隔线高度从 `36px` 缩至 `28px`
  - 描述文字 `margin-bottom` 进一步缩至 `1rem`
  - `page-main--no-scroll` 删除多余的 `max-height` 属性，添加 `background: transparent`

#### 修复首页底部背景色差

> Commit: `45edd78` — 修复首页底部背景色差：Hero添加兜底背景色

- 📄 涉及文件：`App.vue`、`HeroSection.vue`

- 🧠 业务逻辑：
  - `.hero` 添加 `background: linear-gradient(160deg, #DEB87A 0%, #C9965A 35%, #8FA878 65%, #C4A455 100%)` 兜底背景色，解决因 `overflow: hidden` 截断后露出白色底色的问题
  - `page-main--no-scroll` 中 `background: transparent` 改为 `min-height: auto`，统一逻辑

---

### ✨ 新增：首页数字动态化与全局装饰背景

> Commit: `1ff5609` — 首页数字动态化

- 📄 涉及文件：`App.vue`、`variables.css`、`AdoptionSection.vue`、`mockData.js`

- 🛠️ 方法与接口：
  - `AdoptionSection` 中新增「＋ 发布宠物」按钮和 `AddPetModal` 组件引用
  - `openAddPet()` — 检查 `localStorage` 中的 token，未登录则 `alert('请先登录')`

- 🧠 业务逻辑：
  - `App.vue` 新增**全局装饰背景层** `<div class="global-bg">`，包含：
    - `global-bg__halo` — 径向渐变光晕（左上暖米色 + 右下浅绿色）
    - `global-bg__paint-blobs` — 3 个浮动色块，使用 CSS `@keyframes blobFloat` 实现缓动浮动动画
    - `global-bg__grain` — SVG 噪点纹理叠加层（`opacity: 0.04`）
    - `global-bg__paws` — 5 个极浅爪印装饰（`opacity: 0.05`），使用 `@keyframes pawDrift` 缓慢漂移
  - `.section` 全局样式进一步压缩：padding `1.5rem 2rem`，新增 `max-width: 1240px; margin: 0 auto` 居中
  - `mockData.js` 中筛选 Tab 中文化：`All Pets → 所有宠物`，新增 `鸟`、`蝈蝈`、`其他` 分类
  - `db.sql` 默认品种从 `兔子, 鸟类` 改为 `鸟, 蝈蝈`，并指定固定 `id`（1-4, 99）

---

### 🐛 修复：宠物列表分页与图片显示 + 管理员删帖

> Commit: `8d1f5b6` — 修复宠物列表分页和图片显示，添加管理员删帖功能

- 📄 涉及文件：`pets.js`、`AdoptionSection.vue`、`PetCard.vue`、`CommunitySection.vue`、`Pet.java`、`PostServiceImpl.java`、`seed-pets.sql`、`db.sql`

- 🛠️ 方法与接口：
  - `getPets()` 默认 `pageSize` 从 `8` 改为 `50`
  - `PetCard.vue` 新增 `onImgError()` — 图片加载失败时随机替换为 `FALLBACK_IMAGES` 中的 Unsplash 占位图
  - `PostServiceImpl.delete()` 新增**权限校验逻辑**：通过 `SecurityContextHolder.getContext().getAuthentication()` 获取当前用户角色，判断是否为 `ROLE_ADMIN` 或帖子作者本人，否则抛出 `FORBIDDEN`
  - `CommunitySection` 新增 `handleDelete()` 方法 — 管理员角色可见删除按钮，确认后调用 `deletePost(id)` 并从列表中移除

- 🧠 业务逻辑：
  - `Pet.java` 实体移除已注释的 `healthDesc` / `personalityDesc` 字段，统一使用 `description`
  - 社区帖子卡片图片添加 `@error="onImgError"` fallback 处理
  - 管理员删帖按钮为 `.post-card__delete`，绝对定位于图片右上角，默认 `opacity: 0`，hover 卡片时显示
  - `seed-pets.sql` — 新增 **50 条**宠物种子数据（猫/狗/兔/鸟/其他各 10 条），images 使用 Pixabay 公开 URL

---

### ✨ 新增：统计接口、发布弹窗、图片上传修复

> Commit: `dce4c48` — 管理员删帖、图片上传修复、统计接口及发布弹窗

- 📄 涉及文件：`MinioUtil.java`、`StatsVO.java`（新）、`StatsService.java`（新）、`StatsServiceImpl.java`（新）、`StatsController.java`（新）、`stats.js`（新）、`PostServiceImpl.java`、`UploadController.java`、`AddPetModal.vue`（新）、`AddPostModal.vue`（新）、`CommunitySection.vue`

- 🛠️ 方法与接口：
  - `MinioUtil.upload()` 方法签名新增 `long size` 参数，上传时 `.stream(inputStream, size, -1)` 替代原来的 `inputStream.available()`，修复大文件上传时因 `available()` 返回 0 导致的空文件问题
  - `StatsService.getHomeStats()` — 新增首页统计接口
  - `StatsServiceImpl.getHomeStats()` — 使用 `LambdaQueryWrapper` 分别查询 `adoption_apply`（`status=1` 审核通过数）、`pet`（`status=1` 待领养数）、`user`（总数），封装为 `StatsVO`
  - `StatsController` — `GET /stats/home` 公开接口
  - `PostServiceImpl.pageQuery()` 和 `getById()` — 新增用户信息关联：通过 `userMapper.selectById(post.getUserId())` 查询并填充 `vo.userNickname` 和 `vo.userAvatar`

- 🧠 业务逻辑：
  - **AddPetModal.vue**（370 行）：完整的宠物发布弹窗，包含名称、类型下拉框、年龄、性别单选、健康状况、性格描述、多图上传（最多 4 张，`uploadImage()` 上传至 MinIO 获取 key）、地址、发现地点，提交后调用 `createPet()` API
  - **AddPostModal.vue**（290 行）：帖子发布弹窗，包含标题、内容、图片上传、标签输入
  - `CommunitySection` 集成发帖按钮和 `AddPostModal`

---

### 🔀 分支合并

> Commit: `2cc71e1` — 合并claude/nervous-cohen分支：解决冲突

- 🧠 业务逻辑：将 `claude/nervous-cohen` 辅助开发分支的代码合入主线，解决分支间文件冲突后统一代码基线

---

### ✨ 新增：用户删除自己的帖子 + "我的"数据接口

> Commit: `002aaf1` — 用户可以删除帖子

- 📄 涉及文件：`PetQueryDTO.java`、`PetService.java`、`PetServiceImpl.java`、`PostService.java`、`PostServiceImpl.java`、`PetController.java`、`PostController.java`、`SecurityConfig.java`、`WebMvcConfig.java`（App + Admin 端）、`pets.js`、`posts.js`、`AppNavbar.vue`、`CommunitySection.vue`、`format.js`、`PetMapper.xml`、`router/index.js`

- 🛠️ 方法与接口：
  - `PetService` 新增 `myPets(int page, int pageSize)` — 查询当前用户发布的宠物列表
  - `PetServiceImpl.myPets()` — 构建 `PetQueryDTO` 设置 `userId = BaseContext.getCurrentId()`，复用 `selectPetPage` 分页查询
  - `PetQueryDTO` 新增 `userId` 字段，`PetMapper.xml` 的 `<where>` 中新增 `AND p.user_id = #{query.userId}` 条件
  - `PetController` 新增 `GET /pets/my` 端点
  - `PostService` 新增 `myPosts(int page, int pageSize)` — 查询当前用户发布的帖子列表
  - `PostServiceImpl.myPosts()` — 使用 `LambdaQueryWrapper` 按 `userId` 和 `createTime DESC` 查询，同时关联查询用户头像和昵称
  - `PostController` 新增 `GET /posts/my` 端点
  - 前端 `pets.js` 新增 `getMyPets()` API，`posts.js` 新增 `deletePost()` 和 `getMyPosts()` API
  - `SecurityConfig` 调整规则顺序：`/pets/my` 和 `/posts/my` 必须 `authenticated()`，放在 `/pets/**` 通配符之前避免被跳过；`/posts/**` 开放给访客浏览

- 🧠 业务逻辑：
  - **Long 精度丢失修复**：App 端和 Admin 端的 `WebMvcConfig` 均新增 `configureMessageConverters()` 方法，注册 Jackson `SimpleModule` 将 `Long` / `long` 类型序列化为 `String`，解决前端 JS `Number` 精度丢失问题（雪花 ID 超过 `Number.MAX_SAFE_INTEGER`）
  - `PostServiceImpl.delete()` 权限逻辑完善：通过 `SecurityContextHolder` 检查 `ROLE_ADMIN` 权限或判断 `post.getUserId().equals(currentUserId)`，非作者非管理员抛 `FORBIDDEN`
  - 前端 `format.js` 的 `formatPet()` 新增 `id` 字段传递，确保后续功能可用

---

## 未提交（工作区进行中）

### ✨ 新增：宠物详情弹窗（点击卡片查看）

- 📄 涉及文件：`PetCard.vue`、`AdoptionSection.vue`、`PetDetailModal.vue`（新）、`PetVO.java`、`PetMapper.java`、`PetMapper.xml`、`PetServiceImpl.java`

- 🛠️ 方法与接口：
  - `PetCard.vue` 新增 `@click="$emit('view', pet.id)"` 事件和 `defineEmits(['view'])`
  - `AdoptionSection.vue` 新增 `showDetailModal` / `detailPetId` 状态，`openDetail(petId)` 方法，引入 `PetDetailModal` 组件
  - `PetMapper.java` 新增 `selectPetById(Long id)` 方法
  - `PetMapper.xml` 新增 `selectPetById` 查询（含 LEFT JOIN `pet_type` 和 `user`，返回 `typeName`、`userNickname`、`userAvatar`）
  - `PetVO` 新增 `userAvatar` 字段
  - `PetServiceImpl.getById()` 从原来的 `selectById` + `BeanUtils.copyProperties` 改为直接使用 `selectPetById`，修复详情接口缺少关联字段的问题

- 🧠 业务逻辑：
  - `PetDetailModal.vue` 实现完整详情弹窗：顶部多图轮播（左右箭头切换 + 缩略图列表 + 图片计数器），中部展示宠物属性（年龄、性别、状态、地址），描述区，发布者信息（头像 + 昵称 + 发布时间），底部「申请领养」按钮
  - 弹窗通过 `watch([props.visible, props.petId])` 监听，打开时调用 `getPetById(id)` 获取详情，包含 loading 状态和 fallback 图片处理

### ✨ 新增：自定义品种 + 管理员审核机制

- 📄 涉及文件：`Pet.java`、`PetDTO.java`、`PetVO.java`、`PetQueryDTO.java`、`PetMapper.xml`、`PetServiceImpl.java`、`PetService.java`、`StatusConstant.java`、`ErrorCode.java`、`AdminPetController.java`、`AddPetModal.vue`

- 🛠️ 方法与接口：
  - `Pet` 实体新增 `customTypeName`（VARCHAR，自定义品种名称）、`typeReviewStatus`（INT，审核状态：null / 0 / 1 / 2）
  - `PetDTO` 新增 `customTypeName` 字段
  - `PetVO` 新增 `customTypeName`、`typeReviewStatus` 字段
  - `PetQueryDTO` 新增 `typeReviewStatus` 字段用于管理端筛选
  - `StatusConstant` 新增 `TYPE_REVIEW_PENDING(0)` / `TYPE_REVIEW_APPROVED(1)` / `TYPE_REVIEW_REJECTED(2)`
  - `ErrorCode` 新增 `PET_TYPE_REVIEW_NOT_PENDING(2004, "该品种审核已处理，无法重复操作")`
  - `PetServiceImpl.add()` — 检测 `customTypeName` 非空时设置 `typeReviewStatus = TYPE_REVIEW_PENDING`
  - `PetServiceImpl.pendingTypeReviews()` — 新增查询待审核列表方法
  - `PetServiceImpl.reviewCustomType(Long petId, boolean approved)` — `@Transactional` 事务方法：批准时查找或创建 `PetType`（先 `selectOne` 查同名，不存在则 `insert`），更新宠物 `typeId`；拒绝时仅标记状态
  - `PetMapper.xml` 的 `selectPetPage` WHERE 中新增 `typeReviewStatus` 过滤条件
  - `AdminPetController` 新增 `GET /admin/pets/type-reviews`（待审核列表）和 `PUT /admin/pets/{id}/type-review?approved=true|false`（审核操作）

- 🧠 业务逻辑：
  - **前端交互**：`AddPetModal.vue` 中 `select` 下拉框新增 `@change="onTypeChange"` 事件，`isOtherType` 计算属性判断选中项的 `name === '其他'`，为 true 时展开自定义品种输入框（带 `slideDown` 动画），输入框下方显示提示文字「自定义品种将提交管理员审核，审核通过后将正式生效」
  - 提交时根据 `isOtherType` 决定是否携带 `customTypeName` 字段，非"其他"类型时 `delete payload.customTypeName`
  - 关闭弹窗时重置 `customTypeName` 为空
  - **审核流**：管理员通过 `GET /admin/pets/type-reviews` 查看待审核列表 → 调用 `PUT /admin/pets/{id}/type-review?approved=true` 批准 → Service 层自动创建新 `pet_type` 并更新宠物的 `typeId` → 该品种后续可直接在下拉框中选择

---

> 📝 数据库迁移 SQL（待执行）：
> ```sql
> ALTER TABLE pet
>   ADD COLUMN custom_type_name VARCHAR(50) DEFAULT NULL COMMENT '用户自定义品种名称',
>   ADD COLUMN type_review_status TINYINT DEFAULT NULL COMMENT '品种审核状态：NULL-标准品种，0-待审核，1-已通过，2-已拒绝';
> ```
