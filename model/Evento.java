package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um evento da cidade.
 * Armazena informações como nome, endereço, categoria, horário, duração e descrição.
 */
public class Evento {
    private String nome;
    private String endereco;
    private String categoria;
    private LocalDateTime horario;
    private int duracaoHoras;
    private String descricao;
    private List<String> participantesConfirmados;

    /**
     * Construtor completo do Evento.
     */
    public Evento(String nome, String endereco, String categoria,
                  LocalDateTime horario, int duracaoHoras, String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.duracaoHoras = duracaoHoras;
        this.descricao = descricao;
        this.participantesConfirmados = new ArrayList<>();
    }

    /** Adiciona participante se ainda não estiver confirmado. */
    public void adicionarParticipante(String nomeUsuario) {
        if (!participantesConfirmados.contains(nomeUsuario)) {
            participantesConfirmados.add(nomeUsuario);
        }
    }

    /** Remove participante da lista de confirmados. */
    public void removerParticipante(String nomeUsuario) {
        participantesConfirmados.remove(nomeUsuario);
    }

    public List<String> getParticipantesConfirmados() { return participantesConfirmados; }

    /** Verifica se o evento está em andamento agora, baseado na duração informada. */
    public boolean estaOcorrendoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime fim = horario.plusHours(duracaoHoras);
        return agora.isAfter(horario) && agora.isBefore(fim);
    }

    /** Verifica se o evento já encerrou completamente. */
    public boolean jaOcorreu() {
        return LocalDateTime.now().isAfter(horario.plusHours(duracaoHoras));
    }

    // Getters
    public String getNome()           { return nome; }
    public String getEndereco()       { return endereco; }
    public String getCategoria()      { return categoria; }
    public LocalDateTime getHorario() { return horario; }
    public int getDuracaoHoras()      { return duracaoHoras; }
    public String getDescricao()      { return descricao; }

    /**
     * Serializa o evento para gravação no arquivo events.data.
     * Formato: nome;endereco;categoria;horario;duracaoHoras;descricao;participantes
     */
    public String paraArquivo() {
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String participantes = String.join(",", participantesConfirmados);
        return nome + ";" + endereco + ";" + categoria + ";" +
               horario.format(fmt) + ";" + duracaoHoras + ";" + descricao + ";" + participantes;
    }

    /**
     * Reconstrói um Evento a partir de uma linha do arquivo events.data.
     * Retorna null se a linha for inválida.
     */
    public static Evento deArquivo(String linha) {
        String[] p = linha.split(";");
        if (p.length < 6) return null;

        int duracao = 2; // padrão caso campo não exista
        try { duracao = Integer.parseInt(p[4]); } catch (NumberFormatException ignored) {}

        Evento evento = new Evento(p[0], p[1], p[2], LocalDateTime.parse(p[3]), duracao, p[5]);

        if (p.length >= 7 && !p[6].isEmpty()) {
            for (String participante : p[6].split(",")) {
                evento.participantesConfirmados.add(participante);
            }
        }
        return evento;
    }

    /** Exibe resumo do evento com status automático (agora/encerrado). */
    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String status = estaOcorrendoAgora() ? " [ACONTECENDO AGORA]" : jaOcorreu() ? " [ENCERRADO]" : "";
        String confirmados = participantesConfirmados.isEmpty() ? "" : " — " + participantesConfirmados.size() + " confirmado(s)";
        return nome + " | " + categoria + " | " + horario.format(fmt) + " (" + duracaoHoras + "h)" + status + confirmados;
    }

    /** Exibe todas as informações do evento, incluindo endereço e descrição. */
    public String toStringDetalhado() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "  Nome:        " + nome +
               "\n  Categoria:   " + categoria +
               "\n  Endereço:    " + endereco +
               "\n  Horário:     " + horario.format(fmt) + " (duração: " + duracaoHoras + "h)" +
               "\n  Descrição:   " + descricao +
               "\n  Confirmados: " + participantesConfirmados.size();
    }
}
