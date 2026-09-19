ALTER TABLE issue_item
    RENAME COLUMN issue_type TO item_type;

ALTER TABLE issue_item
    ADD COLUMN IF NOT EXISTS no_perforation BOOLEAN NOT NULL DEFAULT FALSE;
