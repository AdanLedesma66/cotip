# CODEX.md

Guia corta para trabajar en `cotip`.

## Principios del proyecto

- Menos es mas: cambios pequenos, claros y con impacto directo.
- Mantener separacion de flujos:
  - `request` -> cache -> DB
  - `scheduler` -> proveedor externo -> normaliza/valida -> DB -> evict cache
- No llamar proveedores externos desde endpoints.
- No reintroducir logica legacy en espanol para schema DB. Todo nuevo en ingles.

## Convenciones clave

- Paquetes actuales usan `py.com.cotip.insfrastructure` (con typo). Mantener consistencia salvo refactor global explicito.
- Modelo de cotizacion:
  - usar `exchangeRate` como nombre visible
  - usar `currencyCode` + `quoteModality` para identidad funcional
  - no reintroducir `currencyName`
- Para Cambios Chaco, sucursales se validan/consultan desde DB (`branch_office`), no desde catalogo hardcoded.

## Seguridad y versionado

- Header de version: `cotip-api-version` (`v1`).
- Header de cliente: `cotip-api-key`.
- Errores funcionales deben pasar por `GlobalExceptionHandler` usando `CotipException`.

## Cache y scheduler

- Cache provider por defecto: Redis (`cotip.cache.provider=redis`).
- `ehcache` queda como opcion local/fallback.
- TTL por proveedor configurable en `application.yml` (`cotip.cache.ttl.*`).
- Scheduler por proveedor configurable con cron (`cotip.ingestion.scheduler.*`).

## Arquitectura por modulo

- `domain`: puertos, enums, modelos.
- `application`: casos de uso (lectura y sync separados).
- `infrastructure`: adapters (REST, DB, scraping, cache, scheduler, seguridad).
- `liquibase`: schema from-scratch y cambios de DB.

## Regla para nuevos cambios

- Si agregas un proveedor nuevo:
  1. Implementar scraper con pipeline `fetchRaw -> parse -> normalize -> validate`.
  2. Ingestar via `SyncExchangeRatesUseCase`.
  3. Exponer lectura solo desde DB (`GetExchangeRatesUseCase`).
  4. Configurar cache alias + TTL + cron.

## Comandos utiles

- Compilar: `./mvnw -pl infrastructure -am -DskipTests compile`
- Run app: `./mvnw -pl infrastructure spring-boot:run`
- Migraciones: `./mvnw -pl liquibase liquibase:update`
