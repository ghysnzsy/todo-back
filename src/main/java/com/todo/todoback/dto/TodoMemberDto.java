package com.todo.todoback.dto;

import com.todo.todoback.domain.Role;
import com.todo.todoback.domain.TodoContents;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class TodoMemberDto {

    private Long id;
    private String userid;
    private String userpw;
    private String username;
    private String userbirth;
    private String useremail;
    private String createdate;
    private String deldate;

    @Enumerated(EnumType.STRING)
    private Role role;
    private List<TodoContents> todoContents = new LinkedList<>();

    public TodoMemberDto(Long id, String userid, String userpw, String username, String userbirth, String useremail, String createdate, String deldate, Role role, List<TodoContents> todoContents) {
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
    }
}
