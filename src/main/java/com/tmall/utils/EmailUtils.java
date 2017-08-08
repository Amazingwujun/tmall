package com.tmall.utils;

import com.tmall.dao.cacheDao.RedisCache;
import com.tmall.entity.po.User;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.Assert;

import java.util.UUID;

public class EmailUtils {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private MailSender mailSender;
    private SimpleMailMessage templateMessage;
    private CacheManager cacheManager;
    public static final String prefix = "email:validateCode:";

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

    public void send(User user) {
        Assert.notNull(user,"用户对象不能为空");

        RedisCache cache = (RedisCache) cacheManager.getCache("COMMON_USER");
        String code = UUID.randomUUID().toString();
        cache.put(prefix + user.getUsername(), 30 * 60, code);

        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(user.getEmail());
        msg.setText(
                "亲爱的用户 " + user.getUsername()
                        + ", 点击以下链接以便验证您的注册邮箱: "
                        + "http://localhost:8080/user/emailValidate?code="
                        + code);
        try {
            this.mailSender.send(msg);
        } catch (MailException ex) {
            log.error(ex.getMessage(),ex);
        }
    }

}