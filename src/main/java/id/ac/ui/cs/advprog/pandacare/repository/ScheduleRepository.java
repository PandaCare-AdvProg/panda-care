package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @EntityGraph(attributePaths = {"doctor"})
    List<Schedule> findByDoctor_Id(Long doctorId);

    Optional<Schedule> findById(Long scheduleId);
    
    List<Schedule> findByDayOfWeek(String dayOfWeek);    
}