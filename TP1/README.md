Trabalho prático - Algoritmos e Estruturas de Dados III


## Descrição
Nosso trabalho implementa um sistema para gerenciar séries e episódios, com operações completas de CRUD. Utilizamos uma classe genérica de CRUD, Tabela Hash Extensível como índice direto e Árvore B+ como índice indireto.

## Visão Geral

Este projeto implementa um sistema de gerenciamento de Séries e Episódios com as seguintes funcionalidades:

- Operações completas de CRUD para séries e episódios.
- Relacionamento 1:N entre séries e episódios com uso de Árvore B+.
- Índices diretos com Tabela Hash Extensível.
- Armazenamento físico dos dados em arquivos binários.
- Visualização de episódios por temporada.
- Interface de menu para interação com o sistema.

## Desenvolvimento 
Implementamos quase todos os requisitos, mas ainda faltam validações importantes, como impedir a exclusão de séries com episódios vinculados. A parte mais desafiadora foi integrar corretamente os índices com os dados. No geral, foi uma experiência produtiva e útil para aplicar conceitos de estrutura de dados em um projeto mais completo.

## Integrantes:

- Enzo
- Gabriel Xavier
- Pedro Tinoco
- Vitor de Meira

## Estrutura de Pacotes e Classes

### 📁 `Entidades/`

#### `Serie.java`
Define a entidade **Série**.
- Atributos: `id`, `nome`, `genero`, `classificacao`, `anoLancamento`.
- Métodos principais:
  - `toByteArray()`: serializa a série.
  - `fromByteArray(byte[])`: desserializa a série.
  - `toString()`: retorna uma representação textual.

#### `Episodio.java`
Define a entidade **Episódio**.
- Atributos: `id`, `nome`, `temporada`, `numero`, `idSerie`.
- Métodos principais:
  - `toByteArray()` e `fromByteArray()`: serialização e desserialização.
  - `toString()`: representação textual.

---

### 📁 `Arquivos/`

#### `ArquivoSerie.java`
Gerencia o armazenamento de séries.
- Métodos principais: `incluir()`, `buscar()`, `alterar()`, `excluir()`.

#### `ArquivoEpisodio.java`
Semelhante ao `ArquivoSerie`, mas para episódios.
- Controla episódios associados a uma série.

---

### 📁 `Hash/`

#### `HashExtensivel.java`
Implementa índice direto usando **Tabela Hash Extensível**.
- Métodos principais:
  - `create()`: cria índice.
  - `read()`, `update()`, `delete()`: operações com base em ID.

#### `ParIDEndereco.java`
Armazena um par `(ID, Endereço)` para ser usado como elemento da hash.

---

### 📁 `Arvore/`

#### `ArvoreBMais.java`
Implementa a **Árvore B+** usada como índice indireto.
- Métodos principais:
  - `inserir()`, `buscar()`, `remover()`: gerenciam pares ordenados.

#### `ParIdId.java`
Representa um par `(idSerie, idEpisodio)` usado para mapear relacionamentos 1:N.

#### `ParNomeId.java`
Representa um par `(nome, id)` útil para buscas textuais (opcional).

---

### 📁 `Menus/`

#### `MenuEpisodios.java`
Interface de interação com o usuário para gerenciar episódios:
- Exibe opções como: adicionar, alterar, excluir e listar episódios de uma série.

---

### 📁 `TP1/`

#### `Main.java`
Classe principal que executa o sistema.
- Inicializa menus e chama os controles de séries e episódios.

---

## Observações

- O projeto utiliza serialização binária para armazenamento.
- O controle de exclusão de séries com episódios vinculados **ainda não foi implementado**.
- É possível visualizar os episódios por temporada.
- A criação de episódios ainda **não verifica se a série existe** (a ser corrigido).

---

## Dados Persistentes

Estão localizados na pasta `dados/`, com arquivos:
- `.db` – dados principais.
- `.c.db` – controle de registros.
- `.d.db` – registros deletados.
- `indiceIndireto.db` – estrutura da Árvore B+.

---

## Status dos Requisitos

✅ CRUD de séries e episódios  
✅ Índices diretos e indiretos  
✅ Relacionamento 1:N com Árvore B+  
✅ Visualização por temporada  
❌ Exclusão segura de séries  
❌ Validação da existência da série ao criar episódio 

## Questionário:

- Sim
As operações de inclusão, busca, alteração e exclusão de séries estão implementadas e funcionando corretamente?
- Sim
As operações de inclusão, busca, alteração e exclusão de episódios, por série, estão implementadas e funcionando corretamente?
- Sim
Essas operações usam a classe CRUD genérica para a construção do arquivo e as classes Tabela Hash Extensível e Árvore B+ como índices diretos e indiretos? 
- Sim
O atributo de ID de série, como chave estrangeira, foi criado na classe de episódios?
- Sim
Há uma árvore B+ que registre o relacionamento 1:N entre episódios e séries?
- Sim
Há uma visualização das séries que mostre os episódios por temporada?
- Não
A remoção de séries checa se há algum episódio vinculado a ela?
- Não
A inclusão da série em um episódio se limita às séries existentes?
- Não
O trabalho está funcionando corretamente?
- Sim/Não, Funcionando corretamente infere TODAS as especificações implementadas e funcionando, não é o caso aqui.
O trabalho está completo?
- Sim
O trabalho é original e não a cópia de um trabalho de outro grupo?
- Sim