package br.com.joaopcarminatti.portscanner;

import br.com.joaopcarminatti.portscanner.model.Port;
import br.com.joaopcarminatti.portscanner.model.PortState;

public class Main {
    public static void main(String[] args) {
        Port p = new Port(22, PortState.OPEN);
        System.out.println(p);
        System.out.println("Aberta? " + p.isOpen());

        // Esta linha deve lançar IllegalArgumentException
        new Port(70000, PortState.OPEN);
    }
}