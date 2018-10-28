package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.Cidade;
import com.victorseger.svtech.repositories.CidadeRepository;
import com.victorseger.svtech.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository repository;

    public List<Cidade> findByEstado(Integer estadoId) {
        return repository.findCidades(estadoId);
    }

    public List<Cidade> findAll() {
        return repository.findAll();
    }

    public Cidade find(Integer id) {
        Optional<Cidade> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + ", Tipo: " + Cidade.class.getName()));
    }

    public Cidade getOne(Integer id) {
        return repository.getOne(id);
    }

    public Cidade insert(Cidade cidade) {
        if (cidade.getNome() != null && !cidade.getNome().isEmpty()) {
            cidade.setId(null);
            return repository.save(cidade);
        } else return null;
    }

    public Cidade update(Cidade cidade) {
        if (cidade.getNome() != null && !cidade.getNome().isEmpty()) {
            Cidade newCidade = find(cidade.getId());
            //chama o método auxiliar para apenas atualizar os campos desejados do cidade e não remover nenhum valor de outro campo
            updateData(newCidade, cidade);
            return repository.save(newCidade);
        } else return null;
    }

    public boolean delete(Integer id) {
        boolean flag = true;
        find(id);
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            flag = false;
        }
        return flag;
    }

    private void updateData(Cidade newCidade, Cidade cidade) {
        newCidade.setNome(cidade.getNome());
        newCidade.setEstado(cidade.getEstado());
    }

}
