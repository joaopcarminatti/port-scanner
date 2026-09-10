package br.com.joaopcarminatti.portscanner;

import br.com.joaopcarminatti.portscanner.model.Port;
import br.com.joaopcarminatti.portscanner.model.ScanRequest;
import br.com.joaopcarminatti.portscanner.model.ScanResult;
import br.com.joaopcarminatti.portscanner.model.Target;
import br.com.joaopcarminatti.portscanner.scanner.PortScanner;
import br.com.joaopcarminatti.portscanner.scanner.TcpConnectScanner;

public class Main {
    public static void main(String[] args) {
        Target target = new Target("127.0.0.1");
        ScanRequest request = new ScanRequest(target, 1, 1024);

        System.out.println("Iniciando scan...");
        System.out.println("Alvo: " + target);
        System.out.println("Portas: " + request.getStartPort() + "-" + request.getEndPort());
        System.out.println("Timeout: " + request.getTimeoutMs() + "ms");
        System.out.println();

        PortScanner scanner = new TcpConnectScanner();
        ScanResult result = scanner.scan(request);

        System.out.println("Scan concluído em " + result.getDuration().toMillis() + "ms");
        System.out.println("Total testadas: " + result.getTotalPortsScanned());
        System.out.println("Portas abertas: " + result.getOpenPortsCount());

        if (result.getOpenPortsCount() > 0) {
            System.out.println();
            System.out.println("=== PORTAS ABERTAS ===");
            for (Port port : result.getOpenPorts()) {
                System.out.println(port);
            }
        }
    }
}