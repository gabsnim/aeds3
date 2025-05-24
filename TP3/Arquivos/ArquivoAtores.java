package Arquivos;

import Registro.*;
import Entidades.Ator;
import Arvore.*;

import java.io.*;
import java.util.*;

public class ArquivoAtores extends Arquivo<Ator> {

    ArvoreBMais<ParNomeId> indiceIndireto;

    public ArquivoAtores() throws Exception {

        super("atores", Ator.class.getConstructor());

        File directory = new File("./dados/atores.db");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        indiceIndireto = new ArvoreBMais<>(
                ParNomeId.class.getConstructor(), 5, "./dados/atores.db" + "/indiceIndireto.db");
    }

    @Override
    public int create(Ator a) throws Exception {

        int id = super.create(a);

        indiceIndireto.create(new ParNomeId(a.getNome(), id));

        return id;
    }

    public Ator[] readNome(String nome) throws Exception {
        if (nome == null || nome.isEmpty())
            return null;

        ArrayList<ParNomeId> pares = indiceIndireto.read(new ParNomeId(nome, -1));
        if (pares.isEmpty())
            return null;

        Ator[] atores = new Ator[pares.size()];
        for (int i = 0; i < pares.size(); i++) {
            atores[i] = read(pares.get(i).getId());
        }

        return atores;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Ator a = read(id);
        if (a != null && super.delete(id)) {
            indiceIndireto.delete(new ParNomeId(a.getNome(), id));
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Ator a) throws Exception {
        Ator antigo = read(a.getId());
        if (antigo == null)
            return false;

        boolean atualizado = super.update(a);

        if (atualizado) {
            if (!antigo.getNome().equals(a.getNome())) {
                indiceIndireto.delete(new ParNomeId(antigo.getNome(), a.getId()));
                indiceIndireto.create(new ParNomeId(a.getNome(), a.getId()));
            }
        }

        return atualizado;
    }

    public boolean isEmpty() throws Exception {
        // Verifica se o arquivo de registros está vazio
        return super.readAll().length == 0;
    }
}