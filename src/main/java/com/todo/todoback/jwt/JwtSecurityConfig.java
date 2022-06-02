package com.todo.todoback.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *  TokenProvider, JwtFilter 를 SecurityConfig에 적용할때 사용
 */
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;

    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * configure 메소드를 Override 하여 JwtFilter를 통해 Security 로직에 필터를 등록
     * @param builder
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity builder) throws Exception {
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        builder.addFilterBefore( customFilter, UsernamePasswordAuthenticationFilter.class );
    }
}
