package com.documentprocessing.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.documentprocessing.dto.AadhaarDto;
import com.documentprocessing.entity.ProcessDetails;

import com.documentprocessing.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.documentprocessing.entity.Aadhaar;
import com.documentprocessing.repository.DocumentProcessingRepository;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class DocumentProcessingService {

	@Autowired
	private DocumentProcessingRepository documentProcessingRepository;

      @Autowired
	 private ProcessRepository processRepository;



	public String processAndSaveAadhaarDetails(MultipartFile file) {
		ITesseract tesseract = new Tesseract();
		tesseract.setDatapath("tessdata");

		try {
			File tempFile = File.createTempFile("temp", file.getOriginalFilename());
			file.transferTo(tempFile);
			String text = tesseract.doOCR(tempFile);
			System.out.println("Extracted Aadhaar data: " + text);

			Aadhaar aadhaar = parseTextToAadhaar(text);
			return "Details saved successfully!";
		} catch (TesseractException | IOException e) {
			e.printStackTrace();
			return "Failed to process and save details";
		}
	}

	private Aadhaar parseTextToAadhaar(String text) {
		String aadhaarNumber = extractPattern(text, "\\b\\d{4} \\d{4} \\d{4}\\b");
		String gender = extractPattern(text, "\\bMALE\\b|\\bFEMALE\\b");
		String name = extractPattern(text, " [A-Za-z]+");
		// String dateOfBirth = extractPattern(text, "\\bDOB[:
		// ]+\\d{2}/\\d{2}/\\d{4}\\b");
		String dateOfBirth = extractPattern(text, "\\b(?:DOB|Year of Birth)[: ]+(\\d{2}/\\d{2}/(\\d{4}))\\b");

		if (dateOfBirth != null) {
			dateOfBirth = dateOfBirth.split(":")[1].trim();
		}
		String address = extractPattern(text, "address:.*");
		if (address != null) {
			address = address.replace("address:", "").trim();
		}

		Aadhaar aadhaar = new Aadhaar();
		aadhaar.setAadhaarNumber(aadhaarNumber);
		aadhaar.setName(name);
		aadhaar.setDateOfBirth(dateOfBirth);
		aadhaar.setGender(gender);
		aadhaar.setAddress(address);
		System.out.println("Aadhaar data : " + aadhaar);
		return aadhaar;
	}

	private String extractPattern(String text, String patternString) {
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	public String readDocumentResolution(MultipartFile file) throws IOException, TesseractException {

		BufferedImage ipimage = ImageIO.read(file.getInputStream());

		double d = ipimage.getRGB(ipimage.getTileWidth() / 2, ipimage.getTileHeight() / 2);

		if (d >= -1.4211511E7 && d < -7254228) {
			processImg(ipimage, 1.4f, -10f);
		} else if (d >= -7254228 && d < -2171170) {
			processImg(ipimage, 1.2f, -5f);
		} else if (d >= -2171170 && d < -1907998) {
			processImg(ipimage, 1.1f, -2f);
		} else if (d >= -1907998 && d < -257) {
			processImg(ipimage, 1.05f, -1f);
		} else if (d >= -257 && d < -1) {
			processImg(ipimage, 1f, -0.5f);
		} else if (d >= -1 && d < 2) {
			processImg(ipimage, 1f, -0.35f);
		}

		return "Document processed successfully";
	}

	public static void processImg(BufferedImage ipimage, float scaleFactor, float offset)
			throws IOException, TesseractException {

		BufferedImage opimage = new BufferedImage(1050, 1024, BufferedImage.TYPE_BYTE_GRAY);

		Graphics2D graphic = opimage.createGraphics();

		graphic.drawImage(ipimage, 0, 0, 1050, 1024, null);
		graphic.dispose();

		// Increase contrast
		RescaleOp rescale = new RescaleOp(scaleFactor, offset, null);
		BufferedImage fopimage = rescale.filter(opimage, null);

		// Save the preprocessed image for verification
		 ImageIO.write(fopimage, "jpg", new File("C:\\Html\\myAadherOpt111.png"));

		// Perform OCR
		Tesseract tesseract = new Tesseract();
		// tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
		tesseract.setDatapath("tessdata");

		String result = tesseract.doOCR(fopimage);
		System.out.println("readDocumentAfterResolution :" + result);
	}

	public AadhaarDto getByBusinessKey(@PathVariable String businessKey)
	{
		Optional<Aadhaar> byBusinessKey = documentProcessingRepository.findByBusinessKey(businessKey);


		if (byBusinessKey.isEmpty()){

			throw  new RuntimeException("Id not found "+businessKey);
		}

		Aadhaar aadhaar = byBusinessKey.get();
		Optional<ProcessDetails> byBusinessKey1 = processRepository.findByBusinessKey(businessKey);

		if (byBusinessKey1.isEmpty()){

			throw  new RuntimeException("Id not found "+businessKey);
		}
		AadhaarDto aadhaarDto =new AadhaarDto();

		String base64Image = Base64.getEncoder().encodeToString(byBusinessKey1.get().getFile());

		 aadhaarDto.setAadhaarImage(base64Image);
		 aadhaarDto.setName(aadhaar.getName());
		 aadhaarDto.setAadhaarNumber(aadhaar.getAadhaarNumber());
		 aadhaarDto.setGender(aadhaar.getGender());
		 aadhaarDto.setDateOfBirth(aadhaar.getDateOfBirth());

		return aadhaarDto;
	}
}
