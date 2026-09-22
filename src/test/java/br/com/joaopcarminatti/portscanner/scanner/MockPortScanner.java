package br.com.joaopcarminatti.portscanner.scanner;

import br.com.joaopcarminatti.portscanner.model.Port;
import br.com.joaopcarminatti.portscanner.model.PortState;
import br.com.joaopcarminatti.portscanner.model.ScanRequest;
import br.com.joaopcarminatti.portscanner.model.ScanResult;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementação falsa de PortScanner para uso em testes.
 * Não toca em rede — devolve resultados predefinidos configuráveis por porta.
 *
 * Exemplo:
 *   MockPortScanner mock = new MockPortScanner(Map.of(
 *       22, PortState.OPEN,
 *       80, PortState.CLOSED
 *   ));
 */
public class MockPortScanner implements PortScanner {

    private final Map<Integer, PortState> predefinedStates;
    private final Duration fakeDuration;

    public MockPortScanner(Map<Integer, PortState> predefinedStates) {
        this(predefinedStates, Duration.ofMillis(100));
    }

    public MockPortScanner(Map<Integer, PortState> predefinedStates, Duration fakeDuration) {
        this.predefinedStates = predefinedStates;
        this.fakeDuration = fakeDuration;
    }

    @Override
    public ScanResult scan(ScanRequest request) {
        List<Port> ports = new ArrayList<>();

        for (int portNumber = request.getStartPort(); portNumber <= request.getEndPort(); portNumber++) {
            PortState state = predefinedStates.getOrDefault(portNumber, PortState.CLOSED);
            ports.add(new Port(portNumber, state));
        }

        return new ScanResult(request.getTarget(), ports, fakeDuration);
    }
}