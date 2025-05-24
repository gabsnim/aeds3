package ListaInvertida;

import java.io.*;
import java.util.*;
import Entidades.Ator;
import Arquivos.ArquivoAtores;

public class IndiceInvertidoAtores {
    private Map<String, List<Integer>> indice;
    private final String caminhoIndice = "dados/indice_invertido_atores.db";

    public IndiceInvertidoAtores() {
        indice = new HashMap<>();
        carregarIndice();
    }

    public void construirIndice(ArquivoAtores fileAtores) throws Exception {
        indice.clear();
        Ator[] atores = fileAtores.readAll();
        for (Ator ator : atores) {
            indexarAtor(ator);
        }
        salvarIndice();
    }

    public void indexarAtor(Ator ator) {
        int id = ator.getId();
        String[] termos = ator.getNome().toLowerCase().split("\\s+");

        for (String termo : termos) {
            indice.computeIfAbsent(termo, k -> new ArrayList<>());
            if (!indice.get(termo).contains(id)) {
                indice.get(termo).add(id);
            }
        }
    }

    public void desindexarAtor(Ator ator) {
        int id = ator.getId();
        String[] termos = ator.getNome().toLowerCase().split("\\s+");

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
            System.out.println("Erro ao salvar o índice invertido: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void carregarIndice() {
        File f = new File(caminhoIndice);
        if (!f.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            indice = (Map<String, List<Integer>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar o índice invertido: " + e.getMessage());
            indice = new HashMap<>();
        }
    }
}
