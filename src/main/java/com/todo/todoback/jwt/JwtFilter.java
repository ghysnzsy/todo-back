package com.todo.todoback.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 토큰을 생성해주고 검증하는 등 토큰 관리 객체
 */
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "refresh";
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ;
        log.info("my value : {}", request.getHeader("Authorization"));
        String jwt = resolveToken( request, AUTHORIZATION_HEADER );
        if ( jwt != null && tokenProvider.validateToken( jwt ) == TokenProvider.JwtCode.ACCESS ) {

            log.info("JWT Filter ACCESS 1::");
            Authentication authentication = tokenProvider.getAuthentication( jwt );
            SecurityContextHolder.getContext().setAuthentication( authentication );
            log.info( "set Authentication to security context for '{}', uri: {}", authentication.getName(), request.getRequestURI() );

        } else if ( jwt != null && tokenProvider.validateToken( jwt ) == TokenProvider.JwtCode.EXPIRED ) {

            log.info("JWT Filter EXPIRED 1::");
            String refesh = resolveToken( request, REFRESH_HEADER );
            // refresh token을 확인해서 발급해준다.
            if ( refesh != null && tokenProvider.validateToken( refesh ) == TokenProvider.JwtCode.ACCESS ) {

                String newRefresh = tokenProvider.reissueRefreshToken( refesh );
                log.info("JWT Filter EXPIRED 2::");
                if ( newRefresh != null ) {
                    log.info("JWT Filter EXPIRED 3::");
                    response.setHeader( REFRESH_HEADER, "Bear-" + newRefresh );

                    // Access token 생성
                    Authentication authentication = tokenProvider.getAuthentication(refesh);
                    response.setHeader( AUTHORIZATION_HEADER, "Bearer-" + tokenProvider.createAccessToken( authentication ) );
                    SecurityContextHolder.getContext().setAuthentication( authentication );

                    log.info("reissue refresh token & access token");
                } else {
                    log.info("JWT Filter EXPIRED 4::");
                    response.setHeader( REFRESH_HEADER, "" );
                    response.setHeader( AUTHORIZATION_HEADER, "");

                }
            }
        }  else {
            log.info("no valid JWT token found, uri : {}", request.getRequestURI());
        }

        filterChain.doFilter( request, response );
    }

    private String resolveToken( HttpServletRequest req, String header ) {
        String bearerToken = req.getHeader( header );
        Enumeration<String> headerNames = req.getHeaderNames();

//        log.info( "ref ref bearer info {}, {}",  bearerToken, req.getParameter("refresh"));
        if ( bearerToken != null && bearerToken.startsWith("Bearer-") )
            return bearerToken.substring(7);
        return null;
    }

}
