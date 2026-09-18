# Port Scanner

Scanner de portas TCP escrito em Java puro, sem dependências externas.
Projeto de estudo com foco em fundamentos de Programação Orientada a Objetos,
concorrência e o funcionamento do protocolo TCP.

## Uso

```bash
java -jar port-scanner.jar <host> <portaInicial-portaFinal> [opções]
```

### Exemplos

```bash
# Scan básico contra localhost
java -jar port-scanner.jar 127.0.0.1 1-1024

# Alvo remoto autorizado com timeout customizado
java -jar port-scanner.jar scanme.nmap.org 1-1024 --timeout 3000

# Saída em JSON para consumo programático
java -jar port-scanner.jar 192.168.1.1 20-30 --json

# Ver todas as opções
java -jar port-scanner.jar --help
```

### Opções

| Flag | Descrição | Padrão |
|------|-----------|--------|
| `--timeout MS` | Timeout de conexão em ms | 1000 |
| `--threads N` | Threads paralelas | 50 |
| `--json` | Saída em JSON | (console colorido) |
| `--help`, `-h` | Mensagem de ajuda | — |

### Exit codes

| Código | Significado |
|--------|-------------|
| 0 | Sucesso |
| 1 | Argumentos inválidos |
| 2 | Alvo inválido (DNS não resolve, host malformado) |
| 3 | Requisição inválida (faixa de portas incoerente, timeout negativo) |
| 99 | Erro inesperado |

## Construindo

Requer JDK 17+ e Maven 3.6+.

```bash
mvn package
```

O JAR executável é gerado em `target/port-scanner.jar`.

## Arquitetura

O projeto aplica separação de responsabilidades em camadas:

- **`model`** — entidades imutáveis com validação no construtor (`Target`, `Port`, `ScanRequest`, `ScanResult`)
- **`scanner`** — hierarquia de scanners (`PortScanner` interface, `AbstractPortScanner` classe abstrata, `SequentialTcpScanner` e `ParallelTcpScanner`)
- **`format`** — apresentação plugável (`ResultFormatter` interface, `ConsoleFormatter` com ANSI, `JsonFormatter` sem lib)
- **`exception`** — exceções nomeadas por domínio (`InvalidTargetException`, `InvalidScanRequestException`, `InvalidArgumentsException`)

O `Main` orquestra: parse via `CliParser`, construção de objetos de domínio, execução do scan, formatação e apresentação.

## Conceitos aplicados

- POO: encapsulamento, herança, polimorfismo (interface + classe abstrata), imutabilidade
- Java: `Socket`, `InetAddress`, `ExecutorService`, `Callable`/`Future`, `try-with-resources`, `equals`/`hashCode`
- Redes: TCP three-way handshake, portas well-known, técnica de TCP connect scan
- Arquitetura: Single Responsibility, Open/Closed, fail-fast na construção

## Aviso legal

Esta ferramenta deve ser usada **apenas contra sistemas próprios ou com autorização
explícita do responsável**. Varredura de portas não autorizada pode configurar crime
conforme a legislação brasileira (Lei 12.737/2012 — Marco Civil da Internet).

Para testes: `127.0.0.1` (localhost), sua própria rede local, ou `scanme.nmap.org`
(servidor mantido pelo projeto Nmap para testes autorizados).