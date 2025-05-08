package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.identity.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.jbence1994.erp.identity.testobject.UserTestObject.user1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void createUserTest_HappyPath() {
        when(userRepository.save(any())).thenReturn(user1());

        var result = userService.createUser(user1());

        assertNotNull(result);
        assertEquals(user1().getId(), result.getId());
        assertEquals(user1().getFirstName(), result.getFirstName());
        assertEquals(user1().getLastName(), result.getLastName());
        assertEquals(user1().getEmail(), result.getEmail());
    }
}
