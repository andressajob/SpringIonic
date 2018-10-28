package com.victorseger.svtech.repositories;

import com.victorseger.svtech.domain.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    List<Endereco> findAllByClienteId(Integer id);
}
