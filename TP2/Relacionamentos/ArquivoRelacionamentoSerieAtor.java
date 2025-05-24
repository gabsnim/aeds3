package Relacionamentos; 

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Arvore.ArvoreBMais;
import Arvore.ParIdId;

public class ArquivoRelacionamentoSerieAtor {

    private ArvoreBMais<ParIdId> serieAtorTree; // Chave: (idSerie, idAtor)
    private ArvoreBMais<ParIdId> atorSerieTree; // Chave: (idAtor, idSerie)
    private final String basePath = ".\\Relacionamentos\\";
    private final int ORDEM_ARVORE = 5; 

    public ArquivoRelacionamentoSerieAtor() throws Exception {
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        serieAtorTree = new ArvoreBMais<>(
                ParIdId.class.getConstructor(),
                ORDEM_ARVORE,
                basePath + "serie_ator.db");

        atorSerieTree = new ArvoreBMais<>(
                ParIdId.class.getConstructor(),
                ORDEM_ARVORE,
                basePath + "ator_serie.db");
    }

    /**
     * Cria um vínculo entre uma série e um ator.
     * @param idSerie ID da Série.
     * @param idAtor ID do Ator.
     * @return true se o vínculo foi criado com sucesso, false caso contrário.
     */
    public boolean createLink(int idSerie, int idAtor) throws Exception {
        boolean success = false;
        ParIdId parSerieAtor = new ParIdId(idSerie, idAtor);
        ParIdId parAtorSerie = new ParIdId(idAtor, idSerie);

        try {
            boolean createdSerieAtor = serieAtorTree.create(parSerieAtor);
            boolean createdAtorSerie = atorSerieTree.create(parAtorSerie);

             success = createdSerieAtor && createdAtorSerie;
             if (!success && createdSerieAtor) { 
                 serieAtorTree.delete(parSerieAtor);
             }
             if (!success && createdAtorSerie) { 
                 atorSerieTree.delete(parAtorSerie);
             }

             if (!createdSerieAtor && !createdAtorSerie) {

                 List<ParIdId> check1 = serieAtorTree.read(parSerieAtor);
                 List<ParIdId> check2 = atorSerieTree.read(parAtorSerie);
                 if (check1 != null && !check1.isEmpty() && check2 != null && !check2.isEmpty()) {
                     success = true; 
                 }
             } else {
                 success = createdSerieAtor && createdAtorSerie;
             }


        } catch (Exception e) {
            System.err.println("Erro ao criar link Serie/Ator: " + e.getMessage());

             try { serieAtorTree.delete(parSerieAtor); } catch (Exception ignored) {}
             try { atorSerieTree.delete(parAtorSerie); } catch (Exception ignored) {}
            throw e; 
        }
        return success;
    }

    /**
     * Remove um vínculo entre uma série e um ator.
     * @param idSerie ID da Série.
     * @param idAtor ID do Ator.
     * @return true se o vínculo foi removido com sucesso, false caso contrário.
     */
    public boolean deleteLink(int idSerie, int idAtor) throws Exception {
         boolean deletedSerieAtor = serieAtorTree.delete(new ParIdId(idSerie, idAtor));
         boolean deletedAtorSerie = atorSerieTree.delete(new ParIdId(idAtor, idSerie));


         return deletedSerieAtor && deletedAtorSerie;
    }

    /**
     * Obtém os IDs de todos os atores vinculados a uma série específica.
     * @param idSerie ID da Série.
     * @return Lista de IDs dos Atores. Retorna lista vazia se não houver atores ou a série não for encontrada.
     */
    public List<Integer> getAtoresPorSerie(int idSerie) throws Exception {
        List<Integer> idsAtores = new ArrayList<>();
        ArrayList<ParIdId> pares = serieAtorTree.read(new ParIdId(idSerie, -1)); 

        if (pares != null) {
            for (ParIdId par : pares) {
                idsAtores.add(par.getId2());
            }
        }
        return idsAtores;
    }

    /**
     * Obtém os IDs de todas as séries vinculadas a um ator específico.
     * @param idAtor ID do Ator.
     * @return Lista de IDs das Séries. Retorna lista vazia se não houver séries ou o ator não for encontrado.
     */
    public List<Integer> getSeriesPorAtor(int idAtor) throws Exception {
        List<Integer> idsSeries = new ArrayList<>();
        // Busca por todos os pares que começam com idAtor
        ArrayList<ParIdId> pares = atorSerieTree.read(new ParIdId(idAtor, -1)); // -1 como wildcard

        if (pares != null) {
            for (ParIdId par : pares) {
                idsSeries.add(par.getId2());
            }
        }
        return idsSeries;
    }

    /**
     * Remove todos os vínculos associados a uma série.
     * Usado quando uma série é excluída.
     * @param idSerie ID da Série a ter os vínculos removidos.
     * @return true se todos os vínculos foram removidos com sucesso, false caso contrário.
     */
    public boolean deleteLinksPorSerie(int idSerie) throws Exception {
        boolean allDeleted = true;
        List<Integer> idsAtores = getAtoresPorSerie(idSerie);

        for (int idAtor : idsAtores) {
            if (!deleteLink(idSerie, idAtor)) {
                allDeleted = false;
                System.err.println("Falha ao remover link para Ator ID: " + idAtor + " da Serie ID: " + idSerie);
            }
        }
        return allDeleted;
    }

     /**
     * Remove todos os vínculos associados a um ator.
     * Usado quando um ator é excluído (embora a validação deva impedir isso se houver links).
     * @param idAtor ID do Ator a ter os vínculos removidos.
     * @return true se todos os vínculos foram removidos com sucesso, false caso contrário.
     */
    public boolean deleteLinksPorAtor(int idAtor) throws Exception {
        boolean allDeleted = true;
        List<Integer> idsSeries = getSeriesPorAtor(idAtor);

        for (int idSerie : idsSeries) {
            if (!deleteLink(idSerie, idAtor)) {
                allDeleted = false;
                System.err.println("Falha ao remover link para Serie ID: " + idSerie + " do Ator ID: " + idAtor);
            }
        }
        return allDeleted;
    }

    /**
     * Verifica se um ator possui algum vínculo com séries.
     * @param idAtor ID do Ator.
     * @return true se o ator está vinculado a pelo menos uma série, false caso contrário.
     */
    public boolean hasLinksAtor(int idAtor) throws Exception {
        List<Integer> series = getSeriesPorAtor(idAtor);
        return series != null && !series.isEmpty();
    }

    /**
     * Fecha os arquivos das árvores B+.
     */
    public void close() throws Exception {
        if (serieAtorTree != null) {
            // A classe ArvoreBMais não tem um método close() explicitamente no código fornecido.
            // Se fosse necessário (para liberar recursos ou garantir escrita), ele deveria ser adicionado.
            // Por ora, assumimos que o RandomAccessFile é fechado internamente ou na finalização.
            // serieAtorTree.close(); // Se existisse
        }
        if (atorSerieTree != null) {
            // atorSerieTree.close(); // Se existisse
        }
    }
}