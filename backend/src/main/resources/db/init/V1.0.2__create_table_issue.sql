CREATE TABLE IF NOT EXISTS issue (
    id          UUID        NOT NULL PRIMARY KEY,
    region_id   UUID        NOT NULL,
    issue_year  INT         NOT NULL,
    issue_date  DATE        NULL,
    title       VARCHAR     NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT issue_region_id_fk FOREIGN KEY (region_id)
        REFERENCES region (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS issue_region_id_idx ON issue(region_id);
CREATE INDEX IF NOT EXISTS issue_year_idx ON issue(issue_year);

COMMENT ON TABLE issue IS 'Issue of a postage stamps';
COMMENT ON COLUMN issue.id IS 'Primary key';
COMMENT ON COLUMN issue.region_id IS 'Region ID';
COMMENT ON COLUMN issue.issue_year IS 'Issue year';
COMMENT ON COLUMN issue.issue_date IS 'Optional issue date from catalogue';
COMMENT ON COLUMN issue.title IS 'Issue title';
