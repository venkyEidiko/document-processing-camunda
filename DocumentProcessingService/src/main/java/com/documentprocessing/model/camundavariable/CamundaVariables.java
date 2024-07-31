package com.documentprocessing.model.camundavariable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CamundaVariables {

    private Map<String,Object> variables;
    private String businessKey;
}
