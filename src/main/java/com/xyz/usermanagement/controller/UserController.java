package com.xyz.usermanagement.controller;

import com.xyz.usermanagement.dto.ReqRes;
import com.xyz.usermanagement.entity.User;
import com.xyz.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    @GetMapping("/user/{email}")
    public ResponseEntity<UserDetails> getUserInfo(@PathVariable String email) throws Exception {
        try {
            return ResponseEntity.ok(userService.loadUserByUsername(email));
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
