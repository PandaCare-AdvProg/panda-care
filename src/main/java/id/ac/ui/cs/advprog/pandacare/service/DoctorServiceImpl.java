package id.ac.ui.cs.advprog.pandacare.service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.repository.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository repo;

    @Autowired
    public DoctorServiceImpl(DoctorRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return repo.findAll();
    }

    @Override
    public Optional<Doctor> getDoctorById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        return repo.save(doctor);
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor doctor) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setName(doctor.getName());
                    existing.setEmail(doctor.getEmail());
                    existing.setAddress(doctor.getAddress());
                    existing.setWorkingAddress(doctor.getWorkingAddress());
                    existing.setPhonenum(doctor.getPhonenum());
                    existing.setSpecialty(doctor.getSpecialty());
                    return repo.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + id));
    }

    @Override
    public void deleteDoctor(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Doctor> searchByName(String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Doctor> searchBySpecialty(String specialty) {
        return repo.findBySpecialtyContainingIgnoreCase(specialty);
    }

    @Override
    public List<Doctor> searchByScheduleDay(DayOfWeek day) {
        return repo.findByScheduleDay(day);
    }

    public List<Doctor> searchDoctors(String name, String specialty, DayOfWeek day) {
    List<Doctor> doctors = getAllDoctors();

    if (name != null && !name.isBlank()) {
        doctors.retainAll(searchByName(name));
    }
    if (specialty != null && !specialty.isBlank()) {
        doctors.retainAll(searchBySpecialty(specialty));
    }
    if (day != null) {
        doctors.retainAll(searchByScheduleDay(day));
    }
    return doctors;

}
}
