package workshop.zepcla.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workshop.zepcla.dto.appointmentDto.AppointmentCreationDto;
import workshop.zepcla.dto.appointmentDto.AppointmentDto;
import workshop.zepcla.entities.AppointmentEntity;
import workshop.zepcla.exceptions.appointmentException.AppointmentNotFound;
import workshop.zepcla.exceptions.appointmentException.ClientAlreadyHaveAppointment;
import workshop.zepcla.exceptions.appointmentException.ClientCantHaveAppointmentInPast;
import workshop.zepcla.exceptions.appointmentException.NoAvaibleAppointment;
import workshop.zepcla.mappers.AppointmentMapper;
import workshop.zepcla.repositories.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentService(AppointmentRepository appointmentRepository,
            AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
    }

    public AppointmentDto createAppointment(AppointmentCreationDto dto) {
        LocalDate date = dto.date_appointment();
        LocalTime time = dto.time_appointment();

        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new ClientCantHaveAppointmentInPast("on " + date + " at " + time + "Please select a valid date");
        }

        Long clientId = dto.id_client().id();
        boolean clientConflict = appointmentRepository.existsByClientIdAndDateAndTime(client_id, date, time);
        if (clientConflict) {
            throw new ClientAlreadyHaveAppointment("on " + date + " at " + time);
        }

        boolean slotTaken = appointmentRepository.existsByDateAndTime(date, time);
        if (slotTaken) {
            throw new NoAvaibleAppointment("on " + date + " at " + time);
        }

        AppointmentEntity entity = appointmentMapper.toEntityForCreation(dto);

        AppointmentEntity saved = appointmentRepository.save(entity);
        return appointmentMapper.toDto(saved);
    }

    public AppointmentDto cancelAppointment(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFound(" with id " + id));

        appointment.setStatus("ANNULE");
        return appointmentMapper.toDto(appointmentRepository.save(appointment));
    }

    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto getAppointmentById(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFound(" with id " + id));
        return appointmentMapper.toDto(appointment);
    }

    public List<AppointmentDto> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByDate(date)
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAppointmentsByClient(Long id_client) {
        return appointmentRepository.findByClientId(id_client)
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAppointmentsByCreator(Long id_creator) {
        return appointmentRepository.findByClientId(id_creator)
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }
}
