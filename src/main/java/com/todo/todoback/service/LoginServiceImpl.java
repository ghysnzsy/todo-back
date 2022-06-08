package com.todo.todoback.service;

import com.todo.todoback.domain.Role;
import com.todo.todoback.dto.SignInResponseDto;
import com.todo.todoback.dto.TodoMemberDto;
import com.todo.todoback.jwt.TokenProvider;
import com.todo.todoback.repository.TodoMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService, Serializable {

    @Autowired
    private TodoMemberRepository memberRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;



    /**
     * 회원가입
     * @param map
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Transactional
    @Override
    public TodoMemberDto createMember( Map<String, String> map ) throws NoSuchAlgorithmException {

        // 1.멤버를 생성한다.
        SimpleDateFormat curDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        // 권한
        // u - ROLE_USER
        // a -  ROLE_ADMIN
        // m - ROLE_MASTER
        String paramRole = map.get("role");
        String authority =  paramRole.equals("u") ? Role.ROLE_USER.getValue() : ( paramRole.equals("a") ? Role.ROLE_ADMIN.getValue() : Role.ROLE_MASTER.getValue() );

        String userId = map.get("userId");
        String userPw = passwordEncoder.encode( map.get("userPw") );

        int resVal = memberRepository.createMember(
                TodoMemberDto.builder()
                        .userid( userId )
                        .userpw( userPw )
                        .userbirth( map.get("userBirth") )
                        .useremail( map.get("userEmail") )
                        .username( map.get("userName") )
                        .role( authority )
                        .createdate( curDate.format( date ) )
                        .build()
        );
        log.info("[회원가입 서비스] result : {}", resVal);
        // 2. 멤버생성이 성공한 경우.
        if ( resVal == 1 ) {

            return TodoMemberDto.builder()
                    .userid( userId )
                    .userbirth( map.get("userbirth") )
                    .useremail( map.get("useremail") )
                    .username( map.get("username") )
                    .build();
        }

        return null;
    }

    /**
     * userId 중복 체크
     * @param userId
     * @return
     */
    @Override
    public boolean checkDuplicateId( String userId ) {

        // 1.id 중복체크를 한다.
        int useridCnt = memberRepository.checkUserid( userId );

        // 2.중복된 id가 존재하는 경우 : true값 반환
        if ( useridCnt > 0 ) return true;

        // 3.중복되지 않은 경우 : false값 반환
        return false;
    }

    @Override
    public boolean checkDuplicateEmail( String userEmail ) {

        // 1.email 중복체크를 한다.
        int useremailCnt = memberRepository.checkUserEmail( userEmail );

        // 2.중복된 email이 존재하는 경우 : true값 반환
        if ( useremailCnt > 0 ) return true;

        // 3.중복되지 않은 경우 : false값 반환
        return false;
    }

    @Override
    public SignInResponseDto signIn(Map<String, String> map ) throws NoSuchAlgorithmException {

        String userId   = map.get("userId");
        String userPw = map.get("userPw") ;

        UserDetails userDetails = customUserDetailsService.loadUserByUsername( userId );
        if ( !passwordEncoder.matches( userPw, userDetails.getPassword() ) ) {
            throw new BadCredentialsException(userDetails.getUsername() + " invalid password");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        return SignInResponseDto.builder()
                .accessToken( "Bearer-" + tokenProvider.createAccessToken( authentication ) )
                .refreshToken( "Bearer-" + tokenProvider.issueRefreshToken( authentication ) )
                .build();
        /*
        // 토큰을 발행한다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( userId, map.get("userPw") );
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication( authenticate );
        String jwt = tokenProvider.createAccessToken( authenticate );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter_bak.AUTHORIZATION_HEADER, "Bearer-" + jwt);
        httpHeaders.add(JwtFilter_bak.AUTHORIZATION_HEADER, "Bearer ");

        return new ResponseEntity<>(
                TodoMemberDto.builder()
                        .token( jwt )
                        .build(),
                httpHeaders,
                HttpStatus.OK
        );
         */

    }



}
