package workshop.zepcla.services;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.holidayDto.HolidayCreationDto;
import workshop.zepcla.entities.HolidayEntity;
import workshop.zepcla.mappers.HolidayMapper;
import workshop.zepcla.repositories.HolidayRepository;

@Service
@AllArgsConstructor
public class HolidayService {

    private final HolidayRepository repo;
    private final HolidayMapper mapper;

    public void createHoliday(HolidayCreationDto dto) {
        HolidayEntity entity = mapper.toCreationEntity(dto);
        repo.save(entity);
    }
}
