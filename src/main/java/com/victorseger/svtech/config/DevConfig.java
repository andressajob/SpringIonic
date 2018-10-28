package com.victorseger.svtech.config;

import com.victorseger.svtech.services.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;


//classe de configuração com o profile de teste conforme indicado no application.properties
@Configuration
@Profile("dev")
public class DevConfig {


    @Autowired
    private DBService dbService;

    //variavel para verificar a estratégia de instanciação do banco de desenvolvimento
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String strategy;


    @Bean
    public boolean instantiateDatabase() throws ParseException {

        //instanciará a database somente se a estratégia de instanciação do banco for igual a create
        if(!"create".equals(strategy)) {
            return false;
        }

        dbService.instantiateDataBase();
        return true;
    }

}
