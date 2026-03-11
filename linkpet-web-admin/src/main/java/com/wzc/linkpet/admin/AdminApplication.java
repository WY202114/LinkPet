package com.wzc.linkpet.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

/**
 * 后台管理端启动类
 *
 * <p>扫描基础包：com.wzc.linkpet — 覆盖所有子模块的 Spring Bean（包括 mapper、service 等）</p>
 */
@SpringBootApplication(scanBasePackages = "com.wzc.linkpet")
@MapperScan("com.wzc.linkpet.mapper")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
