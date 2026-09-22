package br.com.joaopcarminatti.portscanner.model;

import br.com.joaopcarminatti.portscanner.exception.InvalidTargetException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Target")
class TargetTest {

    @Nested
    @DisplayName("Construtor")
    class ConstrutorTests {

        @Test
        @DisplayName("deve criar Target válido a partir de localhost")
        void deveCriarComLocalhost() {
            Target target = new Target("localhost");

            assertEquals("localhost", target.getHost());
            assertNotNull(target.getAddress());
        }

        @Test
        @DisplayName("deve aceitar IP direto sem consultar DNS")
        void deveAceitarIpDireto() {
            Target target = new Target("127.0.0.1");

            assertEquals("127.0.0.1", target.getHost());
            assertEquals("127.0.0.1", target.getAddress().getHostAddress());
        }

        @Test
        @DisplayName("deve remover espaços em branco das pontas do host")
        void deveRemoverEspacos() {
            Target target = new Target("  localhost  ");

            assertEquals("localhost", target.getHost());
        }

        @Test
        @DisplayName("deve lançar InvalidTargetException quando host é nulo")
        void deveRejeitarHostNulo() {
            InvalidTargetException ex = assertThrows(
                    InvalidTargetException.class,
                    () -> new Target(null)
            );

            assertTrue(ex.getMessage().toLowerCase().contains("vazio"));
        }

        @Test
        @DisplayName("deve lançar InvalidTargetException quando host é vazio")
        void deveRejeitarHostVazio() {
            assertThrows(
                    InvalidTargetException.class,
                    () -> new Target("")
            );
        }

        @Test
        @DisplayName("deve lançar InvalidTargetException quando host é só espaços")
        void deveRejeitarHostSoEspacos() {
            assertThrows(
                    InvalidTargetException.class,
                    () -> new Target("     ")
            );
        }

        @Test
        @DisplayName("deve lançar InvalidTargetException quando DNS não resolve o host")
        void deveRejeitarHostInexistente() {
            InvalidTargetException ex = assertThrows(
                    InvalidTargetException.class,
                    () -> new Target("asdfghjklzxcvbnm.invalido")
            );

            assertNotNull(ex.getCause());
            assertEquals("java.net.UnknownHostException", ex.getCause().getClass().getName());
        }
    }

    @Nested
    @DisplayName("equals e hashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("dois Target com mesmo host devem ser iguais")
        void doisTargetsIguaisDevemSerIguais() {
            Target a = new Target("localhost");
            Target b = new Target("localhost");

            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("dois Target com hosts diferentes não devem ser iguais")
        void doisTargetsDiferentesDevemSerDiferentes() {
            Target a = new Target("localhost");
            Target b = new Target("127.0.0.1");

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("Target não deve ser igual a null")
        void naoDeveSerIgualANull() {
            Target target = new Target("localhost");

            assertNotEquals(null, target);
        }

        @Test
        @DisplayName("Target não deve ser igual a objeto de outra classe")
        void naoDeveSerIgualAOutraClasse() {
            Target target = new Target("localhost");

            assertNotEquals("localhost", target);
        }
    }

    @Nested
    @DisplayName("toString()")
    class ToStringTests {

        @Test
        @DisplayName("deve conter host e IP entre parênteses")
        void deveConterHostEIp() {
            Target target = new Target("localhost");
            String output = target.toString();

            assertTrue(output.contains("localhost"));
            assertTrue(output.contains("(") && output.contains(")"));
        }
    }
}