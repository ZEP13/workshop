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

    public void deleteHoliday(Long id) {
        repo.deleteById(id);
    }

    public void updateHoliday(Long id, HolidayCreationDto dto) {
        HolidayEntity entity = mapper.toCreationEntity(dto);
        entity.setId(id);
        repo.save(entity);
    }

    public HolidayEntity getHolidayById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Holiday not found"));
    }

    public Iterable<HolidayEntity> getAllHolidays() {
        return repo.findAll();
    }
}
