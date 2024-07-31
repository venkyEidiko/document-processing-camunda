package com.documentprocessing.model.camundavariable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CamundaChildVariables {

    private String type;
    private Object value;

}
