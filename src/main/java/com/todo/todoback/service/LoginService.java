package com.todo.todoback.service;

import com.todo.todoback.dto.TodoMemberDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface LoginService {

    public TodoMemberDto createMember (@RequestBody Map<String, String> map ) throws NoSuchAlgorithmException;

}
