-- ============================================
-- Migration V6: Limpar dados e adicionar cursos reais
-- ============================================

-- ============================================
-- 1. LIMPAR DADOS EXISTENTES
-- ============================================

-- Desabilitar verificação de chaves estrangeiras temporariamente
SET session_replication_role = 'replica';

-- Limpar tabelas na ordem correta (respeitando dependências)
TRUNCATE TABLE certificado CASCADE;
TRUNCATE TABLE solicitacao_certificado CASCADE;
TRUNCATE TABLE progresso_aula CASCADE;
TRUNCATE TABLE inscricao CASCADE;
TRUNCATE TABLE aula CASCADE;
TRUNCATE TABLE curso CASCADE;
TRUNCATE TABLE area_conhecimento CASCADE;
TRUNCATE TABLE campus CASCADE;

-- Reabilitar verificação de chaves estrangeiras
SET session_replication_role = 'origin';

-- Resetar sequences
ALTER SEQUENCE campus_id_seq RESTART WITH 1;
ALTER SEQUENCE area_conhecimento_id_seq RESTART WITH 1;
ALTER SEQUENCE curso_id_seq RESTART WITH 1;
ALTER SEQUENCE aula_id_seq RESTART WITH 1;

-- ============================================
-- 2. POPULAR TABELA CAMPUS
-- ============================================
INSERT INTO campus (nome, visivel, criado_em) VALUES
('IFPR - Campus Foz do Iguaçu', TRUE, CURRENT_TIMESTAMP),
('IFPR - Campus Curitiba', TRUE, CURRENT_TIMESTAMP),
('IFPR - Campus Londrina', TRUE, CURRENT_TIMESTAMP),
('IFPR - Campus Cascavel', TRUE, CURRENT_TIMESTAMP),
('IFPR - Campus Paranaguá', TRUE, CURRENT_TIMESTAMP);

-- ============================================
-- 3. POPULAR TABELA AREA_CONHECIMENTO
-- ============================================
INSERT INTO area_conhecimento (nome, visivel, criado_em) VALUES
('Tecnologia da Informação', TRUE, CURRENT_TIMESTAMP),
('Desenvolvimento Web', TRUE, CURRENT_TIMESTAMP),
('Banco de Dados', TRUE, CURRENT_TIMESTAMP),
('Programação', TRUE, CURRENT_TIMESTAMP),
('Ciência de Dados', TRUE, CURRENT_TIMESTAMP);

-- ============================================
-- 4. POPULAR TABELA CURSO (Cursos Reais do YouTube)
-- ============================================

-- Curso 1: Python do Zero - Curso Completo e Gratuito (Refatorando)
INSERT INTO curso (nome, descricao, area_conhecimento_id, campus_id, nome_professor, miniatura, carga_horaria, visivel, criado_em) VALUES
(
    'Python Completo - Do Básico ao Avançado',
    'Curso completo de Python abordando desde os fundamentos da linguagem até conceitos avançados. Aprenda programação orientada a objetos, estruturas de dados, manipulação de arquivos, tratamento de exceções e muito mais. Ideal para iniciantes e para quem quer aprofundar seus conhecimentos em Python.',
    4,
    1,
    'Prof. Vinícius Refatorando',
    NULL,
    40,
    TRUE,
    CURRENT_TIMESTAMP
);

-- Curso 2: JavaScript Completo (Curso em Vídeo)
INSERT INTO curso (nome, descricao, area_conhecimento_id, campus_id, nome_professor, miniatura, carga_horaria, visivel, criado_em) VALUES
(
    'JavaScript Completo - Fundamentos e Aplicações',
    'Aprenda JavaScript do zero! Este curso aborda desde os conceitos básicos até aplicações práticas. Você vai aprender sobre variáveis, estruturas de controle, funções, DOM, eventos, arrays, objetos e muito mais. Desenvolva sites interativos e dinâmicos com JavaScript.',
    2,
    1,
    'Prof. Gustavo Guanabara',
    NULL,
    50,
    TRUE,
    CURRENT_TIMESTAMP
);

-- Curso 3: SQL Completo - Do Básico ao Avançado (Boson Treinamentos)
INSERT INTO curso (nome, descricao, area_conhecimento_id, campus_id, nome_professor, miniatura, carga_horaria, visivel, criado_em) VALUES
(
    'MySQL - Banco de Dados Completo',
    'Curso completo de MySQL e Banco de Dados. Aprenda a criar e gerenciar bancos de dados relacionais, desde a modelagem até consultas avançadas. Aborda SQL (SELECT, INSERT, UPDATE, DELETE), JOINs, subconsultas, índices, views, stored procedures e otimização de consultas.',
    3,
    2,
    'Prof. Fábio dos Reis',
    NULL,
    45,
    TRUE,
    CURRENT_TIMESTAMP
);

-- ============================================
-- 5. POPULAR TABELA AULA
-- ============================================

