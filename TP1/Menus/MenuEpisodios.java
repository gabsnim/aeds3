package Menus;

import Arquivos.ArquivoEpisodio;
import Entidades.Episodio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MenuEpisodios {

    private Scanner scan = new Scanner(System.in);
    private ArquivoEpisodio arq;

    public MenuEpisodios() throws Exception {
        arq = new ArquivoEpisodio(); // Usa o construtor que garante o índice
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\nPUCFlix 1.0\n" +
                    "-----------\n" +
                    "> Inicio > Episodios\n\n" +
                    "1) Buscar\n" +
                    "2) Incluir\n" +
                    "3) Alterar\n" +
                    "4) Excluir\n" +
                    "0) Voltar");

            System.out.print("\nOpcao: ");

            try {
                opcao = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            try {
                switch (opcao) {
                    case 1: searchEps(); break;
                    case 2: addEps(); break;
                    case 3: changeEps(); break;
                    case 4: deleteEps(); break;
                    case 0: System.out.println("Voltando..."); break;
                    default: System.out.println("Opcao invalida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

        } while (opcao != 0);
    }

    public void addEps() throws Exception {
        System.out.println("\n--- Incluir Episódio ---");
        System.out.print("ID da série: ");
        int idSerie = Integer.parseInt(scan.nextLine());

        System.out.print("Nome do episódio: ");
        String nome = scan.nextLine();

        System.out.print("Temporada: ");
        int temporada = Integer.parseInt(scan.nextLine());

        System.out.print("Data de lançamento (dd/MM/yyyy): ");
        String dataStr = scan.nextLine();
        LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.print("Duração (minutos): ");
        int duracao = Integer.parseInt(scan.nextLine());

        Episodio novo = new Episodio(idSerie, nome, temporada, data, duracao);
        int idGerado = arq.create(novo);

        System.out.println("Episódio criado com ID: " + idGerado);
    }

    public void searchEps() throws Exception {
        System.out.println("\n--- Buscar Episódio ---");
        System.out.print("Buscar por nome: ");
        
            String nome = scan.nextLine();
            Episodio[] encontrados = arq.readNome(nome);
            if (encontrados != null && encontrados.length > 0) {
                for (Episodio ep : encontrados) {
                    System.out.println(ep);
                }
            } else {
                System.out.println("Nenhum episódio encontrado com esse nome.");
            }


    }

    public void changeEps() throws Exception {
        System.out.println("\n--- Alterar Episódio ---");
        System.out.print("ID do episódio para alterar: ");
        int id = Integer.parseInt(scan.nextLine());

        Episodio ep = arq.read(id);
        if (ep == null) {
            System.out.println("Episódio não encontrado.");
            return;
        }

        System.out.println("Dados atuais:\n" + ep);

        System.out.print("Novo nome (enter p/ manter): ");
        String nome = scan.nextLine();
        if (!nome.isEmpty()) ep.setNome(nome);

        System.out.print("Nova temporada (enter p/ manter): ");
        String tempStr = scan.nextLine();
        if (!tempStr.isEmpty()) ep.setTemporada(Integer.parseInt(tempStr));

        System.out.print("Nova data de lançamento (dd/MM/yyyy, enter p/ manter): ");
        String dataStr = scan.nextLine();
        if (!dataStr.isEmpty()) {
            ep.setDataLancamento(LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        System.out.print("Nova duração (enter p/ manter): ");
        String durStr = scan.nextLine();
        if (!durStr.isEmpty()) ep.setDuracao(Integer.parseInt(durStr));

        boolean ok = arq.update(ep);
        System.out.println(ok ? "Episódio atualizado." : "Erro ao atualizar.");
    }

    public void deleteEps() throws Exception {
        System.out.println("\n--- Excluir Episódio ---");
        System.out.print("ID do episódio para excluir: ");
        int id = Integer.parseInt(scan.nextLine());

        boolean ok = arq.delete(id);
        System.out.println(ok ? "Episódio excluído." : "Episódio não encontrado.");
    }
}
