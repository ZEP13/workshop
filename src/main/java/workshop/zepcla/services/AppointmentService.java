package workshop.zepcla.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    // Créer un RDV sans compte
    public void createAppointmentWithoutAccount(AppointmentPublicCreationDto dto) {
        AppointmentEntity appointment = appointmentMapper.toEntityForPublicCreation(dto);
        appointment.setToken(UUID.randomUUID().toString());
        appointment.setStatus("PLANIFIED");
        appointmentRepository.save(appointment);
    }

    public AppointmentPublicCreationDto getAppointmentByToken(String token) {
        AppointmentEntity ap = appointmentRepository.findByToken(token)
                .orElseThrow(() -> new AppointmentNotFound(" with token " + token));
        return appointmentMapper.toPublicCreationDto(ap);
    }

    public AppointmentPublicCreationDto cancelAppointmentByToken(String token) {
        AppointmentEntity appointment = appointmentRepository.findByToken(token)
                .orElseThrow(() -> new AppointmentNotFound(" with token " + token));
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
        return appointmentMapper.toPublicCreationDto(appointment);
    }

    public AppointmentDto createAppointment(AppointmentCreationDto dto) {

        LocalDate date = dto.date_appointment();
        LocalTime time = dto.time_appointment();

        // Vérifier que la date/heure n'est pas dans le passé
        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new ClientCantHaveAppointmentInPast(
                    "on " + date + " at " + time + ". Please select a valid date");
        }

        // Récupérer l'utilisateur connecté
        UserEntity clientEntity = userService.getCurrentUserEntity(); // méthode existante dans UserService

        // Vérifier si le client a déjà un rendez-vous à cette date/heure
        boolean clientConflict = appointmentRepository.existsByClientAndDateAndTime(clientEntity, date, time);
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
        entity.setClient(clientEntity);

        AppointmentEntity saved = appointmentRepository.save(entity);
        return appointmentMapper.toDto(saved);
    }

    public AppointmentDto cancelAppointment(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFound(" with id " + id));

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
