# Instruções para o candidato

Você recebeu um app de **catálogo de filmes** em Clean Architecture. As camadas
**data** e **domain** já estão prontas, e a **tela (Compose)** também. Falta o cérebro
da tela: o **`MoviesViewModel`**.

## Seu objetivo

Implementar `presentation/MoviesViewModel.kt` até que **todos os testes passem**:

```bash
./gradlew :app:testDebugUnitTest
```

Comece rodando os testes e lendo as falhas — cada teste descreve um comportamento
esperado. **Não altere os testes** nem as outras camadas (combine com o entrevistador
se achar necessário).

## O que o ViewModel precisa fazer

Você tem 4 use cases injetados no construtor:

- `getPopularMovies()` → `List<Movie>` (suspend)
- `searchMovies(query)` → `List<Movie>` (suspend)
- `observeFavoriteIds()` → `Flow<Set<Int>>` (ids dos favoritos, em tempo real)
- `toggleFavorite(movieId)` (suspend)

E precisa expor um `StateFlow<MoviesUiState>` para a tela. Requisitos:

1. **Carga inicial:** ao criar o ViewModel, carregar os filmes populares. Enquanto
   carrega, `isLoading = true`.
2. **Mapeamento domain → UI:** converter `Movie` em `MovieUi`:
   - `year`: String (`1999` → `"1999"`);
   - `rating`: String com **uma casa decimal** (`8.7` → `"8.7"`);
   - `isFavorite`: derivado de `observeFavoriteIds()`. **Favoritar um filme deve refletir
     na lista atual** (o item vira favorito sem recarregar da API).
3. **Busca:** `onQueryChange(query)` atualiza a `query` no estado e busca via
   `searchMovies`. Query em branco → volta para os populares.
4. **Erro:** se um use case lançar exceção, expor `errorMessage` e sair do loading (sem
   crashar). `retry()` repete a última operação.
5. **Favoritar:** `onToggleFavorite(id)` usa o `toggleFavorite`.

## Dicas

- Pense em como a lista de filmes e o fluxo de favoritos se **combinam** para produzir a
  lista final de `MovieUi`.
- Use `viewModelScope` para as coroutines.
- Rode o app no emulador pra ver sua implementação funcionando de verdade (busca no topo,
  coração pra favoritar, spinner de loading, botão de tentar novamente no erro).
- Digite `erro` na busca pra disparar uma falha simulada e testar seu tratamento.

## O que avaliamos

Raciocínio e clareza ao explicar; modelagem do estado; uso correto de coroutines/flows;
tratamento de erro; e como você chega da falha ao verde (testar hipóteses, não chutar).
Pode perguntar à vontade.
