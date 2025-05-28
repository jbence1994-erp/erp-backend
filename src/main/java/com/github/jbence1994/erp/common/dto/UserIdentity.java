package com.github.jbence1994.erp.common.dto;

import com.github.jbence1994.erp.common.model.Role;

public record UserIdentity(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role
) {
}
