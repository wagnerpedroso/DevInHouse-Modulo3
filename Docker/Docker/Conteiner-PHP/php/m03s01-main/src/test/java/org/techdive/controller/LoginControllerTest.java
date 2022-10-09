package org.techdive.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.techdive.dto.LoginRequest;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    private LoginController controller;

    @BeforeEach
    private void setup() {
        controller = new LoginController();
    }


    @Test
    @DisplayName("Quando credenciais inválidas, Deve retornar status code 401")
    void autenticar_credenciaisInvalidas() {
        LoginRequest credenciais = new LoginRequest("errado@email.com", "senha_errada");
        Response result = controller.autenticar(credenciais);
        assertNotNull(result);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), result.getStatus());
        assertNotNull(result.getEntity());
        assertInstanceOf(String.class, result.getEntity());
    }

    @Test
    @DisplayName("Quando credenciais corretas, Deve retornar status code 201")
    void autenticar_credenciaisValidas() {
        LoginRequest credenciais = new LoginRequest("james@kirk.com", "1234");
        Response result = controller.autenticar(credenciais);
        assertNotNull(result);
        assertEquals(Response.Status.CREATED.getStatusCode(), result.getStatus());
        assertNotNull(result.getEntity());
        assertInstanceOf(String.class, result.getEntity());
    }

}