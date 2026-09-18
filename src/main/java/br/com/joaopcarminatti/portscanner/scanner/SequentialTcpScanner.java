package br.com.joaopcarminatti.portscanner.scanner;

import br.com.joaopcarminatti.portscanner.model.Port;
import br.com.joaopcarminatti.portscanner.model.PortState;
import br.com.joaopcarminatti.portscanner.model.ScanRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Scanner TCP connect que testa uma porta por vez.
 * Simples e previsível, mas lento em alvos com portas filtradas.
 * Útil para debugar problemas ou rodar contra alvos locais.
 */
public class SequentialTcpScanner extends AbstractPortScanner {

    @Override
    protected List<Port> executeScan(ScanRequest request) {
        List<Port> results = new ArrayList<>();

        for (int portNumber = request.getStartPort(); portNumber <= request.getEndPort(); portNumber++) {
            PortState state = probe(request, portNumber);
            results.add(new Port(portNumber, state));
        }

        return results;
    }
}