package workshop.zepcla.dto.enterpriseDto;

import workshop.zepcla.entities.BreakEntity;
import workshop.zepcla.entities.HolidayEntity;

public record EnterpriseCreationDto(
        String name,
        String openingTime,
        String closingTime,
        String daysOff,
        HolidayEntity holidayId,
        BreakEntity timeBreakId) {
}
