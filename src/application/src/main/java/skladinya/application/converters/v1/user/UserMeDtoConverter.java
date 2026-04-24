package skladinya.application.converters.v1.user;

import skladinya.application.dtos.v1.user.UserMeDto;
import skladinya.domain.models.user.User;

public final class UserMeDtoConverter {

    private UserMeDtoConverter() {
    }

    public static UserMeDto toDto(User user) {
        return new UserMeDto(
                user.userId(),
                user.username(),
                user.name(),
                user.email(),
                UserRoleDtoConverter.toDto(user.role())
        );
    }
}
