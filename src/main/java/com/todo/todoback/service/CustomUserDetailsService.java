package com.todo.todoback.service;

import com.todo.todoback.domain.TodoMember;
import com.todo.todoback.repository.TodoMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
//@Component("userDetailsService")
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    TodoMemberRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUserId(username)
                .map(user -> createUser( user ) )
                .orElseThrow( () -> new UsernameNotFoundException( username + " -> 데이터베이스에서 찾을 수 없습니다." ) );
    }

    public User createUser(TodoMember user) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add( new SimpleGrantedAuthority( user.getRole().toString()) );
        return new User( user.getUserid(), user.getUserpw(), authorityList );
    }

}
