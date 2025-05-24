package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.dto.ConsultationHistoryDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ProfileDTO;
import id.ac.ui.cs.advprog.pandacare.enums.ConsultationStatus;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.pandacare.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConsultationRepository consultationRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private Patient patient;
    private Doctor doctor;
    private Consultation consultation;

    @BeforeEach
    void setUp() {
        // Set up test data
        patient = new Patient();
        patient.setId(1L);
        patient.setName("Patient Name");
        patient.setEmail("patient@example.com");
        patient.setNik("1234567890");
        patient.setAddress("Patient Address");
        patient.setPhonenum("123456789");
        patient.setRole(Role.PATIENT);
        patient.setMedicalHistory("Test medical history");

        doctor = new Doctor();
        doctor.setId(2L);
        doctor.setName("Doctor Name");
        doctor.setEmail("doctor@example.com");
        doctor.setNik("0987654321");
        doctor.setAddress("Doctor Address");
        doctor.setPhonenum("987654321");
        doctor.setRole(Role.DOCTOR);
        doctor.setSpecialty("Cardiology");
        doctor.setWorkingAddress("Hospital Address");

        consultation = new Consultation();
        consultation.setId(1L);
        consultation.setPatient(patient);
        consultation.setDoctor(doctor);
        consultation.setDayOfWeek(DayOfWeek.MONDAY);
        consultation.setScheduledTime(LocalTime.of(10, 0));
        consultation.setStatus(ConsultationStatus.COMPLETED);
        consultation.setNotes("Regular checkup");
    }

    // ID-based tests

    @Test
    void getUserProfile_patientProfile_returnsCorrectDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));

        ProfileDTO result = profileService.getUserProfile(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Patient Name", result.getName());
        assertEquals("Test medical history", result.getMedicalHistory());
        assertEquals(Role.PATIENT, result.getRole());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserProfile_doctorProfile_returnsCorrectDTO() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(doctor));

        ProfileDTO result = profileService.getUserProfile(2L);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Doctor Name", result.getName());
        assertEquals("Cardiology", result.getSpecialty());
        assertEquals("Hospital Address", result.getWorkingAddress());
        assertEquals(Role.DOCTOR, result.getRole());
        verify(userRepository).findById(2L);
    }

    @Test
    void getUserProfile_userNotFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> profileService.getUserProfile(99L));
        verify(userRepository).findById(99L);
    }

    @Test
    void updateUserProfile_validPatientUpdate_returnsUpdatedProfile() {
        ProfileDTO updateDTO = ProfileDTO.builder()
                .email("updated@example.com")
                .name("Updated Name")
                .address("Updated Address")
                .phonenum("999999999")
                .medicalHistory("Updated history")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(userRepository.save(any(Patient.class))).thenAnswer(i -> i.getArgument(0));

        ProfileDTO result = profileService.updateUserProfile(1L, updateDTO);

        assertNotNull(result);
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Address", result.getAddress());
        assertEquals("999999999", result.getPhonenum());
        assertEquals("Updated history", result.getMedicalHistory());

        verify(userRepository).findById(1L);
        verify(userRepository).save(patient);
    }

    @Test
    void updateUserProfile_validDoctorUpdate_returnsUpdatedProfile() {
        ProfileDTO updateDTO = ProfileDTO.builder()
                .email("updated-doc@example.com")
                .name("Updated Doctor")
                .address("Updated Doc Address")
                .phonenum("888888888")
                .specialty("Neurology")
                .workingAddress("New Hospital")
                .build();

        when(userRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(userRepository.save(any(Doctor.class))).thenAnswer(i -> i.getArgument(0));

        ProfileDTO result = profileService.updateUserProfile(2L, updateDTO);

        assertNotNull(result);
        assertEquals("updated-doc@example.com", result.getEmail());
        assertEquals("Updated Doctor", result.getName());
        assertEquals("Updated Doc Address", result.getAddress());
        assertEquals("888888888", result.getPhonenum());
        assertEquals("Neurology", result.getSpecialty());
        assertEquals("New Hospital", result.getWorkingAddress());

        verify(userRepository).findById(2L);
        verify(userRepository).save(doctor);
    }

    @Test
    void deleteUserAccount_existingUser_deletesUser() {
        doNothing().when(userRepository).deleteById(1L);
        
        profileService.deleteUserAccount(1L);
        
        verify(userRepository).deleteById(1L);
    }

    @Test
    void getUserConsultationHistory_patientUser_returnsPatientConsultations() {
        List<Consultation> consultations = Arrays.asList(consultation);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(consultationRepository.findByPatientId(1L)).thenReturn(consultations);

        List<ConsultationHistoryDTO> result = profileService.getUserConsultationHistory(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Doctor Name", result.get(0).getDoctorName());
        assertEquals("Patient Name", result.get(0).getPatientName());
        assertEquals(DayOfWeek.MONDAY, result.get(0).getDayOfWeek());
        
        verify(userRepository).findById(1L);
        verify(consultationRepository).findByPatientId(1L);
    }

    @Test
    void getUserConsultationHistory_doctorUser_returnsDoctorConsultations() {
        List<Consultation> consultations = Arrays.asList(consultation);
        
        when(userRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(consultationRepository.findByDoctorId(2L)).thenReturn(consultations);

        List<ConsultationHistoryDTO> result = profileService.getUserConsultationHistory(2L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Doctor Name", result.get(0).getDoctorName());
        assertEquals("Patient Name", result.get(0).getPatientName());
        
        verify(userRepository).findById(2L);
        verify(consultationRepository).findByDoctorId(2L);
    }

    @Test
    void getUserConsultationHistory_noConsultations_returnsEmptyList() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(consultationRepository.findByPatientId(1L)).thenReturn(List.of());

        List<ConsultationHistoryDTO> result = profileService.getUserConsultationHistory(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(userRepository).findById(1L);
        verify(consultationRepository).findByPatientId(1L);
    }
    
    // Email-based tests
    
    @Test
    void getUserProfileByEmail_patientProfile_returnsCorrectDTO() {
        when(userRepository.findByEmail("patient@example.com")).thenReturn(Optional.of(patient));

        ProfileDTO result = profileService.getUserProfileByEmail("patient@example.com");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Patient Name", result.getName());
        assertEquals("Test medical history", result.getMedicalHistory());
        assertEquals(Role.PATIENT, result.getRole());
        verify(userRepository).findByEmail("patient@example.com");
    }

    @Test
    void getUserProfileByEmail_doctorProfile_returnsCorrectDTO() {
        when(userRepository.findByEmail("doctor@example.com")).thenReturn(Optional.of(doctor));

        ProfileDTO result = profileService.getUserProfileByEmail("doctor@example.com");

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Doctor Name", result.getName());
        assertEquals("Cardiology", result.getSpecialty());
        assertEquals("Hospital Address", result.getWorkingAddress());
        assertEquals(Role.DOCTOR, result.getRole());
        verify(userRepository).findByEmail("doctor@example.com");
    }

    @Test
    void getUserProfileByEmail_userNotFound_throwsException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
            () -> profileService.getUserProfileByEmail("nonexistent@example.com"));
        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void updateUserProfileByEmail_validPatientUpdate_returnsUpdatedProfile() {
        ProfileDTO updateDTO = ProfileDTO.builder()
                .email("updated@example.com")
                .name("Updated Name")
                .address("Updated Address")
                .phonenum("999999999")
                .medicalHistory("Updated history")
                .build();

        when(userRepository.findByEmail("patient@example.com")).thenReturn(Optional.of(patient));
        when(userRepository.save(any(Patient.class))).thenAnswer(i -> i.getArgument(0));

        ProfileDTO result = profileService.updateUserProfileByEmail("patient@example.com", updateDTO);

        assertNotNull(result);
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Address", result.getAddress());
        assertEquals("999999999", result.getPhonenum());
        assertEquals("Updated history", result.getMedicalHistory());

        verify(userRepository).findByEmail("patient@example.com");
        verify(userRepository).save(patient);
    }

    @Test
    void deleteUserAccountByEmail_existingUser_deletesUser() {
        when(userRepository.findByEmail("patient@example.com")).thenReturn(Optional.of(patient));
        doNothing().when(userRepository).delete(patient);
        
        profileService.deleteUserAccountByEmail("patient@example.com");
        
        verify(userRepository).findByEmail("patient@example.com");
        verify(userRepository).delete(patient);
    }

    @Test
    void getUserConsultationHistoryByEmail_patientUser_returnsPatientConsultations() {
        List<Consultation> consultations = Arrays.asList(consultation);
        
        when(userRepository.findByEmail("patient@example.com")).thenReturn(Optional.of(patient));
        when(consultationRepository.findByPatientId(1L)).thenReturn(consultations);

        List<ConsultationHistoryDTO> result = profileService.getUserConsultationHistoryByEmail("patient@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Doctor Name", result.get(0).getDoctorName());
        assertEquals("Patient Name", result.get(0).getPatientName());
        
        verify(userRepository).findByEmail("patient@example.com");
        verify(consultationRepository).findByPatientId(1L);
    }

    @Test
    void getUserConsultationHistoryByEmail_doctorUser_returnsDoctorConsultations() {
        List<Consultation> consultations = Arrays.asList(consultation);
        
        when(userRepository.findByEmail("doctor@example.com")).thenReturn(Optional.of(doctor));
        when(consultationRepository.findByDoctorId(2L)).thenReturn(consultations);

        List<ConsultationHistoryDTO> result = profileService.getUserConsultationHistoryByEmail("doctor@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Doctor Name", result.get(0).getDoctorName());
        assertEquals("Patient Name", result.get(0).getPatientName());
        
        verify(userRepository).findByEmail("doctor@example.com");
        verify(consultationRepository).findByDoctorId(2L);
    }
}