package br.com.joaopcarminatti.portscanner.format;

import br.com.joaopcarminatti.portscanner.model.Port;
import br.com.joaopcarminatti.portscanner.model.ScanResult;

import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.ZoneOffset;

/**
 * Formata um ScanResult como JSON estruturado,
 * pronto para consumo por outras ferramentas.
 */
public class JsonFormatter implements ResultFormatter {

    @Override
    public String format(ScanResult result) {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");
        sb.append("  \"target\": {\n");
        sb.append("    \"host\": \"").append(escape(result.getTarget().getHost())).append("\",\n");
        sb.append("    \"address\": \"").append(result.getTarget().getAddress().getHostAddress()).append("\"\n");
        sb.append("  },\n");
        sb.append("  \"scan\": {\n");
        sb.append("    \"totalPorts\": ").append(result.getTotalPortsScanned()).append(",\n");
        sb.append("    \"openPortsCount\": ").append(result.getOpenPortsCount()).append(",\n");
        sb.append("    \"durationMs\": ").append(result.getDuration().toMillis()).append(",\n");
        sb.append("    \"generatedAt\": \"").append(currentTimestamp()).append("\"\n");
        sb.append("  },\n");
        sb.append("  \"openPorts\": [");

        var openPorts = result.getOpenPorts();
        for (int i = 0; i < openPorts.size(); i++) {
            Port port = openPorts.get(i);
            sb.append("\n    {");
            sb.append("\"port\": ").append(port.getNumber()).append(", ");
            sb.append("\"service\": \"").append(ServiceRegistry.getServiceName(port.getNumber())).append("\", ");
            sb.append("\"state\": \"").append(port.getState().name().toLowerCase()).append("\"");
            sb.append("}");
            if (i < openPorts.size() - 1) sb.append(",");
        }

        if (!openPorts.isEmpty()) sb.append("\n  ");
        sb.append("]\n");
        sb.append("}\n");

        return sb.toString();
    }

    private String currentTimestamp() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now().atOffset(ZoneOffset.UTC).toInstant());
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}