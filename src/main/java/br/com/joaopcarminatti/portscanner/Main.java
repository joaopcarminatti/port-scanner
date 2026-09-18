package br.com.joaopcarminatti.portscanner;

import br.com.joaopcarminatti.portscanner.exception.InvalidArgumentsException;
import br.com.joaopcarminatti.portscanner.exception.InvalidScanRequestException;
import br.com.joaopcarminatti.portscanner.exception.InvalidTargetException;
import br.com.joaopcarminatti.portscanner.format.ResultFormatter;
import br.com.joaopcarminatti.portscanner.model.ScanRequest;
import br.com.joaopcarminatti.portscanner.model.ScanResult;
import br.com.joaopcarminatti.portscanner.model.Target;
import br.com.joaopcarminatti.portscanner.scanner.ParallelTcpScanner;
import br.com.joaopcarminatti.portscanner.scanner.PortScanner;

public class Main {

    private static final int EXIT_SUCCESS = 0;
    private static final int EXIT_INVALID_ARGS = 1;
    private static final int EXIT_INVALID_TARGET = 2;
    private static final int EXIT_INVALID_REQUEST = 3;
    private static final int EXIT_UNEXPECTED = 99;

    public static void main(String[] args) {
        CliParser parser = new CliParser();
        CliParser.ParsedArguments parsed;

        try {
            parsed = parser.parse(args);
        } catch (InvalidArgumentsException e) {
            System.err.println(e.getMessage());
            System.exit(EXIT_INVALID_ARGS);
            return;
        }

        try {
            Target target = new Target(parsed.getHost());
            ScanRequest request = new ScanRequest(
                    target,
                    parsed.getStartPort(),
                    parsed.getEndPort(),
                    parsed.getTimeoutMs(),
                    parsed.getThreads()
            );

            PortScanner scanner = new ParallelTcpScanner();
            ScanResult result = scanner.scan(request);

            ResultFormatter formatter = parsed.getFormat().createFormatter();
            System.out.println(formatter.format(result));

            System.exit(EXIT_SUCCESS);
        } catch (InvalidTargetException e) {
            System.err.println("Erro no alvo: " + e.getMessage());
            System.exit(EXIT_INVALID_TARGET);
        } catch (InvalidScanRequestException e) {
            System.err.println("Erro na requisição: " + e.getMessage());
            System.exit(EXIT_INVALID_REQUEST);
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(EXIT_UNEXPECTED);
        }
    }
}