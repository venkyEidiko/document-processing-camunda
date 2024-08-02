package com.documentprocessing.service;

import com.documentprocessing.entity.ProcessDetails;
import com.documentprocessing.model.camundavariable.CamundaVariables;
import com.documentprocessing.model.request.*;
import com.documentprocessing.model.response.StartProcessResponse;
import com.documentprocessing.model.response.TaskCamundaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EngineService {


    @Value("${app.urls.startProcess}")
    private String startProcess;
    @Value("${app.urls.getUnassignTask}")
    private String getUnassignTask;
    @Value("${app.urls.claimTask}")
    private String claimTask;
    @Value("${app.urls.unclaimTask}")
    private String unclaimTask;
    @Value("${app.urls.taskCount}")
    private String taskCount;
    @Value("${app.urls.completeTask}")
    private String completeTask;

    private final Random random = new Random();
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
        log.info(startProcess);
        StartProcessResponse response = webClientService.postCall(startProcess,
                camundaVariables, StartProcessResponse.class);

        processService.saveProcess(response.getId(),processDetails);
        log.info("response from camunda engine {}", response);
        return response;
    }

    public List<ProcessDetails> getUnassignTask(){
        TaskCamundaResponse[] response = webClientService.postCall(getUnassignTask, new UnAssignRequest(), com.documentprocessing.model.response.TaskCamundaResponse[].class);

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
        TaskCamundaResponse[] response = webClientService.postCall(getUnassignTask, new AssigntaskRequest(), com.documentprocessing.model.response.TaskCamundaResponse[].class);

        return generateProcessDetails(response);
    }

    public void claimTask(String taskId){
        String url = String.format(claimTask,taskId);
        log.info(url);
        webClientService.postCall(url,new ClaimRequestCamunda(),Object.class);
    }

    public void unClaimTask(String taskId) {
        String url = String.format(unclaimTask,taskId);
        log.info(url);
        webClientService.postCall(url);
    }

    public void completeTask(String taskId){
        String url = String.format(completeTask,taskId);
        log.info(url);
        webClientService.postCall(url,new CompleteTaskRequestCamunda(),Object.class);
    }


    private String generateBusinessKey() {

        return "DOC" + (this.random.nextInt(9000) + 1000);
    }



}
