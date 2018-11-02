package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.Categoria;
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

    @Test
    public void test_find_null() {
        assertThat(categoriaService.find(null)).isNull();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void test_find_invalid() {
        categoriaService.find(-1);
    }

    @Test
    public void test_find() {
        assertThat(categoriaService.find(1)).isNotNull();
    }

    @Test
    public void test_insert_null() {
        assertThat(categoriaService.insert(null)).isNull();
    }

    @Test
    public void test_insert_without_name() {
        assertThat(categoriaService.insert(new Categoria(null, ""))).isNull();
    }

    @Test
    public void test_insert() {
        assertThat(categoriaService.insert(new Categoria(null, "Testing"))).isNotNull();
    }

    @Test
    public void test_update() {
        assertThat(categoriaService.update(categoriaService.find(1))).isNotNull();
    }

    @Test
    public void test_update_null() {
        assertThat(categoriaService.update(null)).isNull();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void test_delete_id_invalid() {
        assertThat(categoriaService.delete(-1)).isFalse();
    }

    @Test
    public void test_delete_id_null() {
        assertThat(categoriaService.delete(null)).isFalse();
    }

    @Test
    public void test_delete() {
        assertThat(categoriaService.delete(1)).isTrue();
    }

}
