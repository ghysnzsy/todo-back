package com.todo.todoback.domain;

import com.todo.todoback.dto.TodoMemberDto;
import com.todo.todoback.repository.TodoMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class TodoMemberTest implements Serializable {

    @Autowired
    TodoMemberRepository todoMemberRepository;

    @AfterEach
    public void cleanup() {
        todoMemberRepository.deleteAll();
    }

    @Test
    public void 회원가입() {

        try {
            // given
            String userid = "test123";
            int resVal = 0;
            TodoMemberDto memberDto = TodoMemberDto.builder()
                    .userid( userid )
                    .userpw( "test" ) // pw 암호화 : sha256
                    .userbirth( "200000000" )
                    .useremail( "01030985843z@naver.com" )
                    .username( "홍길동" )
                    .role( Role.ROLE_USER.getValue() )
                    .createdate( 날짜포맷() )
                    .build();

            // when
            resVal = todoMemberRepository.createMember( memberDto );
            Optional<TodoMember> userInfo = todoMemberRepository.findByUserId(userid);

            // then
            assertThat(resVal).isEqualTo(1);
//            assertThat( userInfo.getUsername() ).isEqualTo( "홍길동" );


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public String 날짜포맷() {

        SimpleDateFormat curDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        System.out.println( curDate.format( date ) );

        return  curDate.format( date );
    }

}
