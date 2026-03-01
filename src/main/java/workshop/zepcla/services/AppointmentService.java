package workshop.zepcla.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.appointmentDto.AppointmentCreationByAdminDto;
import workshop.zepcla.dto.appointmentDto.AppointmentCreationDto;
import workshop.zepcla.dto.appointmentDto.AppointmentDto;
import workshop.zepcla.dto.appointmentDto.AppointmentPublicCreationDto;
import workshop.zepcla.entities.AppointmentEntity;
import workshop.zepcla.entities.UserEntity;
import workshop.zepcla.exceptions.appointmentException.AppointmentNotFound;
import workshop.zepcla.exceptions.appointmentException.ClientAlreadyHaveAppointment;
import workshop.zepcla.exceptions.appointmentException.ClientCantHaveAppointmentInPast;
import workshop.zepcla.exceptions.appointmentException.NoAvaibleAppointment;
import workshop.zepcla.exceptions.userException.UserIdNotFoundException;
import workshop.zepcla.mappers.AppointmentMapper;
import workshop.zepcla.repositories.AppointmentRepository;
import workshop.zepcla.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

// add update logic
// add super finder lis find?clientId=1&date=2024-06-30&time=14:00
// add creator update cancel logic

@Service
@AllArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    public AppointmentDto createAppointment(AppointmentCreationDto dto) {

        LocalDate date = dto.date_appointment();
        LocalTime time = dto.time_appointment();

        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new ClientCantHaveAppointmentInPast(
                    "on " + date + " at " + time + ". Please select a valid date");
        }

        UserEntity clientEntity = userService.getCurrentUserEntity();

        boolean clientConflict = appointmentRepository.existsByClientAndDateAndTime(clientEntity, date, time);
        if (clientConflict) {
            throw new ClientAlreadyHaveAppointment("on " + date + " at " + time);
        }

        boolean slotTaken = appointmentRepository.existsByDateAndTime(date, time);
        if (slotTaken) {
            throw new NoAvaibleAppointment("on " + date + " at " + time);
        }

        AppointmentEntity entity = appointmentMapper.toEntityForCreation(dto);
        entity.setClient(clientEntity);

        AppointmentEntity saved = appointmentRepository.save(entity);
        return appointmentMapper.toDto(saved);
    }

    public AppointmentDto createAppointmentAsAdmin(AppointmentCreationByAdminDto dto) {

        LocalDate date = dto.date_appointment();
        LocalTime time = dto.time_appointment();

        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new ClientCantHaveAppointmentInPast(
                    "on " + date + " at " + time + ". Please select a valid date");
        }

        UserEntity creatorEntity = userService.getCurrentUserEntity();

        boolean clientConflict = appointmentRepository.existsByClientAndDateAndTime(creatorEntity, date, time);
        if (clientConflict) {
            throw new ClientAlreadyHaveAppointment("on " + date + " at " + time);
        }

        boolean slotTaken = appointmentRepository.existsByDateAndTime(date, time);
        if (slotTaken) {
            throw new NoAvaibleAppointment("on " + date + " at " + time);
        }

        AppointmentEntity entity = appointmentMapper.toEntityForCreationByAdmin(dto);
        entity.setCreator(creatorEntity);
        entity.setClient(dto.id_client() != null ? userRepository.findById(dto.id_client().id())
                .orElseThrow(() -> new UserIdNotFoundException("User ID not found: " + dto.id_client())) : null);

        AppointmentEntity saved = appointmentRepository.save(entity);
        return appointmentMapper.toDto(saved);
    }

    public AppointmentDto cancelAppointment(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFound(" with id " + id));

        LocalDateTime appointmentDateTime = LocalDateTime.of(appointment.getDate(), appointment.getTime());

        if (appointmentDateTime.minusHours(12).isBefore(LocalDateTime.now())) {
            throw new AppointmentNotFound(
                    "You can't cancel an appointment less than 12 hours before the appointment date");
        }
        appointment.setStatus("CANCELLED");
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

    public List<AppointmentDto> getMyAppointmentsByDate(LocalDate date) {
        UserEntity clientEntity = userService.getCurrentUserEntity();
        return appointmentRepository.findByClientAndDate(clientEntity, date)
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAppointmentsByCurrentClient() {

        UserEntity clientEntity = userService.getCurrentUserEntity();
        return appointmentRepository.findByClient(clientEntity)
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAppointmentsByClient(Long id_client) {

        UserEntity clientEntity = userRepository.findById(id_client)
                .orElseThrow(() -> new UserIdNotFoundException("User ID not found: " + id_client));
        return appointmentRepository.findByClient(clientEntity)
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAppointmentsByCreator(Long id_creator) {
        UserEntity creatorEntity = userRepository.findById(id_creator)
                .orElseThrow(() -> new UserIdNotFoundException("User ID not found: " + id_creator));
        return appointmentRepository.findByCreator(creatorEntity)
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }
}
