package com.todo.todoback.domain;

import com.todo.todoback.repository.TodoMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class TodoMemberTest {

    @Autowired
    TodoMemberRepository todoMemberRepository;

    @Test
    public void 회원저장() {
        TodoMember.builder()
                .userid("test")
                .userpw("test")
                .userbirth("19911009")
                .useremail("01030985843z@naver.com")
                .username("김동운")
                .createdate("20220526")
                .build();
    }

}