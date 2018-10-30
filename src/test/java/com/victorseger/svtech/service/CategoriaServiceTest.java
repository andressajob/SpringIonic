package com.victorseger.svtech.service;

import com.victorseger.svtech.services.CategoriaService;
import com.victorseger.svtech.services.exceptions.ObjectNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CategoriaServiceTest {

    private static final int CATEGORIES = 7;

    @Autowired
    private CategoriaService categoriaService;

    @Test
    public void test_getOne_should_retrieve_category_by_id() {
        assertThat(categoriaService.find(1)).isNotNull();
    }

    @Test
    public void test_getOne_id_null() {
        assertThat(categoriaService.find(null)).isNull();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void test_getOne_id_invalid() {
        categoriaService.find(-1);
    }

    @Test
    public void test_findAll() {
        assertThat(categoriaService.findAll()).size().isEqualTo(CATEGORIES);
    }

    @Test
    public void test_existsById_id_invalid() {
        assertThat(categoriaService.existsById(-1)).isFalse();
    }

    @Test
    public void test_existsById_id_null() {
        assertThat(categoriaService.existsById(null)).isFalse();
    }

}
