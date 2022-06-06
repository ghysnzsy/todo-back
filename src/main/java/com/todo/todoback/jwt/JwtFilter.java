package com.todo.todoback.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * 토큰을 생성해주고 검증하는 등 토큰 관리 객체
 */
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = resolveToken( request, AUTHORIZATION_HEADER );
        if ( jwt != null && tokenProvider.validateToken( jwt ) == TokenProvider.JwtCode.ACCESS ) {

            Authentication authentication = tokenProvider.getAuthentication( jwt );
            SecurityContextHolder.getContext().setAuthentication( authentication );
            log.info( "set Authentication to security context for '{}', uri: {}", authentication.getName(), request.getRequestURI() );

        } else if ( jwt != null && tokenProvider.validateToken( jwt ) == TokenProvider.JwtCode.EXPIRED ) {

            String refesh = resolveToken( request, REFRESH_HEADER );
            // refresh token을 확인해서 발급해준다.
            if ( refesh != null && tokenProvider.validateToken( refesh ) == TokenProvider.JwtCode.ACCESS ) {

                String newRefresh = tokenProvider.reissueRefreshToken( refesh );
                if ( newRefresh != null ) {

                    response.setHeader( REFRESH_HEADER, "Bear-" + newRefresh );

                    // Access token 생성
                    Authentication authentication = tokenProvider.getAuthentication(refesh);
                    response.setHeader( AUTHORIZATION_HEADER, "Bearer-" + tokenProvider.createAccessToken( authentication ) );
                    SecurityContextHolder.getContext().setAuthentication( authentication );

                    log.info("reissue refresh token & access token");
                }
            }
        }  else {
            log.info("no valid JWT token found, uri : {}", request.getRequestURI());
        }

        filterChain.doFilter( request, response );
    }

    private String resolveToken( HttpServletRequest req, String header ) {
        String bearerToken = req.getHeader( header );
        if ( bearerToken != null && bearerToken.startsWith("Bearer-") )
            return bearerToken.substring(7);
        return null;
    }

}
