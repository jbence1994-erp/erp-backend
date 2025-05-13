package com.github.jbence1994.erp.identity.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.jbence1994.erp.identity.testobject.UserProfileTestObject.userProfile1;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserProfileToUserIdentityMapperImplTests {

    @InjectMocks
    private UserProfileToUserIdentityMapperImpl toUserIdentityMapper;

    @Test
    public void toUserIdentityTest() {
        var result = toUserIdentityMapper.toUserIdentity(userProfile1());

        assertEquals(userProfile1().getId(), result.id());
        assertEquals(userProfile1().getEmail(), result.email());
        assertEquals(userProfile1().getFirstName(), result.firstName());
        assertEquals(userProfile1().getLastName(), result.lastName());
    }
}
