package com.xyz.usermanagement.controller;

import com.xyz.usermanagement.dto.AdminAPIResponse;
import com.xyz.usermanagement.dto.ReqRes;
import com.xyz.usermanagement.service.AdminUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @GetMapping("/get-all-users")
    public ResponseEntity<ReqRes> getAllUser() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    @GetMapping("/add-user")
    public ResponseEntity<AdminAPIResponse> addUser(@RequestBody ReqRes req) {
        return ResponseEntity.ok(adminUserService.addUser(req));
    }

    @DeleteMapping("/delete-user/{email}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable String email) {
        return ResponseEntity.ok(adminUserService.deleteUser(email));
    }

    @PostMapping("/update-user")
    public ResponseEntity<AdminAPIResponse> updateUser(@RequestBody ReqRes req) {
        return ResponseEntity.ok(adminUserService.updateUser(req));
    }

}
