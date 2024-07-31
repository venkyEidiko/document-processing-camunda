package com.documentprocessing.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartProcessResponse {

    private String id;
    private String definitionId;
    private String businessKey;
    private String caseInstanceId;
    private String ended;
    private String suspended;
    private String tenantId;
}
