package com.fantasy.rabbitaicodemother.ai;

import com.fantasy.rabbitaicodemother.utils.SpringContextUtil;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI 生成应用名称服务工厂
 */
@Configuration
@Slf4j
public class AiCodeGenNameServiceFactory {

    /**
     * 创建 AI 生成应用名称服务实例
     */
    public AiCodeGenNameService createAiCodeGenNameService() {
        ChatModel chatModel = SpringContextUtil.getBean("genNameChatModelPrototype", ChatModel.class);
        return AiServices.builder(AiCodeGenNameService.class)
                .chatModel(chatModel)
                .build();
    }

    /**
     * 默认提供一个 Bean
     * @return
     */
    @Bean
    public AiCodeGenNameService aiCodeGenNameService() {
        return createAiCodeGenNameService();
    }
}
