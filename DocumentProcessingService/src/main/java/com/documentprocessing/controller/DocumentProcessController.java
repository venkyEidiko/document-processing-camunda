package com.documentprocessing.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.documentprocessing.entity.ProcessDetails;
import com.documentprocessing.model.request.StartProcessRequest;
import com.documentprocessing.model.response.AadhaarDto;
import com.documentprocessing.service.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.documentprocessing.entity.Aadhaar;
import com.documentprocessing.service.DocumentProcessingService;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;

import static org.apache.commons.io.FilenameUtils.getExtension;

@Slf4j
@RestController
@CrossOrigin(origins = "http://10.0.2.15:3000/**")
public class DocumentProcessController {
	
	@Autowired
	private DocumentProcessingService documentProcessingService;

    @Autowired
    private EngineService engineService;

	@PostMapping("/uploadDocument")
	public ResponseEntity<String> readDocument(@RequestParam("file") MultipartFile file) {

		log.info("Document file name {}", file.getOriginalFilename());

		String processAndSaveAadherDetails = documentProcessingService.processAndSaveAadhaarDetails(file);
		String originalFilename = file.getOriginalFilename();
		System.out.println("original name "+originalFilename);
	    return ResponseEntity.ok(processAndSaveAadherDetails);
	}
	
	@PostMapping("/readDocumentWithResolution")
	public ResponseEntity<String> readDocumentResolution(@RequestParam("file") MultipartFile file) {

		log.info("Document file name {}", file.getOriginalFilename());

		String processAndSaveAadherDetails;
		try {
			processAndSaveAadherDetails = documentProcessingService.readDocumentResolution(file);
			
			return ResponseEntity.ok(processAndSaveAadherDetails);
		}     catch (Exception e) {
           
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to process the document: " + e.getMessage());
        }
	
		
	}
	
	@GetMapping("/getByAadherNumber/{aadherNumber}")
	public ResponseEntity<List<Aadhaar>> getByAadherNumber(@PathVariable("aadherNumber") String aadherNumber) {

		System.out.println("Aadhaar number : "+aadherNumber);
		log.info("aadher number :{ }",aadherNumber);
		List<Aadhaar>list=new ArrayList<>();
		
		//list.add(new Aadhaar(1, "Narendra reddy pallaki", "828487733013", "07-04-2001", "male", "2-40,west bazar ,marripudi,prakasam,Andhra pradesh ,523 240"));
		
		return ResponseEntity.ok(list);
	}

	@PostMapping("/uploadDocument1")
	public String uploadDocument(@RequestParam("file") MultipartFile file) throws IOException {

		String size = String.valueOf(file.getSize());
		String fileName = file.getOriginalFilename();
		String extension = getExtension(fileName);

		ProcessDetails processDetails = ProcessDetails.builder()
				.fileSize(size)
				.fileExtension(extension)
				.fileName(fileName)
				.file(file.getBytes())
				.build();

		StartProcessRequest request = StartProcessRequest.builder()
                .fileSize(size)
                .fileExtension(extension)
                .fileName(fileName)
                .build();

        engineService.startProcess(request, processDetails);

		return "Document Uploaded SuccessFully";
	}

	@GetMapping("/getUnassignTask")
	public List<ProcessDetails> getUnassignTask(){
		return engineService.getUnassignTask();
	}

	@GetMapping("/getAssignTask")
	public List<ProcessDetails> getAssigntask(){
		return engineService.getassignTask();
	}

	@GetMapping("/claimTask")
	public String claimTask(@RequestParam String taskId){
		engineService.claimTask(taskId);
		return "Claimed SuccessFully";
	}

	@GetMapping("/unClaimTask")
	public String unClaim(@RequestParam String taskId){
		engineService.unClaimTask(taskId);
		return "Unclaimed SuccessdFully";
	}

	@GetMapping("/completeTask")
	public String completeTask(@RequestParam String taskId){
		engineService.completeTask(taskId);
		return "Task Completed SuccessFully";
	}

	@GetMapping("/getByBusinesskey/{businesskey}")
	public  ResponseEntity<AadhaarDto>getAadhaarDetailsByBusinesskey(@PathVariable String businesskey)
	{
		System.out.println(" Business key : "+businesskey);
		AadhaarDto byBusinessKey = documentProcessingService.getByBusinessKey(businesskey);

		if (byBusinessKey!=null){

			return ResponseEntity.ok(byBusinessKey);

		}else {

			throw  new RuntimeException("Invalid data");
		}


	}

	private String getExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf('.'));
	}
}
