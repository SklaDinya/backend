package skladinya.domain.services;

import skladinya.domain.models.user.UserCreate;

public interface AuthService {

    String login(String login, String password);

    String register(UserCreate registrationForm);
}
