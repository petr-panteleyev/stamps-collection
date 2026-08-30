CREATE TABLE IF NOT EXISTS region (
    id          UUID        NOT NULL PRIMARY KEY,
    name        VARCHAR     NOT NULL,
    year_start  INT         NOT NULL,
    year_end    INT         NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS region_name_idx ON region(name);

COMMENT ON TABLE region IS 'Region of stamp issue';
COMMENT ON COLUMN region.id IS 'Primary key';
COMMENT ON COLUMN region.name IS 'Unique region name';
COMMENT ON COLUMN region.year_start IS 'Start year of stamp issues';
COMMENT ON COLUMN region.year_end IS 'Final year of stamp issues';
COMMENT ON COLUMN region.created_at IS 'Record creation time';
