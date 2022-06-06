package com.todo.todoback.config;

import com.todo.todoback.jwt.JwtAccessDeniedHandler;
import com.todo.todoback.jwt.JwtAuthenticationEntryPoint;
import com.todo.todoback.jwt.JwtSecurityConfig;
import com.todo.todoback.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
public class SecurityConfig  {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("securityFilterChain");
        return http

                // cross-origin 처리
                .cors().and()

                // token을 사용하는 방식이기 때문에 csrf를 disable
                .csrf().disable()

                // jwt를 쓰려면 Spring Security에서 기본적으로 지원하는 Session설정을 해제해야 한다.
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                // X-Frame-Options : 이 홈페이지는 동일한 도메인의 페이지 내에서만 표시할 수 있음
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()

                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply( new JwtSecurityConfig( tokenProvider ) )
                .and()

                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // HttpSecurity를 인자로 받는 configure메서드에서 http.cors()로 cors 옵션을 활성화 해줄 수 있다.
    // 이때 만일 처음에 정의했던 WebMvcConfigurer 구현체가 존재한다면 corsConfigurationSource()는 추가로 정의 해줄 필요가 없고
    // 정의를 해주지 않았다면 cors설정파일을 빈으로 등록해주어야 한다.
    /*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

     */

}
