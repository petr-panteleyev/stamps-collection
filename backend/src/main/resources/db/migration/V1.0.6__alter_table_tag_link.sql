ALTER TABLE tag_link
    ADD CONSTRAINT tag_link_item_id_fk FOREIGN KEY (item_id)
        REFERENCES issue_item (id) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE INDEX IF NOT EXISTS tag_link_item_id_idx ON tag_link(item_id);