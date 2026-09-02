package br.com.joaopcarminatti.portscanner.exception;

/**
 * Lançada quando um Target é construído com um host inválido —
 * seja porque está vazio, malformado ou não resolve via DNS.
 */
public class InvalidTargetException extends RuntimeException {

    public InvalidTargetException(String message) {
        super(message);
    }

    public InvalidTargetException(String message, Throwable cause) {
        super(message, cause);
    }
}
