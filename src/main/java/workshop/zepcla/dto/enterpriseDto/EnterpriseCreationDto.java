package workshop.zepcla.dto.enterpriseDto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record EnterpriseCreationDto(
        String name,
        LocalTime openingTime,
        LocalTime closingTime,
        Set<DayOfWeek> daysOff) {
}
