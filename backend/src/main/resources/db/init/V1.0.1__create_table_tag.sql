CREATE TABLE IF NOT EXISTS tag (
    id          UUID        NOT NULL PRIMARY KEY,
    name        VARCHAR     NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS tag_name_idx ON tag(name);

COMMENT ON TABLE tag IS 'Tag for issues, stamps, blocks, etc.';
COMMENT ON COLUMN tag.id IS 'Primary key';
COMMENT ON COLUMN tag.name IS 'Tag unique name';
COMMENT ON COLUMN tag.created_at IS 'Record creation time';

--

CREATE TABLE IF NOT EXISTS tag_link (
    item_id     UUID NOT NULL,
    tag_id      UUID NOT NULL,

    CONSTRAINT tag_link_tag_id_fk FOREIGN KEY (tag_id)
        REFERENCES tag (id) ON DELETE CASCADE ON UPDATE CASCADE
);

