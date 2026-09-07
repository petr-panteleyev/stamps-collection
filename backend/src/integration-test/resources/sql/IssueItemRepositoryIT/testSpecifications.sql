TRUNCATE TABLE issue CASCADE;

INSERT INTO issue (id, region_id, issue_year, issue_date, title) VALUES
    ('89924614-c4a0-4eaf-9d4a-6be398761899',
    (SELECT id FROM region WHERE name = 'СССР'),
    1982, '1982-11-25', 'Шедевры Государственного Эрмитажа. Итальянская живопись XV-XVI вв');

INSERT INTO issue_item (id, issue_id, issue_type, issue_year, number_zag, number_cfa, denomination, description) VALUES
    ('806966b9-1681-48d1-ac4b-c7478db6fb28', '89924614-c4a0-4eaf-9d4a-6be398761899', 'BLOCK', 1982, 161, 5353, 0, 'Франческо Мельци (1493-1570). "Портрет молодой женщины"');

INSERT INTO issue_item (id, issue_id, issue_type, issue_year, number_zag, denomination, description) VALUES
    ('a3c80d33-9842-4338-9afe-400190ad8068', '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1982, 5280, 0.04, '"Портрет актера"'),
    ('8ca36c77-85c8-4494-a3fd-bd7ec01567dd', '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1983, 5281, 0.10, '"Святой Себастьян"'),
    ('317d3019-a2d3-4c39-a025-cb1ce1dc99b7', '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1984, 5282, 0.20, '"Даная"'),
    ('22f86df2-2a4c-49fb-a669-ce7a6d45978d', '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1985, 5283, 0.45, '"Женский портрет"'),
    ('c40769df-10d6-4409-a118-d86f003a1307', '89924614-c4a0-4eaf-9d4a-6be398761899', 'STAMP', 1986, 5284, 0.50, '"Портрет молодого человека"');

INSERT INTO tag_link (item_id, tag_id) VALUES
    ('a3c80d33-9842-4338-9afe-400190ad8068', (SELECT id FROM tag WHERE name='Космос')),
    ('a3c80d33-9842-4338-9afe-400190ad8068', (SELECT id FROM tag WHERE name='Живопись')),
    ('8ca36c77-85c8-4494-a3fd-bd7ec01567dd', (SELECT id FROM tag WHERE name='Живопись')),
    ('317d3019-a2d3-4c39-a025-cb1ce1dc99b7', (SELECT id FROM tag WHERE name='Космос')),
    ('22f86df2-2a4c-49fb-a669-ce7a6d45978d', (SELECT id FROM tag WHERE name='Спорт')),
    ('c40769df-10d6-4409-a118-d86f003a1307', (SELECT id FROM tag WHERE name='Космос'));

--

INSERT INTO issue (id, region_id, issue_year, issue_date, title) VALUES
    ('b1883d7b-7eea-437f-8eb0-b4c71e6adffd',
    (SELECT id FROM region WHERE name = 'Россия'),
    2008, '2008-05-20', 'Выпуск-10');

INSERT INTO issue_item (id, issue_id, issue_type, issue_year, number_zag, denomination, description) VALUES
    ('86b01876-1593-44bb-8b74-c2328e604de2', 'b1883d7b-7eea-437f-8eb0-b4c71e6adffd', 'STAMP', 2008, 1232, 0.04, 'Марка 1232'),
    ('f771c03a-001b-4931-977c-cf3627f5610e', 'b1883d7b-7eea-437f-8eb0-b4c71e6adffd', 'STAMP', 2009, 1233, 0.10, 'Марка 1233'),
    ('e3641547-64c1-401e-b6ea-e20714e0a0db', 'b1883d7b-7eea-437f-8eb0-b4c71e6adffd', 'STAMP', 2010, 1234, 0.20, 'Марка 1234'),
    ('e64847ae-bbfa-4669-9b22-da719a78f05f', 'b1883d7b-7eea-437f-8eb0-b4c71e6adffd', 'STAMP', 2011, 1235, 0.45, 'Марка 1235'),
    ('2a9ae51e-99d0-40cc-9f7d-fc52339c3cbc', 'b1883d7b-7eea-437f-8eb0-b4c71e6adffd', 'STAMP', 2012, 1236, 0.50, 'Марка 1236');

INSERT INTO tag_link (item_id, tag_id) VALUES
    ('86b01876-1593-44bb-8b74-c2328e604de2', (SELECT id FROM tag WHERE name='Космос')),
    ('f771c03a-001b-4931-977c-cf3627f5610e', (SELECT id FROM tag WHERE name='Живопись')),
    ('e3641547-64c1-401e-b6ea-e20714e0a0db', (SELECT id FROM tag WHERE name='Живопись')),
    ('e64847ae-bbfa-4669-9b22-da719a78f05f', (SELECT id FROM tag WHERE name='Космос')),
    ('2a9ae51e-99d0-40cc-9f7d-fc52339c3cbc', (SELECT id FROM tag WHERE name='Космос'));