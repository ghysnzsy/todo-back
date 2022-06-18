package com.todo.todoback.controller;

import com.todo.todoback.dto.SignInResponseDto;
import com.todo.todoback.dto.TodoMemberDto;
import com.todo.todoback.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class LoginRestController {

    @Autowired
    LoginService loginService;

    /**
     * 로그인
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDto> signInMember(@RequestBody Map<String, String> map ) {

        log.info( "LoginRestController signInMember useId::", map.get("userId") );

        try {
            return ResponseEntity.ok( loginService.signIn( map ) );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<Integer>signOutMember(@RequestBody Map<String, String> map) {
        log.info("LoginRestController signOutMember userId:: {}", map.get("userId"));
        return ResponseEntity.ok(loginService.signOut(map));
    }

    /**
     * 회원가입
     * @param map
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<TodoMemberDto> createMember (@RequestBody Map<String, String> map ) {

        log.info( "[회원가입] user id = {}, user name = {}, user birth = {}, user email = {}", map.get("userId"), map.get("userName"), map.get("userBirth"), map.get("userEmail") );

        try {
            return ResponseEntity.ok( loginService.createMember( map ) );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * userId 중복 체크
     * @param userId
     * @return
     */
    @GetMapping("/userid")
    public ResponseEntity<Boolean> checkUserId( @RequestParam(value="userId") String userId ) {

        log.info("user id = {}", userId);

        return ResponseEntity.ok(
                loginService.checkDuplicateId( userId )
        );

    }

    @GetMapping("/email")
    public ResponseEntity<Boolean> checkUserEmail( @RequestParam(value="userEmail") String userEmail ) {

        log.info("user email = {}", userEmail);

        return ResponseEntity.ok(
                loginService.checkDuplicateEmail( userEmail )
        );

    }

    // 토큰 재발행
    @PostMapping("/refreshToken")
    public ResponseEntity<Map> refreshToken(@RequestHeader Map<String, Object> map, HttpServletRequest req, HttpServletResponse res) {

        log.info("refresh token controller : {}", map.get("refresh"));

        map.put( "authorization", res.getHeader("Authorization") );
        map.put( "refresh", res.getHeader("refresh") );

        return ResponseEntity.ok(map);
    }


}

