package com.documentprocessing.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCamundaResponse {

        String id;
                String name;
                String assignee;
                Date created;
                String due;
                String followUp;
                Date lastUpdated;
                String delegationState;
                String description;
                String executionId;
                String owner;
                String parentTaskId;
                int priority;
                String processDefinitionId;
                String processInstanceId;
                String taskDefinitionKey;
                String caseExecutionId;
                String caseInstanceId;
                String caseDefinitionId;
                String suspended;
                String formKey;
                String camundaFormRef;
                String tenantId;
}
