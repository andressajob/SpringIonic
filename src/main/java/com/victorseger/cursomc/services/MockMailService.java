package com.victorseger.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockMailService extends AbstractMailService{

    public static final Logger LOGGER = LoggerFactory.getLogger(MockMailService.class);

    @Override
    public void sendEmail(SimpleMailMessage mailMessage) {
        LOGGER.info("Simulando envio de mail.........");
        LOGGER.info(mailMessage.toString());
        LOGGER.info("Email enviado!");

    }
}
