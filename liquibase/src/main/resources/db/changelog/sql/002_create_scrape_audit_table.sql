CREATE TABLE IF NOT EXISTS scrape_audit (
    id UUID PRIMARY KEY,
    provider VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    error_detail VARCHAR(500),
    raw_payload TEXT,
    executed_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_scrape_audit_provider_time ON scrape_audit (provider, executed_at DESC);
