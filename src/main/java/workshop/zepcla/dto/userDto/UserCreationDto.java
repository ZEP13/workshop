package workshop.zepcla.dto.userDto;

public record UserCreationDto(String email, String password, String firstName, String lastName,
        String phoneNumber) {
}
