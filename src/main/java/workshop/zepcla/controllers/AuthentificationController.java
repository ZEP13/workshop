package workshop.zepcla.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import workshop.zepcla.dto.userDto.UserCreationDto;
import workshop.zepcla.dto.userDto.UserLoginDto;
import workshop.zepcla.services.JwtService;
import workshop.zepcla.services.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthentificationController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public void register(@Valid @RequestBody UserCreationDto userDTO) {
        userService.save(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getPassword()));

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String accessToken = jwtService.generateToken(userDetails);

        Map<String, String> body = Map.of("accessToken", accessToken);

        return ResponseEntity.ok(body);
    }
}
