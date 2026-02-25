package workshop.zepcla.mappers;

import org.springframework.stereotype.Component;
import workshop.zepcla.dto.appointmentDto.AppointmentCreationDto;
import workshop.zepcla.dto.appointmentDto.AppointmentDto;
import workshop.zepcla.dto.userDto.UserDto;
import workshop.zepcla.entities.AppointmentEntity;
import workshop.zepcla.entities.UserEntity;

@Component
public class AppointmentMapper {

    public AppointmentDto toDto(AppointmentEntity appointment) {
        if (appointment == null) {
            return null;
        }

        UserDto clientDto = null;
        if (appointment.getId_client() != null) {
            clientDto = new UserDto(
                    appointment.getId_client().getId(),
                    appointment.getId_client().getEmail(),
                    appointment.getId_client().getUsername(),
                    appointment.getId_client().getFirstname(),
                    appointment.getId_client().getLastname(),
                    appointment.getId_client().getPhone()
            );
        }

        UserDto creatorDto = null;
        if (appointment.getId_creator() != null) {
            creatorDto = new UserDto(
                    appointment.getId_creator().getId(),
                    appointment.getId_creator().getEmail(),
                    appointment.getId_creator().getUsername(),
                    appointment.getId_creator().getFirstname(),
                    appointment.getId_creator().getLastname(),
                    appointment.getId_creator().getPhone()
            );
        }

        return new AppointmentDto(
                appointment.getDate(),
                appointment.getTime(),
                clientDto,
                creatorDto,
                appointment.getStatus()
        );
    }

    public AppointmentEntity toEntityForCreation(AppointmentCreationDto dto) {
        if (dto == null) {
            return null;
        }

        AppointmentEntity entity = new AppointmentEntity();
        entity.setDate(dto.date_appointment());
        entity.setTime(dto.time_appointment());

        if (dto.id_client() != null) {
            UserEntity client = new UserEntity();
            client.setId(dto.id_client().id());
            entity.setId_client(client);
        }

        if (dto.id_creator() != null) {
            UserEntity creator = new UserEntity();
            creator.setId(dto.id_creator().id());
            entity.setId_creator(creator);
        }

        return entity;
    }
}