package com.victorseger.cursomc.services;

import com.victorseger.cursomc.domain.Categoria;
import com.victorseger.cursomc.dto.CategoriaDTO;
import com.victorseger.cursomc.repositories.CategoriaRepository;
import com.victorseger.cursomc.services.exceptions.DataIntegrityException;
import com.victorseger.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repo;

    public Categoria find(Integer id) {
        Optional<Categoria> obj = repo.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + ", Tipo: " + Categoria.class.getName()));
    }

    public Categoria insert(Categoria categoria) {
        categoria.setId(null);
        return repo.save(categoria);
    }

    public Categoria update(Categoria categoria) {
        Categoria newCategoria = find(categoria.getId());
        //chama o método auxiliar para apenas atualizar os campos desejados do categoria e não remover nenhum valor de outro campo
        updateData(newCategoria,categoria);
        return repo.save(newCategoria);
    }

    public void delete(Integer id) {
        find(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir categoria que possui produtos");
        }

    }

    public List<Categoria> findAll() {
        return repo.findAll();
    }

    public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        //cria as páginas de requisição com parâmetros (num de páginas, quantidade por página, direção - convertido para Direction e campos para ordenação)
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        //findAll com paginação direto do spring data
        return repo.findAll(pageRequest);
    }

    public boolean existsById (Integer id){
        if (id != null && id > 0)
            return repo.existsById(id);
        return false;
    }

    public Categoria fromDTO(CategoriaDTO categoriaDTO) {
        return new Categoria(categoriaDTO.getId(),categoriaDTO.getNome());
    }

    private void updateData(Categoria newCategoria, Categoria categoria) {
        newCategoria.setNome(categoria.getNome());
    }
}