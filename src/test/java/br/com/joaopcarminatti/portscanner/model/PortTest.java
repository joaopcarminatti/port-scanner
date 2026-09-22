package br.com.joaopcarminatti.portscanner.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Port")
class PortTest {

    @Nested
    @DisplayName("Construtor")
    class ConstrutorTests {

        @Test
        @DisplayName("deve criar porta válida com número e estado")
        void deveCriarPortaValida() {
            Port port = new Port(22, PortState.OPEN);

            assertEquals(22, port.getNumber());
            assertEquals(PortState.OPEN, port.getState());
        }

        @Test
        @DisplayName("deve aceitar limite mínimo (1)")
        void deveAceitarPortaMinima() {
            Port port = new Port(Port.MIN_PORT, PortState.CLOSED);

            assertEquals(1, port.getNumber());
        }

        @Test
        @DisplayName("deve aceitar limite máximo (65535)")
        void deveAceitarPortaMaxima() {
            Port port = new Port(Port.MAX_PORT, PortState.FILTERED);

            assertEquals(65535, port.getNumber());
        }

        @Test
        @DisplayName("deve lançar IllegalArgumentException quando porta é zero")
        void deveRejeitarPortaZero() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Port(0, PortState.OPEN)
            );

            assertTrue(ex.getMessage().contains("intervalo"));
        }

        @Test
        @DisplayName("deve lançar IllegalArgumentException quando porta é negativa")
        void deveRejeitarPortaNegativa() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Port(-1, PortState.OPEN)
            );
        }

        @Test
        @DisplayName("deve lançar IllegalArgumentException quando porta ultrapassa 65535")
        void deveRejeitarPortaAcimaDoMaximo() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Port(65536, PortState.OPEN)
            );
        }

        @Test
        @DisplayName("deve lançar IllegalArgumentException quando estado é nulo")
        void deveRejeitarEstadoNulo() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Port(80, null)
            );
        }
    }

    @Nested
    @DisplayName("isOpen()")
    class IsOpenTests {

        @Test
        @DisplayName("deve retornar true quando estado é OPEN")
        void deveRetornarTrueParaPortaAberta() {
            Port port = new Port(22, PortState.OPEN);

            assertTrue(port.isOpen());
        }

        @Test
        @DisplayName("deve retornar false quando estado é CLOSED")
        void deveRetornarFalseParaPortaFechada() {
            Port port = new Port(22, PortState.CLOSED);

            assertFalse(port.isOpen());
        }

        @Test
        @DisplayName("deve retornar false quando estado é FILTERED")
        void deveRetornarFalseParaPortaFiltrada() {
            Port port = new Port(22, PortState.FILTERED);

            assertFalse(port.isOpen());
        }
    }

    @Nested
    @DisplayName("toString()")
    class ToStringTests {

        @Test
        @DisplayName("deve conter número e label do estado")
        void deveConterNumeroEEstado() {
            Port port = new Port(80, PortState.OPEN);
            String output = port.toString();

            assertTrue(output.contains("80"));
            assertTrue(output.contains("aberta"));
        }
    }
}