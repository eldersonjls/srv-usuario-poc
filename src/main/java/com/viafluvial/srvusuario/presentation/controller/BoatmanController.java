package com.viafluvial.srvusuario.presentation.controller;

import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.application.dto.BoatmanDocumentsDTO;
import com.viafluvial.srvusuario.application.dto.ExistsResponseDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.dto.PagedBoatmanResponseDTO;
import com.viafluvial.srvusuario.application.service.BoatmanService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/boatmen")
@Tag(
    name = "Barqueiros",
    description = "APIs do contexto IAM para gerenciamento do perfil de Barqueiro (dados complementares do usuário). Inclui cadastro, consulta, listagem paginada com filtros e gerenciamento de documentos (URLs)."
)
public class BoatmanController {

    private final BoatmanService boatmanService;

    public BoatmanController(BoatmanService boatmanService) {
        this.boatmanService = boatmanService;
    }

    @PostMapping
    @Operation(
        summary = "Criar barqueiro",
        description = "O que faz: cria o perfil de Barqueiro associado a um usuário (userId).\n" +
            "Quando usar: após criar o usuário base (User) e precisar registrar o perfil específico de Barqueiro.\n" +
            "Observações: CPF e/ou CNPJ devem ser únicos (conforme regras do serviço); retorna 409 em conflito; em erro retorna ErrorResponse com traceId."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso: barqueiro criado (retorna o perfil criado)",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BoatmanDTO.class))),
        @ApiResponse(responseCode = "400", description = "Erro: requisição inválida (validação de campos/formatos)"),
        @ApiResponse(responseCode = "409", description = "Erro: conflito (CPF e/ou CNPJ já registrado)")
    })
    public ResponseEntity<BoatmanDTO> createBoatman(@Valid @RequestBody BoatmanDTO boatmanDTO) {
        BoatmanDTO createdBoatman = boatmanService.createBoatman(boatmanDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoatman);
    }

    @GetMapping
    @Operation(
        summary = "Listar barqueiros (paginado)",
        description = "O que faz: lista barqueiros com paginação e filtros.\n" +
            "Quando usar: listagens administrativas e consultas com critérios (CPF/CNPJ/rating/datas).\n" +
            "Observações: paginação one-based (page=1); filtros por CPF, CNPJ, rating mínimo e intervalos de datas (aprovação/criação); datas em ISO-8601."
    )
    @ApiResponse(responseCode = "200", description = "Sucesso: lista paginada de barqueiros",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedBoatmanResponseDTO.class)))
    public ResponseEntity<PagedResponse<BoatmanDTO>> searchBoatmen(
        @RequestParam(required = false) String cpf,
        @RequestParam(required = false) String cnpj,
        @RequestParam(required = false) BigDecimal ratingMin,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime approvedFrom,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime approvedTo,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdFrom,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTo,
        @Parameter(description = "Índice da página (one-based: 1..N)", schema = @Schema(defaultValue = "1", minimum = "1"))
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @Parameter(description = "Tamanho da página (quantidade de itens por página)", schema = @Schema(defaultValue = "20", minimum = "1", maximum = "200"))
        @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size
    ) {
        return ResponseEntity.ok(
            boatmanService.searchBoatmen(cpf, cnpj, ratingMin, approvedFrom, approvedTo, createdFrom, createdTo, PageRequest.of(page - 1, size))
        );
    }

    @GetMapping("/exists")
    @Operation(
        summary = "Verificar existência de barqueiro",
        description = "O que faz: verifica se existe barqueiro para CPF e/ou CNPJ informados.\n" +
            "Quando usar: validação rápida em cadastros e integrações.\n" +
            "Observações: é obrigatório informar pelo menos um (cpf ou cnpj); retorna apenas exists=true/false."
    )
    @ApiResponse(responseCode = "200", description = "Sucesso: resultado da validação",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExistsResponseDTO.class)))
    public ResponseEntity<ExistsResponseDTO> boatmanExists(
        @RequestParam(required = false) String cpf,
        @RequestParam(required = false) String cnpj
    ) {
        if ((cpf == null || cpf.isBlank()) && (cnpj == null || cnpj.isBlank())) {
            throw new IllegalArgumentException("Informe cpf ou cnpj");
        }

        boolean exists = false;
        if (cpf != null && !cpf.isBlank()) {
            exists = exists || boatmanService.existsByCpf(cpf);
        }
        if (cnpj != null && !cnpj.isBlank()) {
            exists = exists || boatmanService.existsByCnpj(cnpj);
        }

        return ResponseEntity.ok(ExistsResponseDTO.of(exists));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter barqueiro por ID",
        description = "O que faz: recupera um perfil de Barqueiro pelo UUID do próprio perfil.\n" +
            "Quando usar: quando você possui o ID do perfil (Boatman).\n" +
            "Observações: retorna 404 se não existir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: barqueiro encontrado"),
        @ApiResponse(responseCode = "404", description = "Erro: barqueiro não encontrado para o ID informado")
    })
    public ResponseEntity<BoatmanDTO> getBoatmanById(@PathVariable UUID id) {
        BoatmanDTO boatman = boatmanService.getBoatmanById(id);
        return ResponseEntity.ok(boatman);
    }

    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Obter barqueiro por userId",
        description = "O que faz: recupera o perfil de Barqueiro associado a um usuário (User).\n" +
            "Quando usar: quando você tem o UUID do usuário e precisa do perfil específico de Barqueiro.\n" +
            "Observações: retorna 404 se não houver perfil associado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: barqueiro encontrado"),
        @ApiResponse(responseCode = "404", description = "Erro: barqueiro não encontrado para o userId informado")
    })
    public ResponseEntity<BoatmanDTO> getBoatmanByUserId(@PathVariable UUID userId) {
        BoatmanDTO boatman = boatmanService.getBoatmanByUserId(userId);
        return ResponseEntity.ok(boatman);
    }

    @GetMapping("/{id}/documents")
    @Operation(
        summary = "Obter documentos do barqueiro",
        description = "O que faz: retorna as URLs dos documentos vinculados ao perfil de Barqueiro.\n" +
            "Quando usar: exibir/validar documentos durante onboarding e auditoria.\n" +
            "Observações: os campos disponíveis dependem do contrato do BoatmanDocumentsDTO; retorna 404 se o barqueiro não existir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: documentos retornados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BoatmanDocumentsDTO.class))),
        @ApiResponse(responseCode = "404", description = "Erro: barqueiro não encontrado para o ID informado")
    })
    public ResponseEntity<BoatmanDocumentsDTO> getBoatmanDocuments(@PathVariable UUID id) {
        return ResponseEntity.ok(boatmanService.getBoatmanDocuments(id));
    }

    @PatchMapping("/{id}/documents")
    @Operation(
        summary = "Atualizar documentos do barqueiro",
        description = "O que faz: atualiza (parcialmente) as URLs dos documentos do perfil de Barqueiro.\n" +
            "Quando usar: quando novos documentos forem enviados/substituídos no processo de onboarding.\n" +
            "Observações: campos não informados permanecem inalterados; retorna 404 se o barqueiro não existir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: documentos atualizados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BoatmanDocumentsDTO.class))),
        @ApiResponse(responseCode = "404", description = "Erro: barqueiro não encontrado para o ID informado")
    })
    public ResponseEntity<BoatmanDocumentsDTO> updateBoatmanDocuments(
        @PathVariable UUID id,
        @RequestBody BoatmanDocumentsDTO documentsDTO
    ) {
        return ResponseEntity.ok(boatmanService.updateBoatmanDocuments(id, documentsDTO));
    }
}
