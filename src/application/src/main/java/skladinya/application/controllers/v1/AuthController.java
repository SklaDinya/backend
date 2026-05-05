package skladinya.application.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skladinya.application.converters.v1.auth.RegistrationFormDtoConverter;
import skladinya.application.dtos.v1.auth.LoginFormDto;
import skladinya.application.dtos.v1.auth.RegistrationFormDto;
import skladinya.domain.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginFormDto dto) {
        return authService.login(dto.getUsername(), dto.getPassword());
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegistrationFormDto dto) {
        return authService.register(RegistrationFormDtoConverter.toCoreEntity(dto));
    }
}
