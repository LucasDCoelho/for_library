# TESTING.md — Testes do ForLibrary

> Última sincronização com o código: 2026-05-29 (branch `lucasdev`).

## Estado atual: **sem cobertura real de testes**

O projeto contém apenas os arquivos gerados pelo template do Android Studio:

| Arquivo | Tipo | Conteúdo |
|---|---|---|
| `app/src/test/.../ExampleUnitTest.kt` | Unit (JVM) | `assertEquals(4, 2 + 2)` |
| `app/src/androidTest/.../ExampleInstrumentedTest.kt` | Instrumentado | verifica o `applicationId` do contexto |

Nenhum ViewModel, regra de negócio (validação de e-mail, gamificação, parsing de datas) ou fluxo de UI possui testes.

## Dependências de teste disponíveis (`app/build.gradle.kts`)

| Dependência | Escopo |
|---|---|
| `junit` (4.13.2) | `testImplementation` |
| `androidx.test.ext:junit` (1.1.5) | `androidTestImplementation` |
| `androidx.test.espresso:espresso-core` (3.5.1) | `androidTestImplementation` |

Não há mocking (MockK/Mockito), nem `kotlinx-coroutines-test`, nem `compose-ui-test` configurados.

## Como rodar

```
./gradlew test                 # unit tests (JVM)
./gradlew connectedAndroidTest # instrumentados (requer device/emulador)
```

## Verificação atual = manual

A validação hoje é feita rodando o app contra o Supabase real. Riscos disso estão em `CONCERNS.md`.

## Lacunas prioritárias (sugestão)

1. **ViewModels com lógica pura testável sem rede:** `AuthUtils` (validação de domínio / `traduzirErroAuth`), `EventosViewModel.parseMesDia/mapTipo`, `LivroAcervo.isNovo`.
2. **Adicionar** `kotlinx-coroutines-test` + MockK para testar ViewModels que dependem do `supabase` global (hoje difícil de mockar por ser singleton de topo — ver débito "sem Repository").
3. **UI**: smoke tests de navegação no `NavHost` com `compose-ui-test`.
