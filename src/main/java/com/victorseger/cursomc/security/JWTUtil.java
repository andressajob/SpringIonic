package com.victorseger.cursomc.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username) {
        return Jwts.builder() //gerador de token
                .setSubject(username) // associa ao usuário
                .setExpiration(new Date(System.currentTimeMillis()+expiration)) // ajusta a expiração do token
                .signWith(SignatureAlgorithm.HS512, secret.getBytes()) // insere o algoritmo de encriptação + a palavra selecionada
                .compact();
    }
}
