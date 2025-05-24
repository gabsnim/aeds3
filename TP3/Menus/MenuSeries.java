package Menus;

import Arquivos.ArquivoSerie;
import Entidades.Serie;
import ListaInvertida.IndiceInvertidoSeries;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MenuSeries {

    private Scanner scan = new Scanner(System.in);
    private ArquivoSerie arq;
    private IndiceInvertidoSeries indice;

    public MenuSeries() throws Exception {
        arq = new ArquivoSerie();
        indice = new IndiceInvertidoSeries();
        indice.construirIndice(arq);
    }

    public void menu() throws Exception {
        int opcao;

        do {
            System.out.println("\nPUCFlix 1.0\n" +
                    "-----------\n" +
                    "> Séries\n\n" +
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
                    case 1: searchSerie(); break;
                    case 2: addSerie(); break;
                    case 3: changeSerie(); break;
                    case 4: deleteSerie(); break;
                    case 0: System.out.println("Voltando..."); break;
                    default: System.out.println("Opcao invalida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

        } while (opcao != 0);
    }

    public void addSerie() throws Exception {
        System.out.println("\n--- Incluir Série ---");
        System.out.print("Nome da série: ");
        String nome = scan.nextLine();

        System.out.print("Ano de lançamento (dd/MM/yyyy): ");
        String dataStr = scan.nextLine();
        LocalDate ano = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.print("Sinopse: ");
        String sinopse = scan.nextLine();

        System.out.print("Plataforma de Streaming: ");
        String streaming = scan.nextLine();

        Serie nova = new Serie(nome, ano, sinopse, streaming);
        int idGerado = arq.create(nova);
        nova.setId(idGerado);
        indice.indexarSerie(nova);
        indice.salvarIndice();

        System.out.println("Série criada com ID: " + idGerado);
    }

    public void searchSerie() throws Exception {
        System.out.println("\n--- Buscar Série ---");
        System.out.print("Buscar por nome: ");
        String nome = scan.nextLine();

        List<Integer> ids = indice.buscar(nome);
        boolean encontrou = false;

        for (int id : ids) {
            Serie serie = arq.read(id);
            if (serie != null) {
                System.out.println(serie);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma série encontrada com esse nome.");
        }
    }

    public void changeSerie() throws Exception {
        System.out.println("\n--- Alterar Série ---");
        Serie[] series = arq.readAll();

        if (series.length == 0) {
            System.out.println("Nenhuma série cadastrada.");
            return;
        }

        for (int i = 0; i < series.length; i++) {
            System.out.printf("[%d] %s\n", i, series[i]);
        }

        System.out.print("Escolha o índice da série para editar: ");
        int escolha = Integer.parseInt(scan.nextLine());

        if (escolha >= 0 && escolha < series.length) {
            Serie serie = series[escolha];
            indice.desindexarSerie(serie);

            System.out.print("Novo nome (enter p/ manter): ");
            String novoNome = scan.nextLine();
            if (!novoNome.isEmpty()) serie.setNome(novoNome);

            System.out.print("Nova data de lançamento (dd/MM/yyyy, enter p/ manter): ");
            String dataStr = scan.nextLine();
            if (!dataStr.isEmpty()) {
                serie.setAnoLancamento(LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }

            System.out.print("Nova sinopse (enter p/ manter): ");
            String sinopse = scan.nextLine();
            if (!sinopse.isEmpty()) serie.setSinopse(sinopse);

            System.out.print("Nova plataforma de streaming (enter p/ manter): ");
            String plataforma = scan.nextLine();
            if (!plataforma.isEmpty()) serie.setStreaming(plataforma);

            boolean ok = arq.update(serie);
            if (ok) {
                indice.indexarSerie(serie);
                indice.salvarIndice();
                System.out.println("Série atualizada com sucesso!");
            } else {
                System.out.println("Erro ao atualizar série.");
            }
        } else {
            System.out.println("Índice inválido.");
        }
    }

    public void deleteSerie() throws Exception {
        System.out.println("\n--- Excluir Série ---");
        Serie[] series = arq.readAll();

        if (series.length == 0) {
            System.out.println("Nenhuma série cadastrada.");
            return;
        }

        for (int i = 0; i < series.length; i++) {
            System.out.printf("[%d] %s\n", i, series[i]);
        }

        System.out.print("Escolha o índice da série para excluir: ");
        int escolha = Integer.parseInt(scan.nextLine());

        if (escolha >= 0 && escolha < series.length) {
            Serie serie = series[escolha];
            indice.desindexarSerie(serie);
            indice.salvarIndice();

            boolean ok = arq.delete(serie.getId());
            if (ok) {
                System.out.println("Série excluída com sucesso!");
            } else {
                System.out.println("Erro ao excluir série.");
            }
        } else {
            System.out.println("Índice inválido.");
        }
    }
}
