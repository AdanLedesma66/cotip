<a name="readme-top"></a>

# Cotip - API de Tasas de Cambio de Divisas en Paraguay

**Cotip** es un proyecto de código abierto desarrollado en Java con Spring Boot, diseñado para obtener y centralizar las tasas de cambio de divisas extranjeras en Paraguay a partir de múltiples proveedores. La API de Cotip permite acceder a las tasas de compra y venta para diferentes monedas extranjeras en tiempo real, y las almacena en una base de datos PostgreSQL, ofreciendo una fuente de datos confiable y actualizada para aplicaciones financieras y análisis de mercado.

## Acerca del Proyecto

En Paraguay, las tasas de cambio de monedas pueden variar significativamente entre diferentes proveedores, lo que complica la comparación y el uso de estos datos en aplicaciones. **Cotip** resuelve este problema proporcionando una API que:

- **Centraliza los datos de cambio**: Cotip recopila las tasas de cambio de compra y venta de múltiples proveedores de confianza en Paraguay.
- **Almacena la información de manera estructurada**: La API guarda las tasas de cambio en una base de datos PostgreSQL, lo que permite su acceso histórico y en tiempo real.
- **Facilita el acceso programático**: Al ofrecer una API estandarizada, Cotip permite que otros sistemas y aplicaciones consuman estos datos de cambio sin problemas.

Este proyecto está optimizado para su despliegue en contenedores Docker y utiliza Jenkins para automatizar la integración y el despliegue continuo, lo que garantiza que las actualizaciones y mejoras se realicen de manera rápida y eficiente.

<p align="right">(<a href="#readme-top">volver al inicio</a>)</p>

### Tecnologías Principales

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
* ![Spring Boot](https://img.shields.io/badge/spring--boot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

### Herramientas de Soporte

* ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
* ![Jenkins](https://img.shields.io/badge/jenkins-%23D24939.svg?style=for-the-badge&logo=jenkins&logoColor=white)
* ![PostgreSQL](https://img.shields.io/badge/postgresql-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)

<p align="right">(<a href="#readme-top">volver al inicio</a>)</p>

## Roadmap

> **Nota:** La mayoría de los elementos en el roadmap están actualmente en desarrollo. Las contribuciones son bienvenidas para acelerar el progreso de estas funcionalidades.

- [x] Crear la API para obtener tasas de cambio y almacenarlas en PostgreSQL.
- [ ] Añadir soporte para nuevos proveedores de datos de cambio. *(En desarrollo)*
- [ ] Ampliar la documentación de la API con ejemplos de uso y mejores prácticas. *(En desarrollo)*
- [ ] Integración con herramientas de visualización y análisis de datos. *(En desarrollo)*
- [ ] Mejoras de rendimiento y escalabilidad en entornos Docker. *(En desarrollo)*

<p align="right">(<a href="#readme-top">volver al inicio</a>)</p>

## Proveedores

La API actualmente soporta la obtención de tasas de cambio de los siguientes proveedores:

- Banco Atlas
- Banco Continental
- Banco Familiar
- Banco Gnb
- Banco Nacional de Fomento
- Banco Rio
- Banco Solar
- Financiera Fic

<p align="right">(<a href="#readme-top">volver al inicio</a>)</p>