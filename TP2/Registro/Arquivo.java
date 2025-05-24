package Registro;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import Hash.*;

public class Arquivo<T extends Registro> {
    final int TAM_CABECALHO = 12;
    RandomAccessFile arquivo;
    String nomeArquivo;
    Constructor<T> construtor;
    HashExtensivel<ParIDEndereco> indiceDireto;

    public Arquivo(String na, Constructor<T> c) throws Exception {
        File d = new File(".\\dados");
        if(!d.exists())
            d.mkdir();

        d = new File(".\\dados\\"+na);
        if(!d.exists())
            d.mkdir();

        this.nomeArquivo = ".\\dados\\"+na+"\\"+na+".db";
        this.construtor = c;
        arquivo = new RandomAccessFile(this.nomeArquivo, "rw");
        if(arquivo.length()<TAM_CABECALHO) {
            arquivo.writeInt(0);   
            arquivo.writeLong(-1);    
        }

        indiceDireto = new HashExtensivel<>(
            ParIDEndereco.class.getConstructor(), 
            4, 
            ".\\dados\\"+na+"\\"+na+".d.db", // diretório 
            ".\\dados\\"+na+"\\"+na+".c.db"  // cestos
        );
    }

    public int create(T obj) throws Exception {
        arquivo.seek(0);
        int proximoID = arquivo.readInt()+1;
        arquivo.seek(0);
        arquivo.writeInt(proximoID);
        obj.setId(proximoID);
        byte[] b = obj.toByteArray();

        long endereco = getDeleted(b.length);   
        if(endereco == -1) {   
            arquivo.seek(arquivo.length());
            endereco = arquivo.getFilePointer();
            arquivo.writeByte(' ');      
            arquivo.writeShort(b.length); 
            arquivo.write(b);             
        } else {
            arquivo.seek(endereco);
            arquivo.writeByte(' ');     
            arquivo.skipBytes(2);      
            arquivo.write(b);              
        }

        indiceDireto.create(new ParIDEndereco(proximoID, endereco));
        
        return obj.getId();
    }
    
    public T read(int id) throws Exception {
        T obj;
        short tam;
        byte[] b;
        byte lapide;

        ParIDEndereco pid = indiceDireto.read(id);
        if(pid!=null) {
            arquivo.seek(pid.getEndereco());
            obj = construtor.newInstance();
            lapide = arquivo.readByte();
            if(lapide==' ') {
                tam = arquivo.readShort();
                b = new byte[tam];
                arquivo.read(b);
                obj.fromByteArray(b);
                if(obj.getId()==id)
                    return obj;
            }
        }
        return null;
    }

