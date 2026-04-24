package skladinya.application.converters.v1.auth;

import skladinya.application.dtos.v1.auth.RegistrationFormDto;
import skladinya.domain.models.user.UserCreate;

public final class RegistrationFormDtoConverter {

    private RegistrationFormDtoConverter() {
    }

    public static UserCreate toCoreEntity(RegistrationFormDto dto) {
        return new UserCreate(
                dto.getUsername(),
                dto.getPassword(),
                dto.getName(),
                dto.getEmail()
        );
    }
}
