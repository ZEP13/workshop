package workshop.zepcla.services;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.userDto.UserCreationDto;
import workshop.zepcla.dto.userDto.UserDto;
import workshop.zepcla.entities.UserEntity;
import workshop.zepcla.exceptions.userException.UserAlreadyExistsException;
import workshop.zepcla.exceptions.userException.UserEmailNotFoundException;
import workshop.zepcla.exceptions.userException.UserIdNotFoundException;
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
                .username(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles(roles)
                .build();

    }

    public void save(UserCreationDto userCreationDto) {
        if (repo.existsByEmail(userCreationDto.email())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        UserEntity newUser = userMapper.toEntityForCreation(userCreationDto);
        newUser.setPassword(passwordEncoder.encode(userCreationDto.password()));
        repo.save(newUser);
    }

    public void updateUser(Long id, UserCreationDto userCreationDto) {
        UserEntity existingUser = repo.findById(id)
                .orElseThrow(() -> new UserIdNotFoundException("User ID not found: " + id));

        if (!existingUser.getEmail().equals(userCreationDto.email()) &&
                repo.existsByEmail(userCreationDto.email())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        existingUser.setEmail(userCreationDto.email());
        existingUser.setPassword(passwordEncoder.encode(userCreationDto.password()));
        repo.save(existingUser);
    }

    public void deleteUserById(Long id) {
        if (!repo.existsById(id)) {
            throw new UserIdNotFoundException("User ID not found: " + id);
        }
        repo.deleteById(id);
    }

    public UserDto getUserById(Long id) {
        UserEntity userEntity = repo.findById(id)
                .orElseThrow(() -> new UserIdNotFoundException("User ID not found: " + id));
        return userMapper.toDto(userEntity);
    }

    public Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserIdNotFoundException("User not authenticated");
        }

        String username = authentication.getName();

        return repo.findByUsername(username)
                .orElseThrow(() -> new UserIdNotFoundException("User not found with username: " + username))
                .getId();
    }

    public UserEntity getCurrentUserEntity() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserIdNotFoundException("User not authenticated");
        }

        String email = authentication.getName();

        return repo.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("User not found with email: " + email));
    }

    public List<UserDto> getAllUsers() {
        List<UserEntity> userEntities = repo.findAll();
        return userEntities.stream()
                .map(userMapper::toDto)
                .toList();
    }
}
