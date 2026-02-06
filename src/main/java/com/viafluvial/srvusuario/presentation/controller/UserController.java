package com.viafluvial.srvusuario.presentation.controller;

import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.dto.ExistsResponseDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.dto.PagedUserResponseDTO;
import com.viafluvial.srvusuario.application.service.UserService;
import com.viafluvial.srvusuario.domain.entity.User;
import com.viafluvial.srvusuario.infrastructure.util.UnsafeJwtClaimsExtractor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Locale;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(
    name = "Usuários",
    description = "APIs do contexto de Identidade (IAM) para cadastro, consulta e manutenção de usuários. Inclui listagem com paginação/filtros e consulta do usuário corrente (/me)."
)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(
        summary = "Criar usuário",
        description = "O que faz: cria um usuário base no IAM.\n" +
            "Quando usar: no cadastro inicial do usuário (antes de criar perfis como Passageiro/Barqueiro/Agência/Admin).\n" +
            "Observações: o e-mail deve ser único; valores padrão (ex.: status inicial) são definidos pelo serviço; em erro retorna ErrorResponse com traceId para correlação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso: usuário criado (retorna o usuário criado)",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "400", description = "Erro: requisição inválida (validação de campos/formatos)"),
        @ApiResponse(responseCode = "409", description = "Erro: conflito (já existe usuário com o e-mail informado)")
    })
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserDTO createdUser = userService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter usuário por ID",
        description = "O que faz: recupera um usuário pelo UUID.\n" +
            "Quando usar: quando você já possui o ID do usuário (ex.: telas administrativas, auditoria, integrações internas).\n" +
            "Observações: retorna 404 se o usuário não existir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: usuário encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "Erro: usuário não encontrado para o ID informado")
    })
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    @Deprecated
    @Operation(
        summary = "Obter usuário por e-mail (legado)",
        description = "O que faz: consulta um usuário pelo e-mail.\n" +
            "Quando usar: apenas para compatibilidade (endpoint legado).\n" +
            "Observações: prefira GET /users com filtro email para pesquisa padronizada e com paginação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Erro: usuário não encontrado para o e-mail informado")
    })
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @Operation(
        summary = "Listar usuários (paginado)",
        description = "O que faz: lista usuários com paginação e filtros.\n" +
            "Quando usar: listagens de backoffice, relatórios e telas de administração.\n" +
            "Observações: paginação one-based (page=1 é a primeira página); filtros type/status são case-insensitive (ex.: active/ACTIVE); datas usam ISO-8601 (ex.: 2026-02-06T00:00:00)."
    )
    @ApiResponse(responseCode = "200", description = "Sucesso: lista paginada de usuários",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedUserResponseDTO.class)))
    public ResponseEntity<PagedResponse<UserDTO>> searchUsers(
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Boolean emailVerified,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdFrom,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTo,
        @Parameter(description = "Índice da página (one-based: 1..N)", schema = @Schema(defaultValue = "1", minimum = "1"))
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @Parameter(description = "Tamanho da página (quantidade de itens por página)", schema = @Schema(defaultValue = "20", minimum = "1", maximum = "200"))
        @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        User.UserType parsedType = null;
        if (type != null && !type.isBlank()) {
            try {
                parsedType = User.UserType.valueOf(type.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Tipo de usuário inválido: " + type);
            }
        }

        User.UserStatus parsedStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                parsedStatus = User.UserStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Status inválido: " + status);
            }
        }

        PagedResponse<UserDTO> result = userService.searchUsers(
            email,
            parsedType,
            parsedStatus,
            emailVerified,
            createdFrom,
            createdTo,
            pageable
        );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/type/{userType}")
    @Operation(
        summary = "Listar usuários por tipo",
        description = "O que faz: retorna todos os usuários de um tipo específico (sem paginação).\n" +
            "Quando usar: consultas simples por tipo, com volume baixo/moderado.\n" +
            "Observações: valores aceitos: PASSENGER, BOATMAN, ADMIN, AGENCY (case-insensitive); para paginação e múltiplos filtros, prefira GET /users."
    )
    @ApiResponse(responseCode = "200", description = "Sucesso: lista de usuários",
        content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))))
    public ResponseEntity<List<UserDTO>> getUsersByType(@PathVariable String userType) {
        User.UserType parsedType;
        try {
            parsedType = User.UserType.valueOf(userType.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + userType);
        }

        List<UserDTO> users = userService.getUsersByType(parsedType);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/exists")
    @Operation(
        summary = "Verificar existência de usuário",
        description = "O que faz: verifica se existe usuário para o e-mail informado.\n" +
            "Quando usar: validação rápida em cadastros e integrações (ex.: checar disponibilidade de e-mail).\n" +
            "Observações: retorna apenas exists=true/false (não expõe dados do usuário)."
    )
    @ApiResponse(responseCode = "200", description = "Sucesso: resultado da validação",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExistsResponseDTO.class)))
    public ResponseEntity<ExistsResponseDTO> userExists(@RequestParam String email) {
        return ResponseEntity.ok(ExistsResponseDTO.of(userService.existsByEmail(email)));
    }

    @GetMapping("/me")
    @Operation(
        summary = "Obter usuário corrente (/me)",
        description = "O que faz: resolve e retorna o usuário corrente com base no contexto da requisição.\n" +
            "Quando usar: endpoints de perfil (‘minha conta’) e chamadas internas que precisam identificar o usuário corrente.\n" +
            "Observações: prioridade: header X-User-Email; se ausente, tenta claims do token (email/sub); a extração de claims é utilitária e não substitui validação de token em produção."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: usuário resolvido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "400", description = "Erro: não foi possível resolver usuário (headers/claims ausentes ou inválidos)"),
        @ApiResponse(responseCode = "404", description = "Erro: usuário resolvido, mas não encontrado no banco")
    })
    public ResponseEntity<UserDTO> me(HttpServletRequest request) {
        String headerEmail = request.getHeader("X-User-Email");
        if (headerEmail != null && !headerEmail.isBlank()) {
            return ResponseEntity.ok(userService.getUserByEmail(headerEmail.trim()));
        }

        Map<String, Object> claims = UnsafeJwtClaimsExtractor.extractClaims(request);
        Object emailClaim = claims.get("email");
        if (emailClaim instanceof String email && !email.isBlank()) {
            return ResponseEntity.ok(userService.getUserByEmail(email.trim()));
        }

        Object subClaim = claims.get("sub");
        if (subClaim instanceof String sub && !sub.isBlank()) {
            String trimmed = sub.trim();
            try {
                UUID userId = UUID.fromString(trimmed);
                return ResponseEntity.ok(userService.getUserById(userId));
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.ok(userService.getUserByEmail(trimmed));
            }
        }

        throw new IllegalArgumentException("Não foi possível resolver o usuário a partir do token");
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar usuário",
        description = "O que faz: atualiza dados de um usuário existente pelo UUID.\n" +
            "Quando usar: manutenção/edição de cadastro no backoffice.\n" +
            "Observações: validações seguem o contrato do UserDTO; mudanças de status/tipo devem respeitar regras do serviço."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: usuário atualizado"),
        @ApiResponse(responseCode = "404", description = "Erro: usuário não encontrado para o ID informado"),
        @ApiResponse(responseCode = "400", description = "Erro: requisição inválida (validação de campos/formatos)")
    })
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remover usuário",
        description = "O que faz: remove um usuário pelo UUID.\n" +
            "Quando usar: remoção administrativa (ex.: registros inválidos/teste).\n" +
            "Observações: retorna 204 quando removido; retorna 404 se o ID não existir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sucesso: usuário removido"),
        @ApiResponse(responseCode = "404", description = "Erro: usuário não encontrado para o ID informado")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
