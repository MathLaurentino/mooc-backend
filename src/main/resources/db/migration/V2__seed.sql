-- Migration: Seed inicial do banco de dados
-- Exclui a tabela usuario conforme solicitado

-- ============================================
-- 1. Popular tabela CAMPUS
-- ============================================
INSERT INTO campus (nome, visivel, criado_em) VALUES
('Campus São Paulo', TRUE, CURRENT_TIMESTAMP),
('Campus Rio de Janeiro', TRUE, CURRENT_TIMESTAMP),
('Campus Belo Horizonte', TRUE, CURRENT_TIMESTAMP),
('Campus Brasília', TRUE, CURRENT_TIMESTAMP),
('Campus Porto Alegre', FALSE, CURRENT_TIMESTAMP);

-- ============================================
-- 2. Popular tabela AREA_CONHECIMENTO
-- ============================================
INSERT INTO area_conhecimento (nome, visivel, criado_em) VALUES
('Tecnologia da Informação', TRUE, CURRENT_TIMESTAMP),
('Gestão e Negócios', TRUE, CURRENT_TIMESTAMP),
('Saúde e Bem-estar', TRUE, CURRENT_TIMESTAMP),
('Idiomas', TRUE, CURRENT_TIMESTAMP),
('Design e Criatividade', TRUE, CURRENT_TIMESTAMP),
('Ciências Exatas', FALSE, CURRENT_TIMESTAMP);

-- ============================================
-- 3. Popular tabela CURSO
-- ============================================
INSERT INTO curso (nome, descricao, area_conhecimento_id, campus_id, nome_professor, miniatura, carga_horaria, visivel, criado_em) VALUES
-- Cursos de TI
('Introdução à Programação', 'Aprenda os fundamentos da programação com Python e lógica de programação.', 1, 1, 'Dr. Carlos Silva', '/images/curso-programacao.jpg', 40, TRUE, CURRENT_TIMESTAMP),
('Desenvolvimento Web Fullstack', 'Domine HTML, CSS, JavaScript, React e Node.js para criar aplicações web completas.', 1, 1, 'Prof. Ana Santos', '/images/curso-web.jpg', 80, TRUE, CURRENT_TIMESTAMP),
('Banco de Dados SQL', 'Aprenda a modelar, criar e gerenciar bancos de dados relacionais.', 1, 2, 'Prof. Roberto Lima', '/images/curso-sql.jpg', 50, TRUE, CURRENT_TIMESTAMP),
('Segurança da Informação', 'Conceitos e práticas de segurança cibernética para proteger sistemas e dados.', 1, 3, 'Dra. Mariana Costa', '/images/curso-seguranca.jpg', 60, TRUE, CURRENT_TIMESTAMP),

-- Cursos de Gestão
('Gestão de Projetos', 'Metodologias ágeis e tradicionais para gerenciar projetos com eficiência.', 2, 2, 'Prof. Fernando Alves', '/images/curso-gestao-projetos.jpg', 45, TRUE, CURRENT_TIMESTAMP),
('Marketing Digital', 'Estratégias de marketing online, SEO, redes sociais e análise de métricas.', 2, 1, 'Profa. Juliana Mendes', '/images/curso-marketing.jpg', 35, TRUE, CURRENT_TIMESTAMP),
('Empreendedorismo', 'Como planejar, validar e lançar seu próprio negócio.', 2, 4, 'Prof. Paulo Rodrigues', '/images/curso-empreendedorismo.jpg', 30, TRUE, CURRENT_TIMESTAMP),

-- Cursos de Saúde
('Primeiros Socorros', 'Técnicas essenciais de atendimento emergencial e suporte básico de vida.', 3, 3, 'Enf. Carla Martins', '/images/curso-primeiros-socorros.jpg', 20, TRUE, CURRENT_TIMESTAMP),
('Nutrição e Alimentação Saudável', 'Princípios de nutrição equilibrada e planejamento de refeições.', 3, 2, 'Nutri. Beatriz Ferreira', '/images/curso-nutricao.jpg', 25, TRUE, CURRENT_TIMESTAMP),

