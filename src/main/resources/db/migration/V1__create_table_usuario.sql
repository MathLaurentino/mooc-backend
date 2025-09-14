-- V1__create_table_usuario.sql
CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para melhorar performance
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_usuario_cpf ON usuario(cpf);

-- Comentários na tabela
COMMENT ON TABLE usuario IS 'Tabela base de usuários do sistema MOOC IFPR';
COMMENT ON COLUMN usuario.id IS 'Identificador único do usuário';
COMMENT ON COLUMN usuario.nome_completo IS 'Nome completo do usuário';
COMMENT ON COLUMN usuario.cpf IS 'CPF do usuário (apenas números)';
COMMENT ON COLUMN usuario.email IS 'Email único do usuário';
COMMENT ON COLUMN usuario.senha IS 'Senha criptografada do usuário';
COMMENT ON COLUMN usuario.ativo IS 'Indica se o usuário está ativo no sistema';
COMMENT ON COLUMN usuario.data_criacao IS 'Data e hora de criação do registro';
COMMENT ON COLUMN usuario.data_atualizacao IS 'Data e hora da última atualização';