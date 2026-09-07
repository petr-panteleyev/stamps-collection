CREATE TABLE IF NOT EXISTS issue_item (
    id              UUID        NOT NULL PRIMARY KEY,
    issue_id        UUID        NOT NULL,
    issue_type      VARCHAR     NOT NULL,
    --
    issue_year      INT         NOT NULL,
    number_zag      INT         NOT NULL,
    number_cfa      INT         NULL,
    denomination    DECIMAL     NOT NULL,
    description     VARCHAR     NOT NULL DEFAULT '',
    --
    block_num       INT         NULL,
    coupling_items  VARCHAR     NULL,
    --
    has_clean       BOOLEAN     NOT NULL DEFAULT FALSE,
    has_cancelled   BOOLEAN     NOT NULL DEFAULT FALSE,
    repl_required   BOOLEAN     NOT NULL DEFAULT FALSE,
    --
    comment         VARCHAR     NOT NULL DEFAULT '',
    created_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT issue_item_issue_id_fk FOREIGN KEY (issue_id)
        REFERENCES issue (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE INDEX IF NOT EXISTS issue_item_issue_id_idx ON issue_item(issue_id);
CREATE INDEX IF NOT EXISTS issue_item_year_idx ON issue_item(issue_year);
