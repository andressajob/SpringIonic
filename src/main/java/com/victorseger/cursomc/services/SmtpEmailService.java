package com.victorseger.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstractMailService {

    //instancia a classe mailsender com todos os dados configurados em application-dev.properties ou application-prod.properties
    @Autowired
    private MailSender mailSender;


    public static final Logger LOGGER = LoggerFactory.getLogger(SmtpEmailService.class);



    @Override
    public void sendEmail(SimpleMailMessage mailMessage) {
        LOGGER.info("Enviando mail.........");
        mailSender.send(mailMessage);
        LOGGER.info("Email enviado!");

    }
}
