# PROJECT.md — ForLibrary

## Visão Geral

**ForLibrary** é um aplicativo Android de biblioteca digital universitária desenvolvido para a Unifor (Universidade de Fortaleza). Permite que alunos acessem o acervo digital, leiam livros em PDF/ePub, submetam obras autorais e participem da comunidade literária. Administradores gerenciam o acervo, moderam conteúdo e acompanham métricas da plataforma.

## Contexto

- **Tipo:** Projeto acadêmico de faculdade (Disciplina de Engenharia de Software / Desenvolvimento Mobile)
- **Natureza:** Brownfield — app em desenvolvimento ativo, com código já existente e features parcialmente implementadas
- **Equipe:** Grupo de alunos da Unifor (LucasDCoelho + outros membros — Ricardo, Miguel, Tchê)
- **Repositório:** Branch principal `develop`; feature branch atual `lucasdev`

## Objetivos

1. Disponibilizar acervo digital para consulta e leitura por alunos da Unifor
2. Permitir submissão e curadoria de obras autorais de alunos
3. Gamificar o engajamento com leitura (pontos, níveis)
4. Dar ao administrador ferramentas de gestão (acervo, usuários, eventos, moderação)
5. Integrar com Supabase como BaaS para dados, autenticação e storage

## Escopo

| Área | Status Geral |
|---|---|
| Módulo 1 – Autenticação | ~85% implementado |
| Módulo 2 – Área Aluno | ~60% implementado (estrutura UI pronta, lógica parcial) |
| Módulo 3 – Área Admin | ~45% implementado (estrutura UI pronta, integrações pendentes) |

## Restrições e Decisões Arquiteturais Relevantes

- **Email restrito:** Apenas `@unifor.br` e `@edu.unifor.br` são aceitos no cadastro/login
- **ADR 007:** Telas de listagem com BottomBar (admin) não devem ter botão de voltar redundante (RF27.1)
- **Offline:** App deve ter comportamento limitado sem conexão (RNF10)
- **Armazenamento local:** Máximo 200MB (RNF08.2)
- **Android mínimo:** API 26 (Android 8.0) — RNF03.2

## Critérios de Aceitação do Projeto

- Todos os requisitos de Prioridade 1 implementados e funcionais
- Integração com Supabase (auth, DB, storage) funcionando em produção
- Fluxo Aluno (login → leitura → avaliação) completo end-to-end
- Fluxo Admin (login → gestão acervo → moderação) completo end-to-end
- RNF de performance: resposta < 3s, listagens < 2s
