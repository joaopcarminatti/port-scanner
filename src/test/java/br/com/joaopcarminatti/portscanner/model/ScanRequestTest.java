package br.com.joaopcarminatti.portscanner.model;

import br.com.joaopcarminatti.portscanner.exception.InvalidScanRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ScanRequest")
class ScanRequestTest {

    private Target target;

    @BeforeEach
    void setUp() {
        target = new Target("localhost");
    }

    @Nested
    @DisplayName("Construtor completo")
    class ConstrutorCompletoTests {

        @Test
        @DisplayName("deve criar requisição válida com todos os parâmetros")
        void deveCriarRequisicaoValida() {
            ScanRequest request = new ScanRequest(target, 1, 1024, 2000, 100);

            assertEquals(target, request.getTarget());
            assertEquals(1, request.getStartPort());
            assertEquals(1024, request.getEndPort());
            assertEquals(2000, request.getTimeoutMs());
            assertEquals(100, request.getThreads());
        }

        @Test
        @DisplayName("deve lançar InvalidScanRequestException quando target é nulo")
        void deveRejeitarTargetNulo() {
            assertThrows(
                    InvalidScanRequestException.class,
                    () -> new ScanRequest(null, 1, 1024, 1000, 50)
            );
        }

        @Test
        @DisplayName("deve rejeitar porta inicial abaixo do mínimo")
        void deveRejeitarStartPortAbaixoDoMinimo() {
            assertThrows(
                    InvalidScanRequestException.class,
                    () -> new ScanRequest(target, 0, 1024, 1000, 50)
            );
        }

        @Test
        @DisplayName("deve rejeitar porta final acima do máximo")
        void deveRejeitarEndPortAcimaDoMaximo() {
            assertThrows(
                    InvalidScanRequestException.class,
                    () -> new ScanRequest(target, 1, 70000, 1000, 50)
            );
        }

        @Test
        @DisplayName("deve rejeitar intervalo invertido")
        void deveRejeitarIntervaloInvertido() {
            InvalidScanRequestException ex = assertThrows(
                    InvalidScanRequestException.class,
                    () -> new ScanRequest(target, 500, 100, 1000, 50)
            );

            assertTrue(ex.getMessage().contains("500"));
            assertTrue(ex.getMessage().contains("100"));
        }

        @Test
        @DisplayName("deve rejeitar timeout zero")
        void deveRejeitarTimeoutZero() {
            assertThrows(
                    InvalidScanRequestException.class,
                    () -> new ScanRequest(target, 1, 1024, 0, 50)
            );
        }

        @Test
        @DisplayName("deve rejeitar timeout negativo")
        void deveRejeitarTimeoutNegativo() {
            assertThrows(
                    InvalidScanRequestException.class,
                    () -> new ScanRequest(target, 1, 1024, -100, 50)
            );
        }

        @Test
        @DisplayName("deve rejeitar threads zero")
        void deveRejeitarThreadsZero() {
            assertThrows(
                    InvalidScanRequestException.class,
                    () -> new ScanRequest(target, 1, 1024, 1000, 0)
            );
        }

        @Test
        @DisplayName("deve aceitar startPort igual a endPort (intervalo de 1 porta)")
        void deveAceitarIntervaloDeUmaPorta() {
            ScanRequest request = new ScanRequest(target, 22, 22, 1000, 50);

            assertEquals(1, request.getPortCount());
        }
    }

    @Nested
    @DisplayName("Construtor curto")
    class ConstrutorCurtoTests {

        @Test
        @DisplayName("deve usar timeout padrão quando não especificado")
        void deveUsarTimeoutPadrao() {
            ScanRequest request = new ScanRequest(target, 1, 1024);

            assertEquals(ScanRequest.DEFAULT_TIMEOUT_MS, request.getTimeoutMs());
        }

        @Test
        @DisplayName("deve usar número de threads padrão quando não especificado")
        void deveUsarThreadsPadrao() {
            ScanRequest request = new ScanRequest(target, 1, 1024);

            assertEquals(ScanRequest.DEFAULT_THREADS, request.getThreads());
        }

        @Test
        @DisplayName("deve delegar validação ao construtor completo")
        void deveDelegarValidacao() {
            assertThrows(
                    InvalidScanRequestException.class,
                    () -> new ScanRequest(target, 500, 100)
            );
        }
    }

    @Nested
    @DisplayName("getPortCount()")
    class PortCountTests {

        @Test
        @DisplayName("deve calcular corretamente para intervalo comum")
        void deveCalcularIntervaloComum() {
            ScanRequest request = new ScanRequest(target, 1, 1024);

            assertEquals(1024, request.getPortCount());
        }

        @Test
        @DisplayName("deve incluir ambas as portas nos extremos")
        void deveIncluirExtremos() {
            ScanRequest request = new ScanRequest(target, 20, 30);

            assertEquals(11, request.getPortCount());
        }
    }
}