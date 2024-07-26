package com.documentprocessing.service;

import com.documentprocessing.entity.Aadhaar;
import com.documentprocessing.entity.ProcessDetails;
import com.documentprocessing.model.camundaVariable.CamundaVariables;
import com.documentprocessing.model.request.StartProcessRequest;
import com.documentprocessing.model.response.StartProcessResponse;
import com.documentprocessing.repository.DocumentProcessingRepository;
import com.documentprocessing.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EngineService {

    public static String START_PROCESS_URL = "/process-definition/key/document-processing-engine-process/start";
    public static  String GET_UNASSIGN_TASK = "/task";
    public static String CLAIM_TASK = "/task/%s/claim";
    public static String UNCLAIM_TASK = "/task/%s/unclaim";
    public static String TASK_COUNT = "/history/task/count?taskAssignee=admin";
    public static String COMPLETE_TASK = "/task/%s/complete";


    private final WebClientService webClientService;
    private final AadharService aadharService;
    private final ProcessService processService;


    @Transactional
    public StartProcessResponse startProcess (StartProcessRequest request,
                                              ProcessDetails processDetails){
        String businessKey = generateBusinessKey();
        CamundaVariables camundaVariables = CamundaVariables.builder()
                .variables(request.getVarablesMap())
                .businessKey(businessKey)
                .build();
        processDetails = processService.saveProcess(processDetails, businessKey);
        aadharService.saveDocument(businessKey);
        StartProcessResponse response = webClientService.postCall(START_PROCESS_URL,
                camundaVariables, StartProcessResponse.class);
        processService.saveProcess(response.getId(),processDetails);
        log.info("response from camunda engine {}", response);
        return response;
    }

    private String generateBusinessKey() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        return "DOC" + randomNumber;
    }


}
