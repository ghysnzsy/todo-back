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
//@RestController
@RequestMapping("/admin")
public class AdminRestController {
    /*
    @Autowired
    AdminService adminService;
    @GetMapping("/users")
    public ResponseEntity<Map<String, List>> selectMember(@RequestParam(value="role") String role) {

     */
        /*
            null or isEmpty or all -> select All
            else -> where ROLE_USER, ROLE_ADMIN
         */
    /*
        log.info("AdminRestController selectMember");
        Map<String, List> map = new HashMap<>();
        map.put("result", adminService.selectMember(role));
        return ResponseEntity.ok(map);
    }
    @PutMapping("/user")
    public ResponseEntity<String> changeUser(@RequestBody Map<String, Object> map) {

     */
        /*
            Block, Request, Finish
         */
    /*
        log.info("AdminRestController changeAdmin");
        return ResponseEntity.ok(new String());
    }
    @DeleteMapping("/user")
    public ResponseEntity<String> deleteAdmin(@RequestBody Map<String, Object> map) {
        return ResponseEntity.ok(new String());
    }
    */
}
