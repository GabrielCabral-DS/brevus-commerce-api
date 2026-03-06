package br.com.brevus.commerce_api.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String sendRecoverPassword(String destinatario, String assunto, String linkToken, String name) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(assunto);

            String template = templateRecoverPassword();

            template = template
                    .replace("#{linkToken}", linkToken)
                    .replace("#{name}", name);
            helper.setText(template, true);

            javaMailSender.send(mimeMessage);
            return "Email de recuperação de senha enviado com sucesso!";
        } catch (Exception e) {
            return "Erro ao tentar enviar o e-mail: " + e.getLocalizedMessage();
        }
    }


    public String templateRecoverPassword() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/email.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
