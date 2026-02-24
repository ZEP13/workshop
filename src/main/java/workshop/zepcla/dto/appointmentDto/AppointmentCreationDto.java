package workshop.zepcla.dto.appointmentDto;

import java.time.LocalDate;

public record AppointmentCreationDto(LocalDate date, LocalDate time,Long id_client,
                                     Long id_creator, String status) {
}
