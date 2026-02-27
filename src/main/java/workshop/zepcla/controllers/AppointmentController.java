package workshop.zepcla.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import workshop.zepcla.dto.appointmentDto.AppointmentCreationDto;
import workshop.zepcla.dto.appointmentDto.AppointmentDto;
import workshop.zepcla.services.AppointmentService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/logged/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentCreationDto dto) {
        AppointmentDto created = appointmentService.createAppointment(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<AppointmentDto> cancelAppointment(@PathVariable Long id) {
        AppointmentDto cancelled = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(cancelled);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<AppointmentDto> list = appointmentService.getAllAppointments();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        AppointmentDto dto = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDate(@RequestParam LocalDate date) {
        List<AppointmentDto> list = appointmentService.getAppointmentsByDate(date);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<List<AppointmentDto>> getMyAppointments() {
        List<AppointmentDto> list = appointmentService.getAppointmentsByCurrentClient();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/by-client/{id_client}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByClient(@PathVariable Long id_client) {
        List<AppointmentDto> list = appointmentService.getAppointmentsByClient(id_client);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/by-creator/{id_creator}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByCreator(@PathVariable Long id_creator) {
        List<AppointmentDto> list = appointmentService.getAppointmentsByCreator(id_creator);
        return ResponseEntity.ok(list);
    }
}
