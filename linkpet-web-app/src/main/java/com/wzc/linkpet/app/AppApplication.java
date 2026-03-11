package com.wzc.linkpet.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 用户端启动类
 *
 * <p>扫描基础包：com.wzc.linkpet — 覆盖所有子模块的 Spring Bean</p>
 */
@SpringBootApplication(scanBasePackages = "com.wzc.linkpet")
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
