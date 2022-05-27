package com.todo.todoback.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TodoMember {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @Column( name = "userid", length = 30, nullable = false)
    private String userid;

    @Column(name = "userpw", length = 100, nullable = false)
    private String userpw;

    @Column(name = "username", length = 10, nullable = false)
    private String username;

    @Column(name = "userbirth", length = 20, nullable = false)
    private String userbirth;

    @Column(name = "useremail", length = 100, nullable = false)
    private String useremail;

    @Column(name = "createdate", length = 100, nullable = false)
    private String createdate;

    @Column(name = "deldate", length = 100, nullable = true)
    private String deldate;

    @OneToMany(fetch = FetchType.LAZY,  mappedBy = "todoMember")
    private List<TodoContents> todoContents = new LinkedList<>();

    @Builder
    public TodoMember(String userid, String userpw, String username, String userbirth, String useremail, String createdate, String deldate) {
        this.userid = userid;
        this.userpw = userpw;
        this.username = username;
        this.userbirth = userbirth;
        this.useremail = useremail;
        this.createdate = createdate;
        this.deldate = deldate;
    }
}
