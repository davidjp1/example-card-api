package com.powell.cardapi.controller;

import com.powell.cardapi.domain.User;
import com.powell.cardapi.domain.UserUpdateRequest;
import com.powell.cardapi.service.UserService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("Get a list of all User IDs")
    public List<String> getUserIds() {
        return userService.getUsers();
    }

    @GetMapping("/v1/{id}")
    @ApiOperation("Get User details")
    public User getUser(
            @PathVariable String id
    )  {
        log.info("getting user id={}", id);
        return userService.getUser(id);
    }

    @PutMapping("/v1/{id}")
    @ApiOperation("Update a User")
    public User updateUser(
            @PathVariable String id,
            @RequestBody UserUpdateRequest updateRequest
    ) {
        log.info("updating user id={} update={}", id, updateRequest);
        return userService.updateUser(id, updateRequest);
    }

    @PostMapping("/v1")
    @ApiOperation("Register a new User")
    public User registerUser(
            @RequestBody UserUpdateRequest updateRequest
    ) {
        log.info("adding new user request={}", updateRequest);
        return userService.addUser(updateRequest);
    }

    @DeleteMapping("/v1/{id}")
    @ApiOperation("Remove a User")
    public void removeUser(
            @PathVariable String id
    ) {
        log.warn("removing user id={}", id);
        userService.deleteUser(id);
    }
}
