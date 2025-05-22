package id.ac.ui.cs.advprog.pandacare.service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;

public interface DoctorService {
    List<Doctor> getAllDoctors();
    Optional<Doctor> getDoctorById(Long id);
    Doctor createDoctor(Doctor doctor);
    Doctor updateDoctor(Long id, Doctor doctor);
    void deleteDoctor(Long id);
    List<Doctor> searchByName(String name);
    List<Doctor> searchBySpecialty(String specialty);
    List<Doctor> searchByScheduleDay(DayOfWeek day);
    List<Doctor> searchDoctors(String name, String specialty, DayOfWeek day);
}