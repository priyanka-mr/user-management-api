package com.xyz.usermanagement.service;

import com.xyz.usermanagement.dto.AdminAPIResponse;
import com.xyz.usermanagement.dto.ReqRes;
import com.xyz.usermanagement.entity.*;
import com.xyz.usermanagement.repository.AuditLogRepository;
import com.xyz.usermanagement.repository.MakerCheckerRepository;
import com.xyz.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private MakerCheckerRepository makerCheckerRepository;

    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<User> result = userRepository.findAll();
            if (!result.isEmpty()) {
                reqRes.setUserList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public AdminAPIResponse addUser(ReqRes reqRes) {

        AdminAPIResponse adminAPIResponse = new AdminAPIResponse();
        try {
            MakerCheckerRequest userRequest = new MakerCheckerRequest();
            userRequest.setEmail(reqRes.getEmail());
            userRequest.setName(reqRes.getName());
            userRequest.setRole(reqRes.getRole());
            userRequest.setRequestedBy(reqRes.getCreatedBy());
            userRequest.setPassword(passwordEncoder.encode(reqRes.getPassword()));
            userRequest.setCreatedAt(LocalDateTime.now());
            userRequest.setStatus(Status.PENDING);
            userRequest.setAction(Action.CREATE);
            MakerCheckerRequest userResult = makerCheckerRepository.save(userRequest);
            if (userResult.getId() > 0) {
                adminAPIResponse.setMessage("User saved successfully. Pending Approval");
                adminAPIResponse.setStatusCode(200);

                AuditLog auditLog = new AuditLog();
                auditLog.setAction(Action.CREATE);
                auditLog.setDescription(Action.CREATE + " Request Submitted - " + reqRes.getEmail());
                auditLog.setPerformedBy(reqRes.getCreatedBy());
                auditLog.setTimeStamp(LocalDateTime.now());
                auditLogRepository.save(auditLog);
            }
        } catch (Exception e) {
            adminAPIResponse.setStatusCode(500);
            adminAPIResponse.setMessage(e.getMessage());
        }
        return adminAPIResponse;
    }

    public ReqRes deleteUser(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                MakerCheckerRequest makerCheckerRequest  = makerCheckerRepository.findByEmail(email);
                if (makerCheckerRequest.getAction() == Action.DELETE) {
                    reqRes.setStatusCode(200);
                    reqRes.setMessage("Deletion Requested submitted Already. Pending Approval");
                    return reqRes;
                }
                makerCheckerRequest.setAction(Action.DELETE);
                makerCheckerRequest.setStatus(Status.PENDING);
                makerCheckerRepository.save(makerCheckerRequest);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Deletion Requested submitted. Pending Approval");

                AuditLog auditLog = new AuditLog();
                auditLog.setAction(Action.DELETE);
                auditLog.setDescription(Action.DELETE + " Request Submitted - " + makerCheckerRequest.getEmail());
                auditLog.setPerformedBy(makerCheckerRequest.getRequestedBy());
                auditLog.setTimeStamp(LocalDateTime.now());
                auditLogRepository.save(auditLog);
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public AdminAPIResponse updateUser(ReqRes reqRes) {
        AdminAPIResponse response = new AdminAPIResponse();
        try {
            MakerCheckerRequest makerCheckerRequest = makerCheckerRepository.findByEmail(reqRes.getEmail());
            if (makerCheckerRequest.getStatus() == Status.PENDING) {
                makerCheckerRequest.setName(reqRes.getName());
                makerCheckerRequest.setRole(reqRes.getRole());

                if (makerCheckerRequest.getPassword() != null && !makerCheckerRequest.getPassword().isEmpty()) {
                    makerCheckerRequest.setPassword(passwordEncoder.encode(reqRes.getPassword()));
                }
                makerCheckerRepository.save(makerCheckerRequest);
                response.setStatusCode(200);
                response.setMessage("User updated successfully");
            } else {
                response.setStatusCode(404);
                response.setMessage("User cannot be updated");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return response;
    }

}
