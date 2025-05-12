package com.github.jbence1994.erp.identity.mapper;

import com.github.jbence1994.erp.common.dto.UserIdentity;
import com.github.jbence1994.erp.identity.model.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileToUserIdentityMapperImpl implements UserProfileToUserIdentityMapper {

    @Override
    public UserIdentity toUserIdentity(UserProfile userProfile) {
        return new UserIdentity(
                userProfile.getId(),
                userProfile.getEmail(),
                userProfile.getFirstName(),
                userProfile.getLastName()
        );
    }
}
