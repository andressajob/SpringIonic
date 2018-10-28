package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.Categoria;
import com.victorseger.svtech.domain.Produto;
import com.victorseger.svtech.repositories.CategoriaRepository;
import com.victorseger.svtech.repositories.ProdutoRepository;
import com.victorseger.svtech.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repo;

    @Autowired
    private CategoriaRepository categoriaRepository;


    public Produto find(Integer id) {
        Optional<Produto> obj = repo.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + ", Tipo: " + Produto.class.getName()));
    }


    public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction) {
        //cria as páginas de requisição com parâmetros (num de páginas, quantidade por página, direção - convertido para Direction e campos para ordenação)
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        List<Categoria> categorias = categoriaRepository.findAllById(ids);

        return repo.search(nome, categorias, pageRequest);
        //return repo.findDistinctByNomeContainingANDAndCategoriasIn(nome,categorias,pageRequest);

    }

    public List<Produto> findAll() {
        return repo.findAll();
    }

    public Produto save(Produto produto) {
        if (repo.existsByNomeIgnoreCase(produto.getNome())){
            return null;
        }
        return repo.save(produto);
    }

    public boolean delete(int id) {
        boolean flag;
        try {
            if (flag = repo.existsById(id))
                repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            flag = false;
        }
        return flag;
    }

}