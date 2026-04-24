package skladinya.application.converters.v1.user;

import skladinya.application.dtos.v1.user.UserRoleDto;
import skladinya.domain.models.user.UserRole;

public final class UserRoleDtoConverter {

    private UserRoleDtoConverter() {
    }

    public static UserRoleDto toDto(UserRole role) {
        return switch (role) {
            case UserRole.Client -> UserRoleDto.Client;
            case UserRole.StorageOperator -> UserRoleDto.StorageOperator;
            case UserRole.Admin -> UserRoleDto.Admin;
        };
    }

    public static UserRole toCoreEntity(UserRoleDto dto) {
        return switch (dto) {
            case UserRoleDto.Client -> UserRole.Client;
            case UserRoleDto.StorageOperator -> UserRole.StorageOperator;
            case UserRoleDto.Admin -> UserRole.Admin;
        };
    }
}