-- ========================================
-- Aulas do Curso 1: Python Completo
-- ========================================
INSERT INTO aula (curso_id, titulo, descricao, miniatura, url_video, ordem_aula, criado_em) VALUES
(1, 'Introdução ao Python', 'Apresentação do curso, instalação do Python e configuração do ambiente de desenvolvimento. Primeiro programa em Python.', NULL, 'https://www.youtube.com/watch?v=S9uPNppGsGo', 1, CURRENT_TIMESTAMP),
(1, 'Variáveis e Tipos de Dados', 'Aprenda sobre variáveis, tipos de dados (int, float, string, boolean) e operações básicas em Python.', NULL, 'https://www.youtube.com/watch?v=S9uPNppGsGo', 2, CURRENT_TIMESTAMP),
(1, 'Estruturas Condicionais', 'Entenda como usar if, elif e else para criar lógica condicional em seus programas.', NULL, 'https://www.youtube.com/watch?v=S9uPNppGsGo', 3, CURRENT_TIMESTAMP),
(1, 'Estruturas de Repetição', 'Domine os loops for e while para automatizar tarefas repetitivas e iterar sobre coleções.', NULL, 'https://www.youtube.com/watch?v=S9uPNppGsGo', 4, CURRENT_TIMESTAMP),
(1, 'Listas e Tuplas', 'Aprenda a trabalhar com listas e tuplas, estruturas de dados fundamentais em Python.', NULL, 'https://www.youtube.com/watch?v=S9uPNppGsGo', 5, CURRENT_TIMESTAMP),
(1, 'Dicionários e Sets', 'Entenda como usar dicionários (chave-valor) e sets para organizar seus dados de forma eficiente.', NULL, 'https://www.youtube.com/watch?v=S9uPNppGsGo', 6, CURRENT_TIMESTAMP),
(1, 'Funções', 'Crie funções reutilizáveis, aprenda sobre parâmetros, argumentos e retorno de valores.', NULL, 'https://www.youtube.com/watch?v=S9uPNppGsGo', 7, CURRENT_TIMESTAMP),
(1, 'Programação Orientada a Objetos', 'Introdução a POO: classes, objetos, atributos e métodos em Python.', NULL, 'https://www.youtube.com/watch?v=S9uPNppGsGo', 8, CURRENT_TIMESTAMP),
(1, 'Tratamento de Exceções', 'Aprenda a lidar com erros e exceções usando try, except, finally e raise.', NULL, 'https://www.youtube.com/watch?v=S9uPNppGsGo', 9, CURRENT_TIMESTAMP),
(1, 'Manipulação de Arquivos', 'Leia e escreva arquivos em Python, trabalhe com diferentes formatos (txt, csv, json).', NULL, 'https://www.youtube.com/watch?v=S9uPNppGsGo', 10, CURRENT_TIMESTAMP);

-- ========================================
-- Aulas do Curso 2: JavaScript Completo
-- ========================================
INSERT INTO aula (curso_id, titulo, descricao, miniatura, url_video, ordem_aula, criado_em) VALUES
(2, 'Introdução ao JavaScript', 'História do JavaScript, configuração do ambiente e primeiro programa. Como incluir JavaScript no HTML.', NULL, 'https://www.youtube.com/watch?v=1-w1RfGIov4', 1, CURRENT_TIMESTAMP),
(2, 'Variáveis e Tipos Primitivos', 'Aprenda sobre var, let, const e os tipos primitivos do JavaScript (number, string, boolean, null, undefined).', NULL, 'https://www.youtube.com/watch?v=1-w1RfGIov4', 2, CURRENT_TIMESTAMP),
(2, 'Operadores Aritméticos e Relacionais', 'Conheça os operadores matemáticos e de comparação em JavaScript.', NULL, 'https://www.youtube.com/watch?v=1-w1RfGIov4', 3, CURRENT_TIMESTAMP),
(2, 'Estruturas Condicionais', 'Aprenda a usar if, else if, else e switch case para controlar o fluxo do programa.', NULL, 'https://www.youtube.com/watch?v=1-w1RfGIov4', 4, CURRENT_TIMESTAMP),
(2, 'Estruturas de Repetição', 'Domine os loops: while, do-while e for para repetir operações.', NULL, 'https://www.youtube.com/watch?v=1-w1RfGIov4', 5, CURRENT_TIMESTAMP),
(2, 'Funções', 'Crie funções, aprenda sobre parâmetros, retorno de valores e arrow functions.', NULL, 'https://www.youtube.com/watch?v=1-w1RfGIov4', 6, CURRENT_TIMESTAMP),
(2, 'Arrays e Métodos', 'Trabalhe com arrays e seus métodos (push, pop, map, filter, reduce).', NULL, 'https://www.youtube.com/watch?v=1-w1RfGIov4', 7, CURRENT_TIMESTAMP),
(2, 'Objetos', 'Entenda como criar e manipular objetos em JavaScript.', NULL, 'https://www.youtube.com/watch?v=1-w1RfGIov4', 8, CURRENT_TIMESTAMP),
(2, 'Manipulação do DOM', 'Aprenda a selecionar e manipular elementos HTML usando JavaScript.', NULL, 'https://www.youtube.com/watch?v=1-w1RfGIov4', 9, CURRENT_TIMESTAMP),
(2, 'Eventos', 'Trabalhe com eventos do navegador (click, submit, keypress) para criar interatividade.', NULL, 'https://www.youtube.com/watch?v=1-w1RfGIov4', 10, CURRENT_TIMESTAMP),
(2, 'JSON e APIs', 'Aprenda sobre JSON e como fazer requisições para APIs usando fetch.', NULL, 'https://www.youtube.com/watch?v=1-w1RfGIov4', 11, CURRENT_TIMESTAMP),
(2, 'Projeto Prático', 'Desenvolva um projeto completo aplicando todos os conceitos aprendidos no curso.', NULL, 'https://www.youtube.com/watch?v=1-w1RfGIov4', 12, CURRENT_TIMESTAMP);

