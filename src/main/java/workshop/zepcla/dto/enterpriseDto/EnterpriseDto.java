package workshop.zepcla.dto.enterpriseDto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import workshop.zepcla.dto.breakDto.BreakDto;
import workshop.zepcla.dto.holidayDto.HolidayDto;

public record EnterpriseDto(
        Long id,
        String name,
        LocalTime openingTime,
        LocalTime closingTime,
        Set<DayOfWeek> daysOff,
        List<BreakDto> breaks,
        List<HolidayDto> holidays) {
}
