package com.powell.cardapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powell.cardapi.domain.User;
import com.powell.cardapi.domain.UserUpdateRequest;
import com.powell.cardapi.service.CardService;
import com.powell.cardapi.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String ROOT_URL = "/api/user";
    private static final String USER_ID = "aUserId123";
    @Test
    public void getUserIds_callsUnderlyingApi() throws Exception {
        List<String> userIds = asList("aUserId", "anotherUserId");
        when(userService.getUsers()).thenReturn(userIds);

        mockMvc.perform(get(format("%s/%s", ROOT_URL, "v1")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userIds)));
    }

    @Test
    public void getUser_callsUnderlyingApi() throws Exception {
        User user = new User();
        user.setFirstName("David");
        when(userService.getUser(USER_ID)).thenReturn(user);

        mockMvc.perform(get(format("%s/%s/%s", ROOT_URL, "v1", USER_ID)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void updateUser_callsUnderlyingApi() throws Exception {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setFirstName("David");
        User user = new User();
        user.setFirstName("David");
        when(userService.updateUser(USER_ID, userUpdateRequest)).thenReturn(user);

        mockMvc.perform(put(format("%s/%s/%s", ROOT_URL, "v1", USER_ID))
                .content(objectMapper.writeValueAsString(userUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void registerUser_callsUnderlyingApi() throws Exception{
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setFirstName("David");
        User user = new User();
        user.setFirstName("David");
        when(userService.addUser(userUpdateRequest)).thenReturn(user);

        mockMvc.perform(post(format("%s/%s", ROOT_URL, "v1"))
                .content(objectMapper.writeValueAsString(userUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void removeUser_callsUnderlyingApi() throws Exception {

        mockMvc.perform(delete(format("%s/%s/%s", ROOT_URL, "v1", USER_ID)))
                .andExpect(status().isOk());

        verify(userService).deleteUser(USER_ID);
    }

}