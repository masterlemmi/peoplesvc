package com.lemoncode.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {// @formatter:off
        http.cors()
            .and()
              .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/user/info", "/foos/**", "/people*/**", "/descendants/**", "/relations/**")
                  .hasAuthority("SCOPE_read")
                .antMatchers(HttpMethod.POST, "/api/foos", "/people", "/descendants/**", "/relations/**")
                  .hasAuthority("SCOPE_write")
                .anyRequest()
                  .authenticated()
            .and()
              .oauth2ResourceServer()
                .jwt();
    }//@formatter:on

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {// @formatter:off
//        http.cors().and().csrf().disable().authorizeRequests().anyRequest().permitAll();
//    }

}