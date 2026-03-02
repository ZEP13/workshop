package workshop.zepcla.mappers;

import org.springframework.stereotype.Component;

import workshop.zepcla.dto.enterpriseDto.EnterpriseCreationDto;
import workshop.zepcla.dto.enterpriseDto.EnterpriseDto;
import workshop.zepcla.entities.EnterpriseEntity;

@Component
public class EnterpriseMapper {

    public EnterpriseEntity toEntity(EnterpriseDto enterpriseDto) {
        if (enterpriseDto == null) {
            return null;
        }

        EnterpriseEntity enterpriseEntity = new EnterpriseEntity();
        enterpriseEntity.setId(enterpriseDto.id());
        enterpriseEntity.setName(enterpriseDto.name());
        enterpriseEntity.setOpeningTime(enterpriseDto.openingTime());
        enterpriseEntity.setClosingTime(enterpriseDto.closingTime());
        enterpriseEntity.setDaysOff(enterpriseDto.daysOff());

        return enterpriseEntity;
    }

    public EnterpriseDto toDto(EnterpriseEntity enterpriseEntity) {
        if (enterpriseEntity == null) {
            return null;
        }

        return new EnterpriseDto(
                enterpriseEntity.getId(),
                enterpriseEntity.getName(),
                enterpriseEntity.getOpeningTime(),
                enterpriseEntity.getClosingTime(),
                enterpriseEntity.getDaysOff(),
                null,
                null);
    }

    public EnterpriseEntity toCreationEntity(EnterpriseCreationDto enterpriseCreationDto) {
        if (enterpriseCreationDto == null) {
            return null;
        }

        EnterpriseEntity enterpriseEntity = new EnterpriseEntity();
        enterpriseEntity.setName(enterpriseCreationDto.name());
        enterpriseEntity.setOpeningTime(enterpriseCreationDto.openingTime());
        enterpriseEntity.setClosingTime(enterpriseCreationDto.closingTime());
        enterpriseEntity.setDaysOff(enterpriseCreationDto.daysOff());

        return enterpriseEntity;
    }
}
