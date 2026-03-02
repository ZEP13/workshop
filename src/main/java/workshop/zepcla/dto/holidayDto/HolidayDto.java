package workshop.zepcla.dto.holidayDto;

import java.time.LocalDate;

public record HolidayDto(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        String description) {
}
