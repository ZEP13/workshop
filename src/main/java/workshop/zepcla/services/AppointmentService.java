package workshop.zepcla.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.appointmentDto.AppointmentCreationDto;
import workshop.zepcla.dto.appointmentDto.AppointmentDto;
import workshop.zepcla.dto.userDto.UserDto;
import workshop.zepcla.entities.AppointmentEntity;
import workshop.zepcla.entities.UserEntity;
import workshop.zepcla.exceptions.appointmentException.AppointmentNotFound;
import workshop.zepcla.exceptions.appointmentException.ClientAlreadyHaveAppointment;
import workshop.zepcla.exceptions.appointmentException.ClientCantHaveAppointmentInPast;
import workshop.zepcla.exceptions.appointmentException.NoAvaibleAppointment;
import workshop.zepcla.mappers.AppointmentMapper;
import workshop.zepcla.mappers.UserMapper;
import workshop.zepcla.repositories.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public AppointmentDto createAppointment(AppointmentCreationDto dto) {
        LocalDate date = dto.date_appointment();
        LocalTime time = dto.time_appointment();

        // Vérifier que la date/heure n'est pas dans le passé
        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new ClientCantHaveAppointmentInPast(
                    "on " + date + " at " + time + ". Please select a valid date");
        }

        // Récupérer l'utilisateur (client) depuis l'ID
        UserDto client = userService.getUserById(dto.id_client().id());

        UserEntity clientEntity = userMapper.toEntity(client);
        // Vérifier si le client a déjà un rendez-vous à cette date/heure
        boolean clientConflict = appointmentRepository.existsByIdClientAndDateAndTime(clientEntity, date, time);
        if (clientConflict) {
            throw new ClientAlreadyHaveAppointment("on " + date + " at " + time);
        }

        // Vérifier si le créneau est déjà pris
        boolean slotTaken = appointmentRepository.existsByDateAndTime(date, time);
        if (slotTaken) {
            throw new NoAvaibleAppointment("on " + date + " at " + time);
        }

        // Mapper le DTO vers l'entité et sauvegarder
        AppointmentEntity entity = appointmentMapper.toEntityForCreation(dto);
        entity.setIdClient(clientEntity); // S'assurer que l'entité a bien le client

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
        UserDto client = userService.getUserById(id_client);

        UserEntity clientEntity = userMapper.toEntity(client);
        return appointmentRepository.findByIdClient(clientEntity)
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAppointmentsByCreator(Long id_creator) {
        UserDto creator = userService.getUserById(id_creator);

        UserEntity creatorEntity = userMapper.toEntity(creator);
        return appointmentRepository.findByIdCreator(creatorEntity)
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }
}
