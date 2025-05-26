# Trabalho Prático AEDS III – TP2

**Integrantes**  
- Enzo  
- Gabriel Xavier  
- Pedro Tinoco  
- Vitor de Meira  


**Link do TP1**  
[Repositório TP1](../TP1)

## O que o trabalho faz?  
Neste segundo trabalho prático da disciplina de AEDS III, estendemos o sistema PUCFlix para implementar um relacionamento N:N entre Séries e Atores.  

- CRUD completo da entidade **Ator** (inclusão, busca, alteração e exclusão).  
- Relacionamento N:N via **árvores B+**:
  - Uma árvore indexa o par *(idSerie, idAtor)*.
  - Outra árvore indexa o par *(idAtor, idSerie)*.
- Regras de negócio para manter a consistência:
  - Impede exclusão de um ator que ainda esteja vinculado a séries.
  - Ao remover uma série, todos os vínculos de atores são limpos automaticamente.  
- Interface atualizada com menus específicos para Atores e opções de vincular/desvincular no menu de Séries.  
- Padrão **MVC** (Modelo–Visão–Controle) para clareza de responsabilidades.

## Classes e principais métodos

### AtorDAO / AtoresBPlus  
Responsáveis pelo CRUD de atores:
- `incluir(Ator ator)`
- `buscar(int id)`
- `alterar(Ator ator)`
- `excluir(int id)` — com verificação de vínculos

### SerieAtoresBPlus  
Gerencia o relacionamento N:N:
- `vincularAtorSerie(int idAtor, int idSerie)`
- `desvincularAtorSerie(int idAtor, int idSerie)`
- `listarAtoresPorSerie(int idSerie)`
- `listarSeriesPorAtor(int idAtor)`
- `removerVinculosPorSerie(int idSerie)`
- `removerVinculosPorAtor(int idAtor)`

### Outras  
- **ControleAtores**, **ControleSeries**, **VisaoAtores**, **VisaoSeries**: controladores e visões para lidar com menus e ações do usuário.  
- Ajustes em **SerieDAO** e **EpisodioDAO** para exibir vínculos de atores.

## Experiência  
Desafio principal: manter duas árvores B+ sincronizadas para representar um único relacionamento.  
Implementamos checagens de integridade e utilizamos testes manuais e unitários para garantir que não haja dados órfãos nem inconsistências.

## Checklist (respostas)

- As operações de inclusão, busca, alteração e exclusão de atores estão implementadas e funcionando corretamente?  
  **SIM** – validado em `AtorDAO` e `AtoresBPlus`.

- O relacionamento entre séries e atores foi implementado com árvores B+ e assegura consistência?  
  **SIM** – via `SerieAtoresBPlus`.

- É possível consultar os atores de uma série?  
  **SIM** – `listarAtoresPorSerie(serieId)`.

- É possível consultar as séries de um ator?  
  **SIM** – `listarSeriesPorAtor(atorId)`.

- A remoção de séries elimina seus vínculos de atores?  
  **SIM** – `removerVinculosPorSerie`.

- A inclusão de um ator em uma série aceita apenas atores existentes?  
  **SIM** – verificação prévia no B+ antes de vincular.

- A remoção de um ator checa se há séries vinculadas?  
  **SIM** – bloqueia exclusão se houver vínculos ativos.

- O trabalho está funcionando corretamente?  
  **SIM**

- O trabalho está completo?  
  **SIM**

- O trabalho é original e não cópia de outro grupo?  
  **SIM**

---

Com o TP2 concluído, reforçamos nosso domínio de árvores B+ e do padrão MVC, bem como de práticas de integridade referencial em sistemas de informação.