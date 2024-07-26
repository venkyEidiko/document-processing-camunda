package bpm.document.processing.engine.service;

import bpm.document.processing.engine.entity.ProcessDetails;
import bpm.document.processing.engine.repository.ProcessRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentProcess implements JavaDelegate {

    private final ProcessRepository processRepository;
    @Autowired
    public DocumentProcess(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Document processing start");
        String businessKey = delegateExecution.getBusinessKey();
        System.out.println(businessKey);
        ProcessDetails task = processRepository.findByBusinessKey(businessKey).orElseThrow(null);
        System.out.println(task);
        System.out.println("Document processing...");
    }
}
