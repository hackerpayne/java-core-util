package com.qyhstech.core.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping("/")
    public String index() {

        return "test";
    }

    @GetMapping("/get/{id}")
    public String postUser(@PathVariable("id") String id) {

        return "Test" + id;
    }
}
