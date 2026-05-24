# Plano de Validação: RF35

## Critérios de Aceitação
- [ ] **Aprovação:** Ao clicar em "Aprovar", status deve mudar para `APROVADA` e pontos ser creditados ao aluno via `HistoricoPontos`.
- [ ] **Rejeição:** Ao clicar em "Rejeitar", status deve mudar para `REJEITADA`, salvando o motivo da rejeição[cite: 6].
- [ ] **Navegação:** O botão de voltar deve retornar à `Tela de Moderação de Resenhas` (RF34)[cite: 8].
- [ ] **UI:** O lembrete em azul deve ser visível conforme RNF04 (usabilidade)[cite: 8].

## Portões de Qualidade (Quality Gates)
- **Gate 1:** A UI não deve hardcodar cores fora do `Color.kt`[cite: 5].
- **Gate 2:** A lógica de atualização de status deve ser assíncrona, usando `Supabase Client`[cite: 3].
- **Gate 3:** O campo "Motivo da Rejeição" só deve ser enviado se a resenha for rejeitada.