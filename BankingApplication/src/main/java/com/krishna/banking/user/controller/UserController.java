package com.krishna.banking.user.controller;


import com.krishna.banking.user.dto.UserResponseDto;
import com.krishna.banking.user.security.CurrentUser;
import com.krishna.banking.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final CurrentUser currentUser;
    private final UserService userService;

    public UserController(CurrentUser currentUser,
                          UserService userService) {
        this.currentUser = currentUser;
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(){
        Long id = currentUser.getId();
        UserResponseDto responseDto = userService.getCurrentUser(id);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
