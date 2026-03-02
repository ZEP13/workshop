package workshop.zepcla.services;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.enterpriseDto.EnterpriseCreationDto;
import workshop.zepcla.entities.EnterpriseEntity;
import workshop.zepcla.mappers.EnterpriseMapper;
import workshop.zepcla.repositories.EnterpriseRepository;

@Service
@AllArgsConstructor
public class EnterpriseService {

    private final EnterpriseRepository repo;
    private final EnterpriseMapper mapper;

    public void createEnterprise(EnterpriseCreationDto dto) {
        EnterpriseEntity entity = mapper.toCreationEntity(dto);
        repo.save(entity);
    }
}
