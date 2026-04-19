package skladinya.services.auth;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.helpers.Synchronizer;
import skladinya.domain.models.user.UserCreate;
import skladinya.domain.repositories.UserRepository;
import skladinya.domain.services.AuthService;
import skladinya.domain.services.UserService;

@Service
@RequestScope
public final class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final UserService userService;

    private final Synchronizer synchronizer;

    public AuthServiceImpl(UserRepository userRepository, UserService userService, Synchronizer synchronizer) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.synchronizer = synchronizer;
    }

    @Override
    public String login(String login, String password) {
        return synchronizer.executeTransactionFunction(() -> {
            var user = userRepository
                    .getByUsername(login)
                    .orElse(null);
            if (user == null) {
                user = userRepository.getByEmail(login).orElseThrow(() ->
                        SklaDinyaException.invalidAccess("User not found"));
            }
            if (!user.password().equals(password)) {
                throw SklaDinyaException.invalidAccess("Invalid password");
            }
            return userService.generateToken(user);
        });
    }

    @Override
    public String register(UserCreate registrationForm) {
        return synchronizer.executeTransactionFunction(() -> {
            var user = userService.create(registrationForm);
            return userService.generateToken(user);
        });
    }
}
