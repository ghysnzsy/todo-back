package com.todo.todoback.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TodoContents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @Column(name = "createdate", length = 100, nullable = false)
    private String createdate;

    @Column(name = "cmpltime", length = 100, nullable = false)
    private String cmpltime;

    @Column(name = "odr", length = 20, nullable = false)
    private Long odr;

    @Column(name = "isdone", columnDefinition = "varchar(2) not null default 'N'")
    private String isdone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private TodoMember todoMember;

}
