-- Migration: Remove campo chave_publica da tabela certificado
-- A chave pública não precisa mais ser armazenada no banco de dados

-- Remove a coluna chave_publica
ALTER TABLE certificado
DROP COLUMN IF EXISTS chave_publica;

-- Mensagem de confirmação
DO $$
BEGIN
    RAISE NOTICE 'Migration V5 concluída com sucesso!';
    RAISE NOTICE 'Campo removido da tabela certificado:';
    RAISE NOTICE '- chave_publica (VARCHAR(500))';
    RAISE NOTICE '';
    RAISE NOTICE 'A chave pública será gerenciada em nível de aplicação.';
END $$;