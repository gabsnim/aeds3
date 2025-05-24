package Menus;

import Arquivos.ArquivoEpisodio;
import Arquivos.ArquivoSerie;
import Entidades.Episodio;
import Entidades.Serie;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MenuEpisodios {

    private Scanner scan = new Scanner(System.in);
    private ArquivoEpisodio arq;

    public MenuEpisodios() throws Exception {
        arq = new ArquivoEpisodio(); // Usa o construtor que garante o índice
    }

    public void menu() throws Exception {
        int opcao;

        // Solicita o ID da série antes de realizar qualquer ação
        System.out.println("\n--- Identificação da Série ---");
        ArquivoSerie arquivoSerie = new ArquivoSerie(); // Instancia o arquivo de séries
        Serie[] series = arquivoSerie.readAll(); // Lê todas as séries cadastradas

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

        // Verifica se o ID informado é válido
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

        // Menu de ações relacionadas à série selecionada
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
                    case 1: searchEps(idSerie); break; // Passa o ID da série
                    case 2: addEps(idSerie); break;   // Passa o ID da série
                    case 3: changeEps(idSerie); break; // Passa o ID da série
                    case 4: deleteEps(idSerie); break; // Passa o ID da série
                    case 0: System.out.println("Voltando..."); break;
                    default: System.out.println("Opcao invalida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

        } while (opcao != 0);
    }

    public void addEps(int idSerie) throws Exception {
        //Verificar se existe serie para adicionar um ep
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

        System.out.println("Episódio criado com ID: " + idGerado);
    }

    public void searchEps(int idSerie) throws Exception {
        System.out.println("\n--- Buscar Episódio ---");
        System.out.print("Buscar por nome: ");
        
            String nome = scan.nextLine();
            Episodio[] encontrados = arq.readNome(nome);
            if (encontrados != null && encontrados.length > 0) {
                for (Episodio ep : encontrados) {
                    if (ep.getIDserie() == idSerie) {
                        System.out.println(ep);
                    }
                }
            } else {
                System.out.println("Nenhum episódio encontrado com esse nome.");
            }
    }

    public void changeEps(int idSerie) throws Exception {
        System.out.println("\n--- Alterar Episódio ---");
    
        // Passo 1: Listar temporadas disponíveis
        System.out.println("Buscando temporadas disponíveis...");
        Episodio[] episodios = arq.readAll(); // Lê todos os episódios
        Map<Integer, List<Episodio>> temporadas = new HashMap<>();
    
        // Agrupa episódios por temporada
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
    
        // Exibe as temporadas disponíveis
        System.out.println("Temporadas disponíveis:");
        for (Integer temporada : temporadas.keySet()) {
            System.out.println("Temporada: " + temporada);
        }
    
        // Passo 2: Solicitar temporada ao usuário
        System.out.print("\nInforme a temporada para listar os episódios: ");
        int temporadaEscolhida = Integer.parseInt(scan.nextLine());
    
        if (!temporadas.containsKey(temporadaEscolhida)) {
            System.out.println("Temporada inválida.");
            return;
        }
    
        // Exibe os episódios da temporada escolhida
        System.out.println("\nEpisódios da Temporada " + temporadaEscolhida + ":");
        List<Episodio> episodiosDaTemporada = temporadas.get(temporadaEscolhida);
        for (Episodio ep : episodiosDaTemporada) {
            System.out.printf("ID: %d | Nome: %s\n", ep.getId(), ep.getNome());
        }
    
        // Passo 3: Solicitar ID do episódio ao usuário
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
    
        // Passo 4: Alterar os dados do episódio
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
        System.out.println(ok ? "Episódio atualizado." : "Erro ao atualizar.");
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

        boolean ok = arq.delete(id);
        System.out.println(ok ? "Episódio excluído." : "Erro ao excluir.");
    }
}
