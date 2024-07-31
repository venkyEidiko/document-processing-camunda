package bpm.document.processing.engine.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import bpm.document.processing.engine.entity.Aadhaar;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class ImageReader {

    public Aadhaar readImg(byte[] file) throws IOException, TesseractException {
        // Load image
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file);

        // Load image
        BufferedImage image = ImageIO.read(byteArrayInputStream);

        // Preprocess image
        BufferedImage filteredImage = applyMedianFilter(image);
        BufferedImage contrastedImage = enhanceContrast(filteredImage);

        Path tempImageFile = Files.createTempFile("aadhaar", ".jpeg");
        ImageIO.write(contrastedImage, "jpg", tempImageFile.toFile());

        // Initialize Tesseract OCR
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("eng+tel+hin");

        // Perform OCR
        String text = tesseract.doOCR(tempImageFile.toFile());

        // Process extracted text
        String filterText = processText(text);
        System.out.println("English text :"+filterText);

        // Optional: Extract details from filtered text
         Aadhaar details = extractDetails(filterText);
         System.out.println("Aadhaar Details :" + details);
         Files.deleteIfExists(tempImageFile);
        return details;
    }
    

    // Method to apply median filter for noise removal
    public static Aadhaar extractDetails(String text) {
        Aadhaar aadhaar = new Aadhaar();

        // Define patterns
        String[] lines = text.split("\\R");

        Pattern dobPattern = Pattern.compile("(\\d{2}/\\d{2}/\\d{4}|\\d{2}-\\d{2}-\\d{4})");
        Pattern genderPattern = Pattern.compile("MALE|FEMALE|TRANSGENDER", Pattern.CASE_INSENSITIVE);
        Pattern aadhaarNumberPattern = Pattern.compile("\\b\\d{4} \\d{4} \\d{4}\\b");
        Pattern vidPattern = Pattern.compile("VID (\\d{4} \\d{4} \\d{4} \\d{4})");


        String dobLine = null;
        int dobIndex = -1;

        // Find the line with date of birth
        for (int i = 0; i < lines.length; i++) {
            Matcher dobMatcher = dobPattern.matcher(lines[i]);
            if (dobMatcher.find()) {
                dobLine = lines[i];
                dobIndex = i;
                aadhaar.setDateOfBirth(dobMatcher.group());
                break;
            }
        }

        // Extract the name from the line above the date of birth line
        if (dobIndex > 0) {
            String nameLine = lines[dobIndex - 1];
            aadhaar.setName(nameLine.trim());
        }

        // Find and set other details
        for (String line : lines) {
            Matcher genderMatcher = genderPattern.matcher(line);
            if (genderMatcher.find()) {
                aadhaar.setGender(genderMatcher.group());
            }

            Matcher aadhaarNumberMatcher = aadhaarNumberPattern.matcher(line);
            if (aadhaarNumberMatcher.find() && aadhaar.getAadhaarNumber()==null) {
                aadhaar.setAadhaarNumber(aadhaarNumberMatcher.group().replaceAll(" ", ""));
            }

            Matcher vidMatcher = vidPattern.matcher(line);
            if (vidMatcher.find()) {
                aadhaar.setVid(vidMatcher.group(1).replaceAll(" ", ""));
            }

        }

        return aadhaar;
    }
    private BufferedImage applyMedianFilter(BufferedImage image) {
        // Median filter kernel size
    	   int kernelSize = 3;
           float[] kernel = new float[kernelSize * kernelSize];
           for (int i = 0; i < kernel.length; i++) {
               kernel[i] = 1.0f / (kernelSize * kernelSize);
           }

        // Create convolution kernel
        Kernel convolutionKernel = new Kernel(kernelSize, kernelSize, kernel);
        ConvolveOp convolveOp = new ConvolveOp(convolutionKernel, ConvolveOp.EDGE_NO_OP, null);

        // Apply the filter to the image
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        convolveOp.filter(image, result);

        return result;
    }

    // Method to enhance contrast
    private BufferedImage enhanceContrast(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        // Convert image to binary (black and white)
        return convertToBinary(result);
    }

    // Method to convert image to binary
    private BufferedImage convertToBinary(BufferedImage image) {
        BufferedImage binaryImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = binaryImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return binaryImage;
    }

    private static String processText(String text) {
        StringBuilder processed = new StringBuilder();
        String[] lines = text.split("\n");
        Pattern pattern = Pattern.compile("[a-zA-Z0-9\\s/-]+");

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            StringBuilder englishPart = new StringBuilder();

            while (matcher.find()) {
                englishPart.append(matcher.group().trim()).append(" ");
            }
            if (englishPart.length() > 0) {
                processed.append(englishPart.toString().trim()).append("\n");
            }
        }

        return processed.toString();
    }
}
