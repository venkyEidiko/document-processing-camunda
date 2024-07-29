package com.documentprocessing.service;

import com.documentprocessing.entity.ProcessDetails;
import com.documentprocessing.model.camundaVariable.CamundaVariables;
import com.documentprocessing.model.request.*;
import com.documentprocessing.model.response.StartProcessResponse;
import com.documentprocessing.model.response.TaskCamundaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public List<ProcessDetails> getUnassignTask(){
        TaskCamundaResponse[] response = webClientService.postCall(GET_UNASSIGN_TASK, new UnAssignRequest(), com.documentprocessing.model.response.TaskCamundaResponse[].class);
        List<ProcessDetails> list = generateProcessDetails(response);
        return  list;
    }

    private List<ProcessDetails> generateProcessDetails(TaskCamundaResponse[] response) {
        ProcessDetails processDetails;
        List<ProcessDetails> list = new ArrayList<>();
        for(TaskCamundaResponse task : response){
            processDetails = processService.getProcessDetails(task.getProcessInstanceId());
            if(processDetails.getTaskId()==null || processDetails.equals("")) {
                processDetails.setTaskId(task.getId());
                processDetails = processService.save(processDetails);
            }
            list.add(processDetails);
        }
        return list;
    }

    public List<ProcessDetails> getassignTask(){
        TaskCamundaResponse[] response = webClientService.postCall(GET_UNASSIGN_TASK, new AssigntaskRequest(), com.documentprocessing.model.response.TaskCamundaResponse[].class);
        List<ProcessDetails> list = generateProcessDetails(response);
        return list;
    }

    public void claimTask(String taskId){
        String url = String.format(CLAIM_TASK,taskId);
        webClientService.postCall(url,new ClaimRequestCamunda(),Object.class);
    }

    public void unClaimTask(String taskId) {
        String url = String.format(UNCLAIM_TASK,taskId);
        webClientService.postCall(url);
    }

    public void completeTask(String taskId){
        String url = String.format(CLAIM_TASK,taskId);
        webClientService.postCall(url,new CompleteTaskRequestCamunda(),Object.class);
    }


    private String generateBusinessKey() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        return "DOC" + randomNumber;
    }



}
