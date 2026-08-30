TRUNCATE TABLE tag CASCADE;

INSERT INTO tag (id, name) VALUES
    (gen_random_uuid(), 'Космос'),
    (gen_random_uuid(), 'Живопись'),
    (gen_random_uuid(), 'Спорт');
