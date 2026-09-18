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
import java.util.List;

/**
 * Esqueleto comum para implementações de PortScanner que usam TCP connect.
 * Cuida da orquestração (medir tempo, montar ScanResult) e da lógica de
 * testar uma porta específica (probe). Deixa para as subclasses decidir
 * COMO iterar sobre a faixa de portas — sequencial ou paralela.
 */
public abstract class AbstractPortScanner implements PortScanner {

    @Override
    public ScanResult scan(ScanRequest request) {
        Instant start = Instant.now();

        List<Port> results = executeScan(request);

        Duration elapsed = Duration.between(start, Instant.now());

        return new ScanResult(request.getTarget(), results, elapsed);
    }

    /**
     * Cada subclasse decide como percorrer a faixa e produzir a lista de Ports.
     * Sequencial testa uma por vez; paralela dispara várias em threads simultâneas.
     */
    protected abstract List<Port> executeScan(ScanRequest request);

    /**
     * Testa uma porta específica e devolve seu estado.
     * Método compartilhado por todas as subclasses — ninguém reimplementa a lógica TCP.
     */
    protected PortState probe(ScanRequest request, int portNumber) {
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
