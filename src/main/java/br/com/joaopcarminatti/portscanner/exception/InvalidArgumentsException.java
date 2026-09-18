package br.com.joaopcarminatti.portscanner.exception;

/**
 * Lançada quando os argumentos da linha de comando estão em formato inválido —
 * quantidade insuficiente, flag desconhecida, valor não numérico, etc.
 */
public class InvalidArgumentsException extends RuntimeException {

    public InvalidArgumentsException(String message) {
        super(message);
    }
}