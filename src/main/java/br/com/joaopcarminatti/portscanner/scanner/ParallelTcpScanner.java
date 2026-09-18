package br.com.joaopcarminatti.portscanner.scanner;

import br.com.joaopcarminatti.portscanner.model.Port;
import br.com.joaopcarminatti.portscanner.model.PortState;
import br.com.joaopcarminatti.portscanner.model.ScanRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Scanner TCP connect que testa várias portas em paralelo usando um pool de threads.
 * Enquanto uma porta espera o timeout, outras threads já testam outras portas.
 * Muito mais rápido que a versão sequencial em alvos com portas filtradas.
 */
public class ParallelTcpScanner extends AbstractPortScanner {

    @Override
    protected List<Port> executeScan(ScanRequest request) {
        ExecutorService executor = Executors.newFixedThreadPool(request.getThreads());
        List<Future<Port>> futures = new ArrayList<>();

        try {
            for (int portNumber = request.getStartPort(); portNumber <= request.getEndPort(); portNumber++) {
                final int currentPort = portNumber;

                Callable<Port> task = () -> {
                    PortState state = probe(request, currentPort);
                    return new Port(currentPort, state);
                };

                futures.add(executor.submit(task));
            }

            List<Port> results = new ArrayList<>();
            for (Future<Port> future : futures) {
                try {
                    results.add(future.get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Scan interrompido", e);
                } catch (ExecutionException e) {
                    throw new RuntimeException("Falha ao escanear porta", e.getCause());
                }
            }

            results.sort(Comparator.comparingInt(Port::getNumber));
            return results;

        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}