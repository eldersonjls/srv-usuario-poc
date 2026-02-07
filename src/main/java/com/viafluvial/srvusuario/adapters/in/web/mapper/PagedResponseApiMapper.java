package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.ExistsResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PagedAgencyResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PagedApprovalResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PagedBoatmanResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PagedPassengerResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PagedUserResponseApi;
import com.viafluvial.srvusuario.application.dto.ExistsResponseDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.dto.AgencyDTO;
import com.viafluvial.srvusuario.application.dto.ApprovalDTO;
import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.application.dto.PassengerDTO;
import com.viafluvial.srvusuario.application.dto.UserDTO;

import java.util.List;

public final class PagedResponseApiMapper {

    private PagedResponseApiMapper() {
    }

    public static ExistsResponseApi toApi(ExistsResponseDTO app) {
        if (app == null) {
            return null;
        }
        ExistsResponseApi api = new ExistsResponseApi();
        api.setExists(app.isExists());
        return api;
    }

    public static PagedUserResponseApi toPagedUserResponse(PagedResponse<UserDTO> response) {
        if (response == null) {
            return null;
        }
        PagedUserResponseApi api = new PagedUserResponseApi();
        api.setItems(mapUsers(response.getItems()));
        api.setPage(response.getPage());
        api.setSize(response.getSize());
        api.setTotalItems(response.getTotalItems());
        api.setTotalPages(response.getTotalPages());
        return api;
    }

    public static PagedPassengerResponseApi toPagedPassengerResponse(PagedResponse<PassengerDTO> response) {
        if (response == null) {
            return null;
        }
        PagedPassengerResponseApi api = new PagedPassengerResponseApi();
        api.setItems(mapPassengers(response.getItems()));
        api.setPage(response.getPage());
        api.setSize(response.getSize());
        api.setTotalItems(response.getTotalItems());
        api.setTotalPages(response.getTotalPages());
        return api;
    }

    public static PagedBoatmanResponseApi toPagedBoatmanResponse(PagedResponse<BoatmanDTO> response) {
        if (response == null) {
            return null;
        }
        PagedBoatmanResponseApi api = new PagedBoatmanResponseApi();
        api.setItems(mapBoatmen(response.getItems()));
        api.setPage(response.getPage());
        api.setSize(response.getSize());
        api.setTotalItems(response.getTotalItems());
        api.setTotalPages(response.getTotalPages());
        return api;
    }

    public static PagedAgencyResponseApi toPagedAgencyResponse(PagedResponse<AgencyDTO> response) {
        if (response == null) {
            return null;
        }
        PagedAgencyResponseApi api = new PagedAgencyResponseApi();
        api.setItems(mapAgencies(response.getItems()));
        api.setPage(response.getPage());
        api.setSize(response.getSize());
        api.setTotalItems(response.getTotalItems());
        api.setTotalPages(response.getTotalPages());
        return api;
    }

    public static PagedApprovalResponseApi toPagedApprovalResponse(PagedResponse<ApprovalDTO> response) {
        if (response == null) {
            return null;
        }
        PagedApprovalResponseApi api = new PagedApprovalResponseApi();
        api.setItems(mapApprovals(response.getItems()));
        api.setPage(response.getPage());
        api.setSize(response.getSize());
        api.setTotalItems(response.getTotalItems());
        api.setTotalPages(response.getTotalPages());
        return api;
    }

    private static List<com.viafluvial.srvusuario.adapters.in.web.dto.UserDTOApi> mapUsers(List<UserDTO> users) {
        if (users == null) {
            return null;
        }
        return users.stream().map(UserApiMapper::toApi).toList();
    }

    private static List<com.viafluvial.srvusuario.adapters.in.web.dto.PassengerDTOApi> mapPassengers(List<PassengerDTO> passengers) {
        if (passengers == null) {
            return null;
        }
        return passengers.stream().map(PassengerApiMapper::toApi).toList();
    }

    private static List<com.viafluvial.srvusuario.adapters.in.web.dto.BoatmanDTOApi> mapBoatmen(List<BoatmanDTO> boatmen) {
        if (boatmen == null) {
            return null;
        }
        return boatmen.stream().map(BoatmanApiMapper::toApi).toList();
    }

    private static List<com.viafluvial.srvusuario.adapters.in.web.dto.AgencyDTOApi> mapAgencies(List<AgencyDTO> agencies) {
        if (agencies == null) {
            return null;
        }
        return agencies.stream().map(AgencyApiMapper::toApi).toList();
    }

    private static List<com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalDTOApi> mapApprovals(List<ApprovalDTO> approvals) {
        if (approvals == null) {
            return null;
        }
        return approvals.stream().map(ApprovalApiMapper::toApi).toList();
    }
}
