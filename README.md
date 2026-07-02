# Movies — Entrevista Android (Live Coding)

App Android de **catálogo de filmes** com busca e favoritos, em **Clean Architecture**
(data → domain → presentation), Jetpack Compose e Coroutines/Flow.

As camadas **data**, **domain** e a **UI (Compose)** já estão prontas e funcionais.
O que falta é a implementação do **`MoviesViewModel`** — a ponte entre os use cases e a
tela. A entrevista termina quando **todos os testes passarem**.

## Pré-requisitos

- Android Studio (Ladybug ou mais recente) **ou** JDK 17 + Android SDK 34.
- Não precisa de internet: os dados vêm de uma "API" fake em memória.

## Como rodar

```bash
# Testes (é o que precisa ficar verde)
./gradlew :app:testDebugUnitTest

# Rodar o app num emulador/dispositivo
./gradlew :app:installDebug
```

### Rodando no Android Studio

1. **File → Open** e selecione a **pasta raiz** do projeto (a que tem o `settings.gradle.kts`),
   não um arquivo nem a pasta `app`.
2. Confie no projeto quando ele perguntar e **espere o Gradle Sync terminar** (barra de
   progresso embaixo). O ▶ só aparece depois de um sync bem-sucedido.
3. Se o ▶ não aparecer ou não reconhecer como app Android, force um sync:
   **File → Sync Project with Gradle Files** (ícone do elefante do Gradle na toolbar).
4. Confira o **Gradle JDK**: Settings → Build, Execution, Deployment → Build Tools →
   Gradle → **Gradle JDK = 17** (ou o JBR embutido do Studio).
5. Persistindo: **File → Invalidate Caches / Restart…**, reabra e deixe sincronizar.

> Já vem uma run configuration "app" pronta em `.run/app.run.xml`; após o sync ela (ou a
> criada automaticamente) fica disponível no seletor ao lado do ▶.

## Tarefa

Abra **[CANDIDATO.md](CANDIDATO.md)**. Resumo: implemente `MoviesViewModel`
(`app/src/main/java/com/example/movies/presentation/MoviesViewModel.kt`) para que os
testes em `MoviesViewModelTest` passem. Não altere os testes nem as outras camadas
(a menos que combine com o entrevistador).

> Entrevistador: gabarito e roteiro em **[ENTREVISTADOR.md](ENTREVISTADOR.md)**.

## Arquitetura

```
presentation/   Compose + ViewModel (VOCÊ implementa o ViewModel)
   MoviesScreen.kt        # tela pronta, consome MoviesUiState
   MoviesUiState.kt       # MoviesUiState + MovieUi (modelo de UI)
   MoviesViewModel.kt     # <<< só a estrutura; implemente aqui
domain/         Regras e contratos (pronto)
   model/Movie.kt
   repository/MovieRepository.kt
   usecase/MovieUseCases.kt   # GetPopular, Search, ToggleFavorite, ObserveFavoriteIds
data/           Fontes de dados (pronto)
   remote/FakeMovieApi.kt + MovieDto.kt   # "API" fake com latência/erro
   local/FavoritesLocalDataSource.kt      # favoritos em memória (Flow)
   MovieRepositoryImpl.kt                 # mapeia DTO -> domínio
di/ServiceLocator.kt   # DI manual (sem Hilt), monta o ViewModel
```

O fluxo de dados: `MoviesViewModel` chama use cases → use cases chamam `MovieRepository`
→ `MovieRepositoryImpl` usa a API fake + favoritos e converte `MovieDto` em `Movie`.
Você converte `Movie` em `MovieUi` e expõe `MoviesUiState` para a tela.
