CREATE TABLE IF NOT EXISTS image (
    id              UUID        NOT NULL PRIMARY KEY,
    bytes           BYTEA       NOT NULL,
    --
    created_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE image IS 'Image for stamps, blocks and couplings';
COMMENT ON COLUMN image.id IS 'Primary key';
COMMENT ON COLUMN image.bytes IS 'Image bytes';
COMMENT ON COLUMN image.created_at IS 'Record creation time';
