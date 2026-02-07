package com.viafluvial.srvusuario.adapters.in.web.controller;

import com.viafluvial.srvusuario.adapters.in.web.api.UsersApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ExistsResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PagedUserResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserCreateDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserStatusApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserTypeApi;
import com.viafluvial.srvusuario.adapters.in.web.mapper.PagedResponseApiMapper;
import com.viafluvial.srvusuario.adapters.in.web.mapper.UserApiMapper;
import com.viafluvial.srvusuario.application.dto.ExistsResponseDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.port.in.UserManagementUseCase;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import com.viafluvial.srvusuario.infrastructure.util.UnsafeJwtClaimsExtractor;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class UserController implements UsersApi {

    private final UserManagementUseCase userService;

    public UserController(UserManagementUseCase userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserDTOApi> createUser(@Valid UserCreateDTOApi userCreateDTOApi) {
        UserDTO createdUser = userService.createUser(UserApiMapper.toApp(userCreateDTOApi));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserApiMapper.toApi(createdUser));
    }

    @Override
    public ResponseEntity<UserDTOApi> getUserById(UUID id) {
        return ResponseEntity.ok(UserApiMapper.toApi(userService.getUserById(id)));
    }

    @Override
    public ResponseEntity<UserDTOApi> getUserByEmail(String email) {
        return ResponseEntity.ok(UserApiMapper.toApi(userService.getUserByEmail(email)));
    }

    @Override
    public ResponseEntity<PagedUserResponseApi> searchUsers(
        String email,
        UserTypeApi type,
        UserStatusApi status,
        Boolean emailVerified,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Integer page,
        Integer size
    ) {
        int pageValue = page != null ? page : 1;
        int sizeValue = size != null ? size : 20;
        Pageable pageable = PageRequest.of(pageValue - 1, sizeValue);

        UserType parsedType = UserApiMapper.toDomainUserType(type);
        UserStatus parsedStatus = UserApiMapper.toDomainUserStatus(status);

        PagedResponse<UserDTO> result = userService.searchUsers(
            email,
            parsedType,
            parsedStatus,
            emailVerified,
            createdFrom,
            createdTo,
            pageable
        );

        return ResponseEntity.ok(PagedResponseApiMapper.toPagedUserResponse(result));
    }

    @Override
    public ResponseEntity<List<UserDTOApi>> getUsersByType(UserTypeApi userType) {
        UserType parsedType = UserApiMapper.toDomainUserType(userType);
        List<UserDTOApi> users = userService.getUsersByType(parsedType).stream()
            .map(UserApiMapper::toApi)
            .toList();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<ExistsResponseApi> userExists(String email) {
        ExistsResponseDTO response = ExistsResponseDTO.of(userService.existsByEmail(email));
        return ResponseEntity.ok(PagedResponseApiMapper.toApi(response));
    }

    @Override
    public ResponseEntity<UserDTOApi> getCurrentUser() {
        HttpServletRequest request = resolveRequest();
        if (request == null) {
            throw new IllegalArgumentException("Nao foi possivel resolver o usuario a partir do contexto");
        }

        String headerEmail = request.getHeader("X-User-Email");
        if (headerEmail != null && !headerEmail.isBlank()) {
            return ResponseEntity.ok(UserApiMapper.toApi(userService.getUserByEmail(headerEmail.trim())));
        }

        Map<String, Object> claims = UnsafeJwtClaimsExtractor.extractClaims(request);
        Object emailClaim = claims.get("email");
        if (emailClaim instanceof String email && !email.isBlank()) {
            return ResponseEntity.ok(UserApiMapper.toApi(userService.getUserByEmail(email.trim())));
        }

        Object subClaim = claims.get("sub");
        if (subClaim instanceof String sub && !sub.isBlank()) {
            String trimmed = sub.trim();
            try {
                UUID userId = UUID.fromString(trimmed);
                return ResponseEntity.ok(UserApiMapper.toApi(userService.getUserById(userId)));
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.ok(UserApiMapper.toApi(userService.getUserByEmail(trimmed)));
            }
        }

        throw new IllegalArgumentException("Nao foi possivel resolver o usuario a partir do token");
    }

    @Override
    public ResponseEntity<UserDTOApi> updateUser(UUID id, @Valid UserDTOApi userDTOApi) {
        UserDTO updated = userService.updateUser(id, UserApiMapper.toApp(userDTOApi));
        return ResponseEntity.ok(UserApiMapper.toApi(updated));
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private HttpServletRequest resolveRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs) {
            return attrs.getRequest();
        }
        return null;
    }
}
