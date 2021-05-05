package com.powell.cardapi.controller;

import com.powell.cardapi.domain.User;
import com.powell.cardapi.domain.UserUpdateRequest;
import com.powell.cardapi.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @NonNull
    private UserService userService;

    @GetMapping("/v1")
    public List<String> getUserIds() {
        return userService.getUsers();
    }

    @GetMapping("/v1/{id}")
    public User getUser(
            @PathVariable String id
    )  {
        return userService.getUser(id);
    }

    @PutMapping("/v1/{id}")
    public User updateUser(
            @PathVariable String id,
            @RequestBody UserUpdateRequest updateRequest
    ) {
        return userService.updateUser(id, updateRequest);
    }

    @PostMapping("/v1")
    public User addUser(
            @RequestBody UserUpdateRequest updateRequest
    ) {
        return userService.addUser(updateRequest);
    }

    @DeleteMapping("/v1/{id}")
    public void removeUser(
            @PathVariable String id
    ) {
        userService.deleteUser(id);
    }
}
