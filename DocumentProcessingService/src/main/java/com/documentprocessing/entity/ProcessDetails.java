package com.documentprocessing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessDetails {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String fileName;
    private String fileExtension;
    private String fileSize;
    private String taskId;
    private String processInstanceId;
    private String businessKey;
    @Lob
    @Column(name = "file", columnDefinition = "LONGBLOB")
    private byte[] file;

}
