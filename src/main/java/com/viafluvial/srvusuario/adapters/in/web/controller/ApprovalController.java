package com.viafluvial.srvusuario.adapters.in.web.controller;

import com.viafluvial.srvusuario.adapters.in.web.api.ApprovalsApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalCreateDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalStatusApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PagedApprovalResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.mapper.ApprovalApiMapper;
import com.viafluvial.srvusuario.adapters.in.web.mapper.PagedResponseApiMapper;
import com.viafluvial.srvusuario.application.dto.ApprovalCreateDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.dto.ApprovalDTO;
import com.viafluvial.srvusuario.application.port.in.ApprovalUseCase;
import com.viafluvial.srvusuario.domain.model.Approval;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ApprovalController implements ApprovalsApi {

    private final ApprovalUseCase approvalService;

    public ApprovalController(ApprovalUseCase approvalService) {
        this.approvalService = approvalService;
    }

    @Override
    public ResponseEntity<ApprovalDTOApi> createApproval(@Valid ApprovalCreateDTOApi approvalCreateDTOApi) {
        ApprovalCreateDTO createDTO = ApprovalApiMapper.toApp(approvalCreateDTOApi);
        ApprovalDTO created = approvalService.createApproval(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApprovalApiMapper.toApi(created));
    }

    @Override
    public ResponseEntity<PagedApprovalResponseApi> listApprovals(ApprovalStatusApi status, Integer page, Integer size) {
        Approval.ApprovalStatus parsed = ApprovalApiMapper.toDomainStatus(status);
        int pageValue = page != null ? page : 1;
        int sizeValue = size != null ? size : 20;
        Pageable pageable = PageRequest.of(pageValue - 1, sizeValue);
        PagedResponse<ApprovalDTO> result = approvalService.searchApprovals(parsed, pageable);
        return ResponseEntity.ok(PagedResponseApiMapper.toPagedApprovalResponse(result));
    }

    @Override
    public ResponseEntity<ApprovalDTOApi> unblockApproval(UUID id) {
        return ResponseEntity.ok(ApprovalApiMapper.toApi(approvalService.unblock(id)));
    }

    @Override
    public ResponseEntity<ApprovalDTOApi> blockApproval(UUID id) {
        return ResponseEntity.ok(ApprovalApiMapper.toApi(approvalService.block(id)));
    }

    @Override
    public ResponseEntity<ApprovalDTOApi> approveApproval(UUID id) {
        return ResponseEntity.ok(ApprovalApiMapper.toApi(approvalService.approve(id)));
    }

    @Override
    public ResponseEntity<ApprovalDTOApi> activateApproval(UUID id) {
        return ResponseEntity.ok(ApprovalApiMapper.toApi(approvalService.activate(id)));
    }

    @Override
    public ResponseEntity<ApprovalDTOApi> requestMoreInfo(UUID id) {
        return ResponseEntity.ok(ApprovalApiMapper.toApi(approvalService.requestMoreInfo(id)));
    }
}
