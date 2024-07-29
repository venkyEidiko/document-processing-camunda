package bpm.document.processing.engine.repository;

import bpm.document.processing.engine.entity.ProcessDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessRepository extends JpaRepository<ProcessDetails, Integer> {

    ProcessDetails findByBusinessKey(String businessKey);
}
