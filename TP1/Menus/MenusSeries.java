package Menus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Arquivos.ArquivoEpisodio;
import Arquivos.ArquivoSerie;
import Entidades.Episodio;
import Entidades.Serie;

public class MenusSeries {

    private ArquivoSerie fileSerie;
    private Scanner scan;

    public MenusSeries() {
        try {
            fileSerie = new ArquivoSerie();
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o arquivo de séries: " + e.getMessage());
        }
        scan = new Scanner(System.in);
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("PUCFlix 1.0\n" +
                    "-----------\n" +
                    "> Inicio > Series\n\n" +
                    "1) Buscar\n" +
                    "2) Incluir\n" +
                    "3) Alterar\n" +
                    "4) Excluir\n" +
                    "0) Voltar");

            System.out.print("\nOpcao: ");

            try {
                opcao = Integer.valueOf(scan.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    try {
                        searchSeries();
                    } catch (Exception e) {
                        System.out.println("ERRO ao buscar série: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        addSerie();
                    } catch (Exception e) {
                        System.out.println("ERRO ao adicionar série: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        changeSerie();
                    } catch (Exception e) {
                        System.out.println("ERRO ao alterar série: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        deleteSeries();
                    } catch (Exception e) {
                        System.out.println("ERRO ao excluir série: " + e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        searchTemp();
                    } catch (Exception e) {
                        System.out.println("ERRO ao buscar temporada: " + e.getMessage());
                    }
                    break;
                case 6:
                    try {
                        searchEps();
                    } catch (Exception e) {
                        System.out.println("ERRO ao buscar episódios: " + e.getMessage());
                    }
                    break;
                case 7:
                    try
                    {
                        listAllSeries();
                    } catch (Exception e)
                    {
                        System.out.println("ERRO ao listar series: " + e.getMessage());
                    }
                default:
                    break;
            }

        } while (opcao != 0);
    }

    public void changeSerie() throws Exception {
        System.out.print("Nome da série a ser atualizada: ");
        String nome = scan.nextLine();
    
        Serie[] resultados = fileSerie.readNome(nome);
    
        if (resultados == null || resultados.length == 0) {
            System.out.println("Série não encontrada.");
            return;
        }
    
        System.out.println("Séries encontradas:");
        for (int i = 0; i < resultados.length; i++) {
            System.out.println("[" + i + "] " + resultados[i]);
        }
    
        System.out.print("Digite o número da série que deseja atualizar: ");
        int index = Integer.parseInt(scan.nextLine());
    
        if (index >= 0 && index < resultados.length) {
            Serie s = resultados[index];
    
            System.out.print("Novo nome [" + s.getNome() + "]: ");
            String novoNome = scan.nextLine();
            if (!novoNome.isEmpty()) s.setNome(novoNome);
    
            System.out.print("Nova sinopse [" + s.getSinopse() + "]: ");
            String novaSinopse = scan.nextLine();
            if (!novaSinopse.isEmpty()) s.setSinopse(novaSinopse);
    
            System.out.print("Nova plataforma [" + s.getStreaming() + "]: ");
            String novaPlataforma = scan.nextLine();
            if (!novaPlataforma.isEmpty()) s.setStreaming(novaPlataforma);
    
            System.out.print("Novo ano de lançamento (yyyy-mm-dd) [" + s.getAnoLancamento() + "]: ");
            String novoAno = scan.nextLine();
            if (!novoAno.isEmpty()) s.setAnoLancamento(LocalDate.parse(novoAno));
    
            boolean sucesso = fileSerie.update(s);
            if (sucesso)
                System.out.println("Série atualizada com sucesso!");
            else
                System.out.println("Erro ao atualizar série.");
        } else {
            System.out.println("Índice inválido.");
        }
    }

    public void addSerie() throws Exception {
        System.out.print("Nome da série: ");
        String nome = scan.nextLine();

        System.out.print("Ano de lançamento (formato AAAA-MM-DD): ");
        String data = scan.nextLine();

        System.out.print("Sinopse: ");
        String sinopse = scan.nextLine();

        System.out.print("Streaming: ");
        String streaming = scan.nextLine();

        try {
            Serie novaSerie = new Serie(nome, LocalDate.parse(data), sinopse, streaming);
            int idGerado = fileSerie.create(novaSerie);
            System.out.println("Série adicionada com sucesso! ID: " + idGerado);
        } catch (Exception e) {
            System.out.println("Erro ao adicionar série: " + e.getMessage());
        }
    }

    public void searchSeries() throws Exception {
        System.out.print("Nome da série: ");
        String nome = scan.nextLine();

        if (nome != null && !nome.isEmpty()) {
            Serie[] series = fileSerie.readNome(nome);

            if (series != null && series.length > 0) {
                System.out.println("\nSéries encontradas:");
                for (Serie s : series) {
                    System.out.println(s);
                    System.out.println("---------------------------");
                }
            } else {
                System.out.println("Nenhuma série encontrada com esse nome.");
            }
        } else {
            System.out.println("Nome inválido.");
        }
    }

    public void deleteSeries() throws Exception {
        System.out.print("Nome da série a ser excluída: ");
        String nome = scan.nextLine();
    
        Serie[] resultados = fileSerie.readNome(nome);
    
        if (resultados == null || resultados.length == 0) {
            System.out.println("Série não encontrada.");
            return;
        }
    
        System.out.println("Séries encontradas:");
        for (int i = 0; i < resultados.length; i++) {
            System.out.println("[" + i + "] " + resultados[i].getNome() + " - ID: " + resultados[i].getId());
        }
    
        System.out.print("Digite o número da série que deseja excluir: ");
        int index = Integer.parseInt(scan.nextLine());
    
        if (index >= 0 && index < resultados.length) {
            boolean sucesso = fileSerie.delete(resultados[index].getId());
            if (sucesso)
                System.out.println("Série excluída com sucesso!");
            else
                System.out.println("Erro ao excluir série.");
        } else {
            System.out.println("Índice inválido.");
        }
    }

    public void searchEps() throws Exception {

    }

    public void searchTemp() throws Exception {
        
    }

    public void mostraSerie (Serie serie) 
	{
        if (serie != null) 
		{
            System.out.println ("\nDetalhes da Serie:");
            System.out.println ("----------------------");
            System.out.printf  ("Nome.........: %s\n", serie.getNome());
            System.out.printf  ("ID...........: %d\n", serie.getId());
            System.out.printf  ("Streaming....: %s\n", serie.getStreaming());
            System.out.printf  ("Sinopse......: %s\n", serie.getSinopse());
            System.out.printf  ("Nascimento: %s\n", serie.getAnoLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println ("----------------------");
        }
    }

    public void listAllSeries () throws Exception{
        
    }
}
