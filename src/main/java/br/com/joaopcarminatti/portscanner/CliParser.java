package br.com.joaopcarminatti.portscanner;

import br.com.joaopcarminatti.portscanner.exception.InvalidArgumentsException;
import br.com.joaopcarminatti.portscanner.format.OutputFormat;
import br.com.joaopcarminatti.portscanner.model.Port;
import br.com.joaopcarminatti.portscanner.model.ScanRequest;

/**
 * Interpreta argumentos da linha de comando e produz uma configuração de scan.
 *
 * Este parser trata apenas do formato dos argumentos — não valida semântica
 * de domínio (DNS, faixa de portas coerente, etc.). Essa validação vive
 * nas classes de modelo (Target, ScanRequest).
 *
 * Sintaxe esperada:
 *   port-scanner <host> <startPort-endPort> [--timeout MS] [--threads N] [--json]
 */
public class CliParser {

    private static final String FLAG_TIMEOUT = "--timeout";
    private static final String FLAG_THREADS = "--threads";
    private static final String FLAG_JSON = "--json";
    private static final String FLAG_HELP = "--help";

    public static class ParsedArguments {
        private final String host;
        private final int startPort;
        private final int endPort;
        private final int timeoutMs;
        private final int threads;
        private final OutputFormat format;

        public ParsedArguments(String host, int startPort, int endPort,
                               int timeoutMs, int threads, OutputFormat format) {
            this.host = host;
            this.startPort = startPort;
            this.endPort = endPort;
            this.timeoutMs = timeoutMs;
            this.threads = threads;
            this.format = format;
        }

        public String getHost() {
            return host;
        }

        public int getStartPort() {
            return startPort;
        }

        public int getEndPort() {
            return endPort;
        }

        public int getTimeoutMs() {
            return timeoutMs;
        }

        public int getThreads() {
            return threads;
        }

        public OutputFormat getFormat() {
            return format;
        }
    }

    public ParsedArguments parse(String[] args) {
        if (args.length == 0 || isHelpRequest(args)) {
            throw new InvalidArgumentsException(buildUsageMessage());
        }

        if (args.length < 2) {
            throw new InvalidArgumentsException(
                    "Argumentos insuficientes.\n\n" + buildUsageMessage()
            );
        }

        String host = args[0];
        String portRange = args[1];

        int startPort = parseStartPort(portRange);
        int endPort = parseEndPort(portRange);

        int timeoutMs = ScanRequest.DEFAULT_TIMEOUT_MS;
        int threads = ScanRequest.DEFAULT_THREADS;
        OutputFormat format = OutputFormat.CONSOLE;

        for (int i = 2; i < args.length; i++) {
            String flag = args[i];

            switch (flag) {
                case FLAG_TIMEOUT:
                    timeoutMs = parseIntFlag(args, i, FLAG_TIMEOUT);
                    i++;
                    break;
                case FLAG_THREADS:
                    threads = parseIntFlag(args, i, FLAG_THREADS);
                    i++;
                    break;
                case FLAG_JSON:
                    format = OutputFormat.JSON;
                    break;
                default:
                    throw new InvalidArgumentsException(
                            "Flag desconhecida: " + flag + "\n\n" + buildUsageMessage()
                    );
            }
        }

        return new ParsedArguments(host, startPort, endPort, timeoutMs, threads, format);
    }

    private boolean isHelpRequest(String[] args) {
        for (String arg : args) {
            if (FLAG_HELP.equals(arg) || "-h".equals(arg)) {
                return true;
            }
        }
        return false;
    }

    private int parseStartPort(String range) {
        int dashIndex = range.indexOf('-');
        if (dashIndex == -1) {
            throw new InvalidArgumentsException(
                    "Intervalo de portas inválido: " + range + " (esperado formato: início-fim, ex: 1-1024)"
            );
        }
        try {
            return Integer.parseInt(range.substring(0, dashIndex));
        } catch (NumberFormatException e) {
            throw new InvalidArgumentsException(
                    "Porta inicial não é um número: " + range.substring(0, dashIndex)
            );
        }
    }

    private int parseEndPort(String range) {
        int dashIndex = range.indexOf('-');
        try {
            return Integer.parseInt(range.substring(dashIndex + 1));
        } catch (NumberFormatException e) {
            throw new InvalidArgumentsException(
                    "Porta final não é um número: " + range.substring(dashIndex + 1)
            );
        }
    }

    private int parseIntFlag(String[] args, int flagIndex, String flagName) {
        if (flagIndex + 1 >= args.length) {
            throw new InvalidArgumentsException(
                    "Flag " + flagName + " requer um valor numérico"
            );
        }
        try {
            return Integer.parseInt(args[flagIndex + 1]);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentsException(
                    "Valor de " + flagName + " não é um número: " + args[flagIndex + 1]
            );
        }
    }

    private String buildUsageMessage() {
        return "Uso: port-scanner <host> <portaInicial-portaFinal> [opções]\n\n" +
                "Argumentos posicionais:\n" +
                "  host                     Hostname ou IP do alvo (ex: scanme.nmap.org, 192.168.1.1)\n" +
                "  portaInicial-portaFinal  Intervalo de portas (ex: 1-1024)\n\n" +
                "Opções:\n" +
                "  --timeout MS             Timeout de conexão em milissegundos (padrão: " + ScanRequest.DEFAULT_TIMEOUT_MS + ")\n" +
                "  --threads N              Número de threads paralelas (padrão: " + ScanRequest.DEFAULT_THREADS + ")\n" +
                "  --json                   Saída em formato JSON (padrão: console colorido)\n" +
                "  --help, -h               Mostra esta mensagem\n\n" +
                "Restrições de porta: " + Port.MIN_PORT + " a " + Port.MAX_PORT + "\n\n" +
                "Exemplos:\n" +
                "  port-scanner localhost 1-1024\n" +
                "  port-scanner 192.168.1.1 20-25 --timeout 500\n" +
                "  port-scanner scanme.nmap.org 1-1024 --threads 100 --json";
    }
}