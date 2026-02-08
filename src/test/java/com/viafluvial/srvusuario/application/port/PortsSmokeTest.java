package com.viafluvial.srvusuario.application.port;

import com.viafluvial.srvusuario.application.port.in.AdminUseCase;
import com.viafluvial.srvusuario.application.port.in.AgencyUseCase;
import com.viafluvial.srvusuario.application.port.in.ApprovalUseCase;
import com.viafluvial.srvusuario.application.port.in.AuthUseCase;
import com.viafluvial.srvusuario.application.port.in.BoatmanUseCase;
import com.viafluvial.srvusuario.application.port.in.CreateUserUseCase;
import com.viafluvial.srvusuario.application.port.in.GetUserUseCase;
import com.viafluvial.srvusuario.application.port.in.PassengerUseCase;
import com.viafluvial.srvusuario.application.port.in.UserManagementUseCase;
import com.viafluvial.srvusuario.application.port.out.AdminRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.AgencyRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.ApprovalRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.BoatmanRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.PassengerRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserPreferenceRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PortsSmokeTest {

    @Test
    void useCasePortsShouldBeInterfacesWithMethods() {
        assertPort(AdminUseCase.class);
        assertPort(AgencyUseCase.class);
        assertPort(ApprovalUseCase.class);
        assertPort(AuthUseCase.class);
        assertPort(BoatmanUseCase.class);
        assertPort(CreateUserUseCase.class);
        assertPort(GetUserUseCase.class);
        assertPort(PassengerUseCase.class);
        assertPort(UserManagementUseCase.class);
    }

    @Test
    void repositoryPortsShouldBeInterfacesWithMethods() {
        assertPort(AdminRepositoryPort.class);
        assertPort(AgencyRepositoryPort.class);
        assertPort(ApprovalRepositoryPort.class);
        assertPort(BoatmanRepositoryPort.class);
        assertPort(PassengerRepositoryPort.class);
        assertPort(UserPreferenceRepositoryPort.class);
        assertPort(UserRepositoryPort.class);
    }

    private static void assertPort(Class<?> portType) {
        assertThat(portType.isInterface()).isTrue();
        assertThat(portType.getDeclaredMethods().length).isGreaterThan(0);
    }
}