    public boolean delete(int id) throws Exception {
        T obj;
        short tam;
        byte[] b;
        byte lapide;

        ParIDEndereco pie = indiceDireto.read(id);
        if(pie!=null) {
            arquivo.seek(pie.getEndereco());
            obj = construtor.newInstance();
            lapide = arquivo.readByte();
            if(lapide==' ') {
                tam = arquivo.readShort();
                b = new byte[tam];
                arquivo.read(b);
                obj.fromByteArray(b);
                if(obj.getId()==id) {
                    if(indiceDireto.delete(id)) {
                        arquivo.seek(pie.getEndereco());
                        arquivo.write('*');
                        addDeleted(tam, pie.getEndereco());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean update(T novoObj) throws Exception {
        T obj;
        short tam;
        byte[] b;
        byte lapide;
        ParIDEndereco pie = indiceDireto.read(novoObj.getId());
        if(pie!=null) {
            arquivo.seek(pie.getEndereco());
            obj = construtor.newInstance();
            lapide = arquivo.readByte();
            if(lapide==' ') {
                tam = arquivo.readShort();
                b = new byte[tam];
                arquivo.read(b);
                obj.fromByteArray(b);
                if(obj.getId()==novoObj.getId()) {

                    byte[] b2 = novoObj.toByteArray();
                    short tam2 = (short)b2.length;

                
                    if(tam2 <= tam) {
                        arquivo.seek(pie.getEndereco()+3);
                        arquivo.write(b2);
                    }

                    
                    else {
                        arquivo.seek(pie.getEndereco());
                        arquivo.write('*');
                        addDeleted(tam, pie.getEndereco());                        

                        
                        long novoEndereco = getDeleted(b.length);   
                        if(novoEndereco == -1) {     
                            arquivo.seek(arquivo.length());
                            novoEndereco = arquivo.getFilePointer();
                            arquivo.writeByte(' ');       
                            arquivo.writeShort(tam2);       
                            arquivo.write(b2);              
                        } else {
                            arquivo.seek(novoEndereco);
                            arquivo.writeByte(' ');       
                            arquivo.skipBytes(2);
                            arquivo.write(b2);              
                        }

                    
                        indiceDireto.update(new ParIDEndereco(novoObj.getId(), novoEndereco));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void addDeleted(int tamanhoEspaco, long enderecoEspaco) throws Exception {
        long anterior = 4; 
        arquivo.seek(anterior);
        long endereco = arquivo.readLong(); 
        long proximo = -1; 
        int tamanho;
        if(endereco==-1) {  
            arquivo.seek(4);
            arquivo.writeLong(enderecoEspaco);
            arquivo.seek(enderecoEspaco+3);
            arquivo.writeLong(-1);
        } else {
            do {
                arquivo.seek(endereco+1);
                tamanho = arquivo.readShort();
                proximo = arquivo.readLong();
                if(tamanho > tamanhoEspaco) {  
                    if(anterior == 4) 
                        arquivo.seek(anterior);
                    else
                        arquivo.seek(anterior+3);
                    arquivo.writeLong(enderecoEspaco);
                    arquivo.seek(enderecoEspaco+3);
                    arquivo.writeLong(endereco);
                    break;
                }
                if(proximo == -1) {  
                    arquivo.seek(endereco+3);
                    arquivo.writeLong(enderecoEspaco);
                    arquivo.seek(enderecoEspaco+3);
                    arquivo.writeLong(+1);
                    break;
                }
                anterior = endereco;
                endereco = proximo;
            } while (endereco!=-1);
        }
    }
    
    public long getDeleted(int tamanhoNecessario) throws Exception {
        long anterior = 4; 
        arquivo.seek(anterior);
        long endereco = arquivo.readLong(); 
        long proximo = -1; 
        int tamanho;
        while(endereco != -1) {
            arquivo.seek(endereco+1);
            tamanho = arquivo.readShort();
            proximo = arquivo.readLong();
            if(tamanho > tamanhoNecessario) {  
                if(anterior == 4)   
                    arquivo.seek(anterior);
                else
                    arquivo.seek(anterior+3);
                arquivo.writeLong(proximo);
                break;
            }
            anterior = endereco;
            endereco = proximo;
        }
        return endereco;
    }

    public T[] readAll() throws Exception {
        List<T> registros = new ArrayList<>();
        arquivo.seek(TAM_CABECALHO); // Pula o cabeçalho do arquivo

        while (arquivo.getFilePointer() < arquivo.length()) {
            byte lapide = arquivo.readByte(); // Lê a lápide
            short tamanho = arquivo.readShort(); // Lê o tamanho do registro
            byte[] dados = new byte[tamanho];
            arquivo.read(dados); // Lê os dados do registro

            if (lapide == ' ') { // Verifica se o registro não está excluído
                T obj = construtor.newInstance();
                obj.fromByteArray(dados); // Converte os bytes para o objeto
                registros.add(obj);
            }
        }

        // Converte a lista para um array
        @SuppressWarnings("unchecked")
        T[] arrayRegistros = (T[]) java.lang.reflect.Array.newInstance(construtor.getDeclaringClass(), registros.size());
        return registros.toArray(arrayRegistros);
    }

    public void close() throws Exception {
        arquivo.close();
        indiceDireto.close();
    }

}