package com.todo.todoback.service;

import com.todo.todoback.domain.TodoMember;
import com.todo.todoback.repository.TodoMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    TodoMemberRepository memberRepository;

    @Transactional
    @Override
    public TodoMember createMember(Map<String, String> map) {

        return memberRepository.save(
                TodoMember.builder()
                        .userid( map.get("userid") )
                        .userpw( map.get("userpw") )
                        .userbirth( map.get("userbirth") )
                        .useremail( map.get("useremail") )
                        .username( map.get("username") )
                        .createdate( map.get("createdate") )
                        .build()
        );
    }

}
