# Trabalho Prático AEDS III – TP3

**Integrantes**  
- Enzo  
- Gabriel Xavier  
- Pedro Tinoco  
- Vitor de Meira  

**Link do TP2**  
[Repositório TP2](../TP2)

## O que o trabalho faz?  
Neste terceiro trabalho prático da disciplina de AEDS III, implementamos índices invertidos para termos nos títulos de séries, episódios e nomes de atores, usando uma classe genérica de lista invertida. Permite buscas rápidas por palavras-chave e persistência dos índices em disco.

- Índice invertido genérico via classe **ListaInvertida**.  
- Três instâncias para:
  - títulos de séries  
  - títulos de episódios  
  - nomes de atores  
- Métodos de construção, busca e persistência dos índices.  
- Interface de terminal para consultas e exibição de resultados.  
- Segue o padrão **MVC** (Modelo–Visão–Controle).

## Classes e principais métodos

### ListaInvertida<T>  
- inserir(String termo, int idRegistro)  
- buscar(String termo): List<Integer>  
- salvar(String caminhoArquivo)  
- carregar(String caminhoArquivo)  

### Índices específicos  
- **IndiceSeries** (ListaInvertida para títulos de séries)  
- **IndiceEpisodios** (ListaInvertida para títulos de episódios)  
- **IndiceAtores** (ListaInvertida para nomes de atores)  

Principais utilitários:  
- construirIndiceSeries(List<Serie> series)  
- construirIndiceEpisodios(List<Episodio> episodios)  
- construirIndiceAtores(List<Ator> atores)  
- buscarSeriesPorTermo(String termo): List<Serie>  
- buscarEpisodiosPorTermo(String termo): List<Episodio>  
- buscarAtoresPorTermo(String termo): List<Ator>  

## Experiência  
Tratamos tokenização (split em espaços e pontuação) e normalização de termos. O desafio foi garantir eficiência e consistência na serialização e busca dos índices.

## Checklist (respostas)

- O índice invertido com os termos dos títulos das séries foi criado usando a classe `ListaInvertida`?  
  **SIM**

- O índice invertido com os termos dos títulos dos episódios foi criado usando a classe `ListaInvertida`?  
  **SIM**

- O índice invertido com os termos dos nomes dos atores foi criado usando a classe `ListaInvertida`?  
  **SIM**

- É possível buscar séries por palavras usando o índice invertido?  
  **SIM** – via `buscarSeriesPorTermo`

- É possível buscar episódios por palavras usando o índice invertido?  
  **SIM** – via `buscarEpisodiosPorTermo`

- É possível buscar atores por palavras usando o índice invertido?  
  **SIM** – via `buscarAtoresPorTermo`

- O trabalho está completo?  
  **SIM**

- O trabalho é original e não a cópia de um trabalho de um colega?  
  **SIM**

---

Com o TP3 concluído, reforçamos nosso domínio de índices invertidos, persistência de estruturas e buscas eficientes por termos.