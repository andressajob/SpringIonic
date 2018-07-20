package com.victorseger.cursomc.services;

import com.victorseger.cursomc.domain.Cidade;
import com.victorseger.cursomc.repositories.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository repository;

    public List<Cidade> findByEstado(Integer estadoId) {
        return repository.findCidades(estadoId);
    }

    public List<Cidade> findAll() {return repository.findAll();}
}
