package com.documentprocessing.model.request;

import com.documentprocessing.model.camundavariable.CamundaChildVariables;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartProcessRequest {

    String name;
    String extension;
    String size;

    public Map<String,Object> getVarablesMap(){
        Map<String, Object> map = new HashMap<>();
        String type = "String";
        CamundaChildVariables fileName = CamundaChildVariables.builder()
                .type(type)
                .value(this.name)
                .build();
        map.put("fileName", fileName);

        CamundaChildVariables fileExtension = CamundaChildVariables.builder()
                .type(type)
                .value(this.extension)
                .build();
        map.put("fileExtension", fileExtension);

        CamundaChildVariables fileSize = CamundaChildVariables.builder()
                .type(type)
                .value(this.size)
                .build();
        map.put("fileExtension", fileSize);

        return map;
    }
}
