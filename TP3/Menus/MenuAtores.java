// MenuAtores.java com suporte à lista invertida e todas as funcionalidades originais
package Menus;

import java.util.*;
import Relacionamentos.ArquivoRelacionamentoSerieAtor;
import Arquivos.ArquivoAtores;
import Entidades.Ator;
import Arquivos.ArquivoSerie;
import Entidades.Serie;
import ListaInvertida.IndiceInvertidoAtores;
import java.io.*;

public class MenuAtores {

    private ArquivoRelacionamentoSerieAtor fileRelacionamentos;
    private ArquivoAtores fileAtores;
    private Scanner scan;
    private ArquivoSerie fileSerie;
    private IndiceInvertidoAtores indice;

    public MenuAtores() {
        try {
            fileAtores = new ArquivoAtores();
            fileSerie = new ArquivoSerie();
            fileRelacionamentos = new ArquivoRelacionamentoSerieAtor();
            indice = new IndiceInvertidoAtores();
            indice.construirIndice(fileAtores);
        } catch (Exception e) {
            System.out.println("Erro crítico ao iniciar arquivos necessários: " + e.getMessage());
            fileAtores = null;
            fileSerie = null;
            fileRelacionamentos = null;
        }
        scan = new Scanner(System.in);
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\nPUCFlix 1.0\n" + 
                    "-----------\n" +
                    "> Inicio > Atores\n\n" +
                    "1) Buscar Ator\n" +       
                    "2) Incluir Ator\n" +     
                    "3) Alterar Ator\n" +       
                    "4) Excluir Ator\n" +      
                    "5) Listar Atores\n" +
                    "6) Listar Séries do Ator\n" + 
                    "0) Voltar");

            System.out.print("\nOpcao: ");

