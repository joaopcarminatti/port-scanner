package br.com.joaopcarminatti.portscanner.format;

import br.com.joaopcarminatti.portscanner.model.ScanResult;

/**
 * Contrato para qualquer classe que saiba apresentar um ScanResult em texto.
 * Implementações concretas decidem o formato (console colorido, JSON, CSV, etc.).
 */
public interface ResultFormatter {

    /**
     * Formata o resultado do scan como uma string pronta para saída.
     *
     * @param result resultado a ser apresentado
     * @return texto formatado
     */
    String format(ScanResult result);
}