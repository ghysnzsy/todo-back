package com.todo.todoback.cms.controller;

import com.todo.todoback.cms.service.AdminService;
import com.todo.todoback.dto.TodoMemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminRestController {

    @Autowired
    AdminService adminService;
    @GetMapping("/users/{roleNumber}")
    public ResponseEntity<Map<String, List>> selectMember(@PathVariable("roleNumber") int roleNumber) {
        /*
            null or isEmpty or all -> select All
            else -> where ROLE_USER, ROLE_ADMIN
         */
        log.info("AdminRestController selectMember");
        Map<String, List> map = new HashMap<>();
        
        map.put("result", adminService.selectMember(roleNumber));
        return ResponseEntity.ok(map);
    }
    @PutMapping("/user")
    public ResponseEntity<Boolean> changeUser(@RequestBody Map<String, Object> map) {
        /*
            Block, Request, Finish
         */
        log.info("AdminRestController changeAdmin");
        return ResponseEntity.ok(adminService.changeUser(map));
    }
    @DeleteMapping("/user")
    public ResponseEntity<Boolean> deleteAdmin(@RequestBody Map<String, Object> map) {
        if(!map.isEmpty()) {
            String s = map.get("userid").toString();
            if(!s.isEmpty() && s != null)
                return ResponseEntity.ok(adminService.deleteMember(map.get("userid").toString()));
            else {
                return null;
            }
        }
        else return null;
    }
    @GetMapping("/test/{mi}")
    public ResponseEntity<Map> sampleReturnString(@PathVariable("mi") String mi) {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "a");
        map.put("b", "b");
        map.put("c", "c");
        map.put("d", "input: "+mi);
        List<Map<String, Object>> list = new ArrayList<>();
        for(int i = 0; i<4;++i)
            list.add(map);
        Map<String, List> map2 = new HashMap<>();
        map2.put("result", list);
        return ResponseEntity.ok(map2);
    }
}
