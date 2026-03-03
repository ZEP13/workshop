package workshop.zepcla.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

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
        Long enterpriseId = dto.enterpriseId();

        if (!appointmentService.validateAppointmentTimeIsFree(date, time, enterpriseId)) {
            throw new NoAvaibleAppointment("on " + date + " at " + time);
        }

        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new NoAvaibleAppointment(
                    "on " + date + " at " + time + ". Please select a valid date");
        }

        if (dto.email_client() == null || dto.email_client().isEmpty()) {
            throw new NoAvaibleAppointment("Email is required for appointment creation");
        }

        if (dto.duration() == null || dto.duration() <= 0) {
            throw new NoAvaibleAppointment("Duration must be a positive integer");
        }

        EnterpriseEntity enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new AppointmentNotFound(
                        "Enterprise with id " + enterpriseId + " not found"));

        appointmentService.validateEnterpriseAvailability(enterprise, date, time, dto.duration());

        boolean slotTaken = appointmentRepository.existsByDateAndTimeAndEnterprise_Id(date, time, enterpriseId);
        if (slotTaken) {
            throw new NoAvaibleAppointment("on " + date + " at " + time);
        }

        AppointmentEntity appointment = appointmentMapper.toEntityForPublicCreation(dto);

        appointment.setEnterprise(enterprise);
        appointment.setStatus("PLANIFIED");
        String token = UUID.randomUUID().toString();
        appointment.setToken(token);

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
