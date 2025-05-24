package Arquivos;

import Registro.*;
import Entidades.Episodio;
import Arvore.*;

import java.io.*;
import java.util.*;



public class ArquivoEpisodio extends Arquivo<Episodio> {

    ArvoreBMais<ParNomeId> indiceIndireto;

    public ArquivoEpisodio() throws Exception {

        super("episodios", Episodio.class.getConstructor());

        File directory = new File("./dados/episodios.db");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        indiceIndireto = new ArvoreBMais<>(
                ParNomeId.class.getConstructor(), 5, "./dados/episodios.db/indiceIndireto.db");
    }

    @Override
    public int create(Episodio e) throws Exception {
        int id = super.create(e);
        indiceIndireto.create(new ParNomeId(e.getNome(), id));
        return id;
    }

    public Episodio[] readNome(String nome) throws Exception {
        if (nome == null || nome.isEmpty())
            return null;

        ArrayList<ParNomeId> pares = indiceIndireto.read(new ParNomeId(nome, -1));
        if (pares.isEmpty())
            return null;

        Episodio[] episodios = new Episodio[pares.size()];
        for (int i = 0; i < pares.size(); i++) {
            episodios[i] = read(pares.get(i).getId());
        }

        return episodios;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Episodio e = read(id);
        if (e != null && super.delete(id)) {
            indiceIndireto.delete(new ParNomeId(e.getNome(), id));
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Episodio e) throws Exception {
        Episodio antigo = read(e.getId());
        if (antigo == null)
            return false;

        boolean atualizado = super.update(e);

        if (atualizado) {
            if (!antigo.getNome().equals(e.getNome())) {
                indiceIndireto.delete(new ParNomeId(antigo.getNome(), e.getId()));
                indiceIndireto.create(new ParNomeId(e.getNome(), e.getId()));
            }
        }

        return atualizado;
    }

    public ArquivoEpisodio(String nomeArquivo) throws Exception {
        super(nomeArquivo, Episodio.class.getConstructor());
    }

    public Episodio[] readPorSerie(int idSerie) throws Exception {
        ArrayList<Episodio> lista = new ArrayList<>();
        Episodio ep;
        int pos = 0;
    
        while (true) {
            try {
                ep = this.read(pos++);
                if (ep != null && ep.getIDserie() == idSerie) {
                    lista.add(ep);
                }
            } catch (Exception e) {
                break;
            }
        }
    
        return lista.toArray(new Episodio[0]);
    }

    
    // public ArrayList<Episodio> buscarPorIDSerie(int idSerie) throws Exception {
    //     ArrayList<Episodio> todos = this.readAll();
    //     ArrayList<Episodio> resultado = new ArrayList<>();
    
    //     for (Episodio ep : todos) {
    //         if (ep.getIDserie() == idSerie) {
    //             resultado.add(ep);
    //         }
    //     }
    
    //     return resultado;
    // }
    
}
