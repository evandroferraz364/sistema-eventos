package view;

import controller.EventoController;
import model.Evento;
import model.Usuario;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Camada de visualização do sistema.
 * Responsável por exibir menus, coletar entradas do usuário e acionar o controller.
 */
public class MenuView {
    private Scanner scanner;
    private EventoController controller;
    private Usuario usuarioLogado;

    /** Inicializa a view com scanner e controller. */
    public MenuView() {
        this.scanner    = new Scanner(System.in);
        this.controller = new EventoController();
    }

    /**
     * Exibe o menu principal em loop até o usuário optar por sair.
     * Trata entradas inválidas com try/catch para evitar crashes.
     */
    public void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("       SISTEMA DE EVENTOS DA CIDADE");
            System.out.println("========================================");
            System.out.println(" 1. Login / Cadastrar usuário");
            System.out.println(" 2. Cadastrar evento");
            System.out.println(" 3. Listar todos os eventos");
            System.out.println(" 4. Próximos eventos");
            System.out.println(" 5. Eventos acontecendo AGORA");
            System.out.println(" 6. Eventos já encerrados");
            System.out.println(" 7. Confirmar presença em evento");
            System.out.println(" 8. Meus eventos confirmados");
            System.out.println(" 9. Cancelar presença");
            System.out.println("10. Ver participantes de um evento");
            System.out.println(" 0. Sair");
            System.out.println("========================================");

            if (usuarioLogado != null) {
                System.out.println("Logado como: " + usuarioLogado.getNome() + " | " + usuarioLogado.getCidade());
            } else {
                System.out.println("Nenhum usuário logado.");
            }

            int opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:  fazerLogin();                           break;
                case 2:  cadastrarEvento();                      break;
                case 3:  controller.listarEventos();             break;
                case 4:  listarProximos();                       break;
                case 5:  controller.verificarEventoAtual();      break;
                case 6:  controller.listarEventosPassados();     break;
                case 7:  confirmarPresenca();                    break;
                case 8:  meusEventos();                          break;
                case 9:  cancelarPresenca();                     break;
                case 10: verParticipantes();                     break;
                case 0:
                    System.out.println("Encerrando sistema. Até logo!");
                    return;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    // ===================== LOGIN =====================

    /**
     * Realiza login por e-mail. Se o usuário não existir, abre cadastro.
     * Usuários são persistidos entre sessões via arquivo users.data.
     */
    private void fazerLogin() {
        System.out.print("E-mail: ");
        String email = scanner.nextLine().trim();

        // Tenta encontrar usuário já cadastrado
        Usuario encontrado = controller.buscarUsuarioPorEmail(email);

        if (encontrado != null) {
            usuarioLogado = encontrado;
            System.out.println("Bem-vindo(a) de volta, " + usuarioLogado.getNome() + "!");
        } else {
            // Cadastro de novo usuário
            System.out.println("\n--- NOVO USUÁRIO ---");
            System.out.print("Nome: ");
            String nome = scanner.nextLine().trim();
            System.out.print("Cidade: ");
            String cidade = scanner.nextLine().trim();

            usuarioLogado = controller.cadastrarUsuario(nome, email, cidade);
            System.out.println("Usuário cadastrado com sucesso! Bem-vindo(a), " + nome + "!");
        }
    }

    // ===================== EVENTOS =====================

