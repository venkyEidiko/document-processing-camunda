package com.documentprocessing.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j

@RestController
public class DocumentProcessController {
	
	@Autowired
	private DocumentProcessingService documentProcessingService;

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
		
		list.add(new Aadhaar(1, "Narendra reddy pallaki", "828487733013", "07-04-2001", "male", "2-40,west bazar ,marripudi,prakasam,Andhra pradesh ,523 240"));
		
		return ResponseEntity.ok(list);
	}
}
