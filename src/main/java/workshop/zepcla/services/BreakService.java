package workshop.zepcla.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.breakDto.BreakCreationDto;
import workshop.zepcla.entities.BreakEntity;
import workshop.zepcla.entities.EnterpriseEntity;
import workshop.zepcla.exceptions.breakException.BreakNotFound;
import workshop.zepcla.exceptions.breakException.BreakOverlapException;
import workshop.zepcla.exceptions.breakException.InvalidBreakTimeException;
import workshop.zepcla.exceptions.enterpriseException.EnterpriseNotFound;
import workshop.zepcla.mappers.BreakMapper;
import workshop.zepcla.repositories.BreakRepository;
import workshop.zepcla.repositories.EnterpriseRepository;

@Service
@AllArgsConstructor
public class BreakService {

    private final BreakRepository repo;
    private final BreakMapper mapper;
    private final EnterpriseRepository enterpriseRepository;

    private void validateBreak(BreakCreationDto dto, Long excludeId) {

        // 1. Heure de début avant heure de fin
        if (!dto.startTime().isBefore(dto.endTime())) {
            throw new InvalidBreakTimeException("Start time must be before end time");
        }

        // 2. La pause doit être dans les heures d'ouverture
        EnterpriseEntity enterprise = enterpriseRepository.findById(dto.enterpriseId())
                .orElseThrow(() -> new EnterpriseNotFound("with id " + dto.enterpriseId()));

        if (dto.startTime().isBefore(enterprise.getOpeningTime()) ||
                dto.endTime().isAfter(enterprise.getClosingTime())) {
            throw new InvalidBreakTimeException(
                    "Break must be within enterprise opening hours: " +
                            enterprise.getOpeningTime() + " - " + enterprise.getClosingTime());
        }

        // 3. Pas de chevauchement avec d'autres breaks du même jour
        List<BreakEntity> existing = repo.findByEnterpriseId(dto.enterpriseId());
        for (BreakEntity b : existing) {
            if (excludeId != null && b.getId().equals(excludeId))
                continue;
            if (b.getDaysOff() != dto.daysOff())
                continue;

            boolean overlaps = dto.startTime().isBefore(b.getEndTime()) &&
                    dto.endTime().isAfter(b.getStartTime());
            if (overlaps) {
                throw new BreakOverlapException(
                        "Break overlaps with existing break from " +
                                b.getStartTime() + " to " + b.getEndTime());
            }
        }
    }

    public void createBreak(BreakCreationDto dto) {
        validateBreak(dto, null);
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
                .orElseThrow(() -> new BreakNotFound("with id " + id));
        validateBreak(dto, id);
        existing.setStartTime(dto.startTime());
        existing.setEndTime(dto.endTime());
        existing.setDaysOff(dto.daysOff());
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
