package model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nome;
    private String email;
    private String cidade;
    private List<Evento> eventosConfirmados;

    public Usuario(String nome, String email, String cidade) {
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
        this.eventosConfirmados = new ArrayList<>();
    }

    public void confirmarPresenca(Evento evento) {
        if (!eventosConfirmados.contains(evento)) {
            eventosConfirmados.add(evento);
            evento.adicionarParticipante(this.nome);
            System.out.println("Presença confirmada em: " + evento.getNome());
        } else {
            System.out.println("Você já confirmou presença neste evento!");
        }
    }

    public void cancelarPresenca(Evento evento) {
        if (eventosConfirmados.remove(evento)) {
            evento.removerParticipante(this.nome);
            System.out.println("Presença cancelada em: " + evento.getNome());
        } else {
            System.out.println("Você não estava confirmado neste evento!");
        }
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCidade() {
        return cidade;
    }

    public List<Evento> getEventosConfirmados() {
        return eventosConfirmados;
    }

    @Override
    public String toString() {
        return nome + " (" + email + ") - " + cidade;
    }
}
