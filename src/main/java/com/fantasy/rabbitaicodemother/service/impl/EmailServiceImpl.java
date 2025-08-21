package com.fantasy.rabbitaicodemother.service.impl;

import cn.hutool.core.util.StrUtil;
import com.fantasy.rabbitaicodemother.exception.BusinessException;
import com.fantasy.rabbitaicodemother.exception.ErrorCode;
import com.fantasy.rabbitaicodemother.service.EmailService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 邮件服务实现
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    // 邮箱正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Redis键前缀
    private static final String VERIFICATION_CODE_PREFIX = "rabbitAiCode:sendEmail:code:";
    private static final String SEND_LOCK_PREFIX = "rabbitAiCode:sendEmail:lock:";

    @Value("${spring.mail.username}")
    private String FromUsername;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public boolean sendVerificationCode(String email) {
        // 1. 校验参数是否合法
        if (StrUtil.isBlank(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱不能为空");
        }
        // 2. 校验邮箱格式是否合法
        if (!isValidEmail(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式不正确");
        }
        // 检查是否在一分钟内已经发送过
        String lockKey = SEND_LOCK_PREFIX + email;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(lockKey))) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, "请求过于频繁，请稍后再试");
        }
        // 3. 生成验证码
        String verificationCode = generateVerificationCode();
        // 4. 发送验证码邮件
        boolean sendResult = sendEmail(email, verificationCode);
        if (!sendResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "发送邮件失败");
        }
        // 5. 缓存验证码到 redis 中
        String codeKey = VERIFICATION_CODE_PREFIX + email;
        // 存储验证码，有效期 5 分钟
        redisTemplate.opsForValue().set(codeKey, verificationCode, 5, TimeUnit.MINUTES);
        // 设置发送锁，有效期 1 分钟
        redisTemplate.opsForValue().set(lockKey, "locked", 1, TimeUnit.MINUTES);
        // 6. 返回结果
        return true;
    }

    @Override
    public boolean verifyCode(String email, String code) {
        // 1. 校验参数
        if (StrUtil.hasBlank(email, code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱或验证码不能为空");
        }
        // 2. 校验邮箱格式是否合法
        if (!isValidEmail(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式不正确");
        }
        // 3. 校验验证码是否正确
        String codeKey = VERIFICATION_CODE_PREFIX + email;
        String cachedCode = redisTemplate.opsForValue().get(codeKey);
        if (cachedCode == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码已过期或未发送");
        }
        if (!cachedCode.equals(code)) {
            return false;
        }
        // 验证成功后删除验证码
        redisTemplate.delete(codeKey);
        return true;
    }

    @Override
    public boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 生成6位随机验证码
     * @return 验证码
     */
    private String generateVerificationCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    /**
     * 发送邮件
     * @param email 收件邮箱
     * @param code 验证码
     * @return 发送结果
     */
    private boolean sendEmail(String email, String code) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(FromUsername);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("【兔子 AI 零代码应用生成平台】邮箱验证码");
            simpleMailMessage.setText("您的验证码是：" + code + "，有效期5分钟，请勿泄露！");
            log.info("邮箱：{}，验证码：{}", email, code);
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e) {
            log.error("发送邮件失败：{}", e.getMessage());
            return false;
        }
    }
}
