package com.powell.cardapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @GetMapping("/v1")
    public void getUser() {

    }

    @PutMapping("/v1")
    public void updateUser() {

    }

    @PostMapping("/v1")
    public void addUser() {

    }

    @DeleteMapping("/v1")
    public void removeUser() {

    }

    @GetMapping("/v1/formatted")
    public void getUserDetailsFormatted() {

    }
}
