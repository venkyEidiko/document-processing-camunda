package com.documentprocessing.repository;

import com.documentprocessing.entity.ProcessDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessRepository extends JpaRepository<ProcessDetails, Integer> {
}
