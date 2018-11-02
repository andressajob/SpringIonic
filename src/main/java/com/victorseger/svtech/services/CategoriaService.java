package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.Categoria;
import com.victorseger.svtech.repositories.CategoriaRepository;
import com.victorseger.svtech.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repo;

    public Categoria find(Integer id) {
        if (id != null) {
            Optional<Categoria> obj = repo.findById(id);

            return obj.orElseThrow(() -> new ObjectNotFoundException(
                    "Objeto não encontrado! Id: " + ", Tipo: " + Categoria.class.getName()));
        }
        return null;
    }

    public Categoria insert(Categoria categoria) {
        if (categoria != null && categoria.getNome() != null && !categoria.getNome().isEmpty() && !repo.existsByNomeIgnoreCase(categoria.getNome())) {
            categoria.setId(null);
            return repo.save(categoria);
        }
        return null;
    }

    public Categoria update(Categoria categoria) {
        if (categoria != null && categoria.getNome() != null && !categoria.getNome().isEmpty()) {
            Categoria newCategoria = find(categoria.getId());
            //chama o método auxiliar para apenas atualizar os campos desejados do categoria e não remover nenhum valor de outro campo
            updateData(newCategoria, categoria);
            return repo.save(newCategoria);
        } else return null;
    }

    public boolean delete(Integer id) {
        if (id != null) {
            boolean flag = true;
            find(id);
            try {
                repo.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                flag = false;
                //throw new DataIntegrityException("Não é possível excluir categoria que possui produtos");
            }
            return flag;
        }
        return false;
    }

    public List<Categoria> findAll() {
        return repo.findAll();
    }

    public boolean existsById(Integer id) {
        if (id != null && id > 0)
            return repo.existsById(id);
        return false;
    }

    private void updateData(Categoria newCategoria, Categoria categoria) {
        newCategoria.setNome(categoria.getNome());
    }
}