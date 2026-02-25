package workshop.zepcla.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.userDto.UserCreationDto;
import workshop.zepcla.entities.UserEntity;
import workshop.zepcla.exceptions.userException.UserAlreadyExistsException;
import workshop.zepcla.exceptions.userException.UsernameNotFoundException;
import workshop.zepcla.mappers.UserMapper;
import workshop.zepcla.repositories.UserRepository;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity userEntity = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        String[] roles = userEntity.getRole().split(",");

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(roles)
                .build();

    }

    public void save(UserCreationDto userCreationDto) {
        if (repo.existsByUsername(userCreationDto.username())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        UserEntity newUser = userMapper.toEntityForCreation(userCreationDto);
        newUser.setPassword(passwordEncoder.encode(userCreationDto.password()));
        repo.save(newUser);
    }

}
