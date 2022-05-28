package com.todo.todoback.controller;

import com.todo.todoback.domain.TodoMember;
import com.todo.todoback.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginRestController {

    @Autowired
    LoginService loginService;

    // 로그인

    // 회원 가입
    @PostMapping("/sign")
    public ResponseEntity<TodoMember> createMember (@RequestBody Map<String, String> map ) {
        return ResponseEntity.ok( loginService.createMember( map ) );
    }

}

