package workshop.zepcla.mappers;

import org.springframework.stereotype.Component;

import workshop.zepcla.dto.appointmentDto.AppointmentCreationByAdminDto;
import workshop.zepcla.dto.appointmentDto.AppointmentCreationDto;
import workshop.zepcla.dto.appointmentDto.AppointmentDto;
import workshop.zepcla.dto.appointmentDto.AppointmentPublicCreationDto;
import workshop.zepcla.dto.enterpriseDto.EnterpriseDto;
import workshop.zepcla.dto.userDto.UserDto;
import workshop.zepcla.entities.AppointmentEntity;
import workshop.zepcla.entities.EnterpriseEntity;
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

        EnterpriseDto enterpriseDto = null;
        if (appointment.getEnterprise() != null) {

            var enterprise = appointment.getEnterprise();

            enterpriseDto = new EnterpriseDto(
                    enterprise.getId(),
                    enterprise.getName(),
                    enterprise.getOpeningTime(),
                    enterprise.getClosingTime(),
                    enterprise.getDaysOff(),
                    null,
                    null);
        }

        return new AppointmentDto(
                appointment.getDate(),
                appointment.getTime(),
                clientDto,
                creatorDto,
                appointment.getDuration(),
                appointment.getStatus(),
                appointment.getEmailClient(),
                enterpriseDto);
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
            client.setId(dto.id_client());
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

        String clientEmail = appointment.getClient() != null
                ? appointment.getClient().getEmail()
                : appointment.getEmailClient();

        return new AppointmentPublicCreationDto(
                appointment.getDate(),
                appointment.getTime(),
                appointment.getDuration(),
                appointment.getEnterprise().getId(),
                clientEmail);
    }

    public AppointmentEntity toEntityForPublicCreation(AppointmentPublicCreationDto dto) {
        if (dto == null) {
            return null;

        }

        EnterpriseEntity enterprise = new EnterpriseEntity();
        enterprise.setId(dto.enterpriseId());

        AppointmentEntity entity = new AppointmentEntity();
        entity.setDate(dto.date_appointment());
        entity.setTime(dto.time_appointment());
        entity.setDuration(dto.duration());
        entity.setEnterprise(enterprise);
        entity.setEmailClient(dto.email_client());

        return entity;
    }
}