-- Cursos de Idiomas
('Inglês Básico', 'Fundamentos da língua inglesa para iniciantes.', 4, 1, 'Prof. Michael Johnson', '/images/curso-ingles-basico.jpg', 60, TRUE, CURRENT_TIMESTAMP),
('Inglês Intermediário', 'Aprofunde seus conhecimentos em conversação e gramática inglesa.', 4, 1, 'Prof. Sarah Williams', '/images/curso-ingles-intermediario.jpg', 60, TRUE, CURRENT_TIMESTAMP),
('Espanhol para Viagens', 'Vocabulário e expressões essenciais para se comunicar em viagens.', 4, 2, 'Profa. Carmen Gonzalez', '/images/curso-espanhol.jpg', 30, TRUE, CURRENT_TIMESTAMP),

-- Cursos de Design
('Design Gráfico com Photoshop', 'Domine as ferramentas do Photoshop para criar designs profissionais.', 5, 1, 'Designer Lucas Oliveira', '/images/curso-photoshop.jpg', 40, TRUE, CURRENT_TIMESTAMP),
('UI/UX Design', 'Princípios de design de interface e experiência do usuário.', 5, 3, 'Designer Amanda Souza', '/images/curso-uiux.jpg', 50, TRUE, CURRENT_TIMESTAMP),

-- Curso não visível
('Matemática Avançada', 'Cálculo diferencial e integral para engenharias.', 6, 5, 'Prof. João Pereira', '/images/curso-matematica.jpg', 70, FALSE, CURRENT_TIMESTAMP);

-- ============================================
-- 4. Popular tabela AULA
-- ============================================

-- Aulas do curso "Introdução à Programação" (curso_id = 1)
INSERT INTO aula (curso_id, titulo, descricao, miniatura, url_video, ordem_aula, criado_em) VALUES
(1, 'Bem-vindo ao Curso', 'Apresentação do curso e configuração do ambiente de desenvolvimento.', '/images/aula1-1.jpg', 'https://video.exemplo.com/curso1-aula1', 1, CURRENT_TIMESTAMP),
(1, 'Variáveis e Tipos de Dados', 'Entenda como armazenar e manipular dados em Python.', '/images/aula1-2.jpg', 'https://video.exemplo.com/curso1-aula2', 2, CURRENT_TIMESTAMP),
(1, 'Estruturas Condicionais', 'Aprenda a usar if, elif e else para tomar decisões no código.', '/images/aula1-3.jpg', 'https://video.exemplo.com/curso1-aula3', 3, CURRENT_TIMESTAMP),
(1, 'Laços de Repetição', 'Domine os loops for e while para automatizar tarefas repetitivas.', '/images/aula1-4.jpg', 'https://video.exemplo.com/curso1-aula4', 4, CURRENT_TIMESTAMP),
(1, 'Funções', 'Crie funções reutilizáveis para organizar seu código.', '/images/aula1-5.jpg', 'https://video.exemplo.com/curso1-aula5', 5, CURRENT_TIMESTAMP);

-- Aulas do curso "Desenvolvimento Web Fullstack" (curso_id = 2)
INSERT INTO aula (curso_id, titulo, descricao, miniatura, url_video, ordem_aula, criado_em) VALUES
(2, 'Introdução ao Desenvolvimento Web', 'Visão geral das tecnologias web e arquitetura cliente-servidor.', '/images/aula2-1.jpg', 'https://video.exemplo.com/curso2-aula1', 1, CURRENT_TIMESTAMP),
(2, 'HTML5 Essencial', 'Estrutura de documentos HTML e elementos semânticos.', '/images/aula2-2.jpg', 'https://video.exemplo.com/curso2-aula2', 2, CURRENT_TIMESTAMP),
(2, 'CSS3 e Estilização', 'Aplique estilos e crie layouts responsivos com CSS.', '/images/aula2-3.jpg', 'https://video.exemplo.com/curso2-aula3', 3, CURRENT_TIMESTAMP),
(2, 'JavaScript Moderno', 'ES6+, manipulação do DOM e eventos.', '/images/aula2-4.jpg', 'https://video.exemplo.com/curso2-aula4', 4, CURRENT_TIMESTAMP),
(2, 'React Básico', 'Componentes, props e estado no React.', '/images/aula2-5.jpg', 'https://video.exemplo.com/curso2-aula5', 5, CURRENT_TIMESTAMP),
(2, 'Node.js e Express', 'Criando APIs RESTful com Node.js.', '/images/aula2-6.jpg', 'https://video.exemplo.com/curso2-aula6', 6, CURRENT_TIMESTAMP),
(2, 'Projeto Final', 'Construa uma aplicação completa do zero.', '/images/aula2-7.jpg', 'https://video.exemplo.com/curso2-aula7', 7, CURRENT_TIMESTAMP);