    /** Coleta os dados do novo evento e envia ao controller para cadastro. */
    private void cadastrarEvento() {
        System.out.println("\n=== CADASTRAR EVENTO ===");
        System.out.print("Nome do evento: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Endereço: ");
        String endereco = scanner.nextLine().trim();

        // Exibe categorias disponíveis e coleta escolha
        List<String> categorias = controller.getCategorias();
        System.out.println("Categorias disponíveis:");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + categorias.get(i));
        }

        int catIndex = lerInteiro("Escolha a categoria (número): ") - 1;
        if (catIndex < 0 || catIndex >= categorias.size()) {
            System.out.println("Categoria inválida!");
            return;
        }
        String categoria = categorias.get(catIndex);

        // Coleta data e hora com tratamento de erro
        System.out.println("Data e hora do evento:");
        int ano    = lerInteiro("Ano: ");
        int mes    = lerInteiro("Mês (1-12): ");
        int dia    = lerInteiro("Dia: ");
        int hora   = lerInteiro("Hora (0-23): ");
        int minuto = lerInteiro("Minuto: ");

        LocalDateTime horario;
        try {
            horario = LocalDateTime.of(ano, mes, dia, hora, minuto);
        } catch (Exception e) {
            System.out.println("Data/hora inválida! Verifique os valores informados.");
            return;
        }

        int duracaoHoras = lerInteiro("Duração estimada (horas): ");

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine().trim();

        controller.cadastrarEvento(nome, endereco, categoria, horario, duracaoHoras, descricao);
    }

    /** Exibe os próximos eventos em ordem cronológica com detalhes completos. */
    private void listarProximos() {
        List<Evento> proximos = controller.listarEventosProximos();
        if (proximos.isEmpty()) {
            System.out.println("Nenhum evento futuro cadastrado.");
            return;
        }
        System.out.println("\n=== PRÓXIMOS EVENTOS ===");
        for (int i = 0; i < proximos.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            System.out.println(proximos.get(i).toStringDetalhado());
        }
    }

    /** Permite ao usuário confirmar presença em um evento disponível. */
    private void confirmarPresenca() {
        if (!verificarLogin()) return;

        List<Evento> eventos = controller.getEventos();
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado ainda.");
            return;
        }

        System.out.println("\n=== CONFIRMAR PRESENÇA ===");
        for (int i = 0; i < eventos.size(); i++) {
            System.out.println((i + 1) + ". " + eventos.get(i));
        }

        int escolha = lerInteiro("\nNúmero do evento: ");
        if (escolha > 0 && escolha <= eventos.size()) {
            usuarioLogado.confirmarPresenca(eventos.get(escolha - 1));
            controller.salvarUsuarios(); // persiste após alteração
        } else {
            System.out.println("Número inválido!");
        }
    }

    /** Exibe os eventos em que o usuário logado confirmou presença. */
    private void meusEventos() {
        if (!verificarLogin()) return;

        List<Evento> confirmados = usuarioLogado.getEventosConfirmados();
        if (confirmados.isEmpty()) {
            System.out.println("Você não tem eventos confirmados.");
            return;
        }

        System.out.println("\n=== MEUS EVENTOS ===");
        for (int i = 0; i < confirmados.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            System.out.println(confirmados.get(i).toStringDetalhado());
        }
    }

    /** Permite cancelar a presença em um evento confirmado anteriormente. */
    private void cancelarPresenca() {
        if (!verificarLogin()) return;

        List<Evento> confirmados = usuarioLogado.getEventosConfirmados();
        if (confirmados.isEmpty()) {
            System.out.println("Você não tem eventos confirmados.");
            return;
        }

        System.out.println("\n=== CANCELAR PRESENÇA ===");
        for (int i = 0; i < confirmados.size(); i++) {
            System.out.println((i + 1) + ". " + confirmados.get(i));
        }

        int escolha = lerInteiro("\nNúmero do evento para cancelar: ");
        if (escolha > 0 && escolha <= confirmados.size()) {
            usuarioLogado.cancelarPresenca(confirmados.get(escolha - 1));
            controller.salvarUsuarios();
        } else {
            System.out.println("Número inválido!");
        }
    }

    /** Exibe a lista de participantes confirmados em um evento selecionado. */
    private void verParticipantes() {
        List<Evento> eventos = controller.getEventos();
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado ainda.");
            return;
        }

        System.out.println("\n=== VER PARTICIPANTES ===");
        for (int i = 0; i < eventos.size(); i++) {
            System.out.println((i + 1) + ". " + eventos.get(i));
        }

        int escolha = lerInteiro("\nNúmero do evento: ");
        if (escolha > 0 && escolha <= eventos.size()) {
            Evento evento = eventos.get(escolha - 1);
            List<String> participantes = evento.getParticipantesConfirmados();

            System.out.println("\n=== " + evento.getNome().toUpperCase() + " ===");
            if (participantes.isEmpty()) {
                System.out.println("Nenhum participante confirmado ainda.");
            } else {
                System.out.println("Total: " + participantes.size() + " confirmado(s)");
                for (int i = 0; i < participantes.size(); i++) {
                    System.out.println("  " + (i + 1) + ". " + participantes.get(i));
                }
            }
        } else {
            System.out.println("Número inválido!");
        }
    }

    // ===================== UTILITÁRIOS =====================

    /**
     * Lê um inteiro do console com tratamento de entrada inválida.
     * Repete a pergunta até receber um número válido.
     *
     * @param mensagem texto exibido antes da entrada
     * @return inteiro digitado pelo usuário
     */
    private int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                int valor = scanner.nextInt();
                scanner.nextLine(); // limpa buffer
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // descarta entrada inválida
                System.out.println("Entrada inválida! Digite apenas números.");
            }
        }
    }

    /**
     * Verifica se há um usuário logado. Exibe mensagem caso contrário.
     *
     * @return true se logado, false caso contrário
     */
    private boolean verificarLogin() {
        if (usuarioLogado == null) {
            System.out.println("Você precisa fazer login primeiro! (opção 1)");
            return false;
        }
        return true;
    }
}
