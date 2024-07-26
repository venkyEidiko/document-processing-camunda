package com.documentprocessing.service;

import com.documentprocessing.entity.ProcessDetails;
import com.documentprocessing.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessService {

    private final ProcessRepository processRepository;

    public ProcessDetails saveProcess(ProcessDetails processDetails, String businessKey){

       processDetails.setBusinessKey(businessKey);
       return processRepository.save(processDetails);
    }

    public ProcessDetails saveProcess(String processInstanceId, ProcessDetails processDetails){

        processDetails.setProcessInstanceId(processInstanceId);
        return processRepository.save(processDetails);
    }




}
