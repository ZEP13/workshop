package workshop.zepcla.mappers;

import workshop.zepcla.dto.userDto.UserCreationDto;
import workshop.zepcla.dto.userDto.UserDto;
import workshop.zepcla.dto.userDto.UserLoginDto;
import workshop.zepcla.entities.UserEntity;

public class UserMapper {
    public UserDto toDto(UserEntity user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto = new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhone());
        return userDto;

    }

    public UserEntity toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(userDto.id());
        entity.setEmail(userDto.email());
        entity.setUsername(userDto.username());
        entity.setFirstname(userDto.firstName());
        entity.setLastname(userDto.lastName());
        entity.setPhone(userDto.phoneNumber());

        return entity;
    }

    public UserEntity toEntityForCreation(UserCreationDto userDto) {
        if (userDto == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setEmail(userDto.email());
        entity.setUsername(userDto.username());
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
        entity.setEmail(userDto.email());
        entity.setPassword(userDto.password());
        return entity;
    }
}
