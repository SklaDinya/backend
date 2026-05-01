package skladinya.application.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skladinya.application.converters.v1.user.*;
import skladinya.application.dtos.v1.user.*;
import skladinya.application.security.RoleChecker;
import skladinya.domain.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private final RoleChecker roleChecker;

    public UserController(UserService userService, RoleChecker roleChecker) {
        this.userService = userService;
        this.roleChecker = roleChecker;
    }

    @GetMapping
    public List<UserGetDto> getAll(@RequestHeader String authorization, @Valid UserSearchParamsDto dto) {
        roleChecker.requireAdmin(authorization);
        var result = userService.getAllBySearchOptions(UserSearchParamsDtoConverter.toCoreEntity(dto));
        return result.stream().map(UserGetDtoConverter::toDto).toList();
    }

    @PostMapping
    public ResponseEntity<UserGetDto> create(@RequestHeader String authorization, @Valid @RequestBody UserCreateDto dto) {
        roleChecker.requireAdmin(authorization);
        var result = userService.create(UserCreateDtoConverter.toCoreEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserGetDtoConverter.toDto(result));
    }

    @GetMapping("/{userId}")
    public UserGetDto get(@RequestHeader String authorization, @PathVariable UUID userId) {
        roleChecker.requireAdmin(authorization);
        var result = userService.getByUserId(userId);
        return UserGetDtoConverter.toDto(result);
    }

    @PatchMapping("/{userId}")
    public UserGetDto update(
            @RequestHeader String authorization,
            @PathVariable UUID userId,
            @Valid @RequestBody UserUpdateDto dto) {
        roleChecker.requireAdmin(authorization);
        var result = userService.update(userId, UserUpdateDtoConverter.toCoreEntity(dto));
        return UserGetDtoConverter.toDto(result);
    }

    @GetMapping("/me")
    public UserMeDto getMe(@RequestHeader String authorization) {
        var data = roleChecker.authorized(authorization);
        var result = userService.getByUserId(data.userId());
        return UserMeDtoConverter.toDto(result);
    }

    @PatchMapping("/me")
    public String updateMe(@RequestHeader String authorization, @Valid @RequestBody UserMeUpdateDto dto) {
        var data = roleChecker.authorized(authorization);
        return userService.updateSelf(data.userId(), UserMeUpdateDtoConverter.toCoreEntity(dto));
    }
}
