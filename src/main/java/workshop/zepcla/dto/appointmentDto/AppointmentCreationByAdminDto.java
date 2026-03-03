package workshop.zepcla.dto.appointmentDto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentCreationByAdminDto(
        LocalDate date_appointment,
        LocalTime time_appointment,
        Long id_client,
        Integer duration) {
}
