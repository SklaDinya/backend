package skladinya.application.converters.v1.user;

import skladinya.application.dtos.v1.user.UserCreateDto;
import skladinya.domain.models.user.UserCreate;

public final class UserCreateDtoConverter {

    private UserCreateDtoConverter() {
    }

    public static UserCreate toCoreEntity(UserCreateDto dto) {
        return new UserCreate(
                dto.getUsername(),
                dto.getPassword(),
                dto.getName(),
                dto.getEmail(),
                UserRoleDtoConverter.toCoreEntity(dto.getRole()),
                false
        );
    }
}
