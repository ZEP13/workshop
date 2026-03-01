package workshop.zepcla.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.appointmentDto.AppointmentPublicCreationDto;
import workshop.zepcla.entities.AppointmentEntity;
import workshop.zepcla.exceptions.appointmentException.AppointmentNotFound;
import workshop.zepcla.mappers.AppointmentMapper;
import workshop.zepcla.repositories.AppointmentRepository;

@Service
@AllArgsConstructor
public class AppointmentServicePublic {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    public void createAppointmentWithoutAccount(AppointmentPublicCreationDto dto) {
        AppointmentEntity appointment = appointmentMapper.toEntityForPublicCreation(dto);
        appointment.setToken(UUID.randomUUID().toString());
        appointment.setStatus("PLANIFIED");
        appointmentRepository.save(appointment);
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
            throw new AppointmentNotFound(
                    "You can't cancel an appointment less than 12 hours before the appointment date");
        }

        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);

        return appointmentMapper.toPublicCreationDto(appointment);
    }
}
