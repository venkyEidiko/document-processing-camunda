package com.documentprocessing.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AadhaarDto {

        private String name;
        private String aadhaarNumber;
        private String dateOfBirth;
        private String gender;
        private String aadhaarImage;


}
