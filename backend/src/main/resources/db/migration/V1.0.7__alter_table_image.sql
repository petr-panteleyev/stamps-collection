ALTER TABLE image
    ADD CONSTRAINT image_id_issue_item_fk FOREIGN KEY (id)
        REFERENCES issue_item (id) ON DELETE CASCADE ON UPDATE CASCADE;
