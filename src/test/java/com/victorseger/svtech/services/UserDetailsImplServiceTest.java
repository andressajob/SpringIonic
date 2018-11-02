package com.victorseger.svtech.services;


import com.victorseger.svtech.domain.User;
import com.victorseger.svtech.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UserDetailsImplServiceTest {

    private static final Long USER_ID = 1004L;
    private static final String USER_MAIL = "thor@odinson.com";
    private static final String USER = "thor";
    private static final String NAME = "Thor Odinson";
    private static final String PASSWORD = "thor";

    @Autowired
    UserDetailsImplService userDetailsImplService;
    @Autowired
    UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository.save(this.getUser());
    }

    @Test
    public void test_loadUserByUsername_null() {
        assertThat(userDetailsImplService.loadUserByUsername(null)).isNull();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void test_loadUserByUsername_empty() {
        userDetailsImplService.loadUserByUsername("");
    }

    @Test
    public void test_loadUserByUsername() {
        assertThat(userDetailsImplService.loadUserByUsername("user")).isNotNull();
    }

    @Test
    public void test_constructor() {
        assertThat(new UserDetailsImplService(userRepository)).isNotNull();
    }

    @Test
    public void test_constructor_null() {
        assertThat(new UserDetailsImplService(null)).isNotNull();
    }

    private User getUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setUsername(USER);
        user.setEmail(USER_MAIL);
        user.setName(NAME);
        user.setActive(false);
        user.setPassword(PASSWORD);
        return user;
    }
}