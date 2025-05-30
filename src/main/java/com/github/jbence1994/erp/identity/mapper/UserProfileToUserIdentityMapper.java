package com.github.jbence1994.erp.identity.mapper;

import com.github.jbence1994.erp.common.dto.UserIdentity;
import com.github.jbence1994.erp.identity.model.UserProfile;

public interface UserProfileToUserIdentityMapper {
    UserIdentity toUserIdentity(UserProfile userProfile);
}
