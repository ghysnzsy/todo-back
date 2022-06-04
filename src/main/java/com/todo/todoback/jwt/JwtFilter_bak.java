package com.todo.todoback.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 안씀 -- 추후 제거
 * JWT를 위한 커스텀 필터를 만들기 위한 JwtFilter 클래스
 */
@Slf4j
public class JwtFilter_bak extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JwtFilter_bak(TokenProvider tokenProvider ) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = ( HttpServletRequest ) request;
        String jwt = resolveToken( httpServletRequest );
        String reqURI = httpServletRequest.getRequestURI();
        String reqURL = String.valueOf(httpServletRequest.getRequestURL());

        // 토큰 유효성 검사
//        if ( StringUtils.hasText( jwt ) && tokenProvider.validateToken( jwt ) ) {
//
//            // SecurityContext 에 저장
//            SecurityContextHolder.getContext().setAuthentication(
//                    tokenProvider.getAuthentication( jwt )
//            );
//            log.info( "Security Context에 인증 정보를 저장하였습니다. uri : {}", reqURI );
//
//        } else {
//            log.info( "유효한 JWT 토큰이 없습니다. uri : {}, url : {}", reqURI, reqURL);
//        }

        chain.doFilter( request, response);
    }

    /**
     * Request Header 에서 토큰 정보를 가져옮
     * @param request
     * @return
     */
    public String resolveToken( HttpServletRequest request ) {
        String bearerToken = request.getHeader( AUTHORIZATION_HEADER );

        if ( StringUtils.hasText( bearerToken ) && bearerToken.startsWith( "Bearer " ) )
            return bearerToken.substring(7);

        return null;
    }

}
