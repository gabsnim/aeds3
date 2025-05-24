package Menus;

import Arquivos.ArquivoEpisodio;
import Arquivos.ArquivoSerie;
import Entidades.Episodio;
import Entidades.Serie;
import ListaInvertida.IndiceInvertidoEpisodios;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MenuEpisodios {

    private Scanner scan = new Scanner(System.in);
    private ArquivoEpisodio arq;
    private IndiceInvertidoEpisodios indice;

    public MenuEpisodios() throws Exception {
        arq = new ArquivoEpisodio();
        indice = new IndiceInvertidoEpisodios();
        indice.construirIndice(arq);
    }

    public void menu() throws Exception {
        int opcao;

        System.out.println("\n--- Identificação da Série ---");
        ArquivoSerie arquivoSerie = new ArquivoSerie();
        Serie[] series = arquivoSerie.readAll();

        if (series.length == 0) {
            System.out.println("Nenhuma série cadastrada. Retornando ao menu principal...");
            return;
        }

        System.out.println("Séries disponíveis:");
        for (Serie serie : series) {
            System.out.printf("ID: %d | Nome: %s\n", serie.getId(), serie.getNome());
        }

        System.out.print("\nInforme o ID da série para gerenciar os episódios: ");
        int idSerie = Integer.parseInt(scan.nextLine());

        Serie serieSelecionada = null;
        for (Serie serie : series) {
            if (serie.getId() == idSerie) {
                serieSelecionada = serie;
                break;
            }
        }

        if (serieSelecionada == null) {
            System.out.println("ID da série inválido. Retornando ao menu principal...");
            return;
        }

        System.out.println("\nSérie selecionada: " + serieSelecionada.getNome());

        do {
            System.out.println("\nPUCFlix 1.0\n" +
                    "-----------\n" +
                    "> Série: " + serieSelecionada.getNome() + " > Episódios\n\n" +
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
                    case 1: searchEps(idSerie); break;
                    case 2: addEps(idSerie); break;
                    case 3: changeEps(idSerie); break;
                    case 4: deleteEps(idSerie); break;
                    case 0: System.out.println("Voltando..."); break;
                    default: System.out.println("Opcao invalida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

        } while (opcao != 0);
    }

    public void addEps(int idSerie) throws Exception {
        System.out.println("\n--- Incluir Episódio ---");

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
        novo.setId(idGerado);
        indice.indexarEpisodio(novo);
        indice.salvarIndice();

        System.out.println("Episódio criado com ID: " + idGerado);
    }

    public void searchEps(int idSerie) throws Exception {
        System.out.println("\n--- Buscar Episódio ---");
        System.out.print("Buscar por nome: ");
        String nome = scan.nextLine();

        List<Integer> ids = indice.buscar(nome);
        boolean encontrou = false;

        for (int id : ids) {
            Episodio ep = arq.read(id);
            if (ep != null && ep.getIDserie() == idSerie) {
                System.out.println(ep);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum episódio encontrado com esse nome nesta série.");
        }
    }

    public void changeEps(int idSerie) throws Exception {
        System.out.println("\n--- Alterar Episódio ---");
        Episodio[] episodios = arq.readAll();
        Map<Integer, List<Episodio>> temporadas = new HashMap<>();

        for (Episodio ep : episodios) {
            if (ep.getIDserie() == idSerie) {
                temporadas.putIfAbsent(ep.getTemporada(), new ArrayList<>());
                temporadas.get(ep.getTemporada()).add(ep);
            }
        }

        if (temporadas.isEmpty()) {
            System.out.println("Nenhuma temporada encontrada para esta série.");
            return;
        }

        System.out.println("Temporadas disponíveis:");
        for (Integer temporada : temporadas.keySet()) {
            System.out.println("Temporada: " + temporada);
        }

        System.out.print("\nInforme a temporada para listar os episódios: ");
        int temporadaEscolhida = Integer.parseInt(scan.nextLine());

        if (!temporadas.containsKey(temporadaEscolhida)) {
            System.out.println("Temporada inválida.");
            return;
        }

        List<Episodio> episodiosDaTemporada = temporadas.get(temporadaEscolhida);
        for (Episodio ep : episodiosDaTemporada) {
            System.out.printf("ID: %d | Nome: %s\n", ep.getId(), ep.getNome());
        }

        System.out.print("\nInforme o ID do episódio para alterar: ");
        int id = Integer.parseInt(scan.nextLine());

        Episodio epSelecionado = null;
        for (Episodio ep : episodiosDaTemporada) {
            if (ep.getId() == id) {
                epSelecionado = ep;
                break;
            }
        }

        if (epSelecionado == null) {
            System.out.println("Episódio não encontrado.");
            return;
        }

        indice.desindexarEpisodio(epSelecionado);

        System.out.println("Dados atuais:\n" + epSelecionado);

        System.out.print("Novo nome (enter p/ manter): ");
        String nome = scan.nextLine();
        if (!nome.isEmpty()) epSelecionado.setNome(nome);

        System.out.print("Nova temporada (enter p/ manter): ");
        String tempStr = scan.nextLine();
        if (!tempStr.isEmpty()) epSelecionado.setTemporada(Integer.parseInt(tempStr));

        System.out.print("Nova data de lançamento (dd/MM/yyyy, enter p/ manter): ");
        String dataStr = scan.nextLine();
        if (!dataStr.isEmpty()) {
            epSelecionado.setDataLancamento(LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        System.out.print("Nova duração (enter p/ manter): ");
        String durStr = scan.nextLine();
        if (!durStr.isEmpty()) epSelecionado.setDuracao(Integer.parseInt(durStr));

        boolean ok = arq.update(epSelecionado);
        if (ok) {
            indice.indexarEpisodio(epSelecionado);
            indice.salvarIndice();
            System.out.println("Episódio atualizado.");
        } else {
            System.out.println("Erro ao atualizar.");
        }
    }

    public void deleteEps(int idSerie) throws Exception {
        System.out.println("\n--- Excluir Episódio ---");
        System.out.print("ID do episódio para excluir: ");
        int id = Integer.parseInt(scan.nextLine());

        Episodio ep = arq.read(id);
        if (ep == null || ep.getIDserie() != idSerie) {
            System.out.println("Episódio não encontrado.");
            return;
        }

        indice.desindexarEpisodio(ep);
        indice.salvarIndice();

        boolean ok = arq.delete(id);
        System.out.println(ok ? "Episódio excluído." : "Erro ao excluir.");
    }
}
