package com.fantasy.rabbitaicodemother.ai;

import dev.langchain4j.service.SystemMessage;

/**
 * AI 生成应用名称服务
 */
public interface AiCodeGenNameService {

    @SystemMessage(fromResource = "prompt/codegen-app-name-prompt.txt")
    String generateAppName(String userMessage);
}
