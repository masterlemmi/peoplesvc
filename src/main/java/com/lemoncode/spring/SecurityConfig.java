package com.lemoncode.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {// @formatter:off
        http.cors()
                .and()
                .authorizeRequests()
//                    .antMatchers(HttpMethod.GET, "/user/info", "/foos/**", "/people*/**", "/descendants/**", "/relations/**")
//                    .hasAnyRole("family_user")
                .antMatchers(HttpMethod.POST, "/api/foos",
                        "/people/*/image",
                        "/people", "/descendants/**", "/relations/**")
                .hasRole("FAMILY_ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
    }//@formatter:on


    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return  new CustomJwtAuthenticationConverter();
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {// @formatter:off
//        http.cors().and().csrf().disable().authorizeRequests().anyRequest().permitAll();
//        http.headers().frameOptions().disable();
//    }

}