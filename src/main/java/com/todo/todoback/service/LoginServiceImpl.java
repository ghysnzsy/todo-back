package com.todo.todoback.service;

import com.todo.todoback.domain.Role;
import com.todo.todoback.dto.TodoMemberDto;
import com.todo.todoback.repository.TodoMemberRepository;
import com.todo.todoback.util.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    TodoMemberRepository memberRepository;

    @Transactional
    @Override
    public TodoMemberDto createMember(Map<String, String> map) throws NoSuchAlgorithmException {

        // 1.id 중복체크를 한다.
        int useridCnt = memberRepository.checkUserid(map.get("userid"));

        // 중복된 id가 존재하는 경우
        if ( useridCnt > 0 ) return null;

        // 2.멤버를 생성한다.
        SimpleDateFormat curDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        int resVal = memberRepository.createMember(
                TodoMemberDto.builder()
                        .userid( map.get("userid") )
                        .userpw( SHA256.encrypt( map.get("userpw") ) ) // pw 암호화 : sha256
                        .userbirth( map.get("userbirth") )
                        .useremail( map.get("useremail") )
                        .username( map.get("username") )
//                        .role(Role.USER )
                        .createdate( curDate.format( date ) )
                        .build()
        );

        // 3. 멤버생성이 성공한 경우.
        if ( resVal == 1 ) {
            return TodoMemberDto.builder()
                    .userid(map.get("userid"))
                    .userbirth(map.get("userbirth"))
                    .useremail(map.get("useremail"))
                    .username(map.get("username"))
                    .build();
        }

        return null;
    }

}
