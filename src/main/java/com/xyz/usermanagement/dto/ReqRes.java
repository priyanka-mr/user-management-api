package com.xyz.usermanagement.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xyz.usermanagement.entity.Role;
import com.xyz.usermanagement.entity.Status;
import com.xyz.usermanagement.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String email;
    private String password;
    private Role role;
    private String createdBy;
    private Status status;
    private LocalDateTime createdAt;
    private User user;
    private List<User> userList;
}
