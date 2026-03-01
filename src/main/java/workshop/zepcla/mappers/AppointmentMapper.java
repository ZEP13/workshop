package workshop.zepcla.mappers;

import org.springframework.stereotype.Component;

import workshop.zepcla.dto.appointmentDto.AppointmentCreationByAdminDto;
import workshop.zepcla.dto.appointmentDto.AppointmentCreationDto;
import workshop.zepcla.dto.appointmentDto.AppointmentDto;
import workshop.zepcla.dto.appointmentDto.AppointmentPublicCreationDto;
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
                    appointment.getClient().getFirstName(),
                    appointment.getClient().getLastName(),
                    appointment.getClient().getPhone(),
                    appointment.getClient().getRole());
        }

        UserDto creatorDto = null;
        if (appointment.getCreator() != null) {
            creatorDto = new UserDto(
                    appointment.getCreator().getId(),
                    appointment.getCreator().getEmail(),
                    appointment.getCreator().getFirstName(),
                    appointment.getCreator().getLastName(),
                    appointment.getCreator().getPhone(),
                    appointment.getCreator().getRole());
        }

        return new AppointmentDto(
                appointment.getDate(),
                appointment.getTime(),
                clientDto,
                creatorDto,
                appointment.getDuration(),
                appointment.getStatus());
    }

    public AppointmentEntity toEntityForCreationByAdmin(AppointmentCreationByAdminDto dto) {
        if (dto == null) {
            return null;
        }

        AppointmentEntity entity = new AppointmentEntity();
        entity.setDate(dto.date_appointment());
        entity.setTime(dto.time_appointment());

        entity.setDuration(dto.duration());

        entity.setStatus("PLANIFIED");

        if (dto.id_client() != null) {
            UserEntity client = new UserEntity();
            client.setId(dto.id_client().id());
            entity.setClient(client);
        }

        return entity;
    }

    public AppointmentEntity toEntityForCreation(AppointmentCreationDto dto) {
        if (dto == null) {
            return null;
        }

        AppointmentEntity entity = new AppointmentEntity();
        entity.setDate(dto.date_appointment());
        entity.setTime(dto.time_appointment());

        entity.setDuration(dto.duration());

        entity.setStatus("PLANIFIED");

        return entity;
    }

    public AppointmentPublicCreationDto toPublicCreationDto(AppointmentEntity appointment) {
        if (appointment == null) {
            return null;
        }

        return new AppointmentPublicCreationDto(
                appointment.getDate(),
                appointment.getTime(),
                appointment.getDuration(),
                appointment.getStatus());
    }

    public AppointmentEntity toEntityForPublicCreation(AppointmentPublicCreationDto dto) {
        if (dto == null) {
            return null;
        }

        AppointmentEntity entity = new AppointmentEntity();
        entity.setDate(dto.date_appointment());
        entity.setTime(dto.time_appointment());
        entity.setDuration(dto.duration());

        return entity;
    }
}
