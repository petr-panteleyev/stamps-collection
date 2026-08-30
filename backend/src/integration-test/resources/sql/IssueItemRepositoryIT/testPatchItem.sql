TRUNCATE TABLE issue CASCADE;

INSERT INTO issue (id, region_id, issue_year, issue_date, title) VALUES
    ('89924614-c4a0-4eaf-9d4a-6be398761899',
    (SELECT id FROM region WHERE name = 'СССР'),
    1982, '1982-11-25', 'Шедевры Государственного Эрмитажа. Итальянская живопись XV-XVI вв');

INSERT INTO issue_item (id, issue_id, issue_type, issue_year, number_zag, number_cfa, denomination, description, has_clean, has_cancelled, repl_required) VALUES
    ('c977f05c-03aa-4cf3-af2d-6a5e093b653b', '89924614-c4a0-4eaf-9d4a-6be398761899', 'BLOCK', 1982, 161, 5353, 0, 'Франческо Мельци (1493-1570). "Портрет молодой женщины"', false, false, false);
