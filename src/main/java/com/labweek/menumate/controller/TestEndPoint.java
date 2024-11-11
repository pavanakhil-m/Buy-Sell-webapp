package com.labweek.menumate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TestEndPoint {

    @GetMapping("/test")
    public ResponseEntity<List<String>> testMessages(){
        System.out.println("*************************sent**********************");
        return ResponseEntity.ok(Arrays.asList(
                "This is a Protected secret1",
                "This is a protected secret 2")
        );
    }
}
