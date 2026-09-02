package br.com.joaopcarminatti.portscanner.model;

import br.com.joaopcarminatti.portscanner.exception.InvalidTargetException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Representa o alvo de um scan — a máquina que será varrida.
 * Guarda tanto o host original digitado pelo usuário quanto o IP resolvido via DNS.
 * Objeto imutável: uma vez construído, é sempre válido.
 */
public class Target {

    private final String host;
    private final InetAddress address;

    public Target(String host) {
        if (host == null || host.trim().isEmpty()) {
            throw new InvalidTargetException("Host não pode ser vazio ou nulo");
        }

        String cleanHost = host.trim();

        try {
            this.address = InetAddress.getByName(cleanHost);
        } catch (UnknownHostException e) {
            throw new InvalidTargetException(
                    "Não foi possível resolver o host: " + cleanHost, e
            );
        }

        this.host = cleanHost;
    }

    public String getHost() {
        return host;
    }

    public InetAddress getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return host + " (" + address.getHostAddress() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Target)) return false;
        Target target = (Target) o;
        return Objects.equals(host, target.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host);
    }
}
