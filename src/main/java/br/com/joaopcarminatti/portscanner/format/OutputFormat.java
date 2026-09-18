package br.com.joaopcarminatti.portscanner.format;

/**
 * Formatos de saída suportados pelo scanner.
 * Cada valor sabe construir seu próprio ResultFormatter.
 */
public enum OutputFormat {

    CONSOLE {
        @Override
        public ResultFormatter createFormatter() {
            return new ConsoleFormatter();
        }
    },
    JSON {
        @Override
        public ResultFormatter createFormatter() {
            return new JsonFormatter();
        }
    };

    public abstract ResultFormatter createFormatter();
}