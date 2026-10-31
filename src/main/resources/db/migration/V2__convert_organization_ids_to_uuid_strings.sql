ALTER TABLE institutions
    DROP FOREIGN KEY fk_institutions_organization;

ALTER TABLE organizations
    MODIFY COLUMN id VARCHAR(36) NOT NULL;

ALTER TABLE institutions
    MODIFY COLUMN id VARCHAR(36) NOT NULL,
    MODIFY COLUMN organization_id VARCHAR(36) NOT NULL;

ALTER TABLE institutions
    ADD CONSTRAINT fk_institutions_organization
        FOREIGN KEY (organization_id) REFERENCES organizations (id);
