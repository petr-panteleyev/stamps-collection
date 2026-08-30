TRUNCATE TABLE region CASCADE;

INSERT INTO region (id, name, year_start, year_end) VALUES
    (gen_random_uuid(), 'СССР', 1923, 1991),
    (gen_random_uuid(), 'Россия', 1992, NULL);