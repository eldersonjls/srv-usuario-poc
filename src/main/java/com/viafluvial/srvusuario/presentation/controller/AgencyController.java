package com.viafluvial.srvusuario.presentation.controller;

import com.viafluvial.srvusuario.application.dto.AgencyDTO;
import com.viafluvial.srvusuario.application.dto.ExistsResponseDTO;
import com.viafluvial.srvusuario.application.dto.PagedAgencyResponseDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.service.AgencyService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/agencies")
@Tag(
    name = "Agências",
    description = "APIs do contexto IAM para gerenciamento do perfil de Agência (dados complementares do usuário). Inclui cadastro, consulta, atualização e listagem paginada com filtros."
)
public class AgencyController {

    private final AgencyService agencyService;

    public AgencyController(AgencyService agencyService) {
        this.agencyService = agencyService;
    }

    @PostMapping
    @Operation(
        summary = "Criar agência",
        description = "O que faz: cria o perfil de Agência associado a um usuário (userId).\n" +
            "Quando usar: após criar o usuário base (User) e precisar registrar o perfil específico de Agência.\n" +
            "Observações: CNPJ deve ser único; retorna 409 em conflito; em erro retorna ErrorResponse com traceId."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso: agência criada (retorna o perfil criado)",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgencyDTO.class))),
        @ApiResponse(responseCode = "400", description = "Erro: requisição inválida (validação de campos/formatos)"),
        @ApiResponse(responseCode = "409", description = "Erro: conflito (já existe agência com o CNPJ informado)")
    })
    public ResponseEntity<AgencyDTO> createAgency(@Valid @RequestBody AgencyDTO agencyDTO) {
        AgencyDTO created = agencyService.createAgency(agencyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(
        summary = "Listar agências (paginado)",
        description = "O que faz: lista agências com paginação e filtros.\n" +
            "Quando usar: listagens de backoffice e consultas administrativas.\n" +
            "Observações: paginação one-based (page=1); filtros por CNPJ e intervalo de criação; datas em ISO-8601."
    )
    @ApiResponse(responseCode = "200", description = "Sucesso: lista paginada de agências",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedAgencyResponseDTO.class)))
    public ResponseEntity<PagedResponse<AgencyDTO>> searchAgencies(
        @RequestParam(required = false) String cnpj,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdFrom,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTo,
        @Parameter(description = "Índice da página (one-based: 1..N)", schema = @Schema(defaultValue = "1", minimum = "1"))
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @Parameter(description = "Tamanho da página (quantidade de itens por página)", schema = @Schema(defaultValue = "20", minimum = "1", maximum = "200"))
        @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ResponseEntity.ok(agencyService.searchAgencies(cnpj, createdFrom, createdTo, pageable));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter agência por ID",
        description = "O que faz: recupera um perfil de Agência pelo UUID do próprio perfil.\n" +
            "Quando usar: quando você possui o ID do perfil (Agency).\n" +
            "Observações: retorna 404 se não existir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: agência encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgencyDTO.class))),
        @ApiResponse(responseCode = "404", description = "Erro: agência não encontrada para o ID informado")
    })
    public ResponseEntity<AgencyDTO> getAgencyById(@PathVariable UUID id) {
        return ResponseEntity.ok(agencyService.getAgencyById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Obter agência por userId",
        description = "O que faz: recupera o perfil de Agência associado a um usuário (User).\n" +
            "Quando usar: quando você tem o UUID do usuário e precisa do perfil específico de Agência.\n" +
            "Observações: retorna 404 se não houver perfil associado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: agência encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgencyDTO.class))),
        @ApiResponse(responseCode = "404", description = "Erro: agência não encontrada para o userId informado")
    })
    public ResponseEntity<AgencyDTO> getAgencyByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(agencyService.getAgencyByUserId(userId));
    }

    @GetMapping("/exists")
    @Operation(
        summary = "Verificar existência de agência",
        description = "O que faz: verifica se existe agência para o CNPJ informado.\n" +
            "Quando usar: validação rápida em cadastros e integrações.\n" +
            "Observações: retorna apenas exists=true/false (não expõe dados da agência)."
    )
    @ApiResponse(responseCode = "200", description = "Sucesso: resultado da validação",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExistsResponseDTO.class)))
    public ResponseEntity<ExistsResponseDTO> agencyExists(@RequestParam String cnpj) {
        return ResponseEntity.ok(ExistsResponseDTO.of(agencyService.existsByCnpj(cnpj)));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar agência",
        description = "O que faz: atualiza dados do perfil de Agência pelo UUID do perfil.\n" +
            "Quando usar: manutenção/edição de cadastro da agência.\n" +
            "Observações: validações seguem o contrato do AgencyDTO; retorna 404 se o perfil não existir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: agência atualizada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgencyDTO.class))),
        @ApiResponse(responseCode = "404", description = "Erro: agência não encontrada para o ID informado"),
        @ApiResponse(responseCode = "400", description = "Erro: requisição inválida (validação de campos/formatos)")
    })
    public ResponseEntity<AgencyDTO> updateAgency(@PathVariable UUID id, @Valid @RequestBody AgencyDTO agencyDTO) {
        return ResponseEntity.ok(agencyService.updateAgency(id, agencyDTO));
    }
}
