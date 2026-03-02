package workshop.zepcla.services;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.enterpriseDto.EnterpriseCreationDto;
import workshop.zepcla.dto.enterpriseDto.EnterpriseDto;
import workshop.zepcla.entities.EnterpriseEntity;
import workshop.zepcla.mappers.EnterpriseMapper;
import workshop.zepcla.repositories.EnterpriseRepository;

@Service
@AllArgsConstructor
public class EnterpriseService {

    private final EnterpriseRepository repo;
    private final EnterpriseMapper mapper;
    private final BreakService breakService;
    private final HolidayService holidayService;

    public void createEnterprise(EnterpriseCreationDto dto) {
        EnterpriseEntity entity = mapper.toCreationEntity(dto);
        repo.save(entity);
    }

    public void deleteEnterprise(Long id) {
        repo.deleteById(id);
    }

    public void updateEnterprise(Long id, EnterpriseCreationDto dto) {
        EnterpriseEntity entity = mapper.toCreationEntity(dto);
        entity.setId(id);
        repo.save(entity);
    }

    public EnterpriseEntity getEnterpriseById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Enterprise not found"));
    }

    public Iterable<EnterpriseEntity> getAllEnterprises() {
        return repo.findAll();
    }

    public EnterpriseDto getEnterpriseWithDetails(Long id) {
        EnterpriseEntity enterprise = getEnterpriseById(id);
        EnterpriseDto dto = mapper.toDto(enterprise);
        dto.setBreaks(breakService.getAllBreaks());
        dto.setHolidays(holidayService.getAllHolidays());
        return dto;
    }
}
