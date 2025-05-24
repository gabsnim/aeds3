package Entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import Registro.*;

public class Episodio implements Registro{

    protected int ID;
    protected int IDserie;
    protected String Nome;
    protected int Temporada;
    protected LocalDate DataLancamento;
    protected int Duracao;



    //construtor FULL
    public Episodio(int i, int is, String n, int t, LocalDate dl, int d)
    {
        ID = i;
        IDserie = is;
        Nome = n;
        Temporada = t;
        DataLancamento = dl;
        Duracao = d;
    }

    //construtor Vazio
    public Episodio()
    {
        ID = -1;
        IDserie = -1;
        Nome = "";
        Temporada = -1;
        DataLancamento = LocalDate.now();
        Duracao = -1;
    }

    //construtor missing ID
    public Episodio(int is, String n, int t, LocalDate dl, int d)
    {
        ID = -1;
        IDserie = is;
        Nome = n;
        Temporada = t;
        DataLancamento = dl;
        Duracao = d;
    }

    


    public int getId() {
        return ID;
    }

    public void setId(int iD) {
        ID = iD;
    }

    public int getIDserie() {
        return IDserie;
    }

    public void setIDserie(int iDserie) {
        IDserie = iDserie;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public int getTemporada() {
        return Temporada;
    }

    public void setTemporada(int temporada) {
        Temporada = temporada;
    }

    public LocalDate getDataLancamento() {
        return DataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        DataLancamento = dataLancamento;
    }

    public int getDuracao() {
        return Duracao;
    }

    public void setDuracao(int duracao) {
        Duracao = duracao;
    }

    public String toString()
    {
        return  "\nID: " + ID +
                "\nNome do Episodio: " + Nome +
                "\nTemporada: " + Temporada +
                "\nData de lançamento: " + DataLancamento +
                "\nDuração do Episodio:" + Duracao;

    }


    public byte[] toByteArray() throws IOException{
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(ID);
        dos.writeInt(IDserie);
        dos.writeUTF(Nome);
        dos.writeInt(Temporada);
        dos.writeInt((int) DataLancamento.toEpochDay());
        dos.writeInt(Duracao);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException{

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        ID = dis.readInt();
        IDserie = dis.readInt();
        Nome = dis.readUTF();
        Temporada = dis.readInt();
        DataLancamento = LocalDate.ofEpochDay(dis.readInt());
        Duracao = dis.readInt();
    }


}
