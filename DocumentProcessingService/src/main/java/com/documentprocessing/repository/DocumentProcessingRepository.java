package com.documentprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.documentprocessing.entity.Aadhaar;

import java.util.Optional;

@Repository
public interface DocumentProcessingRepository extends JpaRepository<Aadhaar, Integer> {



    Optional<Aadhaar> findByBusinessKey(String businessKey);


}
