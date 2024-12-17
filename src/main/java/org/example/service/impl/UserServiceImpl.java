package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserEntityRepository;
import org.example.service.Service;

import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements Service<User> {
    private UserEntityRepository userRepository;

    public UserServiceImpl(UserEntityRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) { return userRepository.save(user);
    }

    @Override
    public User findById(UUID uuid) {
        return userRepository.findById(uuid);
    }

    @Override
    public List<User> findAll() { return userRepository.findAll(); }

    @Override
    public boolean deleteById(UUID id) { return userRepository.deleteById(id); }

}
