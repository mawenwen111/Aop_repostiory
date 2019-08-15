package com.example.demo.controller;

import com.example.demo.DemoApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class AspectController {
    @DemoApplication.Operation(value = "查询")
    @GetMapping("/{id}/{name}")
    public String listFocus(@PathVariable("id") String id, @PathVariable("name") String name, HttpServletRequest request){
        int i = 1/0;
        return "ok";
    }
}
