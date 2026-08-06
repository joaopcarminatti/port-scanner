package br.com.joaopcarminatti.portscanner.model;

/**
 * Resultado da varredura de uma única porta TCP.
 * Objeto imutável: uma vez criado, não muda.
 */
public class Port {

    public static final int MIN_PORT = 1;
    public static final int MAX_PORT = 65535;

    private final int number;
    private final PortState state;

    public Port(int number, PortState state) {
        if (number < MIN_PORT || number > MAX_PORT) {
            throw new IllegalArgumentException(
                    "Porta fora do intervalo válido (" + MIN_PORT + "-" + MAX_PORT + "): " + number
            );
        }
        if (state == null) {
            throw new IllegalArgumentException("Estado da porta não pode ser nulo");
        }

        this.number = number;
        this.state = state;
    }

    public int getNumber() {
        return number;
    }

    public PortState getState() {
        return state;
    }

    public boolean isOpen() {
        return state == PortState.OPEN;
    }

    @Override
    public String toString() {
        return number + "/tcp  " + state.getLabel();
    }
}