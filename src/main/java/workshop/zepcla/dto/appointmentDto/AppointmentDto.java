package workshop.zepcla.dto.appointmentDto;

import java.time.LocalDate;

import workshop.zepcla.entities.UserEntity;

public record AppointmentDto(UserEntity id, LocalDate date_appointment, LocalDate time_appointment,
        UserEntity id_client,
        UserEntity id_creator, String status) {
}
