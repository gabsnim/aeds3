package ListaInvertida;

import java.io.*;
import java.util.*;
import Entidades.Serie;
import Arquivos.ArquivoSerie;

public class IndiceInvertidoSeries {
    private Map<String, List<Integer>> indice;
    private final String caminhoIndice = "dados/indice_invertido_series.db";

    public IndiceInvertidoSeries() {
        indice = new HashMap<>();
        carregarIndice();
    }

    public void construirIndice(ArquivoSerie fileSeries) throws Exception {
        indice.clear();
        Serie[] series = fileSeries.readAll();
        for (Serie serie : series) {
            indexarSerie(serie);
        }
        salvarIndice();
    }

    public void indexarSerie(Serie serie) {
        int id = serie.getId();
        String[] termos = serie.getNome().toLowerCase().split("\\s+");

        for (String termo : termos) {
            indice.computeIfAbsent(termo, k -> new ArrayList<>());
            if (!indice.get(termo).contains(id)) {
                indice.get(termo).add(id);
            }
        }
    }

    public void desindexarSerie(Serie serie) {
        int id = serie.getId();
        String[] termos = serie.getNome().toLowerCase().split("\\s+");

        for (String termo : termos) {
            if (indice.containsKey(termo)) {
                indice.get(termo).removeIf(existingId -> existingId == id);
                if (indice.get(termo).isEmpty()) {
                    indice.remove(termo);
                }
            }
        }
    }

    public List<Integer> buscar(String termo) {
        termo = termo.toLowerCase();
        return indice.getOrDefault(termo, new ArrayList<>());
    }

    public void salvarIndice() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminhoIndice))) {
            oos.writeObject(indice);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o índice invertido de séries: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void carregarIndice() {
        File f = new File(caminhoIndice);
        if (!f.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            indice = (Map<String, List<Integer>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar o índice invertido de séries: " + e.getMessage());
            indice = new HashMap<>();
        }
    }
}
