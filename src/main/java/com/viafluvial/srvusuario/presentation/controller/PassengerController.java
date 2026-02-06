package com.viafluvial.srvusuario.presentation.controller;

import com.viafluvial.srvusuario.application.dto.PassengerDTO;
import com.viafluvial.srvusuario.application.dto.ExistsResponseDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.dto.PagedPassengerResponseDTO;
import com.viafluvial.srvusuario.application.service.PassengerService;
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
@RequestMapping("/passengers")
@Tag(
    name = "Passageiros",
    description = "APIs do contexto IAM para gerenciamento do perfil de Passageiro (dados complementares do usuário). Inclui cadastro, consulta e listagem paginada com filtros."
)
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping
    @Operation(
        summary = "Criar passageiro",
        description = "O que faz: cria o perfil de Passageiro associado a um usuário (userId).\n" +
            "Quando usar: após criar o usuário base (User) e precisar registrar o perfil específico de Passageiro.\n" +
            "Observações: CPF deve ser único; retorna 409 em conflito; em erro retorna ErrorResponse com traceId."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso: passageiro criado (retorna o perfil criado)",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PassengerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Erro: requisição inválida (validação de campos/formatos)"),
        @ApiResponse(responseCode = "409", description = "Erro: conflito (já existe passageiro com o CPF informado)")
    })
    public ResponseEntity<PassengerDTO> createPassenger(@Valid @RequestBody PassengerDTO passengerDTO) {
        PassengerDTO createdPassenger = passengerService.createPassenger(passengerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPassenger);
    }

    @GetMapping
    @Operation(
        summary = "Listar passageiros (paginado)",
        description = "O que faz: lista passageiros com paginação e filtros.\n" +
            "Quando usar: listagens de backoffice e consultas administrativas.\n" +
            "Observações: paginação one-based (page=1); filtros por CPF e intervalo de criação; datas em ISO-8601."
    )
    @ApiResponse(responseCode = "200", description = "Sucesso: lista paginada de passageiros",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedPassengerResponseDTO.class)))
    public ResponseEntity<PagedResponse<PassengerDTO>> searchPassengers(
        @RequestParam(required = false) String cpf,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdFrom,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTo,
        @Parameter(description = "Índice da página (one-based: 1..N)", schema = @Schema(defaultValue = "1", minimum = "1"))
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @Parameter(description = "Tamanho da página (quantidade de itens por página)", schema = @Schema(defaultValue = "20", minimum = "1", maximum = "200"))
        @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ResponseEntity.ok(passengerService.searchPassengers(cpf, createdFrom, createdTo, pageable));
    }

    @GetMapping("/exists")
    @Operation(
        summary = "Verificar existência de passageiro",
        description = "O que faz: verifica se existe passageiro para o CPF informado.\n" +
            "Quando usar: validação rápida em cadastros e integrações.\n" +
            "Observações: retorna apenas exists=true/false (não expõe dados do passageiro)."
    )
    @ApiResponse(responseCode = "200", description = "Sucesso: resultado da validação",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExistsResponseDTO.class)))
    public ResponseEntity<ExistsResponseDTO> passengerExists(@RequestParam String cpf) {
        return ResponseEntity.ok(ExistsResponseDTO.of(passengerService.existsByCpf(cpf)));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter passageiro por ID",
        description = "O que faz: recupera um perfil de Passageiro pelo UUID do próprio perfil.\n" +
            "Quando usar: quando você possui o ID do perfil (Passenger).\n" +
            "Observações: retorna 404 se não existir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: passageiro encontrado"),
        @ApiResponse(responseCode = "404", description = "Erro: passageiro não encontrado para o ID informado")
    })
    public ResponseEntity<PassengerDTO> getPassengerById(@PathVariable UUID id) {
        PassengerDTO passenger = passengerService.getPassengerById(id);
        return ResponseEntity.ok(passenger);
    }

    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Obter passageiro por userId",
        description = "O que faz: recupera o perfil de Passageiro associado a um usuário (User).\n" +
            "Quando usar: quando você tem o UUID do usuário e precisa do perfil específico de Passageiro.\n" +
            "Observações: retorna 404 se não houver perfil associado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: passageiro encontrado"),
        @ApiResponse(responseCode = "404", description = "Erro: passageiro não encontrado para o userId informado")
    })
    public ResponseEntity<PassengerDTO> getPassengerByUserId(@PathVariable UUID userId) {
        PassengerDTO passenger = passengerService.getPassengerByUserId(userId);
        return ResponseEntity.ok(passenger);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar passageiro",
        description = "O que faz: atualiza dados do perfil de Passageiro pelo UUID do perfil.\n" +
            "Quando usar: manutenção/edição de cadastro do passageiro.\n" +
            "Observações: validações seguem o contrato do PassengerDTO; retorna 404 se o perfil não existir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso: passageiro atualizado"),
        @ApiResponse(responseCode = "404", description = "Erro: passageiro não encontrado para o ID informado"),
        @ApiResponse(responseCode = "400", description = "Erro: requisição inválida (validação de campos/formatos)")
    })
    public ResponseEntity<PassengerDTO> updatePassenger(@PathVariable UUID id, @Valid @RequestBody PassengerDTO passengerDTO) {
        PassengerDTO updatedPassenger = passengerService.updatePassenger(id, passengerDTO);
        return ResponseEntity.ok(updatedPassenger);
    }
}
