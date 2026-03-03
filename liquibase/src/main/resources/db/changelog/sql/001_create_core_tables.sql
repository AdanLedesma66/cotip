CREATE TABLE IF NOT EXISTS branch_office (
    id UUID PRIMARY KEY,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    provider VARCHAR(100) NOT NULL,
    external_branch_id VARCHAR(100),
    name VARCHAR(200) NOT NULL,
    department VARCHAR(150),
    city VARCHAR(150),
    neighborhood VARCHAR(150),
    CONSTRAINT uq_branch_office_provider_external UNIQUE (provider, external_branch_id),
    CONSTRAINT uq_branch_office_provider_name UNIQUE (provider, name)
);

CREATE INDEX IF NOT EXISTS idx_branch_office_provider ON branch_office (provider);
CREATE INDEX IF NOT EXISTS idx_branch_office_department ON branch_office (department);
CREATE INDEX IF NOT EXISTS idx_branch_office_city ON branch_office (city);
CREATE INDEX IF NOT EXISTS idx_branch_office_enabled ON branch_office (enabled);

CREATE TABLE IF NOT EXISTS cotip_entity (
    id UUID PRIMARY KEY,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    provider VARCHAR(100) NOT NULL,
    city VARCHAR(150),
    branch_office VARCHAR(200),
    branch_office_id UUID,
    exchange_rate VARCHAR(200) NOT NULL,
    currency_code VARCHAR(10) NOT NULL,
    currency_name VARCHAR(150) NOT NULL,
    quote_modality VARCHAR(30) NOT NULL,
    buy_rate BIGINT,
    sell_rate BIGINT,
    buy_rate_status VARCHAR(20),
    sell_rate_status VARCHAR(20),
    CONSTRAINT fk_cotip_entity_branch_office
        FOREIGN KEY (branch_office_id) REFERENCES branch_office(id)
);

CREATE INDEX IF NOT EXISTS idx_cotip_provider_updated_at ON cotip_entity (provider, updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_cotip_provider_rate_updated_at ON cotip_entity (provider, exchange_rate, updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_cotip_provider_currency_modality_branch_updated_at
    ON cotip_entity (provider, currency_code, quote_modality, branch_office, updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_cotip_entity_branch_office_id ON cotip_entity (branch_office_id);
