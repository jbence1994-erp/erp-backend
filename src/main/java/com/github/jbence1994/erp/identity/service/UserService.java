package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.identity.model.User;
import com.github.jbence1994.erp.identity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
