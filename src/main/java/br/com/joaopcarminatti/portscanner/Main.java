package br.com.joaopcarminatti.portscanner;

import br.com.joaopcarminatti.portscanner.format.ConsoleFormatter;
import br.com.joaopcarminatti.portscanner.format.JsonFormatter;
import br.com.joaopcarminatti.portscanner.format.ResultFormatter;
import br.com.joaopcarminatti.portscanner.model.ScanRequest;
import br.com.joaopcarminatti.portscanner.model.ScanResult;
import br.com.joaopcarminatti.portscanner.model.Target;
import br.com.joaopcarminatti.portscanner.scanner.PortScanner;
import br.com.joaopcarminatti.portscanner.scanner.TcpConnectScanner;

public class Main {
    public static void main(String[] args) {
        Target target = new Target("127.0.0.1");
        ScanRequest request = new ScanRequest(target, 1, 1024);

        PortScanner scanner = new TcpConnectScanner();
        ScanResult result = scanner.scan(request);

        // Troque aqui para experimentar as duas apresentações:
        ResultFormatter formatter = new ConsoleFormatter();
        // ResultFormatter formatter = new JsonFormatter();

        System.out.println(formatter.format(result));
    }
}