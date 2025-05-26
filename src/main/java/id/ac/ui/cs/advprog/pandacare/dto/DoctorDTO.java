package id.ac.ui.cs.advprog.pandacare.dto;

public class DoctorDTO {
    
    private Long id;
    private String specialty;
    private String working_address;
    
    public DoctorDTO(Long id, String specialty, String working_address) {
        this.id = id;
        this.specialty = specialty;
        this.working_address = working_address;
    }

    public Long getId() {
        return id;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getWorking_adress() {
        return working_address;
    }

}