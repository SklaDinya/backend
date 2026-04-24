package skladinya.application.converters.v1.user;

import skladinya.application.dtos.v1.user.UserUpdateDto;
import skladinya.domain.models.user.UserUpdate;

public final class UserUpdateDtoConverter {

    private UserUpdateDtoConverter() {
    }

    public static UserUpdate toCoreEntity(UserUpdateDto dto) {
        return new UserUpdate(
                dto.getUsername(),
                dto.getPassword(),
                dto.getName(),
                dto.getEmail(),
                null,
                dto.getBanned()
        );
    }
}
