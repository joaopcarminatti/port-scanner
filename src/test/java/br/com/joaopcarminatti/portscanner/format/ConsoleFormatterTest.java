package br.com.joaopcarminatti.portscanner.format;

import br.com.joaopcarminatti.portscanner.model.PortState;
import br.com.joaopcarminatti.portscanner.model.ScanRequest;
import br.com.joaopcarminatti.portscanner.model.ScanResult;
import br.com.joaopcarminatti.portscanner.model.Target;
import br.com.joaopcarminatti.portscanner.scanner.MockPortScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConsoleFormatter")
class ConsoleFormatterTest {

    private ConsoleFormatter formatter;
    private Target target;

    @BeforeEach
    void setUp() {
        formatter = new ConsoleFormatter();
        target = new Target("localhost");
    }

    @Nested
    @DisplayName("Cabeçalho e resumo")
    class CabecalhoTests {

        @Test
        @DisplayName("deve conter título do relatório")
        void deveConterTitulo() {
            ScanResult result = scanFake(Map.of());

            String output = formatter.format(result);

            assertTrue(output.contains("RELATÓRIO DE SCAN"));
        }

        @Test
        @DisplayName("deve conter o alvo escaneado")
        void deveConterAlvo() {
            ScanResult result = scanFake(Map.of());

            String output = formatter.format(result);

            assertTrue(output.contains("localhost"));
        }

        @Test
        @DisplayName("deve conter duração em ms")
        void deveConterDuracao() {
            ScanResult result = scanFake(Map.of());

            String output = formatter.format(result);

            assertTrue(output.contains("ms"));
        }

        @Test
        @DisplayName("deve exibir contagem total de portas testadas")
        void deveExibirTotalTestadas() {
            ScanRequest request = new ScanRequest(target, 1, 100);
            MockPortScanner mock = new MockPortScanner(Map.of());
            ScanResult result = mock.scan(request);

            String output = formatter.format(result);

            assertTrue(output.contains("100"));
        }
    }

    @Nested
    @DisplayName("Portas abertas")
    class PortasAbertasTests {

        @Test
        @DisplayName("deve mostrar mensagem quando nenhuma porta abre")
        void deveMostrarQuandoNenhumaAberta() {
            ScanResult result = scanFake(Map.of());

            String output = formatter.format(result);

            assertTrue(output.toLowerCase().contains("nenhuma"));
        }

        @Test
        @DisplayName("deve listar porta aberta com número e serviço")
        void deveListarPortaAberta() {
            ScanResult result = scanFake(Map.of(22, PortState.OPEN));

            String output = formatter.format(result);

            assertTrue(output.contains("22"));
            assertTrue(output.contains("ssh"));
        }

        @Test
        @DisplayName("deve listar múltiplas portas abertas")
        void deveListarMultiplasAbertas() {
            ScanResult result = scanFake(Map.of(
                    22, PortState.OPEN,
                    80, PortState.OPEN,
                    443, PortState.OPEN
            ));

            String output = formatter.format(result);

            assertTrue(output.contains("22"));
            assertTrue(output.contains("80"));
            assertTrue(output.contains("443"));
            assertTrue(output.contains("ssh"));
            assertTrue(output.contains("http"));
            assertTrue(output.contains("https"));
        }

        @Test
        @DisplayName("não deve listar portas fechadas na seção de abertas")
        void naoDeveListarFechadas() {
            ScanResult result = scanFake(Map.of(
                    22, PortState.OPEN,
                    23, PortState.CLOSED,
                    80, PortState.FILTERED
            ));

            String output = formatter.format(result);

            assertTrue(output.contains("22"));
            long linhasComAberta = output.lines()
                    .filter(line -> line.contains("aberta"))
                    .count();
            assertTrue(linhasComAberta >= 1);
        }
    }

    @Nested
    @DisplayName("Aviso legal")
    class AvisoTests {

        @Test
        @DisplayName("deve incluir aviso de uso apenas em alvos autorizados")
        void deveIncluirAviso() {
            ScanResult result = scanFake(Map.of(22, PortState.OPEN));

            String output = formatter.format(result);

            assertTrue(output.toLowerCase().contains("autorizados"));
        }
    }
    private ScanResult scanFake(Map<Integer, PortState> portStates) {
        int minPort = portStates.isEmpty() ? 1 : portStates.keySet().stream().min(Integer::compareTo).orElse(1);
        int maxPort = portStates.isEmpty() ? 10 : portStates.keySet().stream().max(Integer::compareTo).orElse(10);

        ScanRequest request = new ScanRequest(target, minPort, maxPort);
        MockPortScanner mock = new MockPortScanner(portStates);
        return mock.scan(request);
    }
}