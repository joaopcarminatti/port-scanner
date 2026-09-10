package br.com.joaopcarminatti.portscanner.scanner;

import br.com.joaopcarminatti.portscanner.model.Port;
import br.com.joaopcarminatti.portscanner.model.PortState;
import br.com.joaopcarminatti.portscanner.model.ScanRequest;
import br.com.joaopcarminatti.portscanner.model.ScanResult;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação de PortScanner que usa a técnica de TCP Connect Scan:
 * para cada porta na faixa solicitada, tenta abrir uma conexão TCP
 * e traduz o resultado (sucesso, RST, timeout) em um PortState.
 * Esta versão é sequencial — testa uma porta por vez.
 */
public class TcpConnectScanner implements PortScanner {

    @Override
    public ScanResult scan(ScanRequest request) {
        Instant start = Instant.now();

        List<Port> results = new ArrayList<>();

        for (int portNumber = request.getStartPort(); portNumber <= request.getEndPort(); portNumber++) {
            PortState state = probe(request, portNumber);
            results.add(new Port(portNumber, state));
        }

        Duration elapsed = Duration.between(start, Instant.now());

        return new ScanResult(request.getTarget(), results, elapsed);
    }

    private PortState probe(ScanRequest request, int portNumber) {
        InetSocketAddress endpoint = new InetSocketAddress(
                request.getTarget().getAddress(),
                portNumber
        );

        try (Socket socket = new Socket()) {
            socket.connect(endpoint, request.getTimeoutMs());
            return PortState.OPEN;
        } catch (SocketTimeoutException e) {
            return PortState.FILTERED;
        } catch (ConnectException e) {
            return PortState.CLOSED;
        } catch (IOException e) {
            return PortState.FILTERED;
        }
    }
}