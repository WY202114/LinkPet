# LinkPet 项目开发问题记录

本文档记录了 `LinkPet` 宠物领养平台在初始化搭建与开发过程中遇到的各类配置、依赖冲突及业务逻辑问题，以及最终的解决办法。

---

## 1. MyBatis-Plus Mapper Bean 注入失败

### ❌ 问题现象
在 Spring Boot 启动时，抛出类似以下的异常：
```text
Parameter 0 of constructor in com.wzc.linkpet.service.impl.AdoptionServiceImpl required a bean of type 'com.wzc.linkpet.mapper.AdoptionApplyMapper' that could not be found.
```

### 🔍 原因分析
此项目是一个多模块（Multi-Module）架构。启动类（如 `AdminApplication`）在 `linkpet-web-admin` 模块中，而所有的 Mapper 接口则位于 `linkpet-mapper` 模块的 `com.wzc.linkpet.mapper` 包下。默认情况下，Spring Boot 只会扫描和启动类在同级或子级包中的 Bean，导致跨模块的 Mapper 无法被 Spring Ioc 容器发现。

### ✅ 解决办法
在两个启动类（`AdminApplication` 和 `AppApplication`）上显式加上 `@MapperScan` 注解，告诉 MyBatis-Plus 去哪个包下扫描 Mapper 接口。
```java
@SpringBootApplication(scanBasePackages = "com.wzc.linkpet")
@MapperScan("com.wzc.linkpet.mapper") // 核心修复
public class AdminApplication { ... }
```
*(注意：同时也加上了 `scanBasePackages = "com.wzc.linkpet"` 以确保跨模块的 Service 层也被扫描到)*


---

## 2. Spring Boot 3.x 与 MyBatis-Plus 依赖兼容性问题

### ❌ 问题现象
在解决了 Mapper 扫描后，启动时抛出极其底层的 Spring Bean 实例化异常：
```text
java.lang.IllegalArgumentException: Invalid value type for attribute 'factoryBeanObjectType': java.lang.String
```

### 🔍 原因分析
这是 Java 升级到 Spring Boot 3.2+（底层为 Spring 6.1+）后的一个著名天坑。Spring 在该版本中修改了大量底层反射和泛型的获取逻辑，导致 MyBatis-Plus 的早期 3.5.x 版本在解析 `FactoryBean` 时发生类型转换异常。

### ✅ 解决办法
在 `pom.xml` 中严格对齐 Spring Boot 与 MyBatis-Plus 的版本，并使用专门适配 Spring Boot 3 的启动器：
1. **降级 Spring Boot 父依赖版本：** 将项目根级 `pom.xml` 的 `spring-boot-starter-parent` 改为非常稳定兼容的 **`3.0.5`**。
2. **更换 MyBatis-Plus 依赖坐标：** 对于 Spring Boot 3.x，绝不能再使用旧版的 `mybatis-plus-boot-starter`，必须替换为：
   ```xml
   <dependency>
       <groupId>com.baomidou</groupId>
       <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
       <version>3.5.5</version>
   </dependency>
   ```


---

## 3. Maven 私服配置导致的编译/下载失败 (403 Forbidden / Connection Refused)

### ❌ 问题现象
在 IDEA 中执行 `mvn clean compile` 时，控制台反复报错，无法下载 Spring Boot 父 POM 及各种依赖：
```text
Could not transfer artifact ... from/to nexus-mine (http://localhost:8081/repository/maven-public/): Connect to localhost:8081 failed: Connection refused
# 或
authorization failed for http://localhost:8081/..., status: 403 Forbidden
```

### 🔍 原因分析
这是因为本地电脑的 Maven 配置（通常在 `%USERPROFILE%\.m2\settings.xml`）中包含了一个 `<mirror>` 镜像，指向了本地搭建的 Nexus 私服（`http://localhost:8081/nexus-mine`），而：
1. **Connection Refused**：因为本地的 Nexus 服务当时并没有启动。
2. **403 Forbidden**：因为 `LinkPet` 管理端（AdminApplication）刚好启动在 `8081` 端口！Maven 在下载包时发送了 HTTP 请求到管理端，由于没有携带合法的 JWT Token，被 Spring Security 拦截并无情返回 `403 权限不足`。

