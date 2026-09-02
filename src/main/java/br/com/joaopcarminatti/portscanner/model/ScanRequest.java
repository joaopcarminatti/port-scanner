package br.com.joaopcarminatti.portscanner.model;

import br.com.joaopcarminatti.portscanner.exception.InvalidScanRequestException;

/**
 * Representa os parâmetros de uma requisição de scan:
 * qual alvo, qual faixa de portas, com qual timeout e quantas threads.
 * Objeto imutável.
 */
public class ScanRequest {

    public static final int DEFAULT_TIMEOUT_MS = 1000;
    public static final int DEFAULT_THREADS = 50;

    private final Target target;
    private final int startPort;
    private final int endPort;
    private final int timeoutMs;
    private final int threads;

    public ScanRequest(Target target, int startPort, int endPort, int timeoutMs, int threads) {
        if (target == null) {
            throw new InvalidScanRequestException("Target não pode ser nulo");
        }
        if (startPort < Port.MIN_PORT || startPort > Port.MAX_PORT) {
            throw new InvalidScanRequestException(
                    "Porta inicial fora do intervalo válido (" + Port.MIN_PORT + "-" + Port.MAX_PORT + "): " + startPort
            );
        }
        if (endPort < Port.MIN_PORT || endPort > Port.MAX_PORT) {
            throw new InvalidScanRequestException(
                    "Porta final fora do intervalo válido (" + Port.MIN_PORT + "-" + Port.MAX_PORT + "): " + endPort
            );
        }
        if (startPort > endPort) {
            throw new InvalidScanRequestException(
                    "Porta inicial (" + startPort + ") não pode ser maior que a final (" + endPort + ")"
            );
        }
        if (timeoutMs <= 0) {
            throw new InvalidScanRequestException("Timeout deve ser positivo: " + timeoutMs);
        }
        if (threads <= 0) {
            throw new InvalidScanRequestException("Número de threads deve ser positivo: " + threads);
        }

        this.target = target;
        this.startPort = startPort;
        this.endPort = endPort;
        this.timeoutMs = timeoutMs;
        this.threads = threads;
    }

    public ScanRequest(Target target, int startPort, int endPort) {
        this(target, startPort, endPort, DEFAULT_TIMEOUT_MS, DEFAULT_THREADS);
    }

    public Target getTarget() {
        return target;
    }

    public int getStartPort() {
        return startPort;
    }

    public int getEndPort() {
        return endPort;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public int getThreads() {
        return threads;
    }

    public int getPortCount() {
        return endPort - startPort + 1;
    }

    @Override
    public String toString() {
        return "ScanRequest{" +
                "target=" + target +
                ", ports=" + startPort + "-" + endPort +
                ", timeout=" + timeoutMs + "ms" +
                ", threads=" + threads +
                '}';
    }
}