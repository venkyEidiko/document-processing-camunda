package com.documentprocessing.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    private String fileName;
    private String fileExtension;
    private String fileSize;
    private String taskId;
    private String processInstanceId;
    private String businessKey;
    private byte[] file;

}
