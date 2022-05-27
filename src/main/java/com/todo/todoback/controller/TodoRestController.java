package com.todo.todoback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TodoRestController {

    @PostMapping("/todo")
    public ResponseEntity<String> add(@RequestBody Map<String, String> map) {
        System.out.println( String.format( "Hello World! %s\n", map.get("item") ));
        return ResponseEntity.ok("ok");
    }

}
