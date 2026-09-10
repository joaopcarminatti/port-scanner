package br.com.joaopcarminatti.portscanner.scanner;

import br.com.joaopcarminatti.portscanner.model.ScanRequest;
import br.com.joaopcarminatti.portscanner.model.ScanResult;

/**
 * Contrato para qualquer classe que saiba executar um scan de portas.
 * Implementações concretas podem usar diferentes técnicas
 * (TCP connect, UDP, mock para testes).
 */
public interface PortScanner {

    /**
     * Executa a varredura conforme os parâmetros da requisição.
     *
     * @param request parâmetros do scan (alvo, faixa de portas, timeout, threads)
     * @return resultado agregado com as portas testadas e a duração total
     */
    ScanResult scan(ScanRequest request);
}