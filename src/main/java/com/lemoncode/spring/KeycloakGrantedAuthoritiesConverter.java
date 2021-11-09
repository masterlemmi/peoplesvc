package com.lemoncode.spring;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        String client = source.getClaimAsString("azp");
        Map<String, Object> resourceAccess = source.getClaimAsMap("resource_access");
        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(client);
        List<String> roles = (List<String>) resource.get("roles");

        return roles.stream()
                //.map(Role::from)
                .map(rn -> new SimpleGrantedAuthority("ROLE_" + rn.toUpperCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }
}
