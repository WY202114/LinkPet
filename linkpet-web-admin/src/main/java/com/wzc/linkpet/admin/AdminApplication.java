package com.wzc.linkpet.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 后台管理端启动类
 *
 * <p>扫描基础包：com.wzc.linkpet — 覆盖所有子模块的 Spring Bean（包括 mapper、service 等）</p>
 */
@SpringBootApplication(scanBasePackages = "com.wzc.linkpet")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
