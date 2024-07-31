package com.documentprocessing.service;

import com.documentprocessing.entity.ProcessDetails;
import com.documentprocessing.model.camundavariable.CamundaVariables;
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

    private   Random random = new Random();
    public static final String START_PROCESS_URL = "/process-definition/key/document-processing-engine-process/start";
    public static final String GET_UNASSIGN_TASK = "/task";
    public static final String CLAIM_TASK = "/task/%s/claim";
    public static final String UNCLAIM_TASK = "/task/%s/unclaim";
    public static final String TASK_COUNT = "/history/task/count?taskAssignee=admin";
    public static final String COMPLETE_TASK = "/task/%s/complete";


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

        return generateProcessDetails(response);
    }

    private List<ProcessDetails> generateProcessDetails(TaskCamundaResponse[] response) {
        ProcessDetails processDetails;
        List<ProcessDetails> list = new ArrayList<>();
        for(TaskCamundaResponse task : response){
            processDetails = processService.getProcessDetails(task.getProcessInstanceId());

            log.info("processDetails : {} ",processDetails);
            if(processDetails.getTaskId()==null || processDetails.getTaskId().equals("")) {
                processDetails.setTaskId(task.getId());
                processDetails = processService.save(processDetails);
            }
            list.add(processDetails);
        }
        return list;
    }

    public List<ProcessDetails> getassignTask(){
        TaskCamundaResponse[] response = webClientService.postCall(GET_UNASSIGN_TASK, new AssigntaskRequest(), com.documentprocessing.model.response.TaskCamundaResponse[].class);

        return generateProcessDetails(response);
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
        String url = String.format(COMPLETE_TASK,taskId);
        webClientService.postCall(url,new CompleteTaskRequestCamunda(),Object.class);
    }


    private String generateBusinessKey() {

        return "DOC" + this.random.nextInt(9000) + 1000;
    }



}
