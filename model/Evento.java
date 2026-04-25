package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Evento {
    private String nome;
    private String endereco;
    private String categoria;
    private LocalDateTime horario;
    private String descricao;
    private List<String> participantesConfirmados;

    public Evento(String nome, String endereco, String categoria, 
                  LocalDateTime horario, String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
        this.participantesConfirmados = new ArrayList<>();
    }

    public void adicionarParticipante(String nomeUsuario) {
        if (!participantesConfirmados.contains(nomeUsuario)) {
            participantesConfirmados.add(nomeUsuario);
        }
    }

    public void removerParticipante(String nomeUsuario) {
        participantesConfirmados.remove(nomeUsuario);
    }

    public List<String> getParticipantesConfirmados() {
        return participantesConfirmados;
    }

    public boolean estaOcorrendoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime fim = horario.plusHours(3);
        return agora.isAfter(horario) && agora.isBefore(fim);
    }

    public boolean jaOcorreu() {
        return LocalDateTime.now().isAfter(horario);
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public String getDescricao() {
        return descricao;
    }

    public String paraArquivo() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String participantes = String.join(",", participantesConfirmados);
        return nome + ";" + endereco + ";" + categoria + ";" + 
               horario.format(formatter) + ";" + descricao + ";" + participantes;
    }

    public static Evento deArquivo(String linha) {
        String[] partes = linha.split(";");
        if (partes.length >= 5) {
            Evento evento = new Evento(
                partes[0],
                partes[1],
                partes[2],
                LocalDateTime.parse(partes[3]),
                partes[4]
            );
            
            // Carregar participantes se existirem
            if (partes.length >= 6 && !partes[5].isEmpty()) {
                String[] participantes = partes[5].split(",");
                for (String p : participantes) {
                    evento.participantesConfirmados.add(p);
                }
            }
            
            return evento;
        }
        return null;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String info = nome + " | " + categoria + " | " + horario.format(formatter);
        if (!participantesConfirmados.isEmpty()) {
            info += " (" + participantesConfirmados.size() + " confirmado(s))";
        }
        return info;
    }
}
