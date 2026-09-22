package br.com.joaopcarminatti.portscanner.format;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ServiceRegistry")
class ServiceRegistryTest {

    @Test
    @DisplayName("deve retornar 'ssh' para porta 22")
    void deveRetornarSshParaPorta22() {
        assertEquals("ssh", ServiceRegistry.getServiceName(22));
    }

    @Test
    @DisplayName("deve retornar 'http' para porta 80")
    void deveRetornarHttpParaPorta80() {
        assertEquals("http", ServiceRegistry.getServiceName(80));
    }

    @Test
    @DisplayName("deve retornar 'https' para porta 443")
    void deveRetornarHttpsParaPorta443() {
        assertEquals("https", ServiceRegistry.getServiceName(443));
    }

    @Test
    @DisplayName("deve retornar 'mysql' para porta 3306")
    void deveRetornarMysqlParaPorta3306() {
        assertEquals("mysql", ServiceRegistry.getServiceName(3306));
    }

    @Test
    @DisplayName("deve retornar 'unknown' para porta não registrada")
    void deveRetornarUnknownParaPortaDesconhecida() {
        assertEquals("unknown", ServiceRegistry.getServiceName(9999));
    }

    @Test
    @DisplayName("deve retornar 'unknown' para porta 0")
    void deveRetornarUnknownParaPortaZero() {
        assertEquals("unknown", ServiceRegistry.getServiceName(0));
    }
}