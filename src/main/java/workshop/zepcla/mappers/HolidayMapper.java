package workshop.zepcla.mappers;

import org.springframework.stereotype.Component;

import workshop.zepcla.dto.holidayDto.HolidayCreationDto;
import workshop.zepcla.dto.holidayDto.HolidayDto;
import workshop.zepcla.entities.HolidayEntity;

@Component
public class HolidayMapper {

    public HolidayEntity toCreationEntity(HolidayCreationDto dto) {
        if (dto == null) {
            return null;
        }

        HolidayEntity holiday = new HolidayEntity();
        holiday.setName(dto.name());
        holiday.setStartDate(dto.startDate());
        holiday.setEndDate(dto.endDate());
        holiday.setDescription(dto.description());

        return holiday;
    }

    public HolidayDto toDto(HolidayEntity entity) {
        if (entity == null) {
            return null;
        }

        return new HolidayDto(
                entity.getId(),
                entity.getName(),
                entity.getStartDate(),
                entity.getEndDate(),
                null,
                entity.getDescription());
    }
}
