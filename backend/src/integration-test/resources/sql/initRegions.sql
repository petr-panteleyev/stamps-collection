TRUNCATE TABLE region CASCADE;

INSERT INTO region (id, name, start_year, end_year) VALUES
    (gen_random_uuid(), 'СССР', 1923, 1991),
    (gen_random_uuid(), 'Россия', 1992, NULL);