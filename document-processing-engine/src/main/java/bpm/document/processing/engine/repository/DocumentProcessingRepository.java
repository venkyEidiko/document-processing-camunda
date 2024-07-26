package bpm.document.processing.engine.repository;

import bpm.document.processing.engine.entity.Aadhaar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentProcessingRepository extends JpaRepository<Aadhaar, Integer> {

}
