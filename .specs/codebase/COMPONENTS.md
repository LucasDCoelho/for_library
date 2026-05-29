# COMPONENTS.md — Componentes Compartilhados do ForLibrary

> Última sincronização com o código: 2026-05-29 (branch `lucasdev`).

Fonte: `core/components/` (compartilhados) + alguns Composables auxiliares por feature.

## CapaLivro

**Arquivo:** `core/components/CapaLivro.kt`

Composable para exibir a capa de um livro com três estados:

| Estado | Comportamento |
|---|---|
| Carregando | Retângulo cinza como placeholder |
| ISBN válido | Imagem carregada via OpenLibrary (`covers.openlibrary.org/b/isbn/{ISBN}-L.jpg`) |
| Sem URL / falha | Iniciais do título sobre fundo colorido (`coresFallback`: azul, índigo, roxo, verde, oliva) |

**Props principais:**
```kotlin
@Composable
fun CapaLivro(
    capaUrl: String?,
    titulo: String,
    modifier: Modifier = Modifier
)
```

**Usado em:** TelaAcervoDigital, TelaHomeAluno, TelaGestaoAcervo, TelaDetalhesLivro, TelaEstante, TelaAnaliseObra

---

## PopupLogout

**Arquivo:** `core/components/PopupLogout.kt`

Dialog de confirmação de logout (RF25).

```kotlin
@Composable
fun PopupLogout(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
)
```

Exibe: título, texto explicativo, botões "Cancelar" e "Sim, Sair".  
**Invocado de:** TelaPerfil (RF19.4) e TelaConfiguracoes (RF24.4)

---

## FiltroAvancadoBottomSheet

**Arquivo:** `core/components/FiltroAvancado.kt`

Bottom Sheet para filtros de busca do acervo (RF07).

**Seções:**
- Chips selecionáveis de gênero literário
- Radio buttons de ordenação via enum `OrdemFiltro` (`A_Z`, `Z_A`, `MAIS_RECENTES`, `MELHOR_AVALIADOS`)
- Botões "Limpar" e "Aplicar Filtros"

**Estado:** `FiltroAvancadoState(generosSelecionados, ordem)` — consumido pelo `AcervoViewModel` para montar a query no Supabase.

**Forma do topo:** `RoundedCornerShape(topStart = 20dp, topEnd = 20dp)`

---

## ForLibraryBottomBar

**Arquivo:** `core/navigation/ForLibraryBottomBar.kt`

BottomBar do contexto Aluno.

| Item | Ícone | Rota |
|---|---|---|
| Home | `Home` | `Rota.HomeAluno` |
| Acervo | `MenuBook` | `Rota.Acervo` |
| Estante | `Bookmarks` | `Rota.Estante` |
| Eventos | `Event` | `Rota.Eventos` |
| Perfil | `Person` | `Rota.Perfil` |

---

## AdminBottomBar

**Arquivo:** `core/navigation/AdminBottomBar.kt`

BottomBar do contexto Admin.

| Item | Ícone | Rota |
|---|---|---|
| Dashboard | `Dashboard` | `Rota.DashboardAdmin` |
| Acervo | `MenuBook` | `Rota.AcervoAdmin` |
| Moderação | `Shield` | `Rota.ListaModeracao` |
| Eventos | `Event` | `Rota.EventosAdmin` |

**Visual de ambas as BottomBars:**
- Fundo: branco
- Item selecionado: `#1565C0`
- Item não selecionado: `#9E9E9E`
- Indicador de seleção: `#E3EEF9`
- Label: `10sp`

---

## Componentes auxiliares por feature (não em `core/`)

| Componente | Arquivo | Função |
|---|---|---|
| `BuscaVaziaPlaceholder` | `aluno/acervo/ui/BuscaVaziaPlaceholder.kt` | Estado vazio da busca do acervo |
| `ChatBotAluno` | `aluno/home/ui/ChatBotAluno.kt` | UI do chat (bolhas + indicador "digitando") — respostas locais, ver `INTEGRATIONS.md` |
| `TelaFimLeitura` | `aluno/livro/ui/TelaFimLeitura.kt` | Tela/overlay de conclusão de leitura |
| `TelaConfirmacaoLogout` | `aluno/perfil/ui/TelaConfirmacaoLogout.kt` | Variante de confirmação de logout (coexiste com `PopupLogout`) |
| `PopupDetalhesUsuario` | `adm/moderacao/PopupDetalhesUsuario.kt` | Dialog de detalhes de usuário na gestão admin |

## Padrões de Header (não componentizado, mas recorrente)

```kotlin
Row(
    modifier = Modifier.fillMaxWidth().padding(16.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    // Avatar circular (foto de perfil ou ícone)
    // Texto do título da tela
    // Spacer(Modifier.weight(1f))
    // IconButton (sino de notificações ou engrenagem)
}
```

Presente em: TelaHomeAluno, TelaAcervoDigital, TelaEventos, TelaDashboardAdmin.
