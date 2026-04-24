package skladinya.services.user;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.helpers.Synchronizer;
import skladinya.domain.models.user.*;
import skladinya.domain.repositories.OperatorRepository;
import skladinya.domain.repositories.UserRepository;
import skladinya.domain.services.JwtService;
import skladinya.domain.services.UserService;

import java.util.List;
import java.util.UUID;

@Service
@RequestScope
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final OperatorRepository operatorRepository;

    private final JwtService jwtService;

    private final Synchronizer synchronizer;

    public UserServiceImpl(
            UserRepository userRepository,
            OperatorRepository operatorRepository,
            JwtService jwtService,
            Synchronizer synchronizer
    ) {
        this.userRepository = userRepository;
        this.operatorRepository = operatorRepository;
        this.jwtService = jwtService;
        this.synchronizer = synchronizer;
    }

    @Override
    public User create(UserCreate createForm) {
        return synchronizer.executeTransactionFunction(() -> {
            var user = fromCreateForm(createForm);
            var found = userRepository.getByUsername(user.username());
            if (found.isPresent()) {
                throw SklaDinyaException.conflict("Username already exists");
            }
            if (user.email() != null) {
                found = userRepository.getByEmail(user.email());
                if (found.isPresent()) {
                    throw SklaDinyaException.conflict("Email already in use");
                }
            }
            return userRepository.create(user);
        });
    }

    @Override
    public User getByUserId(UUID userId) {
        return synchronizer.executeSingleFunction(() ->
                userRepository.getByUserId(userId).orElseThrow(() ->
                        SklaDinyaException.notFound("UserNotFound")));
    }

    @Override
    public List<User> getAllBySearchOptions(UserSearchOptions options) {
        return synchronizer.executeSingleFunction(() ->
                userRepository.getAllBySearchOptions(options));
    }

    @Override
    public User update(UUID userId, UserUpdate updateForm) {
        return synchronizer.executeTransactionFunction(() -> {
            var user = userRepository.getByUserId(userId).orElseThrow(
                    () -> SklaDinyaException.notFound("User not found"));
            if (updateForm.username() != null) {
                var found = userRepository.getByUsername(updateForm.username()).orElse(null);
                if (found != null && !found.userId().equals(userId)) {
                    throw SklaDinyaException.conflict("Username already exists");
                }
            }
            if (updateForm.email() != null) {
                var found = userRepository.getByEmail(updateForm.email()).orElse(null);
                if (found != null && !found.userId().equals(userId)) {
                    throw SklaDinyaException.conflict("Email already in use");
                }
            }
            var updated = getUpdatedUser(userId, updateForm, user);
            var saved = userRepository.update(userId, updated);
            jwtService.update(userId);
            return saved;
        });
    }

    @Override
    public String updateSelf(UUID userId, SelfUpdate updateForm) {
        return synchronizer.executeTransactionFunction(() -> {
            var user = userRepository.getByUserId(userId).orElseThrow(
                    () -> SklaDinyaException.notFound("User not found"));
            if (user.banned()) {
                throw SklaDinyaException.invalidAccess("User was banned");
            }
            if (updateForm.username() != null) {
                var found = userRepository.getByUsername(updateForm.username()).orElse(null);
                if (found != null && !found.userId().equals(userId)) {
                    throw SklaDinyaException.conflict("Username already exists");
                }
            }
            if (updateForm.password() != null) {
                if (!user.password().equals(updateForm.oldPassword())) {
                    throw SklaDinyaException.invalidAccess("Invalid password");
                }
            }
            if (updateForm.email() != null) {
                var found = userRepository.getByEmail(updateForm.email()).orElse(null);
                if (found != null && !found.userId().equals(userId)) {
                    throw SklaDinyaException.conflict("Email already in use");
                }
            }
            var updated = getUpdatedUser(userId, new UserUpdate(updateForm), user);
            var saved = userRepository.update(userId, updated);
            return generateToken(saved);
        });
    }

    @Override
    public String generateToken(User user) {
        var result = "";
        if (user.role() == UserRole.StorageOperator) {
            var operator = operatorRepository.getByUserId(user.userId()).orElseThrow(() ->
                    SklaDinyaException.notFound("Operator not found"));
            jwtService.update(user.userId());
            result = jwtService.createStorageOperator(user.userId(), operator.storageId(), operator.role());
        } else {
            jwtService.update(user.userId());
            result = jwtService.create(user.userId(), user.role());
        }
        return result;
    }

    private static User fromCreateForm(UserCreate createForm) {
        var username = createForm.username();
        var password = createForm.password();
        var name = createForm.name();
        var email = createForm.email();
        var role = createForm.role();
        var banned = createForm.banned();
        return new User(username, password, name, email, role, banned);
    }

    private static User getUpdatedUser(UUID userId, UserUpdate updateForm, User user) {
        var username = updateForm.username() == null ? user.username() : updateForm.username();
        var password = updateForm.password() == null ? user.password() : updateForm.password();
        var name = updateForm.name() == null ? user.name() : updateForm.name();
        var email = updateForm.email() == null ? user.email() : updateForm.email();
        var role = updateForm.role() == null ? user.role() : updateForm.role();
        var createdAt = user.createdAt();
        var banned = updateForm.banned() == null ? user.banned() : updateForm.banned();
        return new User(userId, username, password, name, email, role, createdAt, banned);
    }
}
