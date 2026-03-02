<a name="readme-top"></a>

# Cotip - API de Tasas de Cambio de Divisas en Paraguay

**Cotip** es un proyecto de código abierto desarrollado en Java con Spring Boot, diseñado para obtener y centralizar las tasas de cambio de divisas extranjeras en Paraguay a partir de múltiples proveedores. La API de Cotip permite acceder a las tasas de compra y venta para diferentes monedas extranjeras en tiempo real, y las almacena en una base de datos PostgreSQL, ofreciendo una fuente de datos confiable y actualizada para aplicaciones financieras y análisis de mercado.

## Acerca del Proyecto

En Paraguay, las tasas de cambio de monedas pueden variar significativamente entre diferentes proveedores, lo que complica la comparación y el uso de estos datos en aplicaciones. **Cotip** resuelve este problema proporcionando una API que:

- **Centraliza los datos de cambio**: Cotip recopila las tasas de cambio de compra y venta de múltiples proveedores de confianza en Paraguay.
- **Almacena la información de manera estructurada**: La API guarda las tasas de cambio en una base de datos PostgreSQL, lo que permite su acceso histórico y en tiempo real.
- **Facilita el acceso programático**: Al ofrecer una API estandarizada, Cotip permite que otros sistemas y aplicaciones consuman estos datos de cambio sin problemas.

## Tecnologías utilizadas

- ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
- ![Spring Boot](https://img.shields.io/badge/spring--boot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
- Spring Boot 4+
- Spring Cache
- Spring Retry
- ShedLock
- Virtual Threads (Project Loom)
- Spring Boot Actuator
- Micrometer
- Liquibase
- Maven

---

## Requisitos previos

Para ejecutar el proyecto es necesario contar con:

- JDK 25 o superior
- Maven 3.9 o superior
- Un IDE compatible con Spring Boot

## Observabilidad

Cotip expone endpoints de observabilidad para monitoreo operativo:

- `/actuator/health`
- `/actuator/info`
- `/actuator/metrics`
- `/actuator/prometheus`

Tambien se incluyen metricas custom para scraping, ingesta y frescura de datos.

## Configuracion por perfiles

- `application-local.yml`: ejecucion local con PostgreSQL
- `application-dev.yml`: entorno de desarrollo con PostgreSQL
- `application-prod.yml`: entorno productivo con PostgreSQL

Para credenciales y secretos usar variables de entorno (`COTIP_*`).

Para Cambios Chaco se pueden configurar sucursales por ID con:

- `COTIP_CHACO_BASE_URL` (default: `https://www.cambioschaco.com.py/api`)
- `COTIP_CHACO_DEFAULT_BRANCH_OFFICE_ID` (default: `3`)

Versionado de API:

- `COTIP_API_VERSION` (default: `v1`)
- `COTIP_API_BASE_PATH` (default: `/cotip/v1`)

Timeouts HTTP para scrapers:

- `COTIP_HTTP_CONNECT_TIMEOUT_SECONDS` (default: `5`)
- `COTIP_HTTP_READ_TIMEOUT_SECONDS` (default: `12`)

## Estructura de modulos

- `domain`: tipos y reglas base de dominio
- `application`: puertos y contratos de casos de uso
- `liquibase`: dependency de Liquibase y changesets de base de datos
- `infrastructure`: adaptadores de entrada/salida, persistencia, scraping y bootstrap

## Migraciones de base de datos

- Configuracion de host, base y esquema: `liquibase/liquibase-cotip.properties`
- Master changelog: `liquibase/src/main/resources/db/changelog/db.changelog-master.yaml`
- Cambios por archivo yaml: `liquibase/src/main/resources/db/changelog/changesets/`

Ejecucion:

```bash
./mvnw -pl liquibase liquibase:update
```

## Pipeline de scraping

Se utiliza un pipeline por proveedor con etapas estandarizadas:

- `fetchRaw`
- `parse`
- `normalize`
- `validate`

Ademas, cada ejecucion guarda auditoria del payload crudo y resultado (exito o falla).

Para verificar la versión de Java:

```bash
java -version
Ejecución del proyecto
Clonar el repositorio:

git clone https://github.com/AdanLedesma66/cotip.git
cd cotip
Ejecutar en modo desarrollo con Maven:

mvn spring-boot:run
Ejecutar como aplicación empaquetada:

mvn clean package
java -jar target/*.jar

Con estructura multi-modulo, el arranque recomendado es desde `infrastructure`:

```bash
./mvnw -pl infrastructure spring-boot:run
```
Ejemplo de servicios:

- `GET /cotip/v1/version`: version activa de API
- `GET /cotip/v1/cambios-chaco`: cotizaciones de una sucursal por defecto (configurable)
- `GET /cotip/v1/cambios-chaco/sucursal/{branchOfficeId}`: cotizaciones por id de sucursal
- `GET /cotip/v1/cambios-chaco/sucursal?id=2`: busqueda por id via query param
- `GET /cotip/v1/cambios-chaco/sucursal?nombre=Shopping Villa Morra - Asuncion`: busqueda por nombre de sucursal
- `GET /cotip/v1/cambios-chaco/sucursales`: catalogo de sucursales disponibles

curl --location 'localhost:8080/cotip/v1/banco-continental'
Response:

{
  "data": [
    {
      "exchangeRate": "US Dollar Check/Transfer",
      "currencyCode": "USD",
      "buyRate": 6560,
      "sellRate": 6800,
      "buyRateStatus": "UNCHANGED",
      "sellRateStatus": "UNCHANGED",
      "enabled": true,
      "provider": "Continental Bank",
      "branchOffice": null,
      "city": null,
      "lastUpdated": "2026-01-20T13:56:40.7522049-03:00"
    }
  ]
}
```
<p align="right">(<a href="#readme-top">volver al inicio</a>)</p> 
