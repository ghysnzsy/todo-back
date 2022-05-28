package com.todo.todoback.service;

import com.todo.todoback.domain.TodoMember;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface LoginService {

    public TodoMember createMember (@RequestBody Map<String, String> map );

}
