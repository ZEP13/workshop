package workshop.zepcla.dto.appointmentDto;

import java.time.LocalDate;

public record AppointmentDto(LocalDate date, LocalDate time, Long id_client,
                             Long id_creator, String status) {
}
