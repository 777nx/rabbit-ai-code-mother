package com.fantasy.rabbitaicodemother.service;

/**
 * 邮件服务
 */
public interface EmailService {

    /**
     * 发送邮件验证码
     *
     * @param email 邮箱
     * @return 发送结果
     */
    boolean sendVerificationCode(String email);

    /**
     * 验证验证码是否正确
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 验证结果
     */
    boolean verifyCode(String email, String code);

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @return 是否有效
     */
    boolean isValidEmail(String email);
}
