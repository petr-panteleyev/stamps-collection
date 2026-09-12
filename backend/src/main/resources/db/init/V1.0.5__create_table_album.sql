CREATE TABLE IF NOT EXISTS album (
    id              UUID        NOT NULL PRIMARY KEY,
    parent_id       UUID        NULL,
    parent_index    INT         NOT NULL,
    name            VARCHAR     NOT NULL,
    region_id       UUID        NOT NULL,
    start_year      INT         NULL,
    end_year        INT         NULL,
    no_tags         BOOLEAN     NOT NULL,
    created_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT album_parent_id_fk FOREIGN KEY (parent_id)
        REFERENCES album (id) ON DELETE CASCADE ON UPDATE RESTRICT,

    CONSTRAINT album_region_id_fk FOREIGN KEY (region_id)
        REFERENCES region (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE UNIQUE INDEX IF NOT EXISTS album_parent_id_name_idx ON album(parent_id, name) NULLS NOT DISTINCT ;

COMMENT ON TABLE album IS 'Collection album';
COMMENT ON COLUMN album.id IS 'Primary key';
COMMENT ON COLUMN album.parent_id IS 'Link to parent album';
COMMENT ON COLUMN album.parent_index IS 'Order within parent';
COMMENT ON COLUMN album.name IS 'Unique album name';
COMMENT ON COLUMN album.region_id IS 'Link to album region';
COMMENT ON COLUMN album.start_year IS 'Optional album start year';
COMMENT ON COLUMN album.end_year IS 'Optional album end year';
COMMENT ON COLUMN album.no_tags IS 'If this album contains items with no tags';
COMMENT ON COLUMN album.created_at IS 'Record creation time';

CREATE TABLE IF NOT EXISTS album_tag_link (
    album_id    UUID NOT NULL,
    tag_id      UUID NOT NULL,

    CONSTRAINT album_tag_link_album_id_fk FOREIGN KEY (album_id)
        REFERENCES album (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT album_tag_link_tag_id_fk FOREIGN KEY (tag_id)
        REFERENCES tag (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS album_tag_link_album_id_idx ON album_tag_link(album_id);

COMMENT ON TABLE album_tag_link IS 'Link between album and tags';
COMMENT ON COLUMN album_tag_link.album_id IS 'Album reference';
COMMENT ON COLUMN album_tag_link.tag_id IS 'Tag reference';

CREATE TABLE IF NOT EXISTS album_excluded_tag_link (
    album_id    UUID NOT NULL,
    tag_id      UUID NOT NULL,

    CONSTRAINT album_excluded_tag_link_album_id_fk FOREIGN KEY (album_id)
        REFERENCES album (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT album_excluded_tag_link_tag_id_fk FOREIGN KEY (tag_id)
        REFERENCES tag (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS album_excluded_tag_link_album_id_idx ON album_excluded_tag_link(album_id);

COMMENT ON TABLE album_excluded_tag_link IS 'Link between album and excluded tags';
COMMENT ON COLUMN album_excluded_tag_link.album_id IS 'Album reference';
COMMENT ON COLUMN album_excluded_tag_link.tag_id IS 'Tag reference';
