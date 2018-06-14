package com.spss.smarthome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = {"com.spss.smarthome.dao"})
@SpringBootApplication
public class SmarthomeApplication {
    public static Map VCODEMAP = new HashMap();

    public static void main(String[] args) {
        VCODEMAP.put("15392550169", "6666");
        SpringApplication.run(SmarthomeApplication.class, args);
    }

    /**
     * 装载BCrypt密码编码器
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
