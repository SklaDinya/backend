package skladinya.application.converters.v1.user;

import skladinya.application.dtos.v1.user.UserMeUpdateDto;
import skladinya.domain.models.user.SelfUpdate;

public final class UserMeUpdateDtoConverter {

    private UserMeUpdateDtoConverter() {
    }

    public static SelfUpdate toCoreEntity(UserMeUpdateDto dto) {
        return new SelfUpdate(
                dto.getUsername(),
                dto.getOldPassword(),
                dto.getNewPassword(),
                dto.getName(),
                dto.getEmail()
        );
    }
}
