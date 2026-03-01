package workshop.zepcla.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.appointmentDto.AppointmentPublicCreationDto;
import workshop.zepcla.services.AppointmentServicePublic;

import java.util.Map;

@RestController
@RequestMapping("/appointments/public")
@AllArgsConstructor
public class AppointmentControllerWithoutAuth {

    private final AppointmentServicePublic appointmentService;

    @PostMapping("/create")
    public ResponseEntity<?> createRdvWithoutAccount(@RequestBody AppointmentPublicCreationDto dto) {
        appointmentService.createAppointmentWithoutAccount(dto);
        return ResponseEntity.ok(Map.of(
                "message", "Rendez-vous créé"));
    }

    // http://localhost:8080/appointments/public/consult?token=654e8fbc-8107-443f-9d58-1baf80e47be4
    @GetMapping("/consult")
    public ResponseEntity<?> consultRdv(@RequestParam String token) {
        var responseDto = appointmentService.getAppointmentByToken(token);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/cancel")
    public ResponseEntity<?> cancelRdv(@RequestParam String token) {
        var responseDto = appointmentService.cancelAppointmentByToken(token);
        return ResponseEntity.ok(Map.of(
                "message", "Rendez-vous annulé",
                "rdv", responseDto));
    }

}
