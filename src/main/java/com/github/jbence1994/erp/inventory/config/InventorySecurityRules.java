package com.github.jbence1994.erp.inventory.config;

import com.github.jbence1994.erp.common.config.SecurityRules;
import com.github.jbence1994.erp.common.model.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class InventorySecurityRules implements SecurityRules {

    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
                .requestMatchers("/api/products/**").hasAnyRole(
                        Role.ADMIN.name(),
                        Role.USER.name()
                );
    }
}
