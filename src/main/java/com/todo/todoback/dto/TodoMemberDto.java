package com.todo.todoback.dto;

import com.todo.todoback.domain.TodoContents;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
public class TodoMemberDto {

    @Id
    private Long id;
    private String userid;
    private String userpw;
    private String username;
    private String userbirth;
    private String useremail;
    private String createdate;
    private String deldate;

    private String token;

//    @Enumerated(EnumType.STRING)
    private String role;
    private List<TodoContents> todoContents = new LinkedList<>();

    @Builder
    public TodoMemberDto(
            Long id,
            String userid,
            String userpw,
            String username,
            String userbirth,
            String useremail,
            String createdate,
            String deldate,
            String role,
            List<TodoContents> todoContents,
            String token
    ) {
        this.id = id;
        this.userid = userid;
        this.userpw = userpw;
        this.username = username;
        this.userbirth = userbirth;
        this.useremail = useremail;
        this.createdate = createdate;
        this.deldate = deldate;
        this.role = role;
        this.todoContents = todoContents;
        this.token = token;
    }
}
