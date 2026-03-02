package workshop.zepcla.dto.breakDto;

import java.time.LocalTime;

public record BreakDto(
        Long id,
        LocalTime startTime,
        LocalTime endTime) {
}
