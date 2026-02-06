package com.viafluvial.srvusuario.presentation.controller;

import com.viafluvial.srvusuario.application.dto.ApprovalCreateDTO;
import com.viafluvial.srvusuario.application.dto.ApprovalDTO;
import com.viafluvial.srvusuario.application.dto.PagedApprovalResponseDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.service.ApprovalService;
import com.viafluvial.srvusuario.domain.entity.Approval;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/approvals")
@Tag(
    name = "Aprovações",
    description = "APIs para abertura e gestão do ciclo de aprovação (onboarding/revisão) de entidades do IAM (ex.: barqueiro, agência). Permite criar solicitações, listar e executar transições de status."
)
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping
    @Operation(
        summary = "Criar solicitação de aprovação",
        description = "O que faz: cria uma solicitação de aprovação para uma entidade (entityType/entityId), com tipo e referências de documentos.\n" +
            "Quando usar: para iniciar o fluxo de onboarding/revisão antes de aprovar/ativar/bloquear.\n" +
            "Observações: retorna a solicitação criada; em erro retorna ErrorResponse com traceId."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso: solicitação criada (retorna a solicitação)",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApprovalDTO.class))),
        @ApiResponse(responseCode = "400", description = "Erro: requisição inválida (validação de campos/formatos)")
    })
    public ResponseEntity<ApprovalDTO> create(@Valid @RequestBody ApprovalCreateDTO createDTO) {
        ApprovalDTO created = approvalService.createApproval(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(
        summary = "Listar solicitações (paginado)",
        description = "O que faz: lista solicitações de aprovação com paginação e filtro opcional por status.\n" +
            "Quando usar: filas de análise, auditoria e acompanhamento de onboarding.\n" +
            "Observações: paginação one-based (page=1); status é case-insensitive (ex.: pending/PENDING)."
    )
    @ApiResponse(responseCode = "200", description = "Sucesso: lista paginada de solicitações",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedApprovalResponseDTO.class)))
    public ResponseEntity<PagedResponse<ApprovalDTO>> list(
        @Parameter(description = "Status da aprovação (opcional). Ex.: PENDING, APPROVED, ACTIVE, BLOCKED, MORE_INFO_REQUIRED")
        @RequestParam(required = false) String status,
        @Parameter(description = "Índice da página (one-based: 1..N)", schema = @Schema(defaultValue = "1", minimum = "1"))
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @Parameter(description = "Tamanho da página (quantidade de itens por página)", schema = @Schema(defaultValue = "20", minimum = "1", maximum = "200"))
        @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size
    ) {
        Approval.ApprovalStatus parsed = null;
        if (status != null && !status.isBlank()) {
            try {
                parsed = Approval.ApprovalStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Status inválido: " + status);
            }
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        return ResponseEntity.ok(approvalService.searchApprovals(parsed, pageable));
    }

    @PatchMapping("/{id}/unblock")
    @Operation(
        summary = "Desbloquear",
        description = "O que faz: desbloqueia a entidade associada à solicitação.\n" +
            "Quando usar: quando a entidade estava bloqueada e deve voltar a operar.\n" +
            "Observações: o id refere-se à solicitação (approval)."
    )
    public ResponseEntity<ApprovalDTO> unblock(@PathVariable UUID id) {
        return ResponseEntity.ok(approvalService.unblock(id));
    }

    @PatchMapping("/{id}/block")
    @Operation(
        summary = "Bloquear",
        description = "O que faz: bloqueia a entidade associada à solicitação.\n" +
            "Quando usar: para impedir operação/uso da entidade por motivos administrativos, fraude ou pendências.\n" +
            "Observações: o id refere-se à solicitação (approval)."
    )
    public ResponseEntity<ApprovalDTO> block(@PathVariable UUID id) {
        return ResponseEntity.ok(approvalService.block(id));
    }

    @PatchMapping("/{id}/approve")
    @Operation(
        summary = "Aprovar",
        description = "O que faz: aprova a entidade associada à solicitação (passou na análise).\n" +
            "Quando usar: após revisão/validação dos dados e documentos.\n" +
            "Observações: dependendo do fluxo, a ativação pode ser feita em seguida via /activate."
    )
    public ResponseEntity<ApprovalDTO> approve(@PathVariable UUID id) {
        return ResponseEntity.ok(approvalService.approve(id));
    }

    @PatchMapping("/{id}/activate")
    @Operation(
        summary = "Ativar",
        description = "O que faz: ativa a entidade associada à solicitação (torna-a operacional).\n" +
            "Quando usar: após aprovação e validação final do onboarding.\n" +
            "Observações: o id refere-se à solicitação (approval)."
    )
    public ResponseEntity<ApprovalDTO> activate(@PathVariable UUID id) {
        return ResponseEntity.ok(approvalService.activate(id));
    }

    @PatchMapping("/{id}/request-more-info")
    @Operation(
        summary = "Solicitar mais informações",
        description = "O que faz: marca a solicitação como MORE_INFO_REQUIRED.\n" +
            "Quando usar: quando faltam dados/documentos e é necessário solicitar complementação.\n" +
            "Observações: o id refere-se à solicitação (approval)."
    )
    public ResponseEntity<ApprovalDTO> requestMoreInfo(@PathVariable UUID id) {
        return ResponseEntity.ok(approvalService.requestMoreInfo(id));
    }
}
