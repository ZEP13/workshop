package workshop.zepcla.dto.appointmentDto;

import java.time.LocalDate;
import java.time.LocalTime;

import workshop.zepcla.dto.userDto.UserDto;

public record AppointmentCreationDto(LocalDate date_appointment, LocalTime time_appointment,
        UserDto id_client, UserDto id_creator, Integer duration) {
}
