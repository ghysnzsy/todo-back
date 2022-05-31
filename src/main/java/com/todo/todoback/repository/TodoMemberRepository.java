package com.todo.todoback.repository;

import com.todo.todoback.domain.TodoMember;
import com.todo.todoback.dto.TodoMemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TodoMemberRepository extends JpaRepository<TodoMember, Long> {

    /**
     * 회원정보 삽입
     * @param member
     * @return
     */
    @Transactional
    @Modifying
    @Query( value = "insert into todo_member ( userid, userpw, username, userbirth, useremail, role, createdate ) values( :#{#member.userid}, :#{#member.userpw}, :#{#member.username}, :#{#member.userbirth}, :#{#member.useremail}, :#{#member.role}, :#{#member.createdate} )", nativeQuery = true )
    public int createMember( @Param("member") TodoMemberDto member );

    /**
     * userid 중복 체크
     * @param userid
     * @return
     */
    @Query( value = "select count(*) from todo_member where userid = :userid", nativeQuery = true )
    public int checkUserid( @Param("userid") String userid );

    /**
     * userid로 사용자 정보 가져오기
     * @param userid
     * @return
     */
    @Query( value = "select * from todo_member where userid = :userid", nativeQuery = true )
    public TodoMember findByUserId(@Param("userid") String userid);


    /**
     * signIn : 로그인
     * @param userid
     * @param userpw
     * @return
     */
    @Query( value = "select * from todo_member where userid = :userid and userpw = :userpw", nativeQuery = true )
    public TodoMember signIn(@Param("userid") String userid, @Param("userpw") String userpw);


}
