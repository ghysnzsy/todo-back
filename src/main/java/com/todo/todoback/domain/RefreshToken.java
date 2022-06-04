package com.todo.todoback.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "refresh_token_id")
    private Long id;

    private String userId;

    private String token;

    private RefreshToken(String userid, String token) {
        this.userId = userid;
        this.token = token;
    }

    public static RefreshToken createToken( String userid, String token ) {
        return new RefreshToken( userid, token );
    }

    public void changeToken( String token ) {
        this.token = token;
    }

}
