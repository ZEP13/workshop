package workshop.zepcla.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.breakDto.BreakCreationDto;
import workshop.zepcla.entities.BreakEntity;
import workshop.zepcla.exceptions.breakException.BreakNotFound;
import workshop.zepcla.mappers.BreakMapper;
import workshop.zepcla.repositories.BreakRepository;

@Service
@AllArgsConstructor
public class BreakService {

    private final BreakRepository repo;
    private final BreakMapper mapper;

    public void createBreak(BreakCreationDto dto) {
        if (!dto.startTime().isBefore(dto.endTime())) {
            throw new InvalidBreakTimeException("Start time must be before end time");
        }
        BreakEntity entity = mapper.toCreationEntity(dto);
        repo.save(entity);
    }

    public void deleteBreak(Long id) {
        BreakEntity entity = repo.findById(id)
                .orElseThrow(() -> new BreakNotFound("with id " + id));
        repo.delete(entity);
    }

    public void updateBreak(Long id, BreakCreationDto dto) {
        BreakEntity existing = repo.findById(id)
                .orElseThrow(() -> new BreakNotFound("Break not found with id " + id));
        existing.setStartTime(dto.startTime());
        existing.setEndTime(dto.endTime());
        repo.save(existing);
    }

    public BreakEntity getBreakById(Long id) {
        return repo.findById(id).orElseThrow(() -> new BreakNotFound(" with id " + id));
    }

    public List<BreakEntity> getAllBreaks() {
        return repo.findAll();
    }

    public List<BreakEntity> getBreaksByEnterpriseId(Long enterpriseId) {
        return repo.findByEnterpriseId(enterpriseId);
    }

}
