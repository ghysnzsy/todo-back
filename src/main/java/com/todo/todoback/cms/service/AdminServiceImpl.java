package com.todo.todoback.cms.service;

import com.todo.todoback.cms.dto.TodoMemberDto;
import com.todo.todoback.cms.repository.TodoMemberRepository;
import com.todo.todoback.cms.domain.TodoMember;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@NoArgsConstructor
public class AdminServiceImpl implements AdminService{

    @Autowired
    TodoMemberRepository todoMemberRepository;

    @Override
    public List<TodoMemberDto> selectMember(String role) {
        if("".equals(role)) {
            //return todoMemberRepository.findAll();
            List<TodoMember> list = todoMemberRepository.findAll();

            List<TodoMemberDto> dtoList = new ArrayList<>();
            //list.stream().map(m -> new TodoMemberDto(m.getId(), m.getUserid(), m.getUsername(), m.getRole())).toList();
            for(TodoMember todo : list)
                dtoList.add(TodoMemberDto.builder().userid(todo.getUserid()).username(todo.getUsername()).build());
            return  dtoList;
        }
        else {
            List<TodoMember> list = todoMemberRepository.findAllByRole(role);
            List<TodoMemberDto> dtoList = new ArrayList<>();
            for(TodoMember todo : list)
                dtoList.add(TodoMemberDto.builder().userid(todo.getUserid()).username(todo.getUsername()).build());
            return  dtoList;
        }
    }

    @Override
    public boolean changeUser(Map<String, Object> map) {
        return false;
    }

    @Override
    public boolean deleteMember(String userid) {
        return false;
    }
}
