package com.fantasy.rabbitaicodemother.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户重置密码请求
 */
@Data
public class UserResetPasswordRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -7050533868697862927L;

    /**
     * 账号
     */
    private String userEmail;

    /**
     * 验证码
     */
    private String code;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认密码
     */
    private String checkPassword;
}
