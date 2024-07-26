package com.documentprocessing.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Aadher_details")
public class Aadhaar {
	
	
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private Integer id;
	   private String name;
	   private String aadhaarNumber;
	   private String dateOfBirth;
	   private String gender;
	   private String address;
	   private String businessKey;
	   private String vid;
	   private String enrolmentNumber;

	

}
