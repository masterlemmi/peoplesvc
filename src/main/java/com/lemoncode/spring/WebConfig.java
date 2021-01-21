package com.lemoncode.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    Environment env;

    private static final String[] EMPTY_ARRAY = {};


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String originsEnv = env.getProperty("ALLOWED_ORIGINS");
        String[] allowedOrigins = StringUtils.isEmpty(originsEnv)? EMPTY_ARRAY : originsEnv.split(",");

        System.out.println("Allowed Origins") ;
        Arrays.stream(allowedOrigins ).forEach(System.out::println);
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOrigins(allowedOrigins);

    }
}