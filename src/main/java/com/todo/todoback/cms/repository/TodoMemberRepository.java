package com.todo.todoback.cms.repository;

import com.todo.todoback.cms.domain.TodoMember;
import com.todo.todoback.domain.Role;
import com.todo.todoback.dto.TodoMemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TodoMemberRepository extends JpaRepository<TodoMember, Long> {
    public List<TodoMember> findAllByRole(String role);

    public List<TodoMember> findAll();

    @Transactional
    @Modifying
    public boolean removeByUserId(String userid);

    @Transactional
    @Modifying
    @Query(value = "update todo_member set stat = :stat where userid = :userid", nativeQuery = true)
    public boolean updateStatByUserId(@Param("userid") String userid, @Param("stat") String stat);
}
