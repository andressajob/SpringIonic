package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.User;
import com.victorseger.svtech.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public User save(User user) {
        if (user!=null){
            return userRepository.save(user);
        }
        return null;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getOne(Long id) {
        if (id != null && userRepository.existsById(id))
            return userRepository.getOne(id);
        return null;
    }

    public boolean existsById(Long id) {
        if (id != null)
            return userRepository.existsById(id);
        return false;
    }

}
