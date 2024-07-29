package bpm.document.processing.engine.entity;


import jakarta.persistence.*;
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
