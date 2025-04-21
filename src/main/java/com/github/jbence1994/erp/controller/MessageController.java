package com.github.jbence1994.erp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    public ResponseEntity<String> getMessage() {
        return ResponseEntity.status(HttpStatus.OK).body("Hello World!");
    }
}
