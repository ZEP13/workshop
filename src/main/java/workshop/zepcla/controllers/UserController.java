package workshop.zepcla.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import workshop.zepcla.dto.userDto.UserCreationDto;
import workshop.zepcla.dto.userDto.UserDto;
import workshop.zepcla.services.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("/logged/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/update/me")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Void> updateCurrentUser(@RequestBody UserCreationDto request) {
        userService.updateCurrentUser(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@RequestBody UserCreationDto request, @PathVariable Long id) {
        userService.updateUser(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/me")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Void> deleteCurrentUser() {
        userService.deleteCurrentUser();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<UserDto> getCurrentUser() {
        Long userId = userService.getCurrentUserId();
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/by-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/by-name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserByFirstAndLastName(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        return ResponseEntity.ok(userService.getUserByFirstAndLastName(firstName, lastName));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDto>> superSearch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String phoneNumber) {
        return ResponseEntity.ok(userService.superSearch(
                page, size, id, firstName, lastName, email, role, phoneNumber));
    }
}
