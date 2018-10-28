package com.victorseger.svtech.repositories;

import com.victorseger.svtech.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {


    //metodo que busca email
    //não deve ser considerada como transação, reduzindo o lock do banco
    @Transactional(readOnly = true)
    Cliente findByEmail(String email);

}
