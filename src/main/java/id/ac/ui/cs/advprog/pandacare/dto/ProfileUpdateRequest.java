package id.ac.ui.cs.advprog.pandacare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {
    private String email;
    private String name;
    private String address;
    private String phonenum;
    private String medicalHistory; 
    private String specialty;      
    private String workingAddress; 
}