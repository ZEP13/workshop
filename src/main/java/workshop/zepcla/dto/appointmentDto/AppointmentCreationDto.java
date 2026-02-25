package workshop.zepcla.dto.appointmentDto;

import java.time.LocalDate;

import workshop.zepcla.dto.userDto.UserDto;

public record AppointmentCreationDto(LocalDate date_appointment, LocalDate time_appointment, UserDto id_client,
        UserDto id_creator) {
}
