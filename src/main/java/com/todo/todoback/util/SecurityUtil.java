package com.todo.todoback.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
@NoArgsConstructor
public class SecurityUtil {

    /**
     * SecurityContext의 Authentication 객체를 이용해 username을 리턴해주는 간단한 유틸성 메소드
     * @return
     */
    public static Optional<String> getCurrentUserName() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if ( authentication == null ) {
            log.info( "Security Context에 인증 정보가 없습니다." );
            return Optional.empty();
        }

        String userName = null;
        if ( authentication.getPrincipal() instanceof UserDetails ) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            userName = springSecurityUser.getUsername();
        } else if ( authentication.getPrincipal() instanceof String ) {
            userName = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable( userName );
    }

}
