package com.project.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@Log4j2
@Service
//@PropertySource("classpath:my.properties")
public class EmailService {
    // 원래 에러 뜹니다 but 문제 없습니다 -> application.properties에 없고, my.properties에 있어서 그렇습니다
    @Autowired private JavaMailSender mailSender;
    @Autowired private TemplateEngine templateEngine;

    private final String FROM;
    private final String EMAIL_CERT_TEMPLATE = "/mail/email-auth-template.html";

    public EmailService(
        @Value("${spring.mail.username}") String from
    ) {
        this.FROM = from;
        log.info("FROM : " + FROM); // application.properties에서 설정해주세요
    }

    public String send_cert_mail(String TO) throws MessagingException {
        Context context = new Context();

        StringBuilder certNumber = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            certNumber.append(rand.nextInt(10));
        }
        context.setVariable("certNumber", certNumber.toString());
        send_mail(EMAIL_CERT_TEMPLATE, context, "책 토론 회원가입 인증 메일입니다.", TO);
        log.info("이메일 전송됨");
        return certNumber.toString();
    }

    public void send_mail(String template, Context context, String SUBJECT, String TO) throws MessagingException {

        String templateMailContext = templateEngine.process(template, context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(FROM);
        mimeMessageHelper.setTo(TO);
        mimeMessageHelper.setSubject(SUBJECT);
        mimeMessageHelper.setText(templateMailContext, true);
        mailSender.send(mimeMessage);
    }
}
