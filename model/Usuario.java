package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um usuário cadastrado no sistema.
 * Armazena dados pessoais e a lista de eventos em que confirmou presença.
 */
public class Usuario {
    private String nome;
    private String email;
    private String cidade;
    private List<Evento> eventosConfirmados;

    /**
     * Cria um novo usuário com os dados informados.
     *
     * @param nome   nome completo do usuário
     * @param email  e-mail (usado como identificador único)
     * @param cidade cidade de residência
     */
    public Usuario(String nome, String email, String cidade) {
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
        this.eventosConfirmados = new ArrayList<>();
    }

    /**
     * Confirma presença do usuário em um evento.
     * Adiciona o evento à lista pessoal e registra o nome no evento.
     *
     * @param evento evento a confirmar
     */
    public void confirmarPresenca(Evento evento) {
        if (!eventosConfirmados.contains(evento)) {
            eventosConfirmados.add(evento);
            evento.adicionarParticipante(this.nome);
            System.out.println("Presença confirmada em: " + evento.getNome());
        } else {
            System.out.println("Você já confirmou presença neste evento!");
        }
    }

    /**
     * Cancela a presença do usuário em um evento.
     *
     * @param evento evento a cancelar
     */
    public void cancelarPresenca(Evento evento) {
        if (eventosConfirmados.remove(evento)) {
            evento.removerParticipante(this.nome);
            System.out.println("Presença cancelada em: " + evento.getNome());
        } else {
            System.out.println("Você não estava confirmado neste evento!");
        }
    }

    // Getters
    public String getNome()  { return nome; }
    public String getEmail() { return email; }
    public String getCidade(){ return cidade; }
    public List<Evento> getEventosConfirmados() { return eventosConfirmados; }

    @Override
    public String toString() {
        return nome + " (" + email + ") — " + cidade;
    }
}
