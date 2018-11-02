package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.Cliente;
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
public class ClienteServiceTest {

    @Autowired
    ClienteService clienteService;

    private static final int CLIENTS = 2;

    @Test
    public void test_getOne_should_retrieve_city_by_id() {
        assertThat(clienteService.find(1)).isNotNull();
    }

    @Test
    public void test_getOne_id_null() {
        assertThat(clienteService.find(null)).isNull();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void test_getOne_id_invalid() {
        clienteService.find(-1);
    }

    @Test
    public void test_findAll() {
        assertThat(clienteService.findAll()).size().isEqualTo(CLIENTS);
    }

    @Test
    public void test_find_null() {
        assertThat(clienteService.find(null)).isNull();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void test_find_invalid() {
        clienteService.find(-1);
    }

    @Test
    public void test_find() {
        assertThat(clienteService.find(1)).isNotNull();
    }

    @Test
    public void test_insert_null() {
        assertThat(clienteService.insert(null)).isNull();
    }


    @Test
    public void test_update() {
        assertThat(clienteService.update(clienteService.find(1))).isNotNull();
    }

    @Test
    public void test_update_null() {
        assertThat(clienteService.update(null)).isNull();
    }

    @Test
    public void test_delete_id_invalid() {
        assertThat(clienteService.delete(-1)).isFalse();
    }

    @Test
    public void test_delete_id_null() {
        assertThat(clienteService.delete(null)).isFalse();
    }

    @Test
    public void test_delete() {
        assertThat(clienteService.delete(1)).isTrue();
    }

}
