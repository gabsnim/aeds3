package Entidades;

import java.io.*;

import Registro.Registro;

public class Ator implements Registro {
    private int id; // ID único do ator
    private String nome; // Nome do ator

    // Construtores
    public Ator() {
        this(-1, "");
    }

    public Ator(String nome) {
        this(-1, nome);
    }

    public Ator(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Métodos para serialização
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeUTF(nome);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        nome = dis.readUTF();
    }

    @Override
    public String toString() {
        return "Ator [ID=" + id + ", Nome=" + nome + "]";
    }
}