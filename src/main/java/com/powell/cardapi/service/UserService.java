package com.powell.cardapi.service;

import com.powell.cardapi.domain.User;
import com.powell.cardapi.domain.UserUpdateRequest;
import com.powell.cardapi.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserService {
    @NonNull
    private UserRepository userRepository;

    public List<String> getUsers() {
        return userRepository.findAll().stream()
                .map(User::getId)
                .collect(toList());
    }

    public User getUser(String id){
        return userRepository.findById(id).orElse(null);
    }

    public User addUser(UserUpdateRequest request) {
        return updateUser(new User(), request);
    }

    public User updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User Not Found"));
        return updateUser(user, request);
    }

    private User updateUser(User user, UserUpdateRequest request){
        setIfNotNull(request.getFirstName(), user::setFirstName);
        setIfNotNull(request.getSecondName(), user::setSecondName);
        setIfNotNull(request.getPreferredCurrency(), user::setPreferredCurrency);
        setIfNotNull(request.getBirthDate(), user::setBirthDate);
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public <T> void setIfNotNull(T field, Consumer<T> setter) {
        if(field != null) {
            setter.accept(field);
        }
    }
}
