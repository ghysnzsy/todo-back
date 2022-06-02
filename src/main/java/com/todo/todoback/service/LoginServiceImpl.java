package com.todo.todoback.service;

import com.todo.todoback.domain.Role;
import com.todo.todoback.dto.TodoMemberDto;
import com.todo.todoback.jwt.JwtFilter;
import com.todo.todoback.jwt.TokenProvider;
import com.todo.todoback.repository.TodoMemberRepository;
import com.todo.todoback.util.SHA256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    TodoMemberRepository memberRepository;

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final PasswordEncoder passwordEncoder;

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
    public ResponseEntity<TodoMemberDto> signIn(Map<String, String> map ) throws NoSuchAlgorithmException {

        String userId   = map.get("userId");
        String userPw = passwordEncoder.encode( map.get("userPw") );
        log.info("user pw 1 : {}", userPw);
        log.info("user pw 2 : {}", map.get("userPw"));
        log.info("user pw 2 : {}", SHA256.encrypt(map.get("userPw")));
//        TodoMember todoMember = memberRepository.signIn(userId, userPw);

//        if ( todoMember != null ) {

            // 토큰을 발행한다.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( userId, map.get("userPw") );
            log.info("[회원가입 서비스] result2 {}", authenticationToken.getAuthorities());
            Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            log.info("[회원가입 서비스] result3");
            SecurityContextHolder.getContext().setAuthentication( authenticate );
            log.info("[회원가입 서비스] result4");
            String jwt = tokenProvider.createToken( authenticate );
            log.info("[회원가입 서비스] result5");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer ");
            log.info("[회원가입 서비스] result6 : {}", jwt);

            return new ResponseEntity<>(
                    TodoMemberDto.builder()
//                            .id( todoMember.getId() )
//                            .userid( userId )
//                            .username( todoMember.getUsername() )
//                            .useremail( todoMember.getUseremail() )
//                            .userbirth( todoMember.getUserbirth() )
//                            .role( todoMember.getRole().getValue() )
                            .token( jwt )
                            .build(),
                    httpHeaders,
                    HttpStatus.OK
            );


//        }

//        return null;
    }

}
