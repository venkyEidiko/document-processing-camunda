package com.documentprocessing.repository;

import com.documentprocessing.entity.Aadhaar;
import com.documentprocessing.entity.ProcessDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessRepository extends JpaRepository<ProcessDetails, Integer> {
    ProcessDetails findByProcessInstanceId(String processInstanceId);

    Optional<ProcessDetails> findByBusinessKey(String businessKey);

}
