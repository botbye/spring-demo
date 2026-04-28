package com.botbye.springdemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @PostMapping
    public ResponseEntity<Object> post() {
        return ResponseEntity.ok().body("hello world!");
    }
}
