package workshop.zepcla.dto.userDto;

public record UserCreationDto(String email, String username, String password, String firstName, String lastName,
        String phoneNumber) {
}
