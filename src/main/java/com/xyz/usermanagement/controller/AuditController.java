package com.xyz.usermanagement.controller;

import com.xyz.usermanagement.entity.AuditLog;
import com.xyz.usermanagement.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AuditController {
    @Autowired
    private AuditService auditService;

    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        List<AuditLog> auditLogs = auditService.getLogs();
        return ResponseEntity.ok(auditLogs);
    }
}
