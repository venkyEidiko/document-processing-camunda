package com.documentprocessing.controller;

import java.io.IOException;

import java.util.List;

import com.documentprocessing.customexception.IdNotFoundException;
import com.documentprocessing.customexception.InvalidDataException;
import com.documentprocessing.dto.AadhaarDto;
import com.documentprocessing.entity.ProcessDetails;
import com.documentprocessing.model.request.StartProcessRequest;
import com.documentprocessing.service.EngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.documentprocessing.service.DocumentProcessingService;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000/**")
public class DocumentProcessController {

	private final DocumentProcessingService documentProcessingService;
	private final EngineService engineService;

	public DocumentProcessController(DocumentProcessingService documentProcessingService, EngineService engineService) {
		this.documentProcessingService = documentProcessingService;
		this.engineService = engineService;
	}

	@PostMapping("/uploadDocument1")
	public String uploadDocument(@RequestParam("file") MultipartFile file) throws IOException {

		String size = "";
		String fileName = "";
		String extension = "";
		try {
			size = String.valueOf(file.getSize());
			extension = getExtension(file.getOriginalFilename());
			fileName = file.getOriginalFilename();

		} catch (Exception e) {
			log.error(e.getMessage());
		}

		ProcessDetails processDetails = ProcessDetails.builder().fileSize(size).fileExtension(extension)
				.fileName(fileName).file(file.getBytes()).build();

		StartProcessRequest request = StartProcessRequest.builder().size(size).extension(extension)
				.name(fileName).build();

		engineService.startProcess(request, processDetails);

		return "Document Uploaded SuccessFully";
	}

	@GetMapping("/getUnassignTask")
	public List<ProcessDetails> getUnassignTask() {
		return engineService.getUnassignTask();
	}

	@GetMapping("/getAssignTask")
	public List<ProcessDetails> getAssigntask() {
		return engineService.getassignTask();
	}

	@GetMapping("/claimTask")
	public String claimTask(@RequestParam String taskId) {
		engineService.claimTask(taskId);
		return "Claimed SuccessFully";
	}

	@GetMapping("/unClaimTask")
	public String unClaim(@RequestParam String taskId) {
		engineService.unClaimTask(taskId);
		return "Unclaimed SuccessdFully";
	}

	@GetMapping("/completeTask")
	public String completeTask(@RequestParam String taskId) {
		engineService.completeTask(taskId);
		return "Task Completed SuccessFully";
	}

	@GetMapping("/getByBusinesskey/{businesskey}")
	public ResponseEntity<AadhaarDto> getAadhaarDetailsByBusinesskey(@PathVariable String businesskey)
			throws InvalidDataException, IdNotFoundException {

		log.info("Business key : {} ", businesskey);
		AadhaarDto byBusinessKey = documentProcessingService.getByBusinessKey(businesskey);

		if (byBusinessKey != null) {

			return  ResponseEntity.ok(byBusinessKey);

		} else {
			throw new InvalidDataException("Invalid data");
		}
	}

	private String getExtension(String file) {

		return file.substring(file.lastIndexOf('.'));
	}

}
