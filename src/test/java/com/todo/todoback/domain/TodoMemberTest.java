package com.todo.todoback.domain;

import com.todo.todoback.repository.TodoMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.Date;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class TodoMemberTest {

    @Autowired
    TodoMemberRepository todoMemberRepository;

    @Test
    public void 날짜포맷() {

        SimpleDateFormat curDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        System.out.println( curDate.format( date ) );
    }

}