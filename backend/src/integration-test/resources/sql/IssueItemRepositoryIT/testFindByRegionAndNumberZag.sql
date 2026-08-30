TRUNCATE TABLE issue CASCADE;

INSERT INTO issue (id, region_id, issue_year, issue_date, title) VALUES
    ('89924614-c4a0-4eaf-9d4a-6be398761899',
    (SELECT id FROM region WHERE name = 'СССР'),
    1982, '1982-11-25', 'Шедевры Государственного Эрмитажа. Итальянская живопись XV-XVI вв');

INSERT INTO issue_item (id, issue_id, issue_type, issue_year, number_zag, number_cfa, denomination, description) VALUES
    (gen_random_uuid(), '89924614-c4a0-4eaf-9d4a-6be398761899', 'BLOCK', 1982, 161, 5353, 0, 'Франческо Мельци (1493-1570). "Портрет молодой женщины"');

INSERT INTO issue_item (id, issue_id, issue_type, issue_year, number_zag, denomination, description) VALUES
    (gen_random_uuid(), '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1982, 5280, 0.04, '"Портрет актера"'),
    (gen_random_uuid(), '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1982, 5281, 0.10, '"Святой Себастьян"'),
    (gen_random_uuid(), '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1982, 5282, 0.20, '"Даная"'),
    (gen_random_uuid(), '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1982, 5283, 0.45, '"Женский портрет"'),
    (gen_random_uuid(), '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1982, 5284, 0.50, '"Портрет молодого человека"');
