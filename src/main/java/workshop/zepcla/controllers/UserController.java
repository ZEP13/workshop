package workshop.zepcla.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.userDto.UserCreationDto;
import workshop.zepcla.dto.userDto.UserDto;
import workshop.zepcla.services.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/updateCurrentUser")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public void updateCurrentUser(@RequestBody UserCreationDto request) {
	userService.updateCurrentUser(request);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void update(@RequestBody UserCreationDto request, @PathVariable Long id) {
	userService.updateUser(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @DeleteMapping("/deleteCurrentUser")
    @PreAuthorize("hasAnyRole('CLIENT','','ADMIN')")
    public void deleteCurrentUser() {
		userService.deleteCurrentUser();
	}


    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public UserDto getCurrentUser() {
        Long userId = userService.getCurrentUserId();
        return userService.getUserById(userId);
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getUserByEmail")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public UserDto getUserByEmail(@RequestParam String email) {
		return userService.getUserByEmail(email);
    }

    @GetMapping("/getuserByFirstAndLastName")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public UserDto getUserByFirstAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
	return userService.getUserByFirstAndLastName(firstName, lastName);
    } 

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
