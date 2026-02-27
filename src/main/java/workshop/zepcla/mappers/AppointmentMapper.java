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
        if (appointment.getClient() != null) {
            clientDto = new UserDto(
                    appointment.getClient().getId(),
                    appointment.getClient().getEmail(),
                    appointment.getClient().getUsername(),
                    appointment.getClient().getFirstname(),
                    appointment.getClient().getLastname(),
                    appointment.getClient().getPhone());
        }

        UserDto creatorDto = null;
        if (appointment.getCreator() != null) {
            creatorDto = new UserDto(
                    appointment.getCreator().getId(),
                    appointment.getCreator().getEmail(),
                    appointment.getCreator().getUsername(),
                    appointment.getCreator().getFirstname(),
                    appointment.getCreator().getLastname(),
                    appointment.getCreator().getPhone());
        }

        return new AppointmentDto(
                appointment.getDate(),
                appointment.getTime(),
                clientDto,
                creatorDto,
                appointment.getStatus());
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
            entity.setClient(client);
        }

        if (dto.id_creator() != null) {
            UserEntity creator = new UserEntity();
            creator.setId(dto.id_creator().id());
            entity.setCreator(creator);
        }

        return entity;
    }
}
