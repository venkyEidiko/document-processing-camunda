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

    String fileName;
    String fileExtension;
    String fileSize;

    public Map<String,Object> getVarablesMap(){
        Map<String, Object> map = new HashMap<>();

        CamundaChildVariables fileName = CamundaChildVariables.builder()
                .type("String")
                .value(this.fileName)
                .build();
        map.put("fileName", fileName);

        CamundaChildVariables fileExtension = CamundaChildVariables.builder()
                .type("String")
                .value(this.fileExtension)
                .build();
        map.put("fileExtension", fileExtension);

        CamundaChildVariables fileSize = CamundaChildVariables.builder()
                .type("String")
                .value(this.fileSize)
                .build();
        map.put("fileExtension", fileSize);

        return map;
    }
}
