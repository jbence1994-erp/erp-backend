package com.github.jbence1994.erp.identity.config;

import com.github.jbence1994.erp.common.config.SecurityRules;
import com.github.jbence1994.erp.common.model.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class IdentitySecurityRules implements SecurityRules {

    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/refresh").permitAll()
                .requestMatchers("/api/userProfiles/**").hasAnyRole(
                        Role.ADMIN.name(),
                        Role.USER.name()
                );
    }
}