-- ========================================
-- Aulas do Curso 3: MySQL - Banco de Dados
-- ========================================
INSERT INTO aula (curso_id, titulo, descricao, miniatura, url_video, ordem_aula, criado_em) VALUES
(3, 'Introdução a Banco de Dados', 'Conceitos fundamentais de banco de dados, SGBDs, MySQL e instalação do ambiente.', NULL, 'https://www.youtube.com/watch?v=Ofktsne-utM', 1, CURRENT_TIMESTAMP),
(3, 'Criando Banco de Dados e Tabelas', 'Aprenda a criar databases e tabelas usando CREATE DATABASE e CREATE TABLE.', NULL, 'https://www.youtube.com/watch?v=Ofktsne-utM', 2, CURRENT_TIMESTAMP),
(3, 'Tipos de Dados no MySQL', 'Conheça os tipos de dados: INT, VARCHAR, TEXT, DATE, DATETIME, DECIMAL e outros.', NULL, 'https://www.youtube.com/watch?v=Ofktsne-utM', 3, CURRENT_TIMESTAMP),
(3, 'INSERT - Inserindo Dados', 'Aprenda a inserir registros nas tabelas usando o comando INSERT INTO.', NULL, 'https://www.youtube.com/watch?v=Ofktsne-utM', 4, CURRENT_TIMESTAMP),
(3, 'SELECT - Consultando Dados', 'Domine o comando SELECT para consultar dados, usar WHERE, ORDER BY e LIMIT.', NULL, 'https://www.youtube.com/watch?v=Ofktsne-utM', 5, CURRENT_TIMESTAMP),
(3, 'UPDATE e DELETE', 'Aprenda a atualizar e deletar registros com UPDATE e DELETE de forma segura.', NULL, 'https://www.youtube.com/watch?v=Ofktsne-utM', 6, CURRENT_TIMESTAMP),
(3, 'Relacionamentos entre Tabelas', 'Entenda chaves primárias, chaves estrangeiras e relacionamentos 1:1, 1:N e N:N.', NULL, 'https://www.youtube.com/watch?v=Ofktsne-utM', 7, CURRENT_TIMESTAMP),
(3, 'JOINs - INNER, LEFT, RIGHT', 'Aprenda a combinar dados de múltiplas tabelas usando diferentes tipos de JOIN.', NULL, 'https://www.youtube.com/watch?v=Ofktsne-utM', 8, CURRENT_TIMESTAMP),
(3, 'Funções de Agregação', 'Use COUNT, SUM, AVG, MAX, MIN e GROUP BY para análise de dados.', NULL, 'https://www.youtube.com/watch?v=Ofktsne-utM', 9, CURRENT_TIMESTAMP),
(3, 'Subconsultas (Subqueries)', 'Aprenda a criar consultas dentro de consultas para operações complexas.', NULL, 'https://www.youtube.com/watch?v=Ofktsne-utM', 10, CURRENT_TIMESTAMP),
(3, 'Índices e Performance', 'Otimize suas consultas usando índices e entenda como melhorar a performance.', NULL, 'https://www.youtube.com/watch?v=Ofktsne-utM', 11, CURRENT_TIMESTAMP),
(3, 'Views e Stored Procedures', 'Crie views para simplificar consultas e stored procedures para lógica reutilizável.', NULL, 'https://www.youtube.com/watch?v=Ofktsne-utM', 12, CURRENT_TIMESTAMP);

-- ============================================
-- Mensagem de confirmação
-- ============================================
DO $$
BEGIN
    RAISE NOTICE '================================================';
    RAISE NOTICE 'Migration V6 concluída com sucesso!';
    RAISE NOTICE '================================================';
    RAISE NOTICE 'Dados antigos removidos e novos dados inseridos:';
    RAISE NOTICE '- 5 campus do IFPR';
    RAISE NOTICE '- 5 áreas de conhecimento';
    RAISE NOTICE '- 3 cursos reais gratuitos do YouTube';
    RAISE NOTICE '  1. Python Completo (10 aulas)';
    RAISE NOTICE '  2. JavaScript Completo (12 aulas)';
    RAISE NOTICE '  3. MySQL - Banco de Dados (12 aulas)';
    RAISE NOTICE '- Total: 34 aulas';
    RAISE NOTICE '================================================';
END $$;