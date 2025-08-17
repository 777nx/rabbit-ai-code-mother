package com.fantasy.rabbitaicodemother;

import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {RedisEmbeddingStoreAutoConfiguration.class})
@MapperScan("com.fantasy.rabbitaicodemother.mapper")
public class RabbitAiCodeMotherApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitAiCodeMotherApplication.class, args);
    }

}
