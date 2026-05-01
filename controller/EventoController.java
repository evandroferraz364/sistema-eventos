package controller;

import model.Evento;
import model.Usuario;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador principal do sistema.
 * Gerencia eventos e usuários, além da persistência em arquivo.
 */
public class EventoController {
    private List<Evento> eventos;
    private List<Usuario> usuarios;
    private List<String> categorias;
    private static final String ARQUIVO_EVENTOS  = "events.data";
    private static final String ARQUIVO_USUARIOS = "users.data";

    /**
     * Inicializa o controller carregando dados salvos.
     */
    public EventoController() {
        this.eventos   = new ArrayList<>();
        this.usuarios  = new ArrayList<>();
        this.categorias = new ArrayList<>();
        inicializarCategorias();
        carregarUsuarios();
        carregarEventos();
    }

    /** Define as categorias fixas disponíveis para os eventos. */
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

    // ===================== EVENTOS =====================

    /**
     * Cadastra um novo evento após validar a categoria.
     *
     * @param nome         nome do evento
     * @param endereco     endereço
     * @param categoria    deve ser uma das categorias válidas
     * @param horario      data e hora de início
     * @param duracaoHoras duração estimada em horas
     * @param descricao    descrição do evento
     */
    public void cadastrarEvento(String nome, String endereco, String categoria,
                                LocalDateTime horario, int duracaoHoras, String descricao) {
        if (!categorias.contains(categoria)) {
            System.out.println("Categoria inválida!");
            return;
        }
        Evento evento = new Evento(nome, endereco, categoria, horario, duracaoHoras, descricao);
        eventos.add(evento);
        salvarEventos();
        System.out.println("Evento cadastrado com sucesso!");
    }

    /** Lista todos os eventos cadastrados no sistema. */
    public void listarEventos() {
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
            return;
        }
        System.out.println("\n=== TODOS OS EVENTOS ===");
        for (int i = 0; i < eventos.size(); i++) {
            System.out.println((i + 1) + ". " + eventos.get(i));
        }
    }

    /**
     * Retorna a lista de eventos futuros, ordenados por horário (mais próximo primeiro).
     *
     * @return lista ordenada de eventos próximos
     */
    public List<Evento> listarEventosProximos() {
        List<Evento> futuros = new ArrayList<>();
        LocalDateTime agora = LocalDateTime.now();

        for (Evento e : eventos) {
            if (e.getHorario().isAfter(agora)) {
                futuros.add(e);
            }
        }

        // Ordena do mais próximo ao mais distante
        futuros.sort((e1, e2) -> e1.getHorario().compareTo(e2.getHorario()));
        return futuros;
    }

    /** Exibe os eventos que estão acontecendo no momento atual. */
    public void verificarEventoAtual() {
        boolean encontrou = false;
        System.out.println("\n=== EVENTOS ACONTECENDO AGORA ===");

        for (Evento e : eventos) {
            if (e.estaOcorrendoAgora()) {
                System.out.println("• " + e.getNome() + " em " + e.getEndereco());
                encontrou = true;
            }
        }

        if (!encontrou) System.out.println("Nenhum evento acontecendo no momento.");
    }

    /** Exibe os eventos que já foram encerrados. */
    public void listarEventosPassados() {
        System.out.println("\n=== EVENTOS JÁ OCORRIDOS ===");
        boolean encontrou = false;

        for (Evento e : eventos) {
            if (e.jaOcorreu()) {
                System.out.println("• " + e);
                encontrou = true;
            }
        }

        if (!encontrou) System.out.println("Nenhum evento encerrado.");
    }

    // ===================== USUÁRIOS =====================

    /**
     * Busca um usuário pelo e-mail. Retorna null se não encontrado.
     *
     * @param email e-mail a buscar
     * @return usuário encontrado ou null
     */
    public Usuario buscarUsuarioPorEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    /**
     * Cadastra um novo usuário no sistema e persiste no arquivo.
     *
     * @param nome   nome do usuário
     * @param email  e-mail (identificador único)
     * @param cidade cidade de residência
     * @return o usuário criado
     */
    public Usuario cadastrarUsuario(String nome, String email, String cidade) {
        Usuario usuario = new Usuario(nome, email, cidade);
        usuarios.add(usuario);
        salvarUsuarios();
        return usuario;
    }

    // ===================== PERSISTÊNCIA =====================

    /** Salva todos os eventos no arquivo events.data. */
    private void salvarEventos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_EVENTOS))) {
            for (Evento e : eventos) {
                writer.write(e.paraArquivo());
                writer.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Erro ao salvar eventos: " + ex.getMessage());
        }
    }

    /** Carrega os eventos do arquivo events.data na inicialização. */
    private void carregarEventos() {
        File arquivo = new File(ARQUIVO_EVENTOS);
        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_EVENTOS))) {
            String linha;
            int count = 0;
            while ((linha = reader.readLine()) != null) {
                Evento evento = Evento.deArquivo(linha);
                if (evento != null) {
                    eventos.add(evento);
                    count++;
                }
            }
            if (count > 0) System.out.println(count + " evento(s) carregado(s).");
        } catch (IOException ex) {
            System.out.println("Erro ao carregar eventos: " + ex.getMessage());
        }
    }

    /** Salva todos os usuários no arquivo users.data. */
    public void salvarUsuarios() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_USUARIOS))) {
            for (Usuario u : usuarios) {
                writer.write(u.getNome() + ";" + u.getEmail() + ";" + u.getCidade());
                writer.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Erro ao salvar usuários: " + ex.getMessage());
        }
    }

    /** Carrega os usuários do arquivo users.data na inicialização. */
    private void carregarUsuarios() {
        File arquivo = new File(ARQUIVO_USUARIOS);
        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_USUARIOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] p = linha.split(";");
                if (p.length >= 3) {
                    usuarios.add(new Usuario(p[0], p[1], p[2]));
                }
            }
        } catch (IOException ex) {
            System.out.println("Erro ao carregar usuários: " + ex.getMessage());
        }
    }

    // ===================== GETTERS =====================

    public List<Evento>  getEventos()    { return eventos; }
    public List<String>  getCategorias() { return categorias; }
    public List<Usuario> getUsuarios()   { return usuarios; }
}
