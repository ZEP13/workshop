package workshop.zepcla.dto.appointmentDto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentPublicCreationDto(
        LocalDate date_appointment,
        LocalTime time_appointment,
        Integer duration,
        String email_client // email pour contact / token
) {
}
