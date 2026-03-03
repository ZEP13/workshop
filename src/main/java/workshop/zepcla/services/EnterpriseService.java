package workshop.zepcla.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.breakDto.BreakDto;
import workshop.zepcla.dto.enterpriseDto.EnterpriseCreationDto;
import workshop.zepcla.dto.enterpriseDto.EnterpriseDto;
import workshop.zepcla.dto.holidayDto.HolidayDto;
import workshop.zepcla.entities.EnterpriseEntity;
import workshop.zepcla.exceptions.enterpriseException.EnterpriseAlreadyExistsException;
import workshop.zepcla.exceptions.enterpriseException.EnterpriseNotFound;
import workshop.zepcla.exceptions.enterpriseException.InvalidEnterpriseScheduleException;
import workshop.zepcla.mappers.BreakMapper;
import workshop.zepcla.mappers.EnterpriseMapper;
import workshop.zepcla.mappers.HolidayMapper;
import workshop.zepcla.repositories.EnterpriseRepository;

@Service
@AllArgsConstructor
public class EnterpriseService {

    private final EnterpriseRepository repo;
    private final EnterpriseMapper mapper;
    private final BreakMapper breakMapper;
    private final HolidayMapper holidayMapper;
    private final BreakService breakService;
    private final HolidayService holidayService;
    private final UserService userService;

    public EnterpriseEntity createEnterprise(EnterpriseCreationDto dto) {

        if (repo.existsByName(dto.name())) {
            throw new EnterpriseAlreadyExistsException(
                    "with name " + dto.name());
        }

        if (!dto.openingTime().isBefore(dto.closingTime())) {
            throw new InvalidEnterpriseScheduleException(
                    "Opening time must be before closing time");
        }

        EnterpriseEntity entity = mapper.toCreationEntity(dto);

        return repo.save(entity);
    }

    public void deleteEnterprise(Long id) {
        EnterpriseEntity entity = repo.findById(id)
                .orElseThrow(() -> new EnterpriseNotFound("with id " + id));
        repo.delete(entity);
    }

    public EnterpriseEntity updateEnterprise(EnterpriseCreationDto dto) {

        Long id = userService.getCurrentUserEntity().getEnterprise().getId();
        EnterpriseEntity existing = repo.findById(id)
                .orElseThrow(() -> new EnterpriseNotFound("with id " + id));

        if (!dto.openingTime().isBefore(dto.closingTime())) {
            throw new InvalidEnterpriseScheduleException("Opening time must be before closing time");
        }

        existing.setName(dto.name());
        existing.setOpeningTime(dto.openingTime());
        existing.setClosingTime(dto.closingTime());
        existing.setDaysOff(dto.daysOff());

        return repo.save(existing);
    }

    public EnterpriseEntity getEnterpriseById() {
        Long id = userService.getCurrentUserEntity().getEnterprise().getId();
        return repo.findById(id).orElseThrow(() -> new EnterpriseNotFound("with id " + id));
    }

    public Iterable<EnterpriseEntity> getAllEnterprises() {
        return repo.findAll();
    }

    public EnterpriseDto getEnterpriseWithDetails() {

        Long id = userService.getCurrentUserEntity().getEnterprise().getId();
        EnterpriseEntity enterprise = getEnterpriseById();

        List<BreakDto> breakDtos = breakService
                .getBreaksByEnterpriseId(id)
                .stream()
                .map(breakMapper::toDto)
                .toList();

        List<HolidayDto> holidayDtos = holidayService
                .getHolidaysByEnterpriseId(id)
                .stream()
                .map(holidayMapper::toDto)
                .toList();

        return new EnterpriseDto(
                enterprise.getId(),
                enterprise.getName(),
                enterprise.getOpeningTime(),
                enterprise.getClosingTime(),
                enterprise.getDaysOff(),
                breakDtos,
                holidayDtos);
    }

    public EnterpriseDto getEnterpriseBreakDetails() {

        Long id = userService.getCurrentUserEntity().getEnterprise().getId();

        EnterpriseEntity enterprise = getEnterpriseById();

        List<BreakDto> breakDtos = breakService
                .getBreaksByEnterpriseId(id)
                .stream()
                .map(breakMapper::toDto)
                .toList();

        return new EnterpriseDto(
                enterprise.getId(),
                enterprise.getName(),
                enterprise.getOpeningTime(),
                enterprise.getClosingTime(),
                enterprise.getDaysOff(),
                breakDtos,
                null);
    }

    public EnterpriseDto getEnterpriseHolidayDetails() {

        Long id = userService.getCurrentUserEntity().getEnterprise().getId();
        EnterpriseEntity enterprise = getEnterpriseById();

        List<HolidayDto> holidayDtos = holidayService
                .getHolidaysByEnterpriseId(id)
                .stream()
                .map(holidayMapper::toDto)
                .toList();

        return new EnterpriseDto(
                enterprise.getId(),
                enterprise.getName(),
                enterprise.getOpeningTime(),
                enterprise.getClosingTime(),
                enterprise.getDaysOff(),
                null,
                holidayDtos);
    }
}
