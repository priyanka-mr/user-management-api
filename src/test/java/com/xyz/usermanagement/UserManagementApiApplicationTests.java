package com.xyz.usermanagement;

import com.xyz.usermanagement.controller.AdminUserController;
import com.xyz.usermanagement.controller.AuditController;
import com.xyz.usermanagement.controller.MakerCheckerController;
import com.xyz.usermanagement.dto.AdminAPIResponse;
import com.xyz.usermanagement.dto.CheckerRequest;
import com.xyz.usermanagement.dto.ReqRes;
import com.xyz.usermanagement.entity.AuditLog;
import com.xyz.usermanagement.entity.MakerCheckerRequest;
import com.xyz.usermanagement.service.AdminUserService;
import com.xyz.usermanagement.service.AuditService;
import com.xyz.usermanagement.service.MakerCheckerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserManagementApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@InjectMocks
	private AdminUserController adminUserController;
	@Mock
	private AdminUserService adminUserService;
	@InjectMocks
	private AuditController auditController;
	@Mock
	private AuditService auditService;
	@InjectMocks
	private MakerCheckerController makerCheckerController;
	@Mock
	private MakerCheckerService makerCheckerService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getAllUsers() {
		ReqRes reqRes = new ReqRes();
		when(adminUserService.getAllUsers()).thenReturn(reqRes);
		ResponseEntity<ReqRes> response = adminUserController.getAllUser();

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(reqRes);
	}

	@Test
	void addUser() {
		ReqRes req = new ReqRes();
		AdminAPIResponse adminAPIResponse = new AdminAPIResponse();
		when(adminUserService.addUser(any(ReqRes.class))).thenReturn(adminAPIResponse);

		ResponseEntity<AdminAPIResponse> response = adminUserController.addUser(req);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(adminAPIResponse);
	}

	@Test
	void deleteUser() {
		ReqRes reqRes = new ReqRes();
		String email = "admin@gmail.com";
		when(adminUserService.deleteUser(email)).thenReturn(reqRes);

		ResponseEntity<ReqRes> response = adminUserController.deleteUser(email);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(reqRes);
	}

	@Test
	void updateUser() {
		ReqRes req = new ReqRes();
		AdminAPIResponse adminAPIResponse = new AdminAPIResponse();
		when(adminUserService.updateUser(any(ReqRes.class))).thenReturn(adminAPIResponse);

		ResponseEntity<AdminAPIResponse> response = adminUserController.updateUser(req);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(adminAPIResponse);
	}

	@Test
	void getAuditLogs() {
		AuditLog log1 = new AuditLog();
		AuditLog log2 = new AuditLog();
		List<AuditLog> logs = Arrays.asList(log1, log2);

		when(auditService.getLogs()).thenReturn(logs);

		ResponseEntity<List<AuditLog>> response = auditController.getAuditLogs();

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(logs);
	}

	@Test
	void getPendingRequests() {
		MakerCheckerRequest request1 = new MakerCheckerRequest();
		MakerCheckerRequest request2 = new MakerCheckerRequest();
		List<MakerCheckerRequest> requests = Arrays.asList(request1, request2);

		when(makerCheckerService.getPendingRequests()).thenReturn(requests);

		ResponseEntity<List<MakerCheckerRequest>> response = makerCheckerController.getPendingRequests();

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(requests);
	}

	@Test
	void approveRequest() {
		CheckerRequest checkerRequest = new CheckerRequest();
		AdminAPIResponse response = new AdminAPIResponse();

		when(makerCheckerService.approveRequest(any(CheckerRequest.class))).thenReturn(response);

		ResponseEntity<AdminAPIResponse> actualResponse = makerCheckerController.approveRequest(checkerRequest);

		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(response);
	}

	@Test
	void rejectRequest() {
		CheckerRequest checkerRequest = new CheckerRequest();
		checkerRequest.setChecker("admin@gmail.com");
		checkerRequest.setEmail("testuser@gmail.com");
		AdminAPIResponse response = new AdminAPIResponse();

		when(makerCheckerService.rejectRequest(any(CheckerRequest.class))).thenReturn(response);

		ResponseEntity<AdminAPIResponse> actualResponse = makerCheckerController.rejectRequest(checkerRequest);

		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(response);
	}


}
