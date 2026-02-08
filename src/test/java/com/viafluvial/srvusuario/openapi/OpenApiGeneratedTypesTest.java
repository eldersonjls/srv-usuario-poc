package com.viafluvial.srvusuario.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OpenAPI Generated: smoke tests")
class OpenApiGeneratedTypesTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    @DisplayName("APIs geradas: devem ser interfaces carregáveis")
    void generatedApisShouldBeInterfaces() throws Exception {
        List<String> apiClasses = List.of(
            "com.viafluvial.srvusuario.adapters.in.web.api.AdminsApi",
            "com.viafluvial.srvusuario.adapters.in.web.api.AgenciesApi",
            "com.viafluvial.srvusuario.adapters.in.web.api.ApprovalsApi",
            "com.viafluvial.srvusuario.adapters.in.web.api.BoatmenApi",
            "com.viafluvial.srvusuario.adapters.in.web.api.PassengersApi",
            "com.viafluvial.srvusuario.adapters.in.web.api.UsersApi"
        );

        for (String name : apiClasses) {
            Class<?> clazz = Class.forName(name);
            assertThat(clazz.isInterface()).as(name).isTrue();
            assertThat(clazz.getMethods().length).as(name + " methods").isGreaterThan(0);
        }
    }

    @Test
    @DisplayName("Enums gerados: fromValue deve funcionar e validar")
    void generatedEnumsFromValueShouldWork() throws Exception {
        List<String> enumClasses = List.of(
            "com.viafluvial.srvusuario.adapters.in.web.dto.UserTypeApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.UserStatusApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.AdminRoleApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.CabinTypeApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalStatusApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalEntityTypeApi"
        );

        for (String name : enumClasses) {
            Class<?> clazz = Class.forName(name);
            assertThat(clazz.isEnum()).as(name).isTrue();

            Object[] values = clazz.getEnumConstants();
            assertThat(values).as(name + " values").isNotEmpty();

            Method toString = clazz.getMethod("toString");
            Method fromValue = clazz.getMethod("fromValue", String.class);

            String valid = (String) toString.invoke(values[0]);
            Object parsed = fromValue.invoke(null, valid);
            assertThat(parsed).isEqualTo(values[0]);

            assertThatThrownBy(() -> fromValue.invoke(null, "__INVALID__"))
                .hasRootCauseInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("DTOs gerados: devem instanciar e serializar JSON (default ctor + setters)")
    void generatedDtosShouldInstantiateAndSerialize() throws Exception {
        List<String> dtoClasses = List.of(
            "com.viafluvial.srvusuario.adapters.in.web.dto.AdminDTOApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.AgencyDTOApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalCreateDTOApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalDTOApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.BoatmanDTOApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.BoatmanDocumentsDTOApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.ExistsResponseApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.PagedAgencyResponseApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.PagedApprovalResponseApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.PagedBoatmanResponseApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.PagedPassengerResponseApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.PagedUserResponseApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.PassengerDTOApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.UserCreateDTOApi",
            "com.viafluvial.srvusuario.adapters.in.web.dto.UserDTOApi"
        );

        for (String name : dtoClasses) {
            Class<?> clazz = Class.forName(name);
            assertThat(Modifier.isPublic(clazz.getModifiers())).as(name + " public").isTrue();

            Constructor<?> ctor = clazz.getDeclaredConstructor();
            Object instance = ctor.newInstance();

            // chama todos os setters com null/defaults (garante que não explode)
            for (Method m : clazz.getMethods()) {
                if (!m.getName().startsWith("set") || m.getParameterCount() != 1) {
                    continue;
                }
                Class<?> p = m.getParameterTypes()[0];
                Object arg = defaultValue(p);
                assertThatCode(() -> m.invoke(instance, arg))
                    .as(name + "." + m.getName())
                    .doesNotThrowAnyException();
            }

            assertThatCode(() -> objectMapper.writeValueAsString(instance))
                .as("serialize " + name)
                .doesNotThrowAnyException();
        }
    }

    private static Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (type == boolean.class) {
            return false;
        }
        if (type == byte.class) {
            return (byte) 0;
        }
        if (type == short.class) {
            return (short) 0;
        }
        if (type == int.class) {
            return 0;
        }
        if (type == long.class) {
            return 0L;
        }
        if (type == float.class) {
            return 0f;
        }
        if (type == double.class) {
            return 0d;
        }
        if (type == char.class) {
            return '\0';
        }
        return null;
    }
}
