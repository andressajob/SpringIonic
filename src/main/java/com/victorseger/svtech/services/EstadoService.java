package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.Estado;
import com.victorseger.svtech.repositories.EstadoRepository;
import com.victorseger.svtech.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public Estado find(Integer id) {
        Optional<Estado> obj = estadoRepository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + ", Tipo: " + Estado.class.getName()));
    }

    public List<Estado> findAll() {
        return estadoRepository.findAllByOrderByNome();
    }

    public Estado getOne(Integer id) {
        return estadoRepository.getOne(id);
    }

    public Estado insert(Estado estado) {
        if (estado.getNome() != null && !estado.getNome().isEmpty()) {
            estado.setId(null);
            return estadoRepository.save(estado);
        } else return null;
    }

    public Estado update(Estado estado) {
        if (estado.getNome() != null && !estado.getNome().isEmpty()) {
            Estado newEstado = find(estado.getId());
            //chama o método auxiliar para apenas atualizar os campos desejados do estado e não remover nenhum valor de outro campo
            updateData(newEstado, estado);
            return estadoRepository.save(newEstado);
        } else return null;
    }

    public boolean delete(Integer id) {
        boolean flag = true;
        find(id);
        try {
            estadoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            flag = false;
        }
        return flag;
    }

    private void updateData(Estado newEstado, Estado estado) {
        newEstado.setNome(estado.getNome());
    }

}
