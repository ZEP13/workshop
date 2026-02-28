package workshop.zepcla.dto.appointmentDto;

import java.time.LocalDate;
import java.time.LocalTime;

import workshop.zepcla.dto.userDto.UserDto;

public record AppointmentCreationByAdminDto(LocalDate date_appointment, LocalTime time_appointment,
        UserDto id_client, Integer duration) {
}
