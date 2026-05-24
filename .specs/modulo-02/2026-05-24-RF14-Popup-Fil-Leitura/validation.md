# Protocolo de Validação - RF14

## Casos de Teste

1. **Validação de Restrição (RF14.5):**
    - Tentar enviar resenha com 15 caracteres. O botão "Enviar Resenha" deve permanecer desabilitado.
    - Enviar resenha com 25 caracteres. O botão deve habilitar.

2. **Validação de Fluxo de Moderação (RF14.6):**
    - Enviar resenha completa. Verificar no Supabase: a linha na tabela `resenhas` deve ter `status = 'PENDENTE'`.
    - Enviar apenas nota (sem texto). A resenha deve ser registrada e validada.

3. **Correção Visual (RF14.1, RF14.5):**
    - Confirmar se o título "SUA RESENHA" aparece acima do campo de texto, eliminando a "bizarrice" do layout anterior.