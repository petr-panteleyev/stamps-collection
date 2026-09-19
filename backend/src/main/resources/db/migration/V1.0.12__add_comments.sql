-- region
COMMENT ON TABLE region IS 'Регион выпуска';
COMMENT ON COLUMN region.id IS 'Идентификатор региона';
COMMENT ON COLUMN region.name IS 'Название региона';
COMMENT ON COLUMN region.start_year IS 'Год первого выпуска';
COMMENT ON COLUMN region.end_year IS 'Год последнего выпуска';
COMMENT ON COLUMN region.created_at IS 'Время создания записи';

-- tag
COMMENT ON TABLE tag IS 'Тег для элементов коллекции';
COMMENT ON COLUMN tag.id IS 'Идентификатор тега';
COMMENT ON COLUMN tag.name IS 'Уникальное название тега';
COMMENT ON COLUMN tag.created_at IS 'Время создания записи';

-- issue
COMMENT ON TABLE issue IS 'Выпуск почтовых марок';
COMMENT ON COLUMN issue.id IS 'Идентификатор выпуска';
COMMENT ON COLUMN issue.region_id IS 'Ссылка на регион';
COMMENT ON COLUMN issue.issue_year IS 'Год выпуска';
COMMENT ON COLUMN issue.issue_date IS 'Дата выпуска';
COMMENT ON COLUMN issue.title IS 'Название выпуска';
COMMENT ON COLUMN issue.created_at IS 'Время создания записи';

-- issue_item
COMMENT ON TABLE issue_item IS 'Элемент выпуска: марка, блок, сцепка';
COMMENT ON COLUMN issue_item.id IS 'Идентификатор элемента';
COMMENT ON COLUMN issue_item.issue_id IS 'Ссылка на выпуск';
COMMENT ON COLUMN issue_item.item_type IS 'Тип элемента: STAMP, BLOCK, COUPLING';
COMMENT ON COLUMN issue_item.issue_year IS 'Год выпуска';
COMMENT ON COLUMN issue_item.number_zag IS 'Номер по каталогу Загорского';
COMMENT ON COLUMN issue_item.number_cfa IS 'Номер по каталогу ЦФА (для марок СССР)';
COMMENT ON COLUMN issue_item.denomination IS 'Номинал марки или блока';
COMMENT ON COLUMN issue_item.description IS 'Описание';
COMMENT ON COLUMN issue_item.block_num IS 'Для марки в блоке номер блока по каталогу Загорского';
COMMENT ON COLUMN issue_item.coupling_items IS 'Для сцепки номера марок по каталогу Загорского (через запятую)';
COMMENT ON COLUMN issue_item.has_clean IS 'Коллекция содержит чистую марку, блок, сцепку';
COMMENT ON COLUMN issue_item.has_cancelled IS 'Коллекция содержит гашёную марку, блок, сцепку';
COMMENT ON COLUMN issue_item.repl_required IS 'Элемент нуждается в замене (например, поврежден)';
COMMENT ON COLUMN issue_item.no_perforation IS 'Без перфорации';
COMMENT ON COLUMN issue_item.comment IS 'Комментарий';
COMMENT ON COLUMN issue_item.created_at IS 'Время создания записи';

-- tag_link
COMMENT ON TABLE tag_link IS 'Связь между элементами коллекции и тегами';
COMMENT ON COLUMN tag_link.item_id IS 'Ссылка на элемент';
COMMENT ON COLUMN tag_link.tag_id IS 'Ссылка на тег';

-- album
COMMENT ON TABLE album IS 'Альбом';
COMMENT ON COLUMN album.id IS 'Идентификатор альбома';
COMMENT ON COLUMN album.parent_id IS 'Ссылка на альбом верхнего уровня';
COMMENT ON COLUMN album.parent_index IS 'Порядок внутри альбома верхнего уровня';
COMMENT ON COLUMN album.name IS 'Название альбома';
COMMENT ON COLUMN album.region_id IS 'Ссылка на регион';
COMMENT ON COLUMN album.start_year IS 'Начальный год альбома';
COMMENT ON COLUMN album.end_year IS 'Конечный год альбома';
COMMENT ON COLUMN album.created_at IS 'Время создания записи';

-- image
COMMENT ON TABLE image IS 'Изображение для марки, блока или сцепки';
COMMENT ON COLUMN image.id IS 'Идентификатор изображения';
COMMENT ON COLUMN image.bytes IS 'Байты изображения';
COMMENT ON COLUMN image.created_at IS 'Время создания записи';

-- album_tag_link
COMMENT ON TABLE album_tag_link IS 'Связь между альбомами и тегами';
COMMENT ON COLUMN album_tag_link.album_id IS 'Ссылка на альбом';
COMMENT ON COLUMN album_tag_link.tag_id IS 'Ссылка на тег';

-- album_excluded_tag_link
COMMENT ON TABLE album_excluded_tag_link IS 'Связь между альбомами и исключёнными тегами';
COMMENT ON COLUMN album_excluded_tag_link.album_id IS 'Ссылка на альбом';
COMMENT ON COLUMN album_excluded_tag_link.tag_id IS 'Ссылка на тег';
