package workshop.zepcla.dto.appointmentDto;

import java.time.LocalDate;

public record AppointmentCreationDto(LocalDate date_appointment, LocalDate time_appointment, Long id_client,
        Long id_creator) {
}
