package Menus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Arquivos.ArquivoAtores;  
import Relacionamentos.ArquivoRelacionamentoSerieAtor;
import Arquivos.ArquivoEpisodio;
import Arquivos.ArquivoSerie;
import Entidades.Episodio;
import Entidades.Serie;
import Entidades.Ator;  

public class MenusSeries {

    private ArquivoAtores fileAtores; 
    private ArquivoEpisodio fileEpisodio;   
    private ArquivoSerie fileSerie;
    private ArquivoRelacionamentoSerieAtor fileRelacionamentos;
    private Scanner scan;

    public MenusSeries() {
        try {
            fileSerie = new ArquivoSerie();
            fileAtores = new ArquivoAtores(); 
            fileEpisodio = new ArquivoEpisodio(); 
            fileRelacionamentos = new ArquivoRelacionamentoSerieAtor(); 
        } catch (Exception e) {
            System.out.println("Erro crítico ao iniciar arquivos necessários (Séries, Atores, Episódios ou Relacionamentos): " + e.getMessage());
            fileSerie = null;
            fileAtores = null;
            fileEpisodio = null;
            fileRelacionamentos = null;
        }
        scan = new Scanner(System.in);
    }

    public void menu() {
        // Verifica se a inicialização falhou
        if (fileSerie == null || fileAtores == null || fileEpisodio == null || fileRelacionamentos == null) {
            System.out.println("Erro: Falha ao carregar arquivos necessários. Voltando ao menu anterior.");
            return;
        }

        int opcao;

        do {
            System.out.println("\nPUCFlix 1.0\n" + 
                    "-----------\n" +
                    "> Inicio > Series\n\n" +
                    "1) Buscar Série\n" +          
                    "2) Incluir Série\n" +         
                    "3) Alterar Série\n" +         
                    "4) Excluir Série\n" +         
                    "5) Vincular Ator à Série\n" + 
                    " 6) Listar Atores da Série\n" + 
                    " 7) Listar Séries\n" +          
                    " 8) Listar Episódios da Série\n" +
                    " 9) Listar Temporadas da Série\n" +
                    " 0) Voltar");

            System.out.print("\nOpcao: ");

            try {
                opcao = Integer.valueOf(scan.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    try { searchSeries(); } catch (Exception e) { System.out.println("ERRO ao buscar série: " + e.getMessage()); }
                    break;
                case 2:
                    try { addSerie(); } catch (Exception e) { System.out.println("ERRO ao adicionar série: " + e.getMessage()); }
                    break;
                case 3:
                    try { changeSerie(); } catch (Exception e) { System.out.println("ERRO ao alterar série: " + e.getMessage()); }
                    break;
                case 4:
                    try { deleteSeries(); } catch (Exception e) { System.out.println("ERRO ao excluir série: " + e.getMessage()); }
                    break;
                case 5: 
                    try { linkActorToSerie(); } catch (Exception e) { System.out.println("ERRO ao vincular ator: " + e.getMessage()); e.printStackTrace(); }
                    break;
                    case 6: 
                    try { listActorsFromSerie(); } catch (Exception e) { System.out.println("ERRO ao listar atores da série: " + e.getMessage()); e.printStackTrace();}
                    break;
                case 7: 
                    try { listAllSeries(); } catch (Exception e) { System.out.println("ERRO: " + e.getMessage()); } break;
                case 8: 
                    try { searchEps(); } catch (Exception e) { System.out.println("ERRO: " + e.getMessage()); } break;
                case 9: 
                    try { searchTemp(); } catch (Exception e) { System.out.println("ERRO: " + e.getMessage()); } break;
                case 0: break;
                default: System.out.println("Opcao invalida!"); break;
            }

        } while (opcao != 0);
    }

    public void changeSerie() throws Exception {
        if (fileSerie.isEmpty()) { // Verifica se há dados salvos
            System.out.println("Não há séries cadastradas para alterar.");
            return;
        }

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

    public void linkActorToSerie() throws Exception {
        System.out.println("\n--- Vincular Ator a Série ---");

        if (fileSerie.isEmpty()) {
            System.out.println("Não há séries cadastradas para vincular atores.");
            return;
        }
        System.out.print("Digite o nome (ou parte do nome) da série: ");
        String nomeSerie = scan.nextLine();
        Serie[] seriesEncontradas = fileSerie.readNome(nomeSerie);

        if (seriesEncontradas == null || seriesEncontradas.length == 0) {
            System.out.println("Nenhuma série encontrada com o nome '" + nomeSerie + "'.");
            return;
        }

        Serie serieSelecionada;
        int idSerieSelecionada;

        if (seriesEncontradas.length == 1) {
            serieSelecionada = seriesEncontradas[0];
            idSerieSelecionada = serieSelecionada.getId();
            System.out.println("Série selecionada: " + serieSelecionada.getNome() + " (ID: " + idSerieSelecionada + ")");
        } else {
            System.out.println("Múltiplas séries encontradas. Selecione uma:");
            for (int i = 0; i < seriesEncontradas.length; i++) {
                System.out.println("[" + i + "] " + seriesEncontradas[i].getNome() + " (ID: " + seriesEncontradas[i].getId() + ")");
            }
            System.out.print("Número da série: ");
            int indexSerie;
            try {
                indexSerie = Integer.parseInt(scan.nextLine());
                if (indexSerie < 0 || indexSerie >= seriesEncontradas.length) {
                    System.out.println("Índice inválido.");
                    return;
                }
                serieSelecionada = seriesEncontradas[indexSerie];
                idSerieSelecionada = serieSelecionada.getId();
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
                return;
            }
        }

        if (fileAtores.isEmpty()) {
            System.out.println("Não há atores cadastrados para vincular.");
            return;
        }
        System.out.print("\nDigite o nome (ou parte do nome) do ator a ser vinculado à série '" + serieSelecionada.getNome() + "': ");
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
            System.out.println("Ator selecionado: " + atorSelecionado.getNome() + " (ID: " + idAtorSelecionado + ")");
        } else {
            System.out.println("Múltiplos atores encontrados. Selecione um:");
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

        System.out.println("\nTentando vincular Ator ID " + idAtorSelecionado + " com Série ID " + idSerieSelecionada + "...");
        boolean sucesso = fileRelacionamentos.createLink(idSerieSelecionada, idAtorSelecionado);

        if (sucesso) {
            System.out.println("Vínculo criado com sucesso!");
        } else {
            // createLink pode retornar false se o link já existir ou se houver erro parcial.
            // A classe ArquivoRelacionamentoSerieAtor já imprime erros internos.
            System.out.println("Falha ao criar o vínculo. Verifique se ele já existe ou se ocorreu um erro.");
        }
    }

    public void searchSeries() throws Exception {

        if (fileSerie.isEmpty()) { // Verifica se há dados salvos
            System.out.println("Não há séries cadastradas.");
            return;
        }

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

        /**
     * Lista os atores vinculados a uma série específica.
     */
    public void listActorsFromSerie() throws Exception {
        System.out.println("\n--- Listar Atores da Série ---");

        if (fileSerie.isEmpty()) {
            System.out.println("Não há séries cadastradas.");
            return;
        }
        System.out.print("Digite o nome (ou parte do nome) da série: ");
        String nomeSerie = scan.nextLine();
        Serie[] seriesEncontradas = fileSerie.readNome(nomeSerie);

        if (seriesEncontradas == null || seriesEncontradas.length == 0) {
            System.out.println("Nenhuma série encontrada com o nome '" + nomeSerie + "'.");
            return;
        }

        Serie serieSelecionada;
        int idSerieSelecionada;

        if (seriesEncontradas.length == 1) {
            serieSelecionada = seriesEncontradas[0];
            idSerieSelecionada = serieSelecionada.getId();
            System.out.println("Série selecionada: " + serieSelecionada.getNome() + " (ID: " + idSerieSelecionada + ")");
        } else {
            System.out.println("Múltiplas séries encontradas. Selecione uma:");
            for (int i = 0; i < seriesEncontradas.length; i++) {
                System.out.println("[" + i + "] " + seriesEncontradas[i].getNome() + " (ID: " + seriesEncontradas[i].getId() + ")");
            }
            System.out.print("Número da série: ");
            int indexSerie;
            try {
                indexSerie = Integer.parseInt(scan.nextLine());
                if (indexSerie < 0 || indexSerie >= seriesEncontradas.length) {
                    System.out.println("Índice inválido.");
                    return;
                }
                serieSelecionada = seriesEncontradas[indexSerie];
                idSerieSelecionada = serieSelecionada.getId();
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
                return;
            }
        }

        System.out.println("\nBuscando atores para a série '" + serieSelecionada.getNome() + "'...");
        List<Integer> idsAtores = fileRelacionamentos.getAtoresPorSerie(idSerieSelecionada);

        if (idsAtores == null || idsAtores.isEmpty()) {
            System.out.println("Nenhum ator vinculado a esta série foi encontrado.");
        } else {
            System.out.println("Atores vinculados:");
            System.out.println("------------------");
            int count = 0;
            for (int idAtor : idsAtores) {
                Ator ator = fileAtores.read(idAtor);
                if (ator != null) {
                    System.out.println("- " + ator.getNome() + " (ID: " + ator.getId() + ")");
                    count++;
                } else {
                    // Isso indicaria uma inconsistência (link existe, mas ator não)
                    System.out.println("- [Erro: Ator com ID " + idAtor + " não encontrado no arquivo principal]");
                }
            }
            if (count == 0 && !idsAtores.isEmpty()) {
                 System.out.println("Aviso: Foram encontrados vínculos, mas os atores correspondentes não puderam ser lidos.");
            } else if (count > 0) {
                 System.out.println("------------------");
                 System.out.println("Total: " + count + " ator(es) encontrado(s).");
            }
        }
    }

    public void deleteSeries() throws Exception {
        if (fileSerie.isEmpty()) { // Verifica se há séries cadastradas
            System.out.println("Não há séries cadastradas para excluir.");
            return;
        }
        if (fileRelacionamentos == null) {
             System.out.println("Erro: Não foi possível carregar o arquivo de relacionamentos.");
             return;
        }

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
        int index;
        try {
             index = Integer.parseInt(scan.nextLine());
         } catch (NumberFormatException e) {
             System.out.println("Entrada inválida. Por favor, digite um número.");
             return;
         }

        if (index >= 0 && index < resultados.length) {
            Serie serieSelecionada = resultados[index];
            int idSerieParaExcluir = serieSelecionada.getId();

            Episodio[] episodios;
            try {
                // Se fileEpisodio não foi inicializado corretamente, pula a verificação
                if (fileEpisodio != null) {
                    episodios = fileEpisodio.readAll(); // TODO: Otimizar esta leitura
                    for (Episodio ep : episodios) {
                        if (ep != null && ep.getIDserie() == idSerieParaExcluir) {
                            System.out.println("\nERRO: Não é possível excluir a série '" + serieSelecionada.getNome() + "'. Existem episódios associados a ela.");
                            return; 
                        }
                    }
                } else {
                     System.out.println("Aviso: Não foi possível verificar episódios associados devido a erro na inicialização.");
                }
            } catch (Exception e) {
                System.out.println("Erro ao verificar episódios associados: " + e.getMessage());
                System.out.println("A exclusão será cancelada por segurança.");
                return;
            }


            // Exclui a série do arquivo principal
            boolean sucessoExclusaoSerie = fileSerie.delete(idSerieParaExcluir);

            if (sucessoExclusaoSerie) {
                System.out.println("Série '" + serieSelecionada.getNome() + "' excluída com sucesso!");

                // --- INÍCIO DA EXCLUSÃO DOS VÍNCULOS N:N ---
                System.out.println("Removendo vínculos da série com atores...");
                boolean sucessoExclusaoVinculos = fileRelacionamentos.deleteLinksPorSerie(idSerieParaExcluir);
                if (sucessoExclusaoVinculos) {
                    System.out.println("Vínculos com atores removidos com sucesso.");
                } else {
                    System.out.println("Aviso: Ocorreram erros ao remover alguns vínculos com atores.");
                }
                // --- FIM DA EXCLUSÃO DOS VÍNCULOS N:N ---

            } else {
                System.out.println("Erro ao excluir a série '" + serieSelecionada.getNome() + "' do arquivo principal.");
            }
        } else {
            System.out.println("Índice inválido.");
        }
    }

    public void searchEps() throws Exception {
        if (fileSerie.isEmpty()) { // Verifica se há séries cadastradas
            System.out.println("Não há séries cadastradas.");
            return;
        }

        System.out.print("Informe o nome da série para buscar os episódios: ");
        String nome = scan.nextLine();

        Serie[] series = fileSerie.readNome(nome);

        if (series == null || series.length == 0) {
            System.out.println("Série não encontrada.");
            return;
        }

        System.out.println("Séries encontradas:");
        for (int i = 0; i < series.length; i++) {
            System.out.println("[" + i + "] " + series[i].getNome());
        }

        System.out.print("Escolha o índice da série: ");
        int index = Integer.parseInt(scan.nextLine());

        if (index < 0 || index >= series.length) {
            System.out.println("Índice inválido.");
            return;
        }

        Serie serieSelecionada = series[index];
        ArquivoEpisodio arquivoEpisodio = new ArquivoEpisodio();
        Episodio[] episodios = arquivoEpisodio.readAll();

        System.out.println("\nEpisódios da série " + serieSelecionada.getNome() + ":");
        boolean encontrou = false;
        for (Episodio ep : episodios) {
            if (ep.getIDserie() == serieSelecionada.getId()) {
                System.out.println(ep);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum episódio encontrado para esta série.");
        }
    }

    public void searchTemp() throws Exception {
        if (fileSerie.isEmpty()) { // Verifica se há séries cadastradas
            System.out.println("Não há séries cadastradas.");
            return;
        }

        System.out.print("Informe o nome da série para buscar as temporadas: ");
        String nome = scan.nextLine();

        Serie[] series = fileSerie.readNome(nome);

        if (series == null || series.length == 0) {
            System.out.println("Série não encontrada.");
            return;
        }

        System.out.println("Séries encontradas:");
        for (int i = 0; i < series.length; i++) {
            System.out.println("[" + i + "] " + series[i].getNome());
        }

        System.out.print("Escolha o índice da série: ");
        int index = Integer.parseInt(scan.nextLine());

        if (index < 0 || index >= series.length) {
            System.out.println("Índice inválido.");
            return;
        }

        Serie serieSelecionada = series[index];
        ArquivoEpisodio arquivoEpisodio = new ArquivoEpisodio();
        Episodio[] episodios = arquivoEpisodio.readAll();

        // Agrupa os episódios por temporada
        List<Integer> temporadas = new ArrayList<>();
        for (Episodio ep : episodios) {
            if (ep.getIDserie() == serieSelecionada.getId() && !temporadas.contains(ep.getTemporada())) {
                temporadas.add(ep.getTemporada());
            }
        }

        if (temporadas.isEmpty()) {
            System.out.println("Nenhuma temporada encontrada para esta série.");
        } else {
            System.out.println("\nTemporadas disponíveis:");
            for (Integer temporada : temporadas) {
                System.out.println("Temporada: " + temporada);
            }
        }
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

    public void listAllSeries() throws Exception {
        if (fileSerie.isEmpty()) { // Verifica se há dados salvos
            System.out.println("Não há séries cadastradas.");
            return;
        }
    
        Serie[] series = fileSerie.readAll(); // Lê todas as séries do arquivo
    
        if (series.length == 0) {
            System.out.println("Não há séries cadastradas.");
        } else {
            System.out.println("\nLista de Séries:");
            System.out.println("----------------------");
            for (Serie serie : series) {
                mostraSerie(serie); // Reutiliza o método mostraSerie para exibir os detalhes
            }
        }
    }
}