-- Aulas do curso "Banco de Dados SQL" (curso_id = 3)
INSERT INTO aula (curso_id, titulo, descricao, miniatura, url_video, ordem_aula, criado_em) VALUES
(3, 'Introdução a Bancos de Dados', 'Conceitos fundamentais de SGBDs e modelagem de dados.', '/images/aula3-1.jpg', 'https://video.exemplo.com/curso3-aula1', 1, CURRENT_TIMESTAMP),
(3, 'Comandos SQL Básicos', 'SELECT, INSERT, UPDATE e DELETE.', '/images/aula3-2.jpg', 'https://video.exemplo.com/curso3-aula2', 2, CURRENT_TIMESTAMP),
(3, 'Relacionamentos e JOINs', 'Como relacionar tabelas e consultar dados relacionados.', '/images/aula3-3.jpg', 'https://video.exemplo.com/curso3-aula3', 3, CURRENT_TIMESTAMP),
(3, 'Índices e Performance', 'Otimize consultas com índices e boas práticas.', '/images/aula3-4.jpg', 'https://video.exemplo.com/curso3-aula4', 4, CURRENT_TIMESTAMP);

-- Aulas do curso "Marketing Digital" (curso_id = 6)
INSERT INTO aula (curso_id, titulo, descricao, miniatura, url_video, ordem_aula, criado_em) VALUES
(6, 'Fundamentos do Marketing Digital', 'Conceitos básicos e panorama do marketing online.', '/images/aula6-1.jpg', 'https://video.exemplo.com/curso6-aula1', 1, CURRENT_TIMESTAMP),
(6, 'SEO - Otimização para Motores de Busca', 'Como rankear melhor no Google e outros buscadores.', '/images/aula6-2.jpg', 'https://video.exemplo.com/curso6-aula2', 2, CURRENT_TIMESTAMP),
(6, 'Marketing em Redes Sociais', 'Estratégias para Instagram, Facebook e LinkedIn.', '/images/aula6-3.jpg', 'https://video.exemplo.com/curso6-aula3', 3, CURRENT_TIMESTAMP),
(6, 'Google Ads e Tráfego Pago', 'Como criar campanhas eficientes de anúncios pagos.', '/images/aula6-4.jpg', 'https://video.exemplo.com/curso6-aula4', 4, CURRENT_TIMESTAMP);

-- Aulas do curso "Inglês Básico" (curso_id = 10)
INSERT INTO aula (curso_id, titulo, descricao, miniatura, url_video, ordem_aula, criado_em) VALUES
(10, 'Alphabet and Pronunciation', 'Aprenda o alfabeto e a pronúncia correta em inglês.', '/images/aula10-1.jpg', 'https://video.exemplo.com/curso10-aula1', 1, CURRENT_TIMESTAMP),
(10, 'Greetings and Introductions', 'Cumprimentos e como se apresentar em inglês.', '/images/aula10-2.jpg', 'https://video.exemplo.com/curso10-aula2', 2, CURRENT_TIMESTAMP),
(10, 'Numbers and Colors', 'Números, cores e vocabulário básico.', '/images/aula10-3.jpg', 'https://video.exemplo.com/curso10-aula3', 3, CURRENT_TIMESTAMP),
(10, 'Simple Present Tense', 'Tempo presente simples e verbos regulares.', '/images/aula10-4.jpg', 'https://video.exemplo.com/curso10-aula4', 4, CURRENT_TIMESTAMP),
(10, 'Daily Routines', 'Vocabulário sobre rotina diária e atividades cotidianas.', '/images/aula10-5.jpg', 'https://video.exemplo.com/curso10-aula5', 5, CURRENT_TIMESTAMP);

-- ============================================
-- NOTA: As tabelas abaixo dependem de dados
-- da tabela USUARIO, que será populada via código.
-- Por isso, não vamos popular:
-- - inscricao
-- - progresso_aula
-- - solicitacao_certificado
-- - certificado
-- ============================================

-- Mensagem de confirmação
DO $$
BEGIN
    RAISE NOTICE 'Migration concluída com sucesso!';
    RAISE NOTICE 'Tabelas populadas: campus, area_conhecimento, curso, aula';
    RAISE NOTICE 'Total de campus: 5';
    RAISE NOTICE 'Total de áreas de conhecimento: 6';
    RAISE NOTICE 'Total de cursos: 15';
    RAISE NOTICE 'Total de aulas: 30';
END $$;