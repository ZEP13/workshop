package workshop.zepcla.dto.appointmentDto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentCreationDto(LocalDate date_appointment, LocalTime time_appointment,
        Integer duration) {
}
