package com.todo.todoback.cms.controller;

import com.todo.todoback.cms.dto.TodoCMSMemberDto;
import com.todo.todoback.cms.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Integer> changeUser(@RequestBody Map<String, Object> map) {
        /*
            Block, Request, Finish
         */
        log.info("AdminRestController changeAdmin");
        return ResponseEntity.ok(adminService.changeUser(map));
    }
    @DeleteMapping("/user")
    public ResponseEntity<Integer> deleteAdmin(@RequestBody Map<String, Object> map) {
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
    public ResponseEntity<List<TodoCMSMemberDto>> sampleReturnString(@PathVariable("mi") String mi) {
        return ResponseEntity.ok(adminService.selectMember(0));
    }
}
