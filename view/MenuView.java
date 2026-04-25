package view;

import controller.EventoController;
import model.Evento;
import model.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuView {
    private Scanner scanner;
    private EventoController controller;
    private Usuario usuarioLogado;
    private List<Usuario> usuarios;

    public MenuView() {
        this.scanner = new Scanner(System.in);
        this.controller = new EventoController();
        this.usuarios = new ArrayList<>();
    }

    public void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("  SISTEMA DE EVENTOS DA CIDADE");
            System.out.println("========================================");
            System.out.println("1. Fazer login / Cadastrar usuário");
            System.out.println("2. Cadastrar evento");
            System.out.println("3. Listar todos os eventos");
            System.out.println("4. Ver próximos eventos");
            System.out.println("5. Ver eventos acontecendo AGORA");
            System.out.println("6. Ver eventos passados");
            System.out.println("7. Confirmar presença em evento");
            System.out.println("8. Meus eventos confirmados");
            System.out.println("9. Cancelar presença");
            System.out.println("10. Ver participantes de um evento");
            System.out.println("0. Sair");
            System.out.println("========================================");

            if (usuarioLogado != null) {
                System.out.println("Logado como: " + usuarioLogado.getNome());
            }

            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    fazerLogin();
                    break;
                case 2:
                    cadastrarEvento();
                    break;
                case 3:
                    controller.listarEventos();
                    break;
                case 4:
                    listarProximos();
                    break;
                case 5:
                    controller.verificarEventoAtual();
                    break;
                case 6:
                    controller.listarEventosPassados();
                    break;
                case 7:
                    confirmarPresenca();
                    break;
                case 8:
                    meusEventos();
                    break;
                case 9:
                    cancelarPresenca();
                    break;
                case 10:
                    verParticipantes();
                    break;
                case 0:
                    System.out.println("Encerrando sistema...");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void fazerLogin() {
        System.out.print("Email: ");
        String email = scanner.nextLine();

        // Verificar se usuário já existe
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                usuarioLogado = u;
                System.out.println("Login realizado com sucesso! Bem-vindo(a) de volta, " + u.getNome() + "!");
                return;
            }
        }

        // Se não existe, cadastrar novo usuário
        System.out.println("\n--- NOVO USUÁRIO ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();

        usuarioLogado = new Usuario(nome, email, cidade);
        usuarios.add(usuarioLogado);
        System.out.println("Usuário cadastrado e login realizado com sucesso!");
    }

    private void cadastrarEvento() {
        System.out.println("\n=== CADASTRAR EVENTO ===");
        System.out.print("Nome do evento: ");
        String nome = scanner.nextLine();

        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        System.out.println("Categorias disponíveis:");
        List<String> categorias = controller.getCategorias();
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + ". " + categorias.get(i));
        }
        System.out.print("Escolha a categoria (número): ");
        int catIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (catIndex < 0 || catIndex >= categorias.size()) {
            System.out.println("Categoria inválida!");
            return;
        }
        String categoria = categorias.get(catIndex);

        System.out.println("Data e hora do evento:");
        System.out.print("Ano: ");
        int ano = scanner.nextInt();
        System.out.print("Mês (1-12): ");
        int mes = scanner.nextInt();
        System.out.print("Dia: ");
        int dia = scanner.nextInt();
        System.out.print("Hora (0-23): ");
        int hora = scanner.nextInt();
        System.out.print("Minuto: ");
        int minuto = scanner.nextInt();
        scanner.nextLine();

        LocalDateTime horario = LocalDateTime.of(ano, mes, dia, hora, minuto);

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        controller.cadastrarEvento(nome, endereco, categoria, horario, descricao);
    }

    private void listarProximos() {
        List<Evento> proximos = controller.listarEventosProximos();
        if (proximos.isEmpty()) {
            System.out.println("Nenhum evento futuro.");
            return;
        }

        System.out.println("\n=== PRÓXIMOS EVENTOS ===");
        for (int i = 0; i < proximos.size(); i++) {
            System.out.println((i + 1) + ". " + proximos.get(i));
        }
    }

    private void confirmarPresenca() {
        if (usuarioLogado == null) {
            System.out.println("Você precisa fazer login primeiro!");
            return;
        }

        List<Evento> eventos = controller.getEventos();
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado ainda.");
            return;
        }

        System.out.println("\n=== CONFIRMAR PRESENÇA ===");
        for (int i = 0; i < eventos.size(); i++) {
            System.out.println((i + 1) + ". " + eventos.get(i));
        }

        System.out.print("\nEscolha o número do evento: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha > 0 && escolha <= eventos.size()) {
            Evento evento = eventos.get(escolha - 1);
            usuarioLogado.confirmarPresenca(evento);
        } else {
            System.out.println("Número inválido!");
        }
    }

    private void meusEventos() {
        if (usuarioLogado == null) {
            System.out.println("Você precisa fazer login primeiro!");
            return;
        }

        List<Evento> confirmados = usuarioLogado.getEventosConfirmados();
        if (confirmados.isEmpty()) {
            System.out.println("Você não tem eventos confirmados.");
            return;
        }

        System.out.println("\n=== MEUS EVENTOS ===");
        for (Evento e : confirmados) {
            System.out.println("• " + e);
        }
    }

    private void cancelarPresenca() {
        if (usuarioLogado == null) {
            System.out.println("Você precisa fazer login primeiro!");
            return;
        }

        List<Evento> confirmados = usuarioLogado.getEventosConfirmados();
        if (confirmados.isEmpty()) {
            System.out.println("Você não tem eventos confirmados.");
            return;
        }

        System.out.println("\n=== CANCELAR PRESENÇA ===");
        for (int i = 0; i < confirmados.size(); i++) {
            System.out.println((i + 1) + ". " + confirmados.get(i));
        }

        System.out.print("\nEscolha o número do evento para cancelar: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha > 0 && escolha <= confirmados.size()) {
            Evento evento = confirmados.get(escolha - 1);
            usuarioLogado.cancelarPresenca(evento);
        } else {
            System.out.println("Número inválido!");
        }
    }

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

        System.out.print("\nEscolha o número do evento: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha > 0 && escolha <= eventos.size()) {
            Evento evento = eventos.get(escolha - 1);
            List<String> participantes = evento.getParticipantesConfirmados();
            
            System.out.println("\n=== EVENTO: " + evento.getNome() + " ===");
            if (participantes.isEmpty()) {
                System.out.println("Nenhum participante confirmado ainda.");
            } else {
                System.out.println("Total de confirmados: " + participantes.size());
                System.out.println("\nLista de participantes:");
                for (int i = 0; i < participantes.size(); i++) {
                    System.out.println((i + 1) + ". " + participantes.get(i));
                }
            }
        } else {
            System.out.println("Número inválido!");
        }
    }
}
