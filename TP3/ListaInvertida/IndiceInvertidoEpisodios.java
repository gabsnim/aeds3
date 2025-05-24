package ListaInvertida;

import java.io.*;
import java.util.*;
import Entidades.Episodio;
import Arquivos.ArquivoEpisodio;

public class IndiceInvertidoEpisodios {
    private Map<String, List<Integer>> indice;
    private final String caminhoIndice = "dados/indice_invertido_episodios.db";

    public IndiceInvertidoEpisodios() {
        indice = new HashMap<>();
        carregarIndice();
    }

    public void construirIndice(ArquivoEpisodio fileEpisodios) throws Exception {
        indice.clear();
        Episodio[] episodios = fileEpisodios.readAll();
        for (Episodio ep : episodios) {
            indexarEpisodio(ep);
        }
        salvarIndice();
    }

    public void indexarEpisodio(Episodio ep) {
        int id = ep.getId();
        String[] termos = ep.getNome().toLowerCase().split("\\s+");

        for (String termo : termos) {
            indice.computeIfAbsent(termo, k -> new ArrayList<>());
            if (!indice.get(termo).contains(id)) {
                indice.get(termo).add(id);
            }
        }
    }

    public void desindexarEpisodio(Episodio ep) {
        int id = ep.getId();
        String[] termos = ep.getNome().toLowerCase().split("\\s+");

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
            System.out.println("Erro ao salvar o índice invertido de episódios: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void carregarIndice() {
        File f = new File(caminhoIndice);
        if (!f.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            indice = (Map<String, List<Integer>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar o índice invertido de episódios: " + e.getMessage());
            indice = new HashMap<>();
        }
    }
}
