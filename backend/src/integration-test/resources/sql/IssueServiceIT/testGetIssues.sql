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

--

INSERT INTO issue (id, region_id, issue_year, issue_date, title) VALUES
    ('ef41db10-b38e-4a85-94c6-5cf8f83c9ff6',
    (SELECT id FROM region WHERE name = 'Россия'),
    1982, '1992-11-25', 'Шедевры Государственного Эрмитажа. Итальянская живопись XV-XVI вв');

INSERT INTO issue_item (id, issue_id, issue_type, issue_year, number_zag, number_cfa, denomination, description) VALUES
    (gen_random_uuid(), 'ef41db10-b38e-4a85-94c6-5cf8f83c9ff6', 'BLOCK', 1982, 161, 5353, 0, 'Франческо Мельци (1493-1570). "Портрет молодой женщины"');

INSERT INTO issue_item (id, issue_id, issue_type, issue_year, number_zag, denomination, description) VALUES
    ('87c6ca9b-643a-4e3f-beee-5ad1b20602f1', 'ef41db10-b38e-4a85-94c6-5cf8f83c9ff6', 'STAMP', 1992, 5280, 0.04, '"Портрет актера"'),
    ('c3b6de5a-f750-44f9-9823-6bfad9c5fe69', 'ef41db10-b38e-4a85-94c6-5cf8f83c9ff6', 'STAMP', 1992, 5281, 0.10, '"Святой Себастьян"'),
    ('394f6737-6e04-4d31-9501-6b1375d11622', 'ef41db10-b38e-4a85-94c6-5cf8f83c9ff6', 'STAMP', 1992, 5282, 0.20, '"Даная"'),
    ('126a2312-9155-4431-a662-e0586fddf6c3', 'ef41db10-b38e-4a85-94c6-5cf8f83c9ff6', 'STAMP', 1992, 5283, 0.45, '"Женский портрет"'),
    ('a030a488-024c-42d2-a895-4287f557cd64', 'ef41db10-b38e-4a85-94c6-5cf8f83c9ff6', 'STAMP', 1992, 5284, 0.50, '"Портрет молодого человека"');
