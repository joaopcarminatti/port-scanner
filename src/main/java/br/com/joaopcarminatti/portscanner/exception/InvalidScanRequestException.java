package br.com.joaopcarminatti.portscanner.exception;

/**
 * Lançada quando um ScanRequest é construído com parâmetros inválidos —
 * intervalo de portas inconsistente, timeout não positivo, ou faixa vazia.
 */
public class InvalidScanRequestException extends RuntimeException {

    public InvalidScanRequestException(String message) {
        super(message);
    }
}