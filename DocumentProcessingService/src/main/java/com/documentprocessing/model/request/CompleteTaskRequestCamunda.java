package com.documentprocessing.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompleteTaskRequestCamunda {

    boolean withVariablesInReturn = false;
}