            try {
                opcao = Integer.valueOf(scan.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    try { searchAtor(); } catch (Exception e) { System.out.println("ERRO ao buscar ator: " + e.getMessage()); }
                    break;
                case 2:
                    try { addAtor(); } catch (Exception e) { System.out.println("ERRO ao adicionar ator: " + e.getMessage()); }
                    break;
                case 3:
                    try { changeAtor(); } catch (Exception e) { System.out.println("ERRO ao alterar ator: " + e.getMessage()); }
                    break;
                case 4:
                    try { deleteAtor(); } catch (Exception e) { System.out.println("ERRO ao excluir ator: " + e.getMessage()); }
                    break;
                case 5:
                    try { listAllAtores(); } catch (Exception e) { System.out.println("ERRO ao listar atores: " + e.getMessage()); }
                    break;
                case 6:
                    try { listSeriesFromActor(); } catch (Exception e) { System.out.println("ERRO ao listar séries do ator: " + e.getMessage()); }
                    break;
                default:
                    break;
            }

        } while (opcao != 0);
    }

    public void addAtor() throws Exception {
        System.out.print("Nome do ator: ");
        String nome = scan.nextLine();

        Ator novoAtor = new Ator(nome);
        int idGerado = fileAtores.create(novoAtor);
        novoAtor.setId(idGerado);
        indice.indexarAtor(novoAtor);
        indice.salvarIndice();
        System.out.println("Ator adicionado com sucesso! ID: " + idGerado);
    }

    public void searchAtor() throws Exception {
        if (fileAtores.isEmpty()) {
            System.out.println("Não há atores cadastrados.");
            return;
        }

        System.out.print("Termo de busca: ");
        String termo = scan.nextLine();

        List<Integer> ids = indice.buscar(termo);
        if (ids.isEmpty()) {
            System.out.println("Nenhum ator encontrado com esse termo.");
            return;
        }

        System.out.println("\nAtores encontrados:");
        for (int id : ids) {
            Ator ator = fileAtores.read(id);
            if (ator != null) {
                System.out.println(ator);
                System.out.println("---------------------------");
            }
        }
    }

    public void changeAtor() throws Exception {
        if (fileAtores.isEmpty()) {
            System.out.println("Não há atores cadastrados para alterar.");
            return;
        }

        System.out.print("Nome do ator a ser atualizado: ");
        String nome = scan.nextLine();

        Ator[] resultados = fileAtores.readNome(nome);
        if (resultados == null || resultados.length == 0) {
            System.out.println("Ator não encontrado.");
            return;
        }

        for (int i = 0; i < resultados.length; i++) {
            System.out.println("[" + i + "] " + resultados[i]);
        }

        System.out.print("Digite o número do ator que deseja atualizar: ");
        int index = Integer.parseInt(scan.nextLine());

        if (index >= 0 && index < resultados.length) {
            Ator ator = resultados[index];
            indice.desindexarAtor(ator);

            System.out.print("Novo nome [" + ator.getNome() + "]: ");
            String novoNome = scan.nextLine();
            if (!novoNome.isEmpty()) ator.setNome(novoNome);

            boolean sucesso = fileAtores.update(ator);
            if (sucesso) {
                indice.indexarAtor(ator);
                indice.salvarIndice();
                System.out.println("Ator atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar ator.");
            }
        } else {
            System.out.println("Índice inválido.");
        }
    }

    public void deleteAtor() throws Exception {
        if (fileAtores.isEmpty()) {
            System.out.println("Não há atores cadastrados para excluir.");
            return;
        }
        if (fileRelacionamentos == null) {
            System.out.println("Erro: Não foi possível carregar o arquivo de relacionamentos.");
            return;
        }

        System.out.print("Nome do ator a ser excluído: ");
        String nome = scan.nextLine();

        Ator[] resultados = fileAtores.readNome(nome);
        if (resultados == null || resultados.length == 0) {
            System.out.println("Ator não encontrado.");
            return;
        }

        for (int i = 0; i < resultados.length; i++) {
            System.out.println("[" + i + "] " + resultados[i].getNome() + " - ID: " + resultados[i].getId());
        }

        System.out.print("Digite o número do ator que deseja excluir: ");
        int index;
        try {
            index = Integer.parseInt(scan.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            return;
        }

        if (index >= 0 && index < resultados.length) {
            int atorIdParaExcluir = resultados[index].getId();
            boolean temVinculos = fileRelacionamentos.hasLinksAtor(atorIdParaExcluir);

            if (temVinculos) {
                System.out.println("\nERRO: O ator está vinculado a uma ou mais séries.");
            } else {
                boolean sucesso = fileAtores.delete(atorIdParaExcluir);
                if (sucesso) {
                    indice.desindexarAtor(resultados[index]);
                    indice.salvarIndice();
                    System.out.println("Ator excluído com sucesso!");
                } else {
                    System.out.println("Erro ao excluir ator do arquivo principal.");
                }
            }
        } else {
            System.out.println("Índice inválido.");
        }
    }

    public void listAllAtores() throws Exception {
        if (fileAtores.isEmpty()) {
            System.out.println("Não há atores cadastrados.");
            return;
        }

        Ator[] atores = fileAtores.readAll();
        if (atores.length == 0) {
            System.out.println("Não há atores cadastrados.");
        } else {
            System.out.println("\nLista de Atores:");
            System.out.println("----------------------");
            for (Ator ator : atores) {
                System.out.println(ator);
            }
        }
    }

    public void listSeriesFromActor() throws Exception {
        System.out.println("\n--- Listar Séries do Ator ---");

        if (fileAtores.isEmpty()) {
            System.out.println("Não há atores cadastrados.");
            return;
        }

        System.out.print("Digite o nome (ou parte do nome) do ator: ");
        String nomeAtor = scan.nextLine();
        Ator[] atoresEncontrados = fileAtores.readNome(nomeAtor);

        if (atoresEncontrados == null || atoresEncontrados.length == 0) {
            System.out.println("Nenhum ator encontrado com o nome '" + nomeAtor + "'.");
            return;
        }

        Ator atorSelecionado;
        int idAtorSelecionado;

        if (atoresEncontrados.length == 1) {
            atorSelecionado = atoresEncontrados[0];
            idAtorSelecionado = atorSelecionado.getId();
        } else {
            for (int i = 0; i < atoresEncontrados.length; i++) {
                System.out.println("[" + i + "] " + atoresEncontrados[i].getNome() + " (ID: " + atoresEncontrados[i].getId() + ")");
            }
            System.out.print("Número do ator: ");
            int indexAtor;
            try {
                indexAtor = Integer.parseInt(scan.nextLine());
                if (indexAtor < 0 || indexAtor >= atoresEncontrados.length) {
                    System.out.println("Índice inválido.");
                    return;
                }
                atorSelecionado = atoresEncontrados[indexAtor];
                idAtorSelecionado = atorSelecionado.getId();
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
                return;
            }
        }

        List<Integer> idsSeries = fileRelacionamentos.getSeriesPorAtor(idAtorSelecionado);
        if (idsSeries == null || idsSeries.isEmpty()) {
            System.out.println("Nenhuma série vinculada a este ator foi encontrada.");
        } else {
            System.out.println("Séries vinculadas:");
            System.out.println("------------------");
            for (int idSerie : idsSeries) {
                Serie serie = fileSerie.read(idSerie);
                if (serie != null) {
                    System.out.println("- " + serie.getNome() + " (ID: " + serie.getId() + ")");
                }
            }
        }
    }
}
