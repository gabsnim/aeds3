package Entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import Registro.*;

public class Serie implements Registro {
    
    protected int Id;
    protected String Nome;
    protected LocalDate AnoLancamento;
    protected String Sinopse;
    protected String Streaming;


    //construtor FULL
    public Serie(int i, String n, LocalDate a, String si, String st)
    {
        Id = i;
        Nome = n;
        AnoLancamento = a;
        Sinopse = si;
        Streaming = st;
    }

    //construtor Vazio
    public Serie()
    {
        Id = -1;
        Nome = "";
        AnoLancamento = LocalDate.now();
        Sinopse = "";
        Streaming = "";
    }

    //construtor missing ID
    public Serie(String n, LocalDate a, String si, String st)
    {
        Id = -1;
        Nome = n;
        AnoLancamento = a;
        Sinopse = si;
        Streaming = st;

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public LocalDate getAnoLancamento() {
        return AnoLancamento;
    }

    public void setAnoLancamento(LocalDate anoLancamento) {
        AnoLancamento = anoLancamento;
    }

    public String getSinopse() {
        return Sinopse;
    }

    public void setSinopse(String sinopse) {
        Sinopse = sinopse;
    }

    public String getStreaming() {
        return Streaming;
    }

    public void setStreaming(String streaming) {
        Streaming = streaming;
    }

    public String toString()
    {
        return  "\nID: " + Id +
                "\nNome: " + Nome +
                "\nAno de Lançamento: " + AnoLancamento +
                "\nSinopse: " + Sinopse +
                "\nStreaming: " + Streaming;

    }

    public byte[] toByteArray() throws IOException { 

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(Id);
        dos.writeUTF(Nome);
        dos.writeInt( (int) this.AnoLancamento.toEpochDay());
        dos.writeUTF(Sinopse);
        dos.writeUTF(Streaming);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException{

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        Id = dis.readInt();
        Nome = dis.readUTF();
        AnoLancamento = LocalDate.ofEpochDay(dis.readInt());
        Sinopse = dis.readUTF();
        Streaming = dis.readUTF();
    }


}
