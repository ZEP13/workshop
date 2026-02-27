package workshop.zepcla.mappers;

import org.springframework.stereotype.Component;

import workshop.zepcla.dto.userDto.UserCreationDto;
import workshop.zepcla.dto.userDto.UserDto;
import workshop.zepcla.dto.userDto.UserLoginDto;
import workshop.zepcla.entities.UserEntity;

@Component
public class UserMapper {
    public UserDto toDto(UserEntity user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhone(),
                user.getRole());
    }

    public UserEntity toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(userDto.id());
        entity.setEmail(userDto.email());
        entity.setFirstname(userDto.firstName());
        entity.setLastname(userDto.lastName());
        entity.setPhone(userDto.phoneNumber());
        entity.setRole(userDto.role());

        return entity;
    }

    public UserEntity toEntityForCreation(UserCreationDto userDto) {
        if (userDto == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setEmail(userDto.email());
        entity.setFirstname(userDto.firstName());
        entity.setLastname(userDto.lastName());
        entity.setPassword(userDto.password());
        entity.setPhone(userDto.phoneNumber());

        return entity;
    }

    public UserEntity toEntityForLogin(UserLoginDto userDto) {
        if (userDto == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setEmail(userDto.getEmail());
        entity.setPassword(userDto.getPassword());
        return entity;
    }
}
