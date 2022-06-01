package com.todo.todoback.service;

import com.todo.todoback.domain.Role;
import com.todo.todoback.domain.TodoMember;
import com.todo.todoback.dto.TodoMemberDto;
import com.todo.todoback.repository.TodoMemberRepository;
import com.todo.todoback.util.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService, Serializable {

    @Autowired
    TodoMemberRepository memberRepository;

    /**
     * 회원가입
     * @param map
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Transactional
    @Override
    public TodoMemberDto createMember( Map<String, String> map ) throws NoSuchAlgorithmException {

        // 1.멤버를 생성한다.
        SimpleDateFormat curDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        // 권한
        // u - ROLE_USER
        // a -  ROLE_ADMIN
        // m - ROLE_MASTER
        String paramRole = map.get("role");
        String authority =  paramRole.equals("u") ? Role.ROLE_USER.getValue() : ( paramRole.equals("a") ? Role.ROLE_ADMIN.getValue() : Role.ROLE_MASTER.getValue() );

        int resVal = memberRepository.createMember(
                TodoMemberDto.builder()
                        .userid( map.get("userId") )
                        .userpw( SHA256.encrypt( map.get("userPw") ) ) // pw 암호화 : sha256
                        .userbirth( map.get("userBirth") )
                        .useremail( map.get("userEmail") )
                        .username( map.get("userName") )
                        .role( authority )
                        .createdate( curDate.format( date ) )
                        .build()
        );

        // 2. 멤버생성이 성공한 경우.
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

    /**
     * userId 중복 체크
     * @param userId
     * @return
     */
    @Override
    public boolean checkDuplicateId( String userId ) {

        // 1.id 중복체크를 한다.
        int useridCnt = memberRepository.checkUserid( userId );

        // 2.중복된 id가 존재하는 경우 : true값 반환
        if ( useridCnt > 0 ) return true;

        // 3.중복되지 않은 경우 : false값 반환
        return false;
    }

    @Override
    public boolean checkDuplicateEmail( String userEmail ) {

        // 1.email 중복체크를 한다.
        int useremailCnt = memberRepository.checkUserEmail( userEmail );

        // 2.중복된 email이 존재하는 경우 : true값 반환
        if ( useremailCnt > 0 ) return true;

        // 3.중복되지 않은 경우 : false값 반환
        return false;
    }

    @Override
    public TodoMemberDto signIn( Map<String, String> map ) throws NoSuchAlgorithmException {

        String userId   = map.get("userId");
        String userPw = SHA256.encrypt( map.get("userPw") );

        TodoMember todoMember = memberRepository.signIn(userId, userPw);

        if ( todoMember != null ) {
            System.out.println(todoMember.getUsername());
            return TodoMemberDto.builder()
                    .id( todoMember.getId() )
                    .userid( userId )
                    .username( todoMember.getUsername() )
                    .useremail( todoMember.getUseremail() )
                    .userbirth( todoMember.getUserbirth() )
                    .role( todoMember.getRole().getValue() )
                    .build();
        }

        return null;
    }

}
