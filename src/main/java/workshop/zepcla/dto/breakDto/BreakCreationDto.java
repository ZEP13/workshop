package workshop.zepcla.dto.breakDto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record BreakCreationDto(
        LocalTime startTime,
        LocalTime endTime,
        DayOfWeek daysOff,
        Long enterpriseId) {
}
