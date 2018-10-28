package com.victorseger.svtech.config;

import com.victorseger.svtech.services.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;


//classe de configuração com o profile de teste conforme indicado no application.properties
@Configuration
@Profile("test")
public class TestConfig {


    @Autowired
    private DBService dbService;


    //método para instanciar base de dados
    @Bean
    public boolean instantiateDatabase() throws ParseException {

        dbService.instantiateDataBase();
        return true;
    }

}
