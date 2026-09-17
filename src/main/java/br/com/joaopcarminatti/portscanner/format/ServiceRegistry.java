package br.com.joaopcarminatti.portscanner.format;

import java.util.HashMap;
import java.util.Map;

/**
 * Traduz números de porta em nomes de serviço conhecidos.
 * Ex: 22 → "ssh", 443 → "https".
 * Baseado nas atribuições oficiais da IANA (subset comum).
 */
public class ServiceRegistry {

    private static final Map<Integer, String> WELL_KNOWN_SERVICES = new HashMap<>();

    static {
        WELL_KNOWN_SERVICES.put(20, "ftp-data");
        WELL_KNOWN_SERVICES.put(21, "ftp");
        WELL_KNOWN_SERVICES.put(22, "ssh");
        WELL_KNOWN_SERVICES.put(23, "telnet");
        WELL_KNOWN_SERVICES.put(25, "smtp");
        WELL_KNOWN_SERVICES.put(53, "dns");
        WELL_KNOWN_SERVICES.put(67, "dhcp-server");
        WELL_KNOWN_SERVICES.put(68, "dhcp-client");
        WELL_KNOWN_SERVICES.put(69, "tftp");
        WELL_KNOWN_SERVICES.put(80, "http");
        WELL_KNOWN_SERVICES.put(110, "pop3");
        WELL_KNOWN_SERVICES.put(111, "rpcbind");
        WELL_KNOWN_SERVICES.put(123, "ntp");
        WELL_KNOWN_SERVICES.put(135, "msrpc");
        WELL_KNOWN_SERVICES.put(137, "netbios-ns");
        WELL_KNOWN_SERVICES.put(138, "netbios-dgm");
        WELL_KNOWN_SERVICES.put(139, "netbios-ssn");
        WELL_KNOWN_SERVICES.put(143, "imap");
        WELL_KNOWN_SERVICES.put(161, "snmp");
        WELL_KNOWN_SERVICES.put(194, "irc");
        WELL_KNOWN_SERVICES.put(389, "ldap");
        WELL_KNOWN_SERVICES.put(443, "https");
        WELL_KNOWN_SERVICES.put(445, "smb");
        WELL_KNOWN_SERVICES.put(465, "smtps");
        WELL_KNOWN_SERVICES.put(514, "syslog");
        WELL_KNOWN_SERVICES.put(587, "smtp-submission");
        WELL_KNOWN_SERVICES.put(636, "ldaps");
        WELL_KNOWN_SERVICES.put(993, "imaps");
        WELL_KNOWN_SERVICES.put(995, "pop3s");
        WELL_KNOWN_SERVICES.put(1433, "mssql");
        WELL_KNOWN_SERVICES.put(1521, "oracle");
        WELL_KNOWN_SERVICES.put(2049, "nfs");
        WELL_KNOWN_SERVICES.put(3306, "mysql");
        WELL_KNOWN_SERVICES.put(3389, "rdp");
        WELL_KNOWN_SERVICES.put(5432, "postgresql");
        WELL_KNOWN_SERVICES.put(5900, "vnc");
        WELL_KNOWN_SERVICES.put(6379, "redis");
        WELL_KNOWN_SERVICES.put(8080, "http-proxy");
        WELL_KNOWN_SERVICES.put(8443, "https-alt");
        WELL_KNOWN_SERVICES.put(27017, "mongodb");
    }

    private ServiceRegistry() {
        // Classe utilitária: instanciação bloqueada.
    }

    /**
     * Retorna o nome do serviço registrado para a porta, ou "unknown" se desconhecida.
     */
    public static String getServiceName(int portNumber) {
        return WELL_KNOWN_SERVICES.getOrDefault(portNumber, "unknown");
    }
}