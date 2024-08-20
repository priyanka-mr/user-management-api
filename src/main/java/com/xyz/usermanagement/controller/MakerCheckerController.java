package com.xyz.usermanagement.controller;

import com.xyz.usermanagement.dto.AdminAPIResponse;
import com.xyz.usermanagement.dto.CheckerRequest;
import com.xyz.usermanagement.entity.MakerCheckerRequest;
import com.xyz.usermanagement.service.MakerCheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class MakerCheckerController {

    @Autowired
    private MakerCheckerService makerCheckerService;

    @GetMapping("/requests/pending")
    public ResponseEntity<List<MakerCheckerRequest>> getPendingRequests() {
        List<MakerCheckerRequest> requests = makerCheckerService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/requests/approve")
    public ResponseEntity<AdminAPIResponse> approveRequest(@RequestBody CheckerRequest checkerRequest) {
        return ResponseEntity.ok(makerCheckerService.approveRequest(checkerRequest));

    }

    @PostMapping("/requests/reject")
    public ResponseEntity<AdminAPIResponse> rejectRequest(@RequestBody CheckerRequest checkerRequest) {
        return ResponseEntity.ok(makerCheckerService.rejectRequest(checkerRequest));
    }
}
