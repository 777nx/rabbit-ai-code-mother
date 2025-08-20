package com.fantasy.rabbitaicodemother.controller;

import cn.hutool.core.util.StrUtil;
import com.fantasy.rabbitaicodemother.common.BaseResponse;
import com.fantasy.rabbitaicodemother.common.ResultUtils;
import com.fantasy.rabbitaicodemother.exception.ErrorCode;
import com.fantasy.rabbitaicodemother.exception.ThrowUtils;
import com.fantasy.rabbitaicodemother.service.EmailService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件接口
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    @Resource
    private EmailService emailService;

    @PostMapping("/send")
    public BaseResponse<Boolean> sendVerificationCode(String email) {
        // 校验参数
        ThrowUtils.throwIf(StrUtil.isBlank(email), ErrorCode.PARAMS_ERROR, "邮箱不能为空");
        return ResultUtils.success(emailService.sendVerificationCode(email));
    }
}
