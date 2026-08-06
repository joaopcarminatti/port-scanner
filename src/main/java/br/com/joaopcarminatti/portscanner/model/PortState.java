package br.com.joaopcarminatti.portscanner.model;

/**
 * Estados possíveis de uma porta após a tentativa de conexão TCP.
 */
public enum PortState {

    OPEN("aberta", "Serviço aceitou a conexão"),
    CLOSED("fechada", "Host respondeu com RST — nada escutando"),
    FILTERED("filtrada", "Sem resposta — provável firewall");

    private final String label;
    private final String description;

    PortState(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}