package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.Cidade;
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
public class CidadeServiceTest {

    @Autowired
    CidadeService cidadeService;

    @Autowired
    EstadoService estadoService;

    private static final int CITIES = 3;

    @Test
    public void test_getOne_should_retrieve_city_by_id() {
        assertThat(cidadeService.find(1)).isNotNull();
    }

    @Test
    public void test_getOne_id_null() {
        assertThat(cidadeService.find(null)).isNull();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void test_getOne_id_invalid() {
        cidadeService.find(-1);
    }

    @Test
    public void test_findAll() {
        assertThat(cidadeService.findAll()).size().isEqualTo(CITIES);
    }

    @Test
    public void test_find_null() {
        assertThat(cidadeService.find(null)).isNull();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void test_find_invalid() {
        cidadeService.find(-1);
    }

    @Test
    public void test_find() {
        assertThat(cidadeService.find(1)).isNotNull();
    }

    @Test
    public void test_insert_null() {
        assertThat(cidadeService.insert(null)).isNull();
    }

    @Test
    public void test_insert_without_name() {
        assertThat(cidadeService.insert(new Cidade(null,"",estadoService.find(1)))).isNull();
    }

    @Test
    public void test_insert() {
        assertThat(cidadeService.insert(new Cidade(null,"Testing",estadoService.find(1)))).isNotNull();
    }

    @Test
    public void test_update() {
        assertThat(cidadeService.update(cidadeService.find(1))).isNotNull();
    }

    @Test
    public void test_update_null() {
        assertThat(cidadeService.update(null)).isNull();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void test_delete_id_invalid() {
        assertThat(cidadeService.delete(-1)).isFalse();
    }

    @Test
    public void test_delete_id_null() {
        assertThat(cidadeService.delete(null)).isFalse();
    }

    @Test
    public void test_delete() {
        assertThat(cidadeService.delete(1)).isTrue();
    }

}
