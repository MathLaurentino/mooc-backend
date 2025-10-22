-- Adiciona campos de timestamp
ALTER TABLE solicitacao_certificado
ADD COLUMN criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN atualizado_em TIMESTAMP NULL;

-- Adiciona campo para motivo de reprovação
ALTER TABLE solicitacao_certificado
ADD COLUMN motivo_reprovacao TEXT NULL;

-- Adiciona comentários nas colunas
COMMENT ON COLUMN solicitacao_certificado.criado_em IS 'Data de criação da solicitação';
COMMENT ON COLUMN solicitacao_certificado.atualizado_em IS 'Data da última atualização';
COMMENT ON COLUMN solicitacao_certificado.motivo_reprovacao IS 'Motivo da reprovação';

-- Mensagem de confirmação
DO $$
BEGIN
    RAISE NOTICE 'Migration V4 concluída com sucesso!';
    RAISE NOTICE 'Campos adicionados à tabela solicitacao_certificado:';
    RAISE NOTICE '- criado_em (TIMESTAMP)';
    RAISE NOTICE '- atualizado_em (TIMESTAMP)';
    RAISE NOTICE '- motivo_reprovacao (TEXT)';
END $$;