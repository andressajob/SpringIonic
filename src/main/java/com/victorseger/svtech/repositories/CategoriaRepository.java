package com.victorseger.svtech.repositories;

import com.victorseger.svtech.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    boolean existsByNomeIgnoreCase(String name);
}
