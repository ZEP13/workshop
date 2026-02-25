package workshop.zepcla.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.userDto.UserDto;
import workshop.zepcla.services.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('client','ADMIN')")
    public void login(@RequestBody UserDto request) {

    }

    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('client','ADMIN')")
    public UserDto getCurrentUser() {
        Long userId = userService.getCurrentUserId();
        return userService.getUserById(userId);
    }
}
