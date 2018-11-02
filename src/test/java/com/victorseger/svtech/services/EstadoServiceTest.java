package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.Estado;
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
public class EstadoServiceTest {

    @Autowired
    EstadoService estadoService;

    private static final int STATES = 2;


    @Test
    public void test_find_null() {
        assertThat(estadoService.find(null)).isNull();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void test_find_invalid() {
        estadoService.find(-1);
    }

    @Test
    public void test_find() {
        assertThat(estadoService.find(1)).isNotNull();
    }

    @Test
    public void test_getOne_should_retrieve_city_by_id() {
        assertThat(estadoService.find(1)).isNotNull();
    }

    @Test
    public void test_getOne_id_null() {
        assertThat(estadoService.find(null)).isNull();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void test_getOne_id_invalid() {
        estadoService.find(-1);
    }

    @Test
    public void test_findAll() {
        assertThat(estadoService.findAll()).size().isEqualTo(STATES);
    }

    @Test
    public void test_insert_null() {
        assertThat(estadoService.insert(null)).isNull();
    }

    @Test
    public void test_insert_without_name() {
        assertThat(estadoService.insert(new Estado(null,""))).isNull();
    }

    @Test
    public void test_insert() {
        assertThat(estadoService.insert(new Estado(null,"Testing"))).isNotNull();
    }

    @Test
    public void test_update() {
        assertThat(estadoService.update(estadoService.find(1))).isNotNull();
    }

    @Test
    public void test_update_null() {
        assertThat(estadoService.update(null)).isNull();
    }

    @Test
    public void test_update_without_name() {
        Estado estado = getEstado();
        estado.setNome("");
        assertThat(estadoService.update(estado)).isNull();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void test_delete_id_invalid() {
        assertThat(estadoService.delete(-1)).isFalse();
    }

    @Test
    public void test_delete_id_null() {
        assertThat(estadoService.delete(null)).isFalse();
    }

    @Test
    public void test_delete() {
        assertThat(estadoService.delete(1)).isTrue();
    }

    public Estado getEstado(){
        return estadoService.find(1);
    }

}
