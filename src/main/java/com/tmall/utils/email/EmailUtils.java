package com.tmall.utils.email;

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
import java.util.UUID;

public class EmailUtils implements Runnable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private JavaMailSender mailSender;
    private CacheManager cacheManager; //邮箱验证码缓存
    private User user;
    public static final String prefix = "email:validateCode:";

    public void setUser(User user) {
        this.user = user;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailValidate(User user) {
        Assert.notNull(user, "邮件发送对象不能为空");

        RedisCache cache = (RedisCache) cacheManager.getCache("COMMON_USER");
        String token = UUID.randomUUID().toString();
        cache.put(prefix + user.getUsername(), 24 * 60 * 60, token);

        //normal interface implement
        /*MimeMessagePreparator preparator = new MimeMessagePreparator() {

            public void prepare(MimeMessage mimeMessage) throws Exception {

                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(user.getEmail()));
                mimeMessage.setFrom(new InternetAddress("85998282@qq.com"));
                mimeMessage.setSubject("邮箱验证");
                mimeMessage.setText(
                        "亲爱的用户:" + user.getUsername()
                                + ", 点击以下链接以便验证您的注册邮箱:</br> "
                                + "<a href='http://localhost:8080/user/emailValidate?token="
                                + token + "&" + "username=" + user.getUsername() + "'>点击校验</a>", "utf8", "html");
            }
        };*/

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

    @Override
    public void run() {
        sendEmailValidate(user);
    }
}