package com.documentprocessing.service;

import com.documentprocessing.entity.Aadhaar;
import com.documentprocessing.repository.DocumentProcessingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AadharService {

    private final DocumentProcessingRepository docRepository;

    public Aadhaar saveDocument(Aadhaar aadhaar){
        return docRepository.save(aadhaar);
    }

    public Aadhaar saveDocument(String businessKey){
        return docRepository.save(Aadhaar.builder()
                .businessKey(businessKey)
                .build());
    }
}
