package br.com.joaopcarminatti.portscanner.format;

import br.com.joaopcarminatti.portscanner.model.Port;
import br.com.joaopcarminatti.portscanner.model.ScanResult;

/**
 * Formata um ScanResult como texto colorido para terminal,
 * com cabeçalho, tabela de portas abertas e resumo estatístico.
 */
public class ConsoleFormatter implements ResultFormatter {

    // Códigos ANSI de escape
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String GRAY = "\u001B[90m";

    @Override
    public String format(ScanResult result) {
        StringBuilder sb = new StringBuilder();

        sb.append(BOLD).append(CYAN)
                .append("=== RELATÓRIO DE SCAN ===")
                .append(RESET).append("\n\n");

        sb.append(BOLD).append("Alvo:     ").append(RESET)
                .append(result.getTarget()).append("\n");
        sb.append(BOLD).append("Duração:  ").append(RESET)
                .append(result.getDuration().toMillis()).append(" ms\n");
        sb.append(BOLD).append("Testadas: ").append(RESET)
                .append(result.getTotalPortsScanned()).append(" portas\n");
        sb.append(BOLD).append("Abertas:  ").append(RESET)
                .append(GREEN).append(result.getOpenPortsCount()).append(RESET)
                .append("\n\n");

        if (result.getOpenPortsCount() == 0) {
            sb.append(GRAY)
                    .append("Nenhuma porta aberta encontrada no intervalo.")
                    .append(RESET).append("\n");
            return sb.toString();
        }

        sb.append(BOLD).append("PORTAS ABERTAS").append(RESET).append("\n");
        sb.append(GRAY)
                .append(String.format("%-10s %-15s %s", "PORTA", "SERVIÇO", "ESTADO"))
                .append(RESET).append("\n");
        sb.append(GRAY).append("-".repeat(40)).append(RESET).append("\n");

        for (Port port : result.getOpenPorts()) {
            String service = ServiceRegistry.getServiceName(port.getNumber());
            sb.append(String.format(
                    "%-10s %-15s %s%s%s%n",
                    port.getNumber() + "/tcp",
                    service,
                    GREEN, "aberta", RESET
            ));
        }

        sb.append("\n").append(YELLOW)
                .append("Aviso: use apenas em alvos autorizados.")
                .append(RESET).append("\n");

        return sb.toString();
    }
}