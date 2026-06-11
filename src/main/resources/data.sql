-- ============================================================
--  Dados iniciais — Biblioteca Digital
--  Executado após criação das tabelas via Hibernate (ddl-auto: create-drop)
--  Sintaxe: PostgreSQL
-- ============================================================

-- Autores
INSERT INTO authors (id, name, nationality, birth_date, biography, email)
VALUES
    (1, 'Gabriel García Márquez', 'Colombiana', '1927-03-06',
     'Prêmio Nobel de Literatura 1982. Fundador do realismo mágico na literatura latino-americana.',
     'garcia.marquez@literatura.com'),
    (2, 'Clarice Lispector', 'Brasileira', '1920-12-10',
     'Uma das escritoras mais influentes da literatura brasileira do século XX.',
     'clarice@literatura.com.br'),
    (3, 'George Orwell', 'Britânica', '1903-06-25',
     'Escritor e jornalista britânico conhecido por suas críticas ao totalitarismo.',
     'orwell@fiction.uk'),
    (4, 'Machado de Assis', 'Brasileira', '1839-06-21',
     'Fundador da Academia Brasileira de Letras. Principal nome do Realismo brasileiro.',
     'machado@academia.br'),
    (5, 'J.R.R. Tolkien', 'Britânica', '1892-01-03',
     'Professor e escritor britânico, criador do universo da Terra-média.',
     'tolkien@middleearth.uk');

-- Livros
INSERT INTO books (id, title, isbn, publish_year, synopsis, genre, available, author_id)
VALUES
    (1, 'Cem Anos de Solidão', '9788535914849', 1967,
     'A saga da família Buendía ao longo de sete gerações em Macondo.',
     'ROMANCE', true, 1),
    (2, 'A Paixão Segundo G.H.', '9788532630384', 1964,
     'Uma mulher confronta uma barata e desencadeia uma jornada existencial.',
     'ROMANCE', true, 2),
    (3, '1984', '9788535914993', 1949,
     'Distopia clássica sobre vigilância total e controle do Estado sobre o indivíduo.',
     'SCIENCE_FICTION', true, 3),
    (4, 'A Revolução dos Bichos', '9788535909555', 1945,
     'Fábula política sobre a corrupção do poder revolucionário.',
     'FANTASY', true, 3),
    (5, 'Dom Casmurro', '9788535914955', 1899,
     'Narrativa de Bentinho que questiona a fidelidade de Capitu.',
     'ROMANCE', true, 4),
    (6, 'O Senhor dos Anéis: A Sociedade do Anel', '9788533613379', 1954,
     'A jornada de Frodo Bolseiro para destruir o Um Anel.',
     'FANTASY', true, 5),
    (7, 'Memórias Póstumas de Brás Cubas', '9788535909753', 1881,
     'Narrado por um defunto, obra fundadora do Realismo no Brasil.',
     'ROMANCE', true, 4),
    (8, 'A Hora da Estrela', '9788532630407', 1977,
     'Macabéa, nordestina no Rio de Janeiro, e sua insignificante existência.',
     'ROMANCE', true, 2);

-- Empréstimos
INSERT INTO loans (id, book_id, member_name, member_email, loan_date, expected_return_date, actual_return_date, status)
VALUES
    (1, 1, 'Ana Paula Souza', 'ana.paula@email.com',
     CURRENT_DATE - INTERVAL '20 days', CURRENT_DATE - INTERVAL '6 days', null, 'OVERDUE'),
    (2, 3, 'Carlos Eduardo Lima', 'carlos.lima@email.com',
     CURRENT_DATE - INTERVAL '10 days', CURRENT_DATE + INTERVAL '4 days', null, 'ACTIVE'),
    (3, 5, 'Maria Fernanda Costa', 'maria.costa@email.com',
     CURRENT_DATE - INTERVAL '30 days', CURRENT_DATE - INTERVAL '15 days',
     CURRENT_DATE - INTERVAL '14 days', 'RETURNED');

-- Atualizar disponibilidade dos livros com empréstimos ativos/vencidos
UPDATE books SET available = false WHERE id IN (1, 3);

-- Reposiciona as sequences após inserts com IDs hardcoded
ALTER TABLE authors ALTER COLUMN id RESTART WITH 6;
ALTER TABLE books ALTER COLUMN id RESTART WITH 9;
ALTER TABLE loans ALTER COLUMN id RESTART WITH 4;