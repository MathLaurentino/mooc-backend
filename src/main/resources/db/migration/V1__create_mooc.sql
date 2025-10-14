]-- Tabela: usuario
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome_completo VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    email VARCHAR(70) NOT NULL UNIQUE,
    cpf CHAR(11) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario ENUM('ALUNO', 'ADMINISTRADOR') NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela: campus
CREATE TABLE campus (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela: area_conhecimento
CREATE TABLE area_conhecimento (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela: curso
CREATE TABLE curso (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    area_conhecimento_id INT NOT NULL,
    campus_id INT NOT NULL,
    nome_professor VARCHAR(100) NOT NULL,
    miniatura VARCHAR(255) NOT NULL,
    carga_horaria INTEGER NOT NULL,
    visivel BOOLEAN NOT NULL DEFAULT FALSE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (area_conhecimento_id) REFERENCES area_conhecimento(id),
    FOREIGN KEY (campus_id) REFERENCES campus(id)
);

-- Tabela: aula
CREATE TABLE aula (
    id SERIAL PRIMARY KEY,
    curso_id INT NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    miniatura VARCHAR(255),
    url_video VARCHAR(255) NOT NULL,
    ordem_aula INT NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (curso_id) REFERENCES curso(id)
);

-- Tabela: inscricao
CREATE TABLE inscricao (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL,
    curso_id INT NOT NULL,
    concluido BOOLEAN DEFAULT FALSE,
    data_inscricao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_conclusao TIMESTAMP NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (curso_id) REFERENCES curso(id)
);

-- Tabela: progresso_aula
CREATE TABLE progresso_aula (
    id SERIAL PRIMARY KEY,
    inscricao_id INT NOT NULL,
    aula_id INT NOT NULL,
    concluido BOOLEAN DEFAULT FALSE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (inscricao_id) REFERENCES inscricao(id),
    FOREIGN KEY (aula_id) REFERENCES aula(id)
);

-- Tabela: solicitacao_certificado
CREATE TABLE solicitacao_certificado (
    id VARCHAR(36) PRIMARY KEY,
    inscricao_id INT NOT NULL,
    status ENUM('analise', 'aprovado', 'reprovado') NOT NULL,
    FOREIGN KEY (inscricao_id) REFERENCES inscricao(id)
);

-- Tabela: certificado
CREATE TABLE certificado (
    id SERIAL PRIMARY KEY,
    inscricao_id INT NOT NULL,
    nome_aluno VARCHAR(255) NOT NULL,
    cpf_aluno CHAR(11) NOT NULL,
    nome_curso VARCHAR(255) NOT NULL,
    carga_horaria TEXT NOT NULL,
    nome_campus VARCHAR(255) NOT NULL,
    data_conclusao TIMESTAMP NOT NULL,
    hash_documento VARCHAR(500) NOT NULL,
    assinatura_digital VARCHAR(500) NOT NULL,
    chave_publica VARCHAR(500) NOT NULL,
    FOREIGN KEY (inscricao_id) REFERENCES inscricao(id)
);

-- Índices para melhorar a performance
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_usuario_cpf ON usuario(cpf);
CREATE INDEX idx_curso_visivel ON curso(visivel);
CREATE INDEX idx_aula_curso ON aula(curso_id);
CREATE INDEX idx_inscricao_usuario ON inscricao(usuario_id);
CREATE INDEX idx_inscricao_curso ON inscricao(curso_id);
CREATE INDEX idx_progresso_inscricao ON progresso_aula(inscricao_id);
CREATE INDEX idx_progresso_aula ON progresso_aula(aula_id);