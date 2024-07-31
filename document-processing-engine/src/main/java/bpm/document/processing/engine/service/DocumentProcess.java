package bpm.document.processing.engine.service;

import bpm.document.processing.engine.entity.Aadhaar;
import bpm.document.processing.engine.entity.ProcessDetails;
import bpm.document.processing.engine.repository.DocumentProcessingRepository;
import bpm.document.processing.engine.repository.ProcessRepository;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DocumentProcess implements JavaDelegate {

    private final ProcessRepository processRepository;
    private final ImageReader imageReader;
    private final DocumentProcessingRepository docRepository;

    @Autowired
    public DocumentProcess(ProcessRepository processRepository,
                           ImageReader imageReader,
                           DocumentProcessingRepository docRepository) {
        this.processRepository = processRepository;
        this.imageReader = imageReader;
        this.docRepository = docRepository;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Document processing started");
        String businessKey = delegateExecution.getBusinessKey();
        ProcessDetails processDetails = processRepository.findByBusinessKey(businessKey);
        byte[] file = processDetails.getFile();
        Aadhaar aadhaar = imageReader.readImg(file);
        Aadhaar aadharB = docRepository.findByBusinessKey(businessKey);
        aadharB.setGender(aadhaar.getGender());
        aadharB.setName(aadhaar.getName());
        aadharB.setAddress(aadhaar.getAddress());
        aadharB.setAadhaarNumber(aadhaar.getAadhaarNumber());
        aadharB.setDateOfBirth(aadhaar.getDateOfBirth());
        aadharB.setVid(aadhaar.getVid());
        docRepository.save(aadharB);
        delegateExecution.setVariable("name", aadharB.getName());
        delegateExecution.setVariable("dateOfBirth",aadharB.getDateOfBirth());
        delegateExecution.setVariable("gender", aadharB.getGender());
        delegateExecution.setVariable("aadhaarNumber", aadharB.getAadhaarNumber());
        delegateExecution.setVariable("vid", aadharB.getVid());
        delegateExecution.setVariable("address",aadharB.getAddress());

        log.info("Document processing ends");

    }
}
