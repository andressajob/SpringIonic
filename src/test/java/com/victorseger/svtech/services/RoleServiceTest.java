package com.victorseger.svtech.services;

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
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Test
    public void test_getOne_should_retrieve_role_by_id() {
        assertThat(roleService.getOne(1L)).isNotNull();
    }

    @Test
    public void test_getOne_id_null() {
        assertThat(roleService.getOne(null)).isNull();
    }

    @Test
    public void test_getOne_id_invalid() {
        assertThat(roleService.getOne(-1L)).isNull();
    }

    @Test
    public void test_findAll() {
        assertThat(roleService.findAll()).size().isEqualTo(2);
    }

    @Test
    public void test_existsById_id_invalid() {
        assertThat(roleService.existsById(-1L)).isFalse();
    }

    @Test
    public void test_existsById_id_null() {
        assertThat(roleService.existsById(null)).isFalse();
    }

    @Test
    public void test_findByRole_role_null() {
        assertThat(roleService.findByRole(null)).isNull();
    }

    @Test
    public void test_findByRole_id_null() {
        assertThat(roleService.findByRole("Testing")).isNull();
    }

}
