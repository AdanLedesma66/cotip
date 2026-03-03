CREATE TABLE IF NOT EXISTS api_client (
    id UUID PRIMARY KEY,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    client_name VARCHAR(120) NOT NULL,
    api_key_hash VARCHAR(128) NOT NULL,
    requests_per_minute INTEGER NOT NULL DEFAULT 120,
    CONSTRAINT uq_api_client_name UNIQUE (client_name),
    CONSTRAINT uq_api_client_key_hash UNIQUE (api_key_hash),
    CONSTRAINT ck_api_client_rpm_positive CHECK (requests_per_minute > 0)
);

CREATE INDEX IF NOT EXISTS idx_api_client_enabled ON api_client (enabled);
