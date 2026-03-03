package workshop.zepcla.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import javax.naming.CannotProceedException;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.appointmentDto.AppointmentPublicCreationDto;
import workshop.zepcla.entities.AppointmentEntity;
import workshop.zepcla.entities.EnterpriseEntity;
import workshop.zepcla.exceptions.appointmentException.AppointmentNotFound;
import workshop.zepcla.exceptions.appointmentException.NoAvaibleAppointment;
import workshop.zepcla.exceptions.appointmentException.NoCancelationAllowed;
import workshop.zepcla.mappers.AppointmentMapper;
import workshop.zepcla.repositories.AppointmentRepository;
import workshop.zepcla.repositories.EnterpriseRepository;

@Service
@AllArgsConstructor
public class AppointmentServicePublic {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentService appointmentService;
    private final EnterpriseRepository enterpriseRepository;

    public String createAppointmentWithoutAccount(AppointmentPublicCreationDto dto) {
        LocalDate date = dto.date_appointment();
        LocalTime time = dto.time_appointment();

        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new CannotProceedException(
                    "on " + date + " at " + time + ". Please select a valid date");
        }

        Long id = dto.enterpriseId();
        EnterpriseEntity enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFound("Enterprise with id " + id + " not found"));

        appointmentService.validateEnterpriseAvailability(enterprise, date, time, dto.duration());

        boolean slotTaken = appointmentRepository.existsByDateAndTimeAndEnterprise(date, time, enterprise);
        if (slotTaken) {
            throw new NoAvaibleAppointment("on " + date + " at " + time);
        }

        AppointmentEntity appointment = appointmentMapper.toEntityForPublicCreation(dto);

        String token = UUID.randomUUID().toString();
        appointment.setToken(token);
        appointment.setStatus("PLANIFIED");
        appointment.setEnterprise(enterprise);

        appointmentRepository.save(appointment);
        return token;
    }

    public AppointmentPublicCreationDto getAppointmentByToken(String token) {
        AppointmentEntity ap = appointmentRepository.findByToken(token)
                .orElseThrow(() -> new AppointmentNotFound(" with token " + token));
        if (ap.getDate().isBefore(LocalDate.now())
                || (ap.getDate().isEqual(LocalDate.now()) && ap.getTime().isBefore(LocalTime.now()))) {
            throw new AppointmentNotFound(" with token " + token + " because it is in the past");
        }
        return appointmentMapper.toPublicCreationDto(ap);
    }

    public AppointmentPublicCreationDto cancelAppointmentByToken(String token) {
        AppointmentEntity appointment = appointmentRepository.findByToken(token)
                .orElseThrow(() -> new AppointmentNotFound(" with token " + token));

        LocalDateTime appointmentDateTime = LocalDateTime.of(appointment.getDate(), appointment.getTime());

        if (appointmentDateTime.minusHours(12).isBefore(LocalDateTime.now())) {
            throw new NoCancelationAllowed(
                    "You can't cancel an appointment less than 12 hours before the appointment date");
        }

        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);

        return appointmentMapper.toPublicCreationDto(appointment);
    }
}
