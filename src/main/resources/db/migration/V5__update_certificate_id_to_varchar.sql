DROP TABLE IF EXISTS certificado CASCADE;

CREATE TABLE certificado (
    id VARCHAR(36) PRIMARY KEY,
    inscricao_id BIGINT NOT NULL,
    nome_aluno VARCHAR(255) NOT NULL,
    cpf_aluno VARCHAR(11) NOT NULL,
    nome_curso VARCHAR(255) NOT NULL,
    carga_horaria TEXT NOT NULL,
    nome_campus VARCHAR(255) NOT NULL,
    data_conclusao TIMESTAMP NOT NULL,
    hash_documento VARCHAR(500) NOT NULL,
    assinatura_digital VARCHAR(500) NOT NULL,
    chave_publica VARCHAR(500) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (inscricao_id) REFERENCES inscricao(id)
);

CREATE INDEX idx_certificado_inscricao ON certificado(inscricao_id);
CREATE INDEX idx_certificado_hash ON certificado(hash_documento);