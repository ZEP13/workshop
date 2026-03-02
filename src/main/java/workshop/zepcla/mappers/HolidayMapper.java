package workshop.zepcla.mappers;

import workshop.zepcla.dto.holidayDto.HolidayCreationDto;
import workshop.zepcla.dto.holidayDto.HolidayDto;
import workshop.zepcla.entities.HolidayEntity;

public class HolidayMapper {

    public HolidayEntity toEntity(HolidayEntity dto) {
        if (dto == null) {
            return null;
        }
        HolidayEntity holiday = new HolidayEntity();
        holiday.setId(dto.getId());
        holiday.setName(dto.getName());
        holiday.setStartDate(dto.getStartDate());
        holiday.setEndDate(dto.getEndDate());
        holiday.setDescription(dto.getDescription());
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
                entity.getDescription());
    }

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
}
