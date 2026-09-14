package com.barter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 校园闲置物品互助交换平台 - 启动类
 */
@SpringBootApplication
@MapperScan("com.barter.mapper")
public class BarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarterApplication.class, args);
        System.out.println("==========================================");
        System.out.println(" 校园闲置物品互助交换平台 启动成功！");
        System.out.println(" 访问地址: http://localhost:8080/");
        System.out.println("==========================================");
    }
}
