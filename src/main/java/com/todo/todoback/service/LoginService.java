package com.todo.todoback.service;

import com.todo.todoback.dto.SignInResponseDto;
import com.todo.todoback.dto.TodoMemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface LoginService {

    public TodoMemberDto createMember ( Map<String, String> map ) throws NoSuchAlgorithmException;

    public boolean checkDuplicateId( String userId );
    public boolean checkDuplicateEmail( String userEmail );

    public SignInResponseDto signIn(Map<String, String> map ) throws NoSuchAlgorithmException;


}
