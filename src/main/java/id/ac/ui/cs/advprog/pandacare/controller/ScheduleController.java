package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        Schedule saved = scheduleService.createSchedule(schedule);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getSchedulesByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByDoctor(doctorId));
    }

    @PostMapping("/{id}/consultations")
    public ResponseEntity<Schedule> addConsultation(
            @PathVariable("id") Long scheduleId,
            @RequestBody Consultation consultation
    ) {
        Schedule updated = scheduleService.addConsultationToSchedule(scheduleId, consultation);
        return ResponseEntity.ok(updated);
    }
}