package workshop.zepcla.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.holidayDto.HolidayCreationDto;
import workshop.zepcla.entities.HolidayEntity;
import workshop.zepcla.exceptions.holidayException.HolidayDateInvalidException;
import workshop.zepcla.exceptions.holidayException.HolidayNotFound;
import workshop.zepcla.mappers.HolidayMapper;
import workshop.zepcla.repositories.HolidayRepository;

@Service
@AllArgsConstructor
public class HolidayService {

    private final HolidayRepository repo;
    private final HolidayMapper mapper;

    private void validateHoliday(HolidayCreationDto dto) {

        if (dto.startDate().isAfter(dto.endDate())) {
            throw new HolidayDateInvalidException("Start date must be before or equal to end date");
        }

        if (dto.endDate().isBefore(LocalDate.now())) {
            throw new HolidayDateInvalidException("Holiday end date cannot be in the past");
        }
    }

    public HolidayEntity createHoliday(HolidayCreationDto dto) {
        validateHoliday(dto);
        return repo.save(mapper.toCreationEntity(dto));
    }

    public void deleteHoliday(Long id) {
        HolidayEntity entity = repo.findById(id)
                .orElseThrow(() -> new HolidayNotFound("with id " + id));
        repo.delete(entity);
    }

    public void updateHoliday(Long id, HolidayCreationDto dto) {
        HolidayEntity existing = repo.findById(id)
                .orElseThrow(() -> new HolidayNotFound("with id " + id));
        validateHoliday(dto);
        existing.setStartDate(dto.startDate());
        existing.setEndDate(dto.endDate());
        repo.save(existing);
    }

    public HolidayEntity getHolidayById(Long id) {
        return repo.findById(id).orElseThrow(() -> new HolidayNotFound("with id " + id));
    }

    public Iterable<HolidayEntity> getAllHolidays() {
        return repo.findAll();
    }

    public List<HolidayEntity> getHolidaysByEnterpriseId(Long enterpriseId) {
        return repo.findByEnterpriseId(enterpriseId);
    }
}
