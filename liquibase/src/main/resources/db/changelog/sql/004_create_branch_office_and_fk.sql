CREATE TABLE IF NOT EXISTS branch_office (
    id UUID PRIMARY KEY,
    provider VARCHAR(100) NOT NULL,
    external_branch_id VARCHAR(100),
    name VARCHAR(200) NOT NULL,
    department VARCHAR(150),
    city VARCHAR(150),
    neighborhood VARCHAR(150),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uq_branch_office_provider_external UNIQUE (provider, external_branch_id),
    CONSTRAINT uq_branch_office_provider_name UNIQUE (provider, name)
);

ALTER TABLE cotip_entity
    ADD COLUMN IF NOT EXISTS branch_office_id UUID;

ALTER TABLE cotip_entity
    ADD CONSTRAINT fk_cotip_entity_branch_office
        FOREIGN KEY (branch_office_id) REFERENCES branch_office(id);

CREATE INDEX IF NOT EXISTS idx_branch_office_provider ON branch_office (provider);
CREATE INDEX IF NOT EXISTS idx_branch_office_department ON branch_office (department);
CREATE INDEX IF NOT EXISTS idx_branch_office_city ON branch_office (city);
CREATE INDEX IF NOT EXISTS idx_cotip_entity_branch_office_id ON cotip_entity (branch_office_id);
