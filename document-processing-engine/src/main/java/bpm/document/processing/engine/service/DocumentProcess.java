package bpm.document.processing.engine.service;

import bpm.document.processing.engine.entity.ProcessDetails;
import bpm.document.processing.engine.repository.ProcessRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class DocumentProcess implements JavaDelegate {

    @Autowired
    ProcessRepository processRepository;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String businessKey = delegateExecution.getBusinessKey();
        ProcessDetails task = processRepository.findByBusinessKey(businessKey);
        System.out.println(task);
        System.out.println("Document processing...");
    }
}
