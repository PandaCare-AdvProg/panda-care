package id.ac.ui.cs.advprog.pandacare.request;
import id.ac.ui.cs.advprog.pandacare.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String name;
  private String email;
  private String password;
  private Role role;
  private String nik;
  private String address;
  private String phonenum;
  private String workingAddress;
  private String specialty;
  private String medicalHistory;
  

}