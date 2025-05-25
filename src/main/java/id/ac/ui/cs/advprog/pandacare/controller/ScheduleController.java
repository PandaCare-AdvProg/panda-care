package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        log.info("Creating schedule: {}", schedule);
        Schedule saved = scheduleService.createSchedule(schedule);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getSchedulesByDoctor(@PathVariable Long doctorId) {
        log.info("Getting schedules for doctor id {}", doctorId);
        return ResponseEntity.ok(scheduleService.getSchedulesByDoctor(doctorId));
    }

    @PostMapping("/{id}/consultations")
    public ResponseEntity<Schedule> addConsultation(
            @PathVariable("id") Long scheduleId,
            @RequestBody Consultation consultation
    ) {
        log.info("Adding consultation {} to schedule {}", consultation, scheduleId);
        Schedule updated = scheduleService.addConsultationToSchedule(scheduleId, consultation);
        return ResponseEntity.ok(updated);
    }
}