package com.todo.todoback.repository;

import com.todo.todoback.domain.TodoMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoMemberRepository extends JpaRepository<TodoMember, String> {
}
