package controller;

import model.Evento;
import model.Usuario;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoController {
    private List<Evento> eventos;
    private List<String> categorias;
    private static final String ARQUIVO = "events.data";

    public EventoController() {
        this.eventos = new ArrayList<>();
        this.categorias = new ArrayList<>();
        inicializarCategorias();
        carregarEventos();
    }

    private void inicializarCategorias() {
        categorias.add("Festas");
        categorias.add("Shows");
        categorias.add("Esportes");
        categorias.add("Teatro");
        categorias.add("Cinema");
        categorias.add("Workshops");
        categorias.add("Gastronomia");
        categorias.add("Outros");
    }

    public void cadastrarEvento(String nome, String endereco, String categoria,
                                LocalDateTime horario, String descricao) {
        if (!categorias.contains(categoria)) {
            System.out.println("Categoria inválida!");
            return;
        }
        
        Evento evento = new Evento(nome, endereco, categoria, horario, descricao);
        eventos.add(evento);
        System.out.println("Evento cadastrado com sucesso!");
        salvarEventos();
    }

    public void listarEventos() {
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
            return;
        }
        
        System.out.println("\n=== EVENTOS CADASTRADOS ===");
        for (int i = 0; i < eventos.size(); i++) {
            System.out.println((i + 1) + ". " + eventos.get(i));
        }
    }

    public List<Evento> listarEventosProximos() {
        List<Evento> futuros = new ArrayList<>();
        LocalDateTime agora = LocalDateTime.now();
        
        for (Evento e : eventos) {
            if (e.getHorario().isAfter(agora)) {
                futuros.add(e);
            }
        }
        
        futuros.sort((e1, e2) -> e1.getHorario().compareTo(e2.getHorario()));
        return futuros;
    }

    public void verificarEventoAtual() {
        boolean encontrou = false;
        System.out.println("\n=== EVENTOS ACONTECENDO AGORA ===");
        
        for (Evento e : eventos) {
            if (e.estaOcorrendoAgora()) {
                System.out.println("• " + e.getNome() + " em " + e.getEndereco());
                encontrou = true;
            }
        }
        
        if (!encontrou) {
            System.out.println("Nenhum evento acontecendo no momento.");
        }
    }

    public void listarEventosPassados() {
        System.out.println("\n=== EVENTOS JÁ OCORRIDOS ===");
        boolean encontrou = false;
        
        for (Evento e : eventos) {
            if (e.jaOcorreu()) {
                System.out.println("• " + e);
                encontrou = true;
            }
        }
        
        if (!encontrou) {
            System.out.println("Nenhum evento passado.");
        }
    }

    private void salvarEventos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Evento e : eventos) {
                writer.write(e.paraArquivo());
                writer.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Erro ao salvar eventos: " + ex.getMessage());
        }
    }

    private void carregarEventos() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            System.out.println("Arquivo events.data não encontrado. Iniciando sistema vazio.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            int count = 0;
            while ((linha = reader.readLine()) != null) {
                Evento evento = Evento.deArquivo(linha);
                if (evento != null) {
                    eventos.add(evento);
                    count++;
                }
            }
            System.out.println(count + " eventos carregados de events.data");
        } catch (IOException ex) {
            System.out.println("Erro ao carregar eventos: " + ex.getMessage());
        }
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public Evento buscarEventoPorNome(String nome) {
        for (Evento e : eventos) {
            if (e.getNome().equalsIgnoreCase(nome)) {
                return e;
            }
        }
        return null;
    }
}
