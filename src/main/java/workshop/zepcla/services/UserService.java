package workshop.zepcla.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.userDto.UserCreationDto;
import workshop.zepcla.dto.userDto.UserDto;
import workshop.zepcla.entities.AppointmentEntity;
import workshop.zepcla.entities.UserEntity;
import workshop.zepcla.exceptions.userException.UserAlreadyExistsException;
import workshop.zepcla.exceptions.userException.UserEmailNotFoundException;
import workshop.zepcla.exceptions.userException.UserIdNotFoundException;
import workshop.zepcla.exceptions.userException.UsernameNotFoundException;
import workshop.zepcla.mappers.UserMapper;
import workshop.zepcla.repositories.AppointmentRepository;
import workshop.zepcla.repositories.UserRepository;

import static workshop.zepcla.specifications.UserSpecification.*;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final AppointmentRepository appointmentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserEntity userEntity = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

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

        // 1️⃣ Créer le nouvel utilisateur
        UserEntity newUser = userMapper.toEntityForCreation(userCreationDto);
        newUser.setPassword(passwordEncoder.encode(userCreationDto.password()));
        repo.save(newUser);

        // 2️⃣ Si token fourni, associer les RDV publics existants
        // Dans UserService.save()
        if (userCreationDto.tokenRdv() != null && !userCreationDto.tokenRdv().isEmpty()) {
            List<AppointmentEntity> appointments = appointmentRepository.findAllByToken(userCreationDto.tokenRdv());
            for (AppointmentEntity appt : appointments) {
                appt.setClient(newUser);
                appt.setToken(null);
                appointmentRepository.save(appt); // on peut sauvegarder directement ici
            }
        }
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

    public void updateCurrentUser(UserCreationDto userCreationDto) {
        Long currentUserId = getCurrentUserId();
        updateUser(currentUserId, userCreationDto);
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

    public void deleteCurrentUser() {
        Long currentUserId = getCurrentUserId();
        deleteUserById(currentUserId);
    }

    public Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserIdNotFoundException("User not authenticated");
        }

        String email = authentication.getName();

        return repo.findByEmail(email)
                .orElseThrow(() -> new UserIdNotFoundException("User not found with username: " + email))
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

    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = repo.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("User not found with email: " + email));
        return userMapper.toDto(userEntity);
    }

    public UserDto getUserByFirstAndLastName(String firstName, String lastName) {
        UserEntity userEntity = repo.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new UserIdNotFoundException(
                        "User not found with first name: " + firstName + " and last name: " + lastName));
        return userMapper.toDto(userEntity);
    }

    public List<UserDto> getAllUsers() {
        List<UserEntity> userEntities = repo.findAll();
        return userEntities.stream()
                .map(userMapper::toDto)
                .toList();
    }

    public Page<UserDto> superSearch(
            Integer page,
            Integer size,
            Long id,
            String firstName,
            String lastName,
            String email,
            String role,
            String phoneNumber) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<UserEntity> spec = Specification
                .where(hasLastName(lastName))
                .and(hasFirstName(firstName))
                .and(hasEmail(email))
                .and(hasRole(role))
                .and(hasPhoneNumber(phoneNumber));

        return repo.findAll(spec, pageable)
                .map(userMapper::toDto);
    }
}
