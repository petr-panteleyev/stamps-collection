TRUNCATE TABLE issue CASCADE;

INSERT INTO issue (id, region_id, issue_year, issue_date, title) VALUES
    ('89924614-c4a0-4eaf-9d4a-6be398761899',
    (SELECT id FROM region WHERE name = 'СССР'),
    1982, '1982-11-25', 'Шедевры Государственного Эрмитажа. Итальянская живопись XV-XVI вв');

INSERT INTO issue_item (id, issue_id, issue_type, issue_year, number_zag, number_cfa, denomination, description) VALUES
    (gen_random_uuid(), '89924614-c4a0-4eaf-9d4a-6be398761899', 'BLOCK', 1982, 161, 5353, 0, 'Франческо Мельци (1493-1570). "Портрет молодой женщины"');

INSERT INTO issue_item (id, issue_id, issue_type, issue_year, number_zag, denomination, description) VALUES
    ('a3c80d33-9842-4338-9afe-400190ad8068', '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1982, 5280, 0.04, '"Портрет актера"'),
    ('8ca36c77-85c8-4494-a3fd-bd7ec01567dd', '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1982, 5281, 0.10, '"Святой Себастьян"'),
    ('317d3019-a2d3-4c39-a025-cb1ce1dc99b7', '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1982, 5282, 0.20, '"Даная"'),
    ('22f86df2-2a4c-49fb-a669-ce7a6d45978d', '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1982, 5283, 0.45, '"Женский портрет"'),
    ('c40769df-10d6-4409-a118-d86f003a1307', '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1982, 5284, 0.50, '"Портрет молодого человека"');

INSERT INTO tag_link (item_id, tag_id) VALUES
    ('a3c80d33-9842-4338-9afe-400190ad8068', (SELECT id FROM tag WHERE name='Космос')),
    ('a3c80d33-9842-4338-9afe-400190ad8068', (SELECT id FROM tag WHERE name='Живопись')),
    ('8ca36c77-85c8-4494-a3fd-bd7ec01567dd', (SELECT id FROM tag WHERE name='Живопись')),
    ('317d3019-a2d3-4c39-a025-cb1ce1dc99b7', (SELECT id FROM tag WHERE name='Космос')),
    ('22f86df2-2a4c-49fb-a669-ce7a6d45978d', (SELECT id FROM tag WHERE name='Спорт')),
    ('c40769df-10d6-4409-a118-d86f003a1307', (SELECT id FROM tag WHERE name='Космос'));