package com.victorseger.cursomc.services;

import com.victorseger.cursomc.domain.Cliente;
import com.victorseger.cursomc.repositories.ClienteRepository;
import com.victorseger.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

/*
    @Autowired
    private EmailService emailService;
*/

    private Random random = new Random();


    public void sendNewPassword(String email) {

        Cliente cliente = clienteRepository.findByEmail(email);
        if(cliente == null) {
            throw new ObjectNotFoundException("Email não encontrado");
        }

        String novaSenha = newPassword();
        cliente.setSenha(bCryptPasswordEncoder.encode(novaSenha));

        clienteRepository.save(cliente);

        //emailService.sendNewPasswordEmail(cliente, novaSenha);

    }

    private String newPassword() {
        char[] vet = new char[10];
        for (int i=0;i<10;i++) {
            vet[i] = randomChar();
        }
        return new String(vet);

    }

    private char randomChar() {
        int opt = random.nextInt();

        if(opt == 0) { // numero
            return (char) (random.nextInt(10) + 48); // gera numeros a partir do codigo ASCII
        } else if(opt == 1) { // letra maiuscula
            return (char) (random.nextInt(26) + 65); // gera letras maiúsculas a partir do codigo ASCII
        } else { //letra minuscula
            return (char) (random.nextInt(26) + 97);// gera letras minusculas a partir do codigo ASCII
        }
    }

}
