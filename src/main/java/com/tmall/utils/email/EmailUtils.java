package com.tmall.utils.email;

import com.tmall.common.constant.Cache;
import com.tmall.dao.cacheDao.RedisCache;
import com.tmall.entity.po.User;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.Assert;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 
 */
public class EmailUtils implements Runnable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private JavaMailSender mailSender;  //配置参数
    private CacheManager cacheManager; //邮箱验证码缓存
    private User user;  //邮件发送对象
    private Map<Integer,String> map;    //策略缓存
    private int strategy;   //策略
    public static final String EMAIL_VALIDATE_TOKEN = "email:validateCode:";
    public static final String EMAIL_FORGETPASSWORD_TOKEN = "email:forgetPasswordToken:";

    public EmailUtils() {
        super();
        init(); 
    }

    private void init() {
        map = new HashMap<>(2);
        map.put(1, EMAIL_VALIDATE_TOKEN);
        map.put(2, EMAIL_FORGETPASSWORD_TOKEN);
    }

    /**
     * 配置将要执行的策略
     *
     * @param user
     * @param strategy 1_邮箱验证,2_忘记密码
     */
    public void setStrategy(User user,Integer strategy) {
        this.user = user;
        this.strategy = strategy;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailValidate(User user) {
        Assert.notNull(user, "邮件发送对象不能为空");

        RedisCache cache = (RedisCache) cacheManager.getCache(Cache.COMMON_USE_CACHE);
        String token = UUID.randomUUID().toString();
        cache.put(EMAIL_VALIDATE_TOKEN + user.getUsername(), 24 * 60 * 60, token);

        //lambda expression
        try {
            this.mailSender.send(
                    msg -> {
                        msg.setRecipient(Message.RecipientType.TO,
                                new InternetAddress(user.getEmail()));
                        msg.setFrom(new InternetAddress("85998282@qq.com"));
                        msg.setSubject("邮箱验证");
                        msg.setText(
                                "亲爱的用户:" + user.getUsername()
                                        + ", 点击以下链接以便验证您的注册邮箱:</br> "
                                        + "<a href='http://localhost:8080/user/emailValidate?token="
                                        + token + "&" + "username=" + user.getUsername() + "'>点击校验</a>",
                                "utf8", "html");
                    });

            log.info("用户:{}的邮箱验证邮件已发送", user.getUsername());
        } catch (MailException ex) {
            log.error(ex.getMessage(),ex);
        }
    }

    /**
     * 这个功能结合前端页面来处理,所以只是做了半成品
     *
     * @param user
     */
    public void forgetPassword(User user) {
        Assert.notNull(user, "邮件发送对象不能为空");

        RedisCache cache = (RedisCache) cacheManager.getCache(Cache.COMMON_USE_CACHE);
        String token = UUID.randomUUID().toString();
        cache.put(EMAIL_FORGETPASSWORD_TOKEN + user.getUsername(), 30 * 60, token);

        //lambda expression
        try {
            this.mailSender.send(
                    msg -> {
                        msg.setRecipient(Message.RecipientType.TO,
                                new InternetAddress(user.getEmail()));
                        msg.setFrom(new InternetAddress("85998282@qq.com"));
                        msg.setSubject("忘记密码");
                        msg.setText(
                                "亲爱的用户:" + user.getUsername()
                                        + ", 点击以下链接以便重置您的密码:</br> "
                                        + "<a href='http://localhost:8080/user/resetPassword?token="
                                        + token + "&" + "username=" + user.getUsername() + "'>开始重置密码</a>",
                                "utf8", "html");
                    });

            log.info("用户:{}的密码重置邮件已发送", user.getUsername());
        } catch (MailException ex) {
            log.error(ex.getMessage(),ex);
        }
    }


    @Override
    public void run() {
        String value = map.get(strategy);

        if (EMAIL_VALIDATE_TOKEN.equals(value)) {
            sendEmailValidate(user);
        } else if (EMAIL_FORGETPASSWORD_TOKEN.equals(value)) {
            forgetPassword(user);
        }else {
            log.info("邮件发送策略无法匹配");
        }
    }
}