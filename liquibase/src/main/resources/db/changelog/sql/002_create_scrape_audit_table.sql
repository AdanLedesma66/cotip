CREATE TABLE IF NOT EXISTS scrape_audit (
    id UUID PRIMARY KEY,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    provider VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    error_detail VARCHAR(500),
    raw_payload TEXT
);

CREATE INDEX IF NOT EXISTS idx_scrape_audit_provider_updated_at ON scrape_audit (provider, updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_scrape_audit_enabled ON scrape_audit (enabled);
