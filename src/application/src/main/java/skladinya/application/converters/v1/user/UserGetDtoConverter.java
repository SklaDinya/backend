package skladinya.application.converters.v1.user;

import skladinya.application.converters.v1.help.TimeConverter;
import skladinya.application.dtos.v1.user.UserGetDto;
import skladinya.domain.models.user.User;

public final class UserGetDtoConverter {

    private UserGetDtoConverter() {
    }

    public static UserGetDto toDto(User user) {
        return new UserGetDto(
                user.userId(),
                user.username(),
                user.name(),
                user.email(),
                UserRoleDtoConverter.toDto(user.role()),
                TimeConverter.toOffsetDateTime(user.createdAt()),
                TimeConverter.toOffsetDateTime(user.updatedAt()),
                user.banned()
        );
    }
}
