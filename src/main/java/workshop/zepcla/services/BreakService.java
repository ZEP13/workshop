package workshop.zepcla.services;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.breakDto.BreakCreationDto;
import workshop.zepcla.entities.BreakEntity;
import workshop.zepcla.mappers.BreakMapper;
import workshop.zepcla.repositories.BreakRepository;

@Service
@AllArgsConstructor
public class BreakService {

    private final BreakRepository repo;
    private final BreakMapper mapper;

    public void createBreak(BreakCreationDto dto) {
        BreakEntity entity = mapper.toCreationEntity(dto);
        repo.save(entity);
    }

}
