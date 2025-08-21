package com.fantasy.rabbitaicodemother.ai;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCodeGenNameServiceTest {

    @Resource
    private AiCodeGenNameService aiCodeGenNameService;

    @Test
    void generateAppName() {
        String userMessage = "创建一个现代化的个人博客网站，包含文章列表、详情页、分类标签、搜索功能、评论系统和个人简介页面。采用简洁的设计风格，支持响应式布局，文章支持Markdown格式，首页展示最新文章和热门推荐。";
        String appName = aiCodeGenNameService.generateAppName(userMessage);
        Assertions.assertNotNull(appName);
    }
}
