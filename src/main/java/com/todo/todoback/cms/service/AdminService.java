package com.todo.todoback.cms.service;

import com.todo.todoback.cms.dto.TodoCMSMemberDto;

import java.util.List;
import java.util.Map;

public interface AdminService {

    public List<TodoCMSMemberDto> selectMember(int roleNumber);

    public int changeUser(Map<String, Object> map);

    public int deleteMember(String userid);

}
