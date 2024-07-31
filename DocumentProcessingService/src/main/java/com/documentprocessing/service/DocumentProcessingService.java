package com.documentprocessing.service;

import java.util.Base64;
import java.util.Optional;

import com.documentprocessing.customexception.IdNotFoundException;
import com.documentprocessing.dto.AadhaarDto;
import com.documentprocessing.entity.ProcessDetails;
import com.documentprocessing.repository.ProcessRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.documentprocessing.entity.Aadhaar;
import com.documentprocessing.repository.DocumentProcessingRepository;

@Service
public class DocumentProcessingService {

	private final DocumentProcessingRepository documentProcessingRepository;

	private final ProcessRepository processRepository;

	public DocumentProcessingService(DocumentProcessingRepository documentProcessingRepository,
			ProcessRepository processRepository) {
		super();
		this.documentProcessingRepository = documentProcessingRepository;
		this.processRepository = processRepository;
	}

	public AadhaarDto getByBusinessKey(@PathVariable String businessKey) throws IdNotFoundException {
		Optional<Aadhaar> byBusinessKey = documentProcessingRepository.findByBusinessKey(businessKey);

		if (byBusinessKey.isEmpty()) {

			throw new IdNotFoundException("Id not found " + businessKey);
		}

		Aadhaar aadhaar = byBusinessKey.get();
		Optional<ProcessDetails> byBusinessKey1 = processRepository.findByBusinessKey(businessKey);

		if (byBusinessKey1.isEmpty()) {

			throw new IdNotFoundException("Id not found " + businessKey);
		}


		AadhaarDto aadhaarDto = new AadhaarDto();

		String base64Image = Base64.getEncoder().encodeToString(byBusinessKey1.get().getFile());

		aadhaarDto.setAadhaarImage(base64Image);
		aadhaarDto.setName(aadhaar.getName());
		aadhaarDto.setAadhaarNumber(aadhaar.getAadhaarNumber());
		aadhaarDto.setGender(aadhaar.getGender());
		aadhaarDto.setDateOfBirth(aadhaar.getDateOfBirth());

		return aadhaarDto;
	}
}
