package com.xyz.usermanagement.repository;

import com.xyz.usermanagement.entity.MakerCheckerRequest;
import com.xyz.usermanagement.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


public interface MakerCheckerRepository extends JpaRepository<MakerCheckerRequest, Long> {
    List<MakerCheckerRequest> findByStatus(Status status);

    MakerCheckerRequest findByEmail(String email);

}
