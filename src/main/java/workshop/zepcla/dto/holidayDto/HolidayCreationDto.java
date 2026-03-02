package workshop.zepcla.dto.holidayDto;

import java.time.LocalDate;

public record HolidayCreationDto(
        String name,
        LocalDate startDate,
        LocalDate endDate,
        String description) {
}
