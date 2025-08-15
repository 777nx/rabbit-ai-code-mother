package com.fantasy.rabbitaicodemother;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fantasy.rabbitaicodemother.mapper")
public class RabbitAiCodeMotherApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitAiCodeMotherApplication.class, args);
    }

}
