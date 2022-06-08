package com.todo.todoback.jwt;


import com.todo.todoback.domain.RefreshToken;
import com.todo.todoback.repository.RefreshTokenRepository;
import com.todo.todoback.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * jwt 패키지를 생성하고, 토큰의 생성과 토큰의 유효성 검증등을 담당
 */
@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    @Autowired
    private final CustomUserDetailsService customUserDetailsService;
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    private Key key;

    /**
     * yml설정 파일로부터 값을 가져온다.
     *
     * @param customUserDetailsService
     * @param refreshTokenRepository
     * @param secret
     * @param accessTokenValidityInMilliseconds
     */
    public TokenProvider(
            CustomUserDetailsService customUserDetailsService,
            RefreshTokenRepository refreshTokenRepository,
            @Value( "${jwt.secret}" ) String secret,
            @Value( "${jwt.access-token-validity-in-seconds}" ) long accessTokenValidityInMilliseconds,
            @Value( "${jwt.refresh-token-validity-in-seconds}" ) long refreshTokenValidityInMilliseconds
    ) {
        this.customUserDetailsService = customUserDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000;
    }

    /**
     * InitializingBean 인터페이스를 구현하여, afterPropertiesSet 메소드를 Override 한 이유는
     * Bean이 생성이 되고, 의존성 주입을 받은 후에 secret 값을 Base64 Decode 해서 key 변수에 할당한다.
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor( keyBytes );
    }

    /**
     *  Authentication 객체의 권한정보를 이용해서 토큰을 생성
     * @param authentication
     * @return
     */
    public String createAccessToken( Authentication authentication ) {

        String authorities = authentication.getAuthorities().stream()
                .map( GrantedAuthority::getAuthority )
                .collect( Collectors.joining() );

        long now = ( new Date() ).getTime();
        Date validity = new Date( now + this.accessTokenValidityInMilliseconds );

        return Jwts.builder()
                .setSubject( authentication.getName() )
                .claim( AUTHORITIES_KEY, authorities )
                .signWith( key, SignatureAlgorithm.HS512 )
                .setExpiration( validity )
                .compact();
    }

    /**
     *  Refresh Token 발급
     * @param authentication
     * @return
     */
    public String createRefreshToken( Authentication authentication ) {

        String authorities = authentication.getAuthorities().stream()
                .map( GrantedAuthority::getAuthority )
                .collect( Collectors.joining() );

        long now = ( new Date() ).getTime();
        Date validity = new Date( now + this.refreshTokenValidityInMilliseconds );

        return Jwts.builder()
                .setSubject( authentication.getName() )
                .claim( AUTHORITIES_KEY, authorities )
                .signWith( key, SignatureAlgorithm.HS512 )
                .setExpiration( validity )
                .compact();
    }

    /**
     * token을 매개변수로 받아서, 토큰에 담긴 정보를 이용해 Authentication 객체를 반환
     * @param token
     * @return
     */
    public Authentication getAuthentication( String token ) {

        // Claims란? : JWT의 속성정보, java에서  Claims는 Json 형식의 인터페이스이다.
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        /*
        Collection<? extends GrantedAuthority> authorities = Arrays.stream( claims.get( AUTHORITIES_KEY ).toString().split(",") )
                .map( SimpleGrantedAuthority::new )
                .collect( Collectors.toList() );

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken( principal, token, authorities );
         */
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken( userDetails, token, userDetails.getAuthorities() );
    }

    /**
     * token을 매개변수로 받아서, 토큰의 유효성 검증을 수행
     * @param token
     * @return
     */
    public JwtCode validateToken( String token ) {
        /*
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
         */

        try {
            Jwts.parserBuilder().setSigningKey( key ).build().parseClaimsJws( token );
            return JwtCode.ACCESS;
        } catch ( ExpiredJwtException e ) {
            // 만료된 경우에는 refresh token을 확인하기 위해
            return JwtCode.EXPIRED;
        } catch ( JwtException | IllegalArgumentException e ) {
            log.info(" jwt exception : {}", e);
        }
        return JwtCode.DENIED;
    }

    @Transactional
    public String reissueRefreshToken( String refreshToken ) throws RuntimeException {
        //  refresh token을 데이터베이스의 값과 비교
        Authentication authentication = getAuthentication( refreshToken );

        RefreshToken findRefreshToken = refreshTokenRepository.findByUserId( authentication.getName() )
                .orElseThrow( () -> new UsernameNotFoundException( "User id : " + authentication.getName() + " was not found " ));

        if ( findRefreshToken.getToken().equals( refreshToken ) ) {
            // 새로운 생성
            String newRefreshToken = createAccessToken( authentication );
            findRefreshToken.changeToken( newRefreshToken );
            return newRefreshToken;
        } else {
            log.info( "refresh 토큰이 일치하지 않습니다." );
            return null;
        }

    }

    @Transactional
    public String issueRefreshToken( Authentication authentication ) {

        String newRefreshToken = createRefreshToken( authentication );

        // 기존 것이 있다면 바꿔주고, 없다면 만들어준다.
        refreshTokenRepository.findByUserId( authentication.getName() )
                .ifPresentOrElse(
                        r -> {
                            r.changeToken( newRefreshToken );
                            log.info("issueRefreshToken method | change token");
                        },
                        () -> {
                            RefreshToken token = RefreshToken.createToken( authentication.getName(), newRefreshToken );
                            log.info("issueRefreshToken method | save token id : {}, token : {}", token.getUserId(), token.getToken());
                            refreshTokenRepository.save(token);
                        }
                    );

        return newRefreshToken;
    }

    public static enum JwtCode {
        DENIED,
        ACCESS,
        EXPIRED
    }

}
