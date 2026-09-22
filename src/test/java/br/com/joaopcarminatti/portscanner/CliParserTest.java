package br.com.joaopcarminatti.portscanner;

import br.com.joaopcarminatti.portscanner.exception.InvalidArgumentsException;
import br.com.joaopcarminatti.portscanner.format.OutputFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CliParser")
class CliParserTest {

    private CliParser parser;

    @BeforeEach
    void setUp() {
        parser = new CliParser();
    }

    @Nested
    @DisplayName("Argumentos posicionais")
    class PosicionaisTests {

        @Test
        @DisplayName("deve parsear host e faixa de portas")
        void deveParsearHostEPortas() {
            CliParser.ParsedArguments parsed = parser.parse(
                    new String[]{"localhost", "1-1024"}
            );

            assertEquals("localhost", parsed.getHost());
            assertEquals(1, parsed.getStartPort());
            assertEquals(1024, parsed.getEndPort());
        }

        @Test
        @DisplayName("deve usar defaults quando nenhuma flag é passada")
        void deveUsarDefaults() {
            CliParser.ParsedArguments parsed = parser.parse(
                    new String[]{"localhost", "1-1024"}
            );

            assertEquals(1000, parsed.getTimeoutMs());
            assertEquals(50, parsed.getThreads());
            assertEquals(OutputFormat.CONSOLE, parsed.getFormat());
        }
    }

    @Nested
    @DisplayName("Flags")
    class FlagsTests {

        @Test
        @DisplayName("deve aplicar --timeout")
        void deveAplicarTimeout() {
            CliParser.ParsedArguments parsed = parser.parse(
                    new String[]{"localhost", "1-100", "--timeout", "500"}
            );

            assertEquals(500, parsed.getTimeoutMs());
        }

        @Test
        @DisplayName("deve aplicar --threads")
        void deveAplicarThreads() {
            CliParser.ParsedArguments parsed = parser.parse(
                    new String[]{"localhost", "1-100", "--threads", "200"}
            );

            assertEquals(200, parsed.getThreads());
        }

        @Test
        @DisplayName("deve aplicar --json e trocar formato de saída")
        void deveAplicarJson() {
            CliParser.ParsedArguments parsed = parser.parse(
                    new String[]{"localhost", "1-100", "--json"}
            );

            assertEquals(OutputFormat.JSON, parsed.getFormat());
        }

        @Test
        @DisplayName("deve aplicar múltiplas flags combinadas")
        void deveAplicarMultiplasFlags() {
            CliParser.ParsedArguments parsed = parser.parse(
                    new String[]{"localhost", "1-100", "--timeout", "500", "--threads", "100", "--json"}
            );

            assertEquals(500, parsed.getTimeoutMs());
            assertEquals(100, parsed.getThreads());
            assertEquals(OutputFormat.JSON, parsed.getFormat());
        }
    }

    @Nested
    @DisplayName("Erros de parsing")
    class ErrosTests {

        @Test
        @DisplayName("deve lançar exceção quando nenhum argumento é passado")
        void deveRejeitarArgumentosVazios() {
            assertThrows(
                    InvalidArgumentsException.class,
                    () -> parser.parse(new String[]{})
            );
        }

        @Test
        @DisplayName("deve lançar exceção quando só o host é passado")
        void deveRejeitarSoUmArgumento() {
            assertThrows(
                    InvalidArgumentsException.class,
                    () -> parser.parse(new String[]{"localhost"})
            );
        }

        @Test
        @DisplayName("deve lançar exceção quando faixa não tem hífen")
        void deveRejeitarFaixaSemHifen() {
            assertThrows(
                    InvalidArgumentsException.class,
                    () -> parser.parse(new String[]{"localhost", "1024"})
            );
        }

        @Test
        @DisplayName("deve lançar exceção quando porta inicial não é número")
        void deveRejeitarPortaInicialNaoNumerica() {
            assertThrows(
                    InvalidArgumentsException.class,
                    () -> parser.parse(new String[]{"localhost", "abc-1024"})
            );
        }

        @Test
        @DisplayName("deve lançar exceção quando porta final não é número")
        void deveRejeitarPortaFinalNaoNumerica() {
            assertThrows(
                    InvalidArgumentsException.class,
                    () -> parser.parse(new String[]{"localhost", "1-abc"})
            );
        }

        @Test
        @DisplayName("deve lançar exceção com flag desconhecida")
        void deveRejeitarFlagDesconhecida() {
            InvalidArgumentsException ex = assertThrows(
                    InvalidArgumentsException.class,
                    () -> parser.parse(new String[]{"localhost", "1-100", "--banana"})
            );

            assertTrue(ex.getMessage().contains("--banana"));
        }

        @Test
        @DisplayName("deve lançar exceção quando --timeout não tem valor")
        void deveRejeitarTimeoutSemValor() {
            assertThrows(
                    InvalidArgumentsException.class,
                    () -> parser.parse(new String[]{"localhost", "1-100", "--timeout"})
            );
        }

        @Test
        @DisplayName("deve lançar exceção quando --timeout tem valor não numérico")
        void deveRejeitarTimeoutNaoNumerico() {
            assertThrows(
                    InvalidArgumentsException.class,
                    () -> parser.parse(new String[]{"localhost", "1-100", "--timeout", "abc"})
            );
        }
    }

    @Nested
    @DisplayName("Help")
    class HelpTests {

        @Test
        @DisplayName("deve lançar exceção com mensagem de uso quando --help é passado")
        void deveMostrarHelpComFlagLonga() {
            InvalidArgumentsException ex = assertThrows(
                    InvalidArgumentsException.class,
                    () -> parser.parse(new String[]{"--help"})
            );

            assertTrue(ex.getMessage().contains("Uso"));
        }

        @Test
        @DisplayName("deve lançar exceção com mensagem de uso quando -h é passado")
        void deveMostrarHelpComFlagCurta() {
            InvalidArgumentsException ex = assertThrows(
                    InvalidArgumentsException.class,
                    () -> parser.parse(new String[]{"-h"})
            );

            assertTrue(ex.getMessage().contains("Uso"));
        }
    }
}