# Port Scanner

Scanner de portas TCP escrito em Java puro, sem nenhuma dependência externa.

```
$ java -jar port-scanner.jar scanme.nmap.org 20-80

Escaneando scanme.nmap.org (45.33.32.156) — portas 20 a 80

  22/tcp    aberta    ssh
  80/tcp    aberta    http

2 portas abertas de 61 verificadas em 2,4s
```
> *(substitua esse bloco por um print real do terminal — a saída colorida não aparece em texto)*

## Por que esse projeto existe

Eu queria entender três coisas que só se aprende construindo: como o TCP realmente estabelece uma conexão, como paralelizar trabalho de I/O em Java sem quebrar tudo, e como estruturar um projeto orientado a objetos que não vire uma classe gigante de 500 linhas.

Um scanner de portas é o exercício perfeito para isso, porque ele é simples de descrever ("tenta conectar em cada porta e vê quais respondem") e cheio de decisões de design escondidas.

A regra que me impus: **zero bibliotecas externas.** Tudo em Java padrão — inclusive o JSON, que escrevi na mão. A ideia era não deixar nenhuma biblioteca esconder de mim o que eu queria aprender.

## Como funciona

O scanner usa a técnica de **TCP connect scan**: para cada porta, ele tenta abrir um socket. Se o handshake de três vias completa, a porta está aberta. Se o sistema recusa ou o timeout estoura, está fechada ou filtrada.

É a técnica mais simples e a mais "barulhenta" — ela completa a conexão de verdade, então aparece nos logs do alvo. Foi uma escolha consciente: eu queria entender bem o básico antes de partir para técnicas de SYN scan, que exigem pacotes crus e privilégio de root.

## Uso

```bash
java -jar port-scanner.jar <host> <portaInicial-portaFinal> [opções]
```

```bash
# Scan básico contra localhost
java -jar port-scanner.jar 127.0.0.1 1-1024

# Alvo remoto autorizado, com timeout customizado
java -jar port-scanner.jar scanme.nmap.org 1-1024 --timeout 3000

# Saída em JSON, para consumo por outra ferramenta
java -jar port-scanner.jar 192.168.1.1 20-30 --json

# Ajuda
java -jar port-scanner.jar --help
```

### Opções

| Flag | Descrição | Padrão |
|---|---|---|
| `--timeout MS` | Timeout de conexão em ms | 1000 |
| `--threads N` | Threads paralelas | 50 |
| `--json` | Saída em JSON | console colorido |
| `--help`, `-h` | Mensagem de ajuda | — |

### Exit codes

| Código | Significado |
|---|---|
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

Quatro camadas, cada uma com uma responsabilidade:

**`model`** — entidades imutáveis que se validam no próprio construtor (`Target`, `Port`, `ScanRequest`, `ScanResult`). A ideia é *fail-fast*: se um objeto existe, ele é válido. Não existe `Port` com número 70000 circulando pelo sistema.

**`scanner`** — a hierarquia de scanners: a interface `PortScanner`, a classe abstrata `AbstractPortScanner` com o que é comum, e duas implementações (`SequentialTcpScanner` e `ParallelTcpScanner`). Fiz o sequencial primeiro justamente para ter com o que comparar quando o paralelo entrasse.

**`format`** — apresentação plugável. `ConsoleFormatter` com cores ANSI e `JsonFormatter` escrito à mão, ambos atrás da interface `ResultFormatter`. Adicionar um formato novo (CSV, XML) não exige tocar em nenhuma linha do scanner.

**`exception`** — exceções nomeadas pelo problema de domínio (`InvalidTargetException`, `InvalidScanRequestException`, `InvalidArgumentsException`). `IllegalArgumentException` diz que algo deu errado; `InvalidTargetException` diz *o que* deu errado.

O `Main` só orquestra: parse dos argumentos pelo `CliParser`, construção dos objetos de domínio, execução, formatação, saída.

## Testes

```bash
mvn test
```

68 testes com JUnit 5, cobrindo validação de domínio, parser de linha de comando, registro de serviços e apresentação de resultados.

O detalhe que mais gostei: os testes do formatter usam um `MockPortScanner` — uma implementação falsa que devolve resultados prontos e **nunca toca na rede**. Foi aí que a separação por interfaces deixou de ser teoria e virou algo prático: dá para testar toda a camada de apresentação sem depender de um socket, de conexão ou de nenhum host real.

## O que aprendi

- **Modelar antes de codar economiza tempo.** Separar `Target`, `Port` e `ScanRequest` em classes com validação própria fez o scanner em si ficar quase trivial.
- **Timeout é o parâmetro que define tudo.** Muito curto, você marca porta aberta como fechada. Muito longo, um scan de 1024 portas leva uma eternidade. Não existe valor universalmente certo.
- **Paralelismo de I/O é diferente de paralelismo de CPU.** Dá para usar muito mais threads do que núcleos, porque a maior parte do tempo elas estão paradas esperando resposta da rede.
- **Interface só prova seu valor quando aparece a segunda implementação.** Enquanto era só o scanner sequencial, ela parecia burocracia. Com o paralelo e o mock, virou a peça central do projeto.

## Próximos passos

- [ ] Detecção de banner (ler a resposta inicial do serviço)
- [ ] Scan de UDP
- [ ] Relatório em HTML
- [ ] Rate limiting configurável

## Aviso de uso

Escanear portas de sistemas sem autorização do proprietário pode ser ilegal, inclusive no Brasil. Use essa ferramenta apenas contra sistemas que você administra, contra o seu próprio ambiente de laboratório, ou contra alvos explicitamente liberados para testes — como o `scanme.nmap.org`, mantido pelo projeto Nmap justamente para isso.