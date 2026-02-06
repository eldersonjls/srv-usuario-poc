package com.viafluvial.srvusuario.presentation.controller;

import com.viafluvial.srvusuario.application.dto.AdminDTO;
import com.viafluvial.srvusuario.application.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admins")
@Tag(
    name = "Admins",
    description = "APIs do contexto IAM para gerenciamento do perfil de Admin (dados complementares do usuário)."
)
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    @Operation(
        summary = "Criar admin",
        description = "O que faz: cria o perfil de Admin associado a um usuário (userId).\n" +
            "Quando usar: após criar o usuário base (User) e precisar habilitar o perfil administrativo.\n" +
            "Observações: validações seguem o contrato do AdminDTO; em erro retorna ErrorResponse com traceId."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso: admin criado (retorna o perfil criado)",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminDTO.class))),
        @ApiResponse(responseCode = "400", description = "Erro: requisição inválida (validação de campos/formatos)")
    })
    public ResponseEntity<AdminDTO> createAdmin(@Valid @RequestBody AdminDTO adminDTO) {
        AdminDTO created = adminService.createAdmin(adminDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter admin por ID",
        description = "O que faz: recupera um perfil de Admin pelo UUID do próprio perfil.\n" +
            "Quando usar: quando você possui o ID do perfil (Admin).\n" +
            "Observações: retorna 404 se não existir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: admin encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminDTO.class))),
        @ApiResponse(responseCode = "404", description = "Erro: admin não encontrado para o ID informado")
    })
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Obter admin por userId",
        description = "O que faz: recupera o perfil de Admin associado a um usuário (User).\n" +
            "Quando usar: quando você tem o UUID do usuário e precisa do perfil específico de Admin.\n" +
            "Observações: retorna 404 se não houver perfil associado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: admin encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminDTO.class))),
        @ApiResponse(responseCode = "404", description = "Erro: admin não encontrado para o userId informado")
    })
    public ResponseEntity<AdminDTO> getAdminByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(adminService.getAdminByUserId(userId));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar admin",
        description = "O que faz: atualiza dados do perfil de Admin pelo UUID do perfil.\n" +
            "Quando usar: manutenção/edição de cadastro administrativo.\n" +
            "Observações: validações seguem o contrato do AdminDTO; retorna 404 se o perfil não existir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: admin atualizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminDTO.class))),
        @ApiResponse(responseCode = "404", description = "Erro: admin não encontrado para o ID informado"),
        @ApiResponse(responseCode = "400", description = "Erro: requisição inválida (validação de campos/formatos)")
    })
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable UUID id, @Valid @RequestBody AdminDTO adminDTO) {
        return ResponseEntity.ok(adminService.updateAdmin(id, adminDTO));
    }
}
