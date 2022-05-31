package com.todo.todoback.controller;

import com.todo.todoback.dto.TodoMemberDto;
import com.todo.todoback.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/member")
public class LoginRestController {

    @Autowired
    LoginService loginService;

    /**
     * 로그인
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<TodoMemberDto> signUpMember( @RequestBody Map<String, String> map ) {

        log.info( "user id = {}", map.get("userId") );

        try {
            return ResponseEntity.ok( loginService.signIn( map ) );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 회원가입
     * @param map
     * @return
     */
    @PostMapping
    public ResponseEntity<TodoMemberDto> createMember (@RequestBody Map<String, String> map ) {

        log.info( "user id = {}, user name = {}, user birth = {}, user email = {}", map.get("userId"), map.get("userName"), map.get("user birth"), map.get("userEmail") );

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
    @GetMapping
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


}

