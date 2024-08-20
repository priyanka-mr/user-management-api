package com.xyz.usermanagement.service;

import com.xyz.usermanagement.dto.AdminAPIResponse;
import com.xyz.usermanagement.dto.CheckerRequest;
import com.xyz.usermanagement.entity.*;
import com.xyz.usermanagement.repository.AuditLogRepository;
import com.xyz.usermanagement.repository.MakerCheckerRepository;
import com.xyz.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MakerCheckerService {

    @Autowired
    private MakerCheckerRepository makerCheckerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuditLogRepository auditLogRepository;

    public List<MakerCheckerRequest> getPendingRequests() {
        return makerCheckerRepository.findByStatus(Status.PENDING);
    }

    public AdminAPIResponse approveRequest(CheckerRequest checkerRequest) {
        String email = checkerRequest.getEmail();
        String checker = checkerRequest.getChecker();
        AdminAPIResponse adminAPIResponse = new AdminAPIResponse();
        MakerCheckerRequest makerCheckerRequest = makerCheckerRepository.findByEmail(email);
        try {
            if (makerCheckerRequest.getStatus() == Status.APPROVED) {
                return adminAPIResponse;
            }
            Action currentAction = makerCheckerRequest.getAction();
            switch (currentAction) {
                case CREATE: {
                    makerCheckerRequest.setStatus(Status.APPROVED);
                    makerCheckerRequest.setCheckedBy(checker);

                    User user = new User();
                    user.setStatus(Status.APPROVED);
                    user.setEmail(makerCheckerRequest.getEmail());
                    user.setPassword(makerCheckerRequest.getPassword());
                    user.setRole(makerCheckerRequest.getRole());
                    user.setName(makerCheckerRequest.getName());
                    user.setCreatedBy(makerCheckerRequest.getRequestedBy());
                    user.setApprovedBy(checker);
                    user.setApprovedAt(LocalDateTime.now());
                    userRepository.save(user);

                    makerCheckerRepository.save(makerCheckerRequest);
                    adminAPIResponse.setStatusCode(200);
                    adminAPIResponse.setMessage("Request Approved");
                    break;
                }
                case DELETE: {
                    makerCheckerRequest.setStatus(Status.APPROVED);
                    makerCheckerRequest.setCheckedBy(checker);
                    Optional<User> user = userRepository.findByEmail(makerCheckerRequest.getEmail());
                    user.ifPresent(value -> userRepository.deleteById(value.getId()));
                    adminAPIResponse.setStatusCode(200);
                    adminAPIResponse.setMessage("Request Approved");
                    break;
                }
                default: {
                    break;
                }
            }
        } catch (Exception e) {
            adminAPIResponse.setStatusCode(500);
            adminAPIResponse.setMessage("Error occurred while approving request");
            throw new RuntimeException(e);
        }

        AuditLog auditLog = new AuditLog();
        auditLog.setAction(Action.APPROVE);
        auditLog.setDescription(makerCheckerRequest.getEmail() + " " + makerCheckerRequest.getAction() + " Request Approved");
        auditLog.setPerformedBy(checker);
        auditLog.setTimeStamp(LocalDateTime.now());
        auditLogRepository.save(auditLog);

        return adminAPIResponse;
    }

    public AdminAPIResponse rejectRequest(CheckerRequest checkerRequest) {
        String email = checkerRequest.getEmail();
        String checker = checkerRequest.getChecker();
        AdminAPIResponse adminAPIResponse = new AdminAPIResponse();
        MakerCheckerRequest makerCheckerRequest = makerCheckerRepository.findByEmail(email);
        try {
            if (makerCheckerRequest.getStatus() == Status.REJECTED){
                return adminAPIResponse;
            }
            makerCheckerRequest.setStatus(Status.REJECTED);
            makerCheckerRequest.setCheckedBy(checker);
            makerCheckerRepository.save(makerCheckerRequest);
            adminAPIResponse.setStatusCode(200);
            adminAPIResponse.setMessage("Request Rejected");
        } catch (RuntimeException e) {
            adminAPIResponse.setStatusCode(500);
            adminAPIResponse.setMessage("Error occurred while rejecting request");
            throw new RuntimeException(e);
        }

        AuditLog auditLog = new AuditLog();
        auditLog.setAction(Action.REJECT);
        auditLog.setDescription(makerCheckerRequest.getEmail() + " " + makerCheckerRequest.getAction() + " Request Rejected");
        auditLog.setPerformedBy(checker);
        auditLog.setTimeStamp(LocalDateTime.now());
        auditLogRepository.save(auditLog);
        return adminAPIResponse;
    }
}
