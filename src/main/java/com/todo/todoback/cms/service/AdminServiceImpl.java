package com.todo.todoback.cms.service;

import com.todo.todoback.cms.repository.TodoCMSMemberRepository;
import com.todo.todoback.domain.Role;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.todo.todoback.cms.dto.TodoCMSMemberDto;
import com.todo.todoback.domain.TodoMember;
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
    TodoCMSMemberRepository todoMemberRepository;

    @Override
    public List<TodoCMSMemberDto> selectMember(int roleNumber) {
        if(roleNumber == 0) {
            //return todoMemberRepository.findAll();
            List<TodoMember> list = todoMemberRepository.findAll();

            List<TodoCMSMemberDto> dtoList = new ArrayList<>();
            //list.stream().map(m -> new TodoMemberDto(m.getId(), m.getUserid(), m.getUsername(), m.getRole())).toList();
            for(TodoMember todo : list)
                dtoList.add(TodoCMSMemberDto.builder().userid(todo.getUserid()).username(todo.getUsername()).build());
            return  dtoList;
        }
        else {
            String role = Role.values()[roleNumber].toString();
            List<TodoMember> list = todoMemberRepository.findAllByRole(role);
            List<TodoCMSMemberDto> dtoList = new ArrayList<>();
            for(TodoMember todo : list)
                dtoList.add(TodoCMSMemberDto.builder().userid(todo.getUserid()).username(todo.getUsername()).build());
            return  dtoList;
        }
    }

    @Override
    public int changeUser(Map<String, Object> map) {
        return todoMemberRepository.updateStatByUserId(map.get("userid").toString(), map.get("stat").toString());
    }

    @Override
    public int deleteMember(String userid) {
        return todoMemberRepository.deleteByUserid(userid);
    }


}
