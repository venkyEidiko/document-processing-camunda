package bpm.document.processing.engine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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
