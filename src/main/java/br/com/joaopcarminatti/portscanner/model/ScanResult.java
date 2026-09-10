package br.com.joaopcarminatti.portscanner.model;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Resultado completo de um scan: qual alvo foi varrido,
 * quais portas foram encontradas em que estado, e quanto tempo levou.
 * Objeto imutável.
 */
public class ScanResult {

    private final Target target;
    private final List<Port> ports;
    private final Duration duration;

    public ScanResult(Target target, List<Port> ports, Duration duration) {
        if (target == null) {
            throw new IllegalArgumentException("Target não pode ser nulo");
        }
        if (ports == null) {
            throw new IllegalArgumentException("Lista de portas não pode ser nula");
        }
        if (duration == null || duration.isNegative()) {
            throw new IllegalArgumentException("Duração inválida");
        }

        this.target = target;
        this.ports = Collections.unmodifiableList(ports);
        this.duration = duration;
    }

    public Target getTarget() {
        return target;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public Duration getDuration() {
        return duration;
    }

    public int getTotalPortsScanned() {
        return ports.size();
    }

    public long getOpenPortsCount() {
        return ports.stream().filter(Port::isOpen).count();
    }

    public List<Port> getOpenPorts() {
        return ports.stream().filter(Port::isOpen).toList();
    }

    @Override
    public String toString() {
        return "ScanResult{" +
                "target=" + target +
                ", totalPorts=" + getTotalPortsScanned() +
                ", openPorts=" + getOpenPortsCount() +
                ", duration=" + duration.toMillis() + "ms" +
                '}';
    }
}