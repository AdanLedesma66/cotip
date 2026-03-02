CREATE TABLE IF NOT EXISTS cotip_entity (
    id UUID PRIMARY KEY,
    habilitado BOOLEAN NOT NULL,
    proveedor VARCHAR(100) NOT NULL,
    ciudad VARCHAR(150),
    fecha_carga TIMESTAMP WITH TIME ZONE NOT NULL,
    cotip_details JSONB,
    tipo_cambio VARCHAR(200) NOT NULL,
    codigo_moneda VARCHAR(10),
    tasa_compra BIGINT,
    tasa_venta BIGINT,
    estado_compra VARCHAR(20),
    estado_venta VARCHAR(20)
);

CREATE INDEX IF NOT EXISTS idx_cotip_provider_upload_date ON cotip_entity (proveedor, fecha_carga DESC);
CREATE INDEX IF NOT EXISTS idx_cotip_provider_rate ON cotip_entity (proveedor, tipo_cambio, fecha_carga DESC);
