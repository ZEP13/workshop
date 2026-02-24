package workshop.zepcla.dto.appointmentDto;

import java.time.LocalDate;

public record AppointmentDto(Long id, LocalDate date_appointment, LocalDate time_appointment, Long id_client,
        Long id_creator, String status) {
}
