package com.xyz.usermanagement.service;

import com.xyz.usermanagement.entity.AuditLog;
import com.xyz.usermanagement.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {
    @Autowired
    private AuditLogRepository auditLogRepository;

    public List<AuditLog> getLogs() {
        return auditLogRepository.findAll();
    }
}
