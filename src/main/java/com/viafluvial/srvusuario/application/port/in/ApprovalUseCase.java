package com.viafluvial.srvusuario.application.port.in;

import com.viafluvial.srvusuario.application.dto.ApprovalCreateDTO;
import com.viafluvial.srvusuario.application.dto.ApprovalDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.domain.model.Approval;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Port IN: Use case para gerenciamento de aprovacoes.
 */
public interface ApprovalUseCase {

    ApprovalDTO createApproval(ApprovalCreateDTO createDTO);

    List<ApprovalDTO> listApprovals(Approval.ApprovalStatus status);

    PagedResponse<ApprovalDTO> searchApprovals(Approval.ApprovalStatus status, Pageable pageable);

    ApprovalDTO approve(UUID approvalId);

    ApprovalDTO activate(UUID approvalId);

    ApprovalDTO block(UUID approvalId);

    ApprovalDTO unblock(UUID approvalId);

    ApprovalDTO requestMoreInfo(UUID approvalId);
}
