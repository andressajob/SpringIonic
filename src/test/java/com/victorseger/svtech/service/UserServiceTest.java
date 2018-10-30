package com.victorseger.svtech.service;

import com.victorseger.svtech.domain.Role;
import com.victorseger.svtech.domain.User;
import com.victorseger.svtech.repositories.RoleRepository;
import com.victorseger.svtech.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UserServiceTest {

    private static final long USERS = 2;
    private static final String USER_MAIL = "thor@asgard.com";
    private static final String USER = "thor";
    private static final String NAME = "Thor Odinson";
    private static final String PASSWORD = "thor";
    private static HashSet<Role> ROLE = new HashSet<>();

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        ROLE.add(roleRepository.getOne(1L));
    }

    @Test
    public void test_getOne_id_null() {
        assertThat(userService.getOne(null)).isNull();
    }

    @Test
    public void test_getOne_id_invalid() {
        assertThat(userService.getOne(-1L)).isNull();
    }

    @Test
    public void test_findAll() {
        assertThat(userService.findAll().size()).isEqualTo(USERS);
    }

    @Test
    public void test_save() {
        assertThat(userService.save(new User(NAME, USER, USER_MAIL, PASSWORD, true, ROLE))).isNotNull();
    }
    @Test
    public void test_save_user_roles_empty() {
        assertThat(userService.save(new User(NAME, "dessa", USER_MAIL, PASSWORD, true, new HashSet<>()))).isNotNull();
    }

    @Test
    public void test_save_null_object() {
        assertThat(userService.save(null)).isNull();
    }

}
