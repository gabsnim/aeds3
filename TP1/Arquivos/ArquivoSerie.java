package Arquivos;

import Registro.*;
import Entidades.Serie;
import Arvore.*;

import java.io.*;
import java.util.*;

public class ArquivoSerie extends Arquivo<Serie> {

    Arquivo<Serie> arqSeries;
    ArvoreBMais<ParNomeId> indiceIndireto;

    public ArquivoSerie() throws Exception {

        super("series", Serie.class.getConstructor());

        File directory = new File("./dados/serie.db");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        indiceIndireto = new ArvoreBMais<>(
                ParNomeId.class.getConstructor(), 5, "./dados/serie.db" + "/indiceIndireto.db");

    }

    @Override
    public int create(Serie s) throws Exception {

        int id = super.create(s);

        indiceIndireto.create(new ParNomeId(s.getNome(), id));

        return id;

    }

    public Serie[] readNome(String nome) throws Exception {
        if (nome == null || nome.isEmpty())
            return null;

        ArrayList<ParNomeId> pares = indiceIndireto.read(new ParNomeId(nome, -1));
        if (pares.isEmpty())
            return null;

        Serie[] series = new Serie[pares.size()];
        for (int i = 0; i < pares.size(); i++) {
            series[i] = read(pares.get(i).getId());
        }

        return series;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Serie s = read(id);
        if (s != null && super.delete(id)) {
            indiceIndireto.delete(new ParNomeId(s.getNome(), id));
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Serie s) throws Exception {
        Serie antiga = read(s.getId());
        if (antiga == null)
            return false;

        boolean atualizado = super.update(s);

        if (atualizado) {
            if (!antiga.getNome().equals(s.getNome())) {
                indiceIndireto.delete(new ParNomeId(antiga.getNome(), s.getId()));
                indiceIndireto.create(new ParNomeId(s.getNome(), s.getId()));
            }
        }

        return atualizado;
    }

}
