package com.todo.todoback.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .exposedHeaders("refreshToken")
                .allowedOrigins( "http://localhost:3000", "http://localhost:3001", "http://localhost:4000")
                .allowCredentials(true);
    }
}
