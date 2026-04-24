package skladinya.application.converters.v1.user;

import skladinya.application.dtos.v1.user.UserSearchParamsDto;
import skladinya.domain.models.user.UserSearchOptions;

public final class UserSearchParamsDtoConverter {

    private UserSearchParamsDtoConverter() {
    }

    public static UserSearchOptions toCoreEntity(UserSearchParamsDto dto) {
        return new UserSearchOptions(
                dto.getUsername(),
                dto.getName(),
                dto.getEmail(),
                dto.getRole() == null ? null : UserRoleDtoConverter.toCoreEntity(dto.getRole()),
                dto.getPageSize() == null ? 50 : dto.getPageSize(),
                dto.getPageNumber() == null ? 0 : dto.getPageNumber()
        );
    }
}