### ✅ 解决办法
如果本地私服未使用，建议暂时修改 Maven 的 `settings.xml` 文件，注释掉或删除 `<mirror>` 节点，或者换成阿里云公共镜像：
```xml
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>*</mirrorOf>
    <name>阿里云公共仓库</name>
    <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```


---

## 4. 管理端缺失独立登录接口 (AdminAuthController)

### ❌ 问题现象
使用 Knife4j 接口文档（`http://localhost:8081/doc.html`）测试管理端服务时，发现所有的接口点下去全部返回 `403` 且内容显示为“下载文件”，并且找不到任何用于登录的接口路径。

### 🔍 原因分析
在多模块拆分时，用户端的 `/app/controller/AuthController` 虽然存在，但这是针对普通 App 端用户的（走 `ROLE_USER` 验证）。针对后台管理系统的 `/admin` 请求，因为没有写专门的 Controller 暴露出来，所以 Spring Security 直接把所有前端路由拦截死了。

### ✅ 解决办法
在 `linkpet-web-admin` 模块的 `controller` 包下，新增了 `AdminAuthController` 并编写针对性的登录逻辑。在登录时会：
1. 限定查询是否存在该用户。
2. 比对是否具备 `ROLE_ADMIN` 管理员角色。
3. 校验账号是否被停用（`status == 0`）。
4. 通过后才颁发包含 Admin 权限 Payload 的 JWT Token。


---

## 5. 异常抛出类型错误 (int 无法转换为 ErrorCode)

### ❌ 问题现象
在补写 `AdminAuthController` 时，报出以下 Java 编译错误：
```text
java: 不兼容的类型: int无法转换为com.wzc.linkpet.common.exception.ErrorCode
```

### 🔍 原因分析
这是代码编写的习惯冲突。本项目的 `BusinessException` 构造器抛弃了直接传递魔法数字（如 `throw new BusinessException(401, "...")`）的简写方式，而是在 `linkpet-common` 模块下定义了极其严谨的枚举类 `ErrorCode`，用来统一管理业务与异常码的关系。

### ✅ 解决办法
在 Controller 中引入 `ErrorCode`，将原有的硬编码数字状态码替换为指定的枚举：
```java
// 修改前
throw new BusinessException(401, "用户名或密码错误");

// 修改后
throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
```


---

## 6. BCrypt 密码匹配失败问题 (永远返回“用户名或密码错误”)

### ❌ 问题现象
就算输入了正确的管理员账号 `admin` 和确定的密码 `123456`，调用 `/admin/auth/login` 依然疯狂返回 `401 用户名或密码错误`。在接入 `log.info` 调试后，发现 Spring Security 的 `passwordEncoder.matches()` 方法坚决返回 `false`。

### 🔍 原因分析
在提供的 `db.sql` 脚本中，为 `admin` 写入的初始加密密码假定是 `$2a$10$7JB...`。如果这串密文不是在本机用匹配的 Spring Security 版本或同样的随机算法规则直接生成的，由于 BCrypt 强大的“强随机盐”特性与复杂的加解密环境校验，哪怕明文都是 `123456`，系统也是匹配不出来的。手动去生成密文的兼容性在不同系统、JDK或Spring大版本更迭下可能会受细微影响。

### ✅ 解决办法
**让当前运行环境自己生成：**
通过在 `AdminApplication` 启动类中注入了一个一次性的 `ApplicationRunner`，在 IDEA 启动的一瞬间，利用环境自带的 `passwordEncoder.encode("123456")` 生成并用 `System.out.println` 打印出了一段专属于本系统的合法 BCrypt 密文。
将其复制并 UPDATE 进数据库表中覆盖掉原有的散列值后，密码方可完美验证通过。
