package workshop.zepcla.dto.enterpriseDto;

public record EnterpriseDto(
        Long id,
        String name,
        String openingTime,
        String closingTime,
        String daysOff,
        Long holidayId,
        Long timeBreakId) {
}
