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
- H2 Database (en memoria)
- Spring Cache
- Spring Retry
- Virtual Threads (Project Loom)
- Maven

---

## Requisitos previos

Para ejecutar el proyecto es necesario contar con:

- JDK 21 o superior
- Maven 3.9 o superior
- Un IDE compatible con Spring Boot

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
Ejemplo de servicios:

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
      "location": null,
      "city": null,
      "lastUpdated": "2026-01-20T13:56:40.7522049-03:00"
    }
  ]
}
```
<p align="right">(<a href="#readme-top">volver al inicio</a>)</p> 