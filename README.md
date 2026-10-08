# Proyecto AI QA Test Optimizer

# <span style="color:#AB83E1"> **📌 ACME Corp — QA Challenge **

<br>

<span style="color:#91E1B9">

**AI QA Test Optimizer** es una prueba de concepto orientada a mejorar el proceso de análisis y mantenimiento de pruebas dentro de un entorno de desarrollo ágil.

La solución busca utilizar capacidades de análisis automatizado e inteligencia artificial para ayudar al equipo de QA a identificar:

* 🔎 Casos de prueba duplicados.
* ⚠️ Riesgos potenciales.
* 🔗 Problemas de trazabilidad.
* 🧪 Escenarios de prueba faltantes.
* ♻️ Oportunidades de optimización de la suite.
* 🤖 Oportunidades de automatización.
* 📊 Cobertura de criterios de aceptación.
* 🎯 Casos relevantes, parcialmente relevantes e irrelevantes.

La herramienta está planteada como un **asistente para QA**, donde la tecnología ayuda a acelerar el análisis inicial, mientras que la decisión final y validación de la cobertura permanecen bajo responsabilidad del equipo de calidad.

---

# 📋 Descripción del Proyecto

En organizaciones con múltiples equipos de desarrollo, ciclos de entrega frecuentes y una gran cantidad de casos de prueba manuales, mantener una suite de regresión eficiente puede convertirse en un reto.

El objetivo de este proyecto es demostrar cómo un asistente de QA puede analizar una historia de usuario, sus criterios de aceptación y una suite de pruebas existente para proporcionar información que ayude al equipo a tomar decisiones sobre la cobertura y mantenimiento de las pruebas.

El flujo principal de la aplicación es:

```text
Historia de usuario
        +
Criterios de aceptación
        +
Suite actual de pruebas
        │
        ▼
┌─────────────────────────┐
│   AI QA TEST OPTIMIZER  │
└─────────────────────────┘
        │
        ├──► Duplicados
        │
        ├──► Riesgos
        │
        ├──► Trazabilidad
        │
        ├──► Cobertura
        │
        ├──► Escenarios faltantes
        │
        └──► Recomendaciones
```

---

# 🎯 Objetivo

Demostrar una alternativa para reducir el esfuerzo manual asociado al análisis inicial de historias y al mantenimiento de suites de pruebas.

La solución busca que QA pueda pasar de un proceso principalmente reactivo a uno más orientado a la identificación temprana de riesgos y oportunidades de mejora.

### Principales objetivos

* Reducir el análisis repetitivo de casos de prueba.
* Identificar posibles duplicidades.
* Mejorar la trazabilidad entre requisitos y pruebas.
* Detectar escenarios que podrían no estar cubiertos.
* Identificar riesgos relacionados con una funcionalidad.
* Facilitar la priorización de pruebas.
* Generar recomendaciones para la evolución de la suite.
* Incorporar análisis semántico mediante inteligencia artificial.

---

# 🧠 Enfoque de IA para QA

La solución está diseñada bajo el concepto de **AI-Assisted Quality Engineering**.

La IA no reemplaza la decisión del QA.

El flujo propuesto es:

```text
        Historia / Cambio
                │
                ▼
         Análisis asistido
                │
       ┌────────┼────────┐
       ▼        ▼        ▼
     Riesgos Escenarios  Cobertura
       │        │        │
       └────────┼────────┘
                ▼
            QA Review
                │
                ▼
       Casos aprobados
                │
                ▼
        Suite de regresión
```

La responsabilidad del equipo de QA permanece en la validación de los resultados, priorización y decisión final sobre qué pruebas deben formar parte de la estrategia de calidad.

Actualmente el proyecto cuenta con una integración con OpenAI para realizar un análisis semántico de la historia de usuario, criterios de aceptación y casos de prueba.

---

# 🛠️ Tecnologías utilizadas

El proyecto utiliza las siguientes tecnologías:

* **Java 17** como lenguaje principal.
* **Spring Boot 3.4.5** como framework para la aplicación web.
* **Maven** como herramienta de construcción y gestión de dependencias.
* **JUnit 5** para pruebas automatizadas.
* **OpenAI Java SDK 4.70.0** para la integración con OpenAI.
* **OpenAI Responses API** para el análisis semántico.
* **HTML5** para la interfaz de usuario.
* **CSS3** para estilos y diseño visual.
* **JavaScript** para la interacción con el backend.
* **REST API** para la comunicación entre frontend y backend.
* **JSON** para el intercambio de información.
* **Git/GitHub** para control de versiones.

---

# 📋 Requisitos Previos

Antes de ejecutar el proyecto es necesario contar con:

### Java

Se requiere:

```text
Java 17+
```

Verificar instalación:

```sh
java -version
```

Ejemplo:

```text
java version "17.0.19"
```

### Maven

Verificar instalación:

```sh
mvn -version
```

Ejemplo:

```text
Apache Maven ...
Java version: 17
```

### Navegador

Se recomienda utilizar:

* Google Chrome
* Microsoft Edge
* Mozilla Firefox

---

# 📁 Estructura del Proyecto

```text
qa-test-ai-optimizer/
│
├── src/
│   │
│   ├── main/
│   │   │
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── acme/
│   │   │           └── qa/
│   │   │               │
│   │   │               ├── AiQaApplication.java
│   │   │               │
│   │   │               ├── config/
│   │   │               │   └── OpenAiConfig.java
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   └── QaController.java
│   │   │               │
│   │   │               ├── model/
│   │   │               │   ├── AnalysisRequest.java
│   │   │               │   ├── AnalysisResponse.java
│   │   │               │   └── TestCase.java
│   │   │               │
│   │   │               └── service/
│   │   │                   ├── AiQaService.java
│   │   │                   ├── LocalQaAnalyzer.java
│   │   │                   └── OpenAiQaAnalyzer.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html
│   │           └── test-cases.json
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── acme/
│                   └── qa/
│                       └── LocalQaAnalyzerTest.java
│
├── pom.xml
├── README.md
└── .gitignore
```

---

# 📂 Directorios y componentes principales

### `AiQaApplication.java`

Es el punto de entrada de la aplicación Spring Boot.

Se encarga de iniciar el servidor y dejar disponible la aplicación mediante:

```text
http://localhost:8080
```

Además, la aplicación puede abrir automáticamente el navegador cuando Spring Boot termina de iniciar.

---

### `model/TestCase.java`

Representa la estructura de un caso de prueba.

La información contempla elementos como:

```text
ID
Título
Pasos
Resultado esperado
Prioridad
```

Esto permite trabajar con una representación estructurada de la suite de pruebas.

---

### `model/AnalysisRequest.java`

Representa la información enviada al backend para realizar el análisis.

Incluye:

```text
Historia de usuario
Criterios de aceptación
Casos de prueba
```

---

### `model/AnalysisResponse.java`

Representa la respuesta estructurada generada por el análisis.

Además de las métricas originales, actualmente contempla información relacionada con:

* Casos relevantes.
* Casos parcialmente relevantes.
* Casos irrelevantes.
* Cobertura de criterios.
* Criterios cubiertos.
* Criterios parcialmente cubiertos.
* Criterios no cubiertos.
* Evaluación individual de casos.
* Escenarios faltantes detallados.
* Resumen ejecutivo.
* Recomendaciones.

---

### `service/LocalQaAnalyzer.java`

Contiene la lógica principal del análisis de QA.

Actualmente permite realizar análisis sobre la suite existente, incluyendo la identificación de posibles casos duplicados y otros indicadores utilizados por la aplicación.

Esta capa también funciona como mecanismo de respaldo cuando el análisis mediante IA no está disponible.

---

### `service/AiQaService.java`

Es el servicio encargado de seleccionar el tipo de análisis que se ejecutará.

Dependiendo de la configuración puede utilizar:

* `LocalQaAnalyzer`.
* Mock AI.
* `OpenAiQaAnalyzer`.

Si el análisis mediante OpenAI genera un error, el servicio utiliza automáticamente `LocalQaAnalyzer` como fallback.

---

### `service/OpenAiQaAnalyzer.java`

Contiene la integración con OpenAI para realizar un análisis semántico de la historia de usuario, criterios de aceptación y casos de prueba.

El análisis permite obtener información adicional como:

* Cobertura de criterios de aceptación.
* Casos relevantes.
* Casos parcialmente relevantes.
* Casos irrelevantes.
* Nivel de riesgo.
* Duplicidades semánticas.
* Escenarios faltantes.
* Recomendaciones.
* Resumen ejecutivo.

---

### `config/OpenAiConfig.java`

Contiene la configuración del cliente de OpenAI.

El cliente utiliza la variable de entorno:

```text
OPENAI_API_KEY
```

para autenticarse con OpenAI.

La API key no debe almacenarse directamente en el código fuente ni subirse al repositorio.

---

### `LocalQaAnalyzerTest.java`

Contiene pruebas automatizadas para validar el comportamiento del analizador.

Uno de los escenarios actuales verifica la detección de posibles casos de prueba duplicados.

---

### `static/index.html`

Contiene la interfaz web de la aplicación.

Desde esta pantalla el usuario puede:

1. Introducir una historia de usuario.
2. Introducir criterios de aceptación.
3. Cargar una suite de pruebas de ejemplo.
4. Ejecutar el análisis.
5. Visualizar métricas y hallazgos.

---

### `static/test-cases.json`

Contiene una suite de casos de prueba de ejemplo utilizada para realizar demostraciones y validar el funcionamiento del analizador.

---

# 🖥️ Interfaz de usuario

La aplicación cuenta con tres secciones principales:

## 1. Requisito

Permite introducir:

* Historia de usuario.
* Criterios de aceptación.

Ejemplo:

```text
Como usuario quiero editar un mensaje enviado
para corregir errores después de enviarlo.
```

---

## 2. Suite actual

Permite introducir la suite existente en formato JSON.

También dispone del botón:

```text
Cargar ejemplo
```

que permite cargar automáticamente una suite de prueba representativa.

---

## 3. Resultado

Después de ejecutar el análisis, la aplicación presenta información como:

* Número total de casos.
* Grupos de duplicados.
* Casos con trazabilidad.
* Casos de alto riesgo.
* Casos relevantes.
* Casos parcialmente relevantes.
* Casos irrelevantes.
* Cobertura de criterios de aceptación.
* Criterios cubiertos.
* Criterios parcialmente cubiertos.
* Criterios no cubiertos.
* Resumen ejecutivo.
* Hallazgos.
* Escenarios faltantes.
* Recomendaciones.

---

# 🤖 Configuración de IA

La aplicación permite trabajar en tres modos diferentes.

## Modo local

```properties
ai.enabled=false
ai.mock=false
```

Utiliza `LocalQaAnalyzer` y no realiza llamadas a OpenAI.

---

## Modo Mock

```properties
ai.enabled=true
ai.mock=true
```

Permite probar el flujo de análisis mediante IA sin realizar llamadas reales a OpenAI.

Este modo es útil para:

* Desarrollo.
* Pruebas.
* Demostraciones.
* Validación de la interfaz.
* Pruebas sin consumir créditos de API.

---

## Modo OpenAI

```properties
ai.enabled=true
ai.mock=false
```

Utiliza `OpenAiQaAnalyzer` y realiza el análisis mediante OpenAI.

La API key debe estar configurada mediante la variable de entorno:

```text
OPENAI_API_KEY
```

Ejemplo de configuración:

```properties
spring.application.name=ai-qa-test-optimizer
server.port=8080

ai.enabled=true
ai.mock=false
ai.model=gpt-5.6-luna
```

Para realizar pruebas sin consumir API:

```properties
ai.enabled=true
ai.mock=true
ai.model=gpt-5.6-luna
```

---

# ▶️ Ejecución del Proyecto

## 1. Clonar el proyecto

```sh
git clone https://github.com/Oscarfsg19/qa-test-ai-optimizer.git
```

Ingresar al proyecto:

```sh
cd qa-test-ai-optimizer
```

---

## 2. Ejecutar las pruebas

Antes de iniciar la aplicación se recomienda validar que las pruebas automatizadas pasen correctamente:

```sh
mvn clean test
```

Resultado esperado:

```text
Tests run: 1
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

---

## 3. Ejecutar la aplicación

```sh
mvn spring-boot:run
```

Spring Boot iniciará el servidor en:

```text
http://localhost:8080
```

El navegador puede abrirse automáticamente cuando la aplicación termina de iniciar.

Si no se abre automáticamente, acceder manualmente a:

```text
http://localhost:8080
```

---

# 🧪 Ejemplo para la demostración

Para realizar una demostración se puede utilizar la siguiente historia:

```text
Como usuario de la plataforma de mensajería empresarial,
quiero poder editar o eliminar un mensaje que ya envié,
para corregir información incorrecta o retirar contenido
enviado por error, manteniendo la sincronización y
trazabilidad de los cambios para todos los participantes.
```

### Criterios de aceptación

```text
1. El usuario puede editar únicamente mensajes enviados por él mismo.

2. El usuario puede editar un mensaje únicamente durante los 15 minutos
posteriores a su envío.

3. Al editar un mensaje, todos los participantes deben visualizar
el contenido actualizado y una indicación de que fue editado.

4. La edición debe sincronizarse correctamente entre diferentes
sesiones y dispositivos.

5. Si el usuario intenta editar un mensaje fuera del período permitido,
la operación debe ser rechazada.

6. El usuario puede eliminar un mensaje enviado dentro del período permitido.

7. Después de eliminar un mensaje, los participantes deben visualizar
que el mensaje fue eliminado.

8. Si dos dispositivos intentan modificar el mismo mensaje
aproximadamente al mismo tiempo, el sistema debe mantener un estado consistente.

9. Si se pierde temporalmente la conexión durante una edición o eliminación,
el sistema debe informar el resultado de la operación.

10. Las modificaciones deben conservar información de auditoría.

11. Un usuario no puede modificar ni eliminar mensajes pertenecientes
a otro usuario.

12. Las notificaciones relacionadas con el mensaje deben mantener
un estado consistente después de una edición o eliminación.
```

Después:

1. Introducir la historia.
2. Introducir los criterios.
3. Pulsar **Cargar ejemplo**.
4. Pulsar **Analizar con IA**.
5. Revisar los resultados.
6. Explicar los riesgos y escenarios detectados.
7. Mostrar cómo el QA valida los resultados antes de incorporarlos a la suite.

---

# 🔌 API

La interfaz utiliza un endpoint para ejecutar el análisis:

```text
POST /api/analyze
```

El frontend envía información estructurada mediante JSON:

```json
{
  "userStory": "Historia de usuario",
  "acceptanceCriteria": "Criterios de aceptación",
  "testCases": []
}
```

El backend procesa la información y devuelve los resultados utilizados por la interfaz.

La respuesta puede incluir información adicional generada por el análisis semántico:

```text
Casos relevantes
Casos parcialmente relevantes
Casos irrelevantes

Criterios cubiertos
Criterios parcialmente cubiertos
Criterios no cubiertos
Porcentaje de cobertura

Cobertura por criterio
Evaluación por caso
Escenarios faltantes
Recomendaciones
Resumen ejecutivo
```

---

# 📊 Valor para el proceso de QA

La solución está orientada a atacar algunos de los problemas habituales en equipos con suites de pruebas grandes:

| Problema                     | Enfoque de la solución                   |
| ---------------------------- | ---------------------------------------- |
| Casos duplicados             | Identificación de posibles duplicidades  |
| Suites difíciles de mantener | Análisis de redundancias                 |
| Poca trazabilidad            | Relación entre requisito y pruebas       |
| Cobertura incompleta         | Identificación de escenarios faltantes   |
| Regresiones                  | Identificación de áreas de riesgo        |
| Mucho trabajo manual         | Asistencia automatizada para el análisis |
| Casos irrelevantes           | Evaluación semántica mediante IA         |

---

# 🚀 Evolución propuesta

El prototipo puede evolucionar hacia una solución más completa incorporando:

### IA generativa

La primera integración con IA ya fue implementada mediante OpenAI.

Actualmente permite:

* Analizar historias de usuario.
* Analizar criterios de aceptación.
* Evaluar la cobertura de los criterios.
* Identificar casos relevantes e irrelevantes.
* Identificar riesgos.
* Detectar escenarios faltantes.
* Generar recomendaciones.
* Generar un resumen ejecutivo.

Como siguiente evolución se pueden incorporar:

* Generación automática de nuevos casos de prueba.
* Identificación de candidatos para automatización.
* Integración con herramientas de gestión de pruebas.

### Integración CI/CD

La solución podría integrarse posteriormente con pipelines para analizar cambios automáticamente:

```text
Pull Request
     │
     ▼
Análisis de cambios
     │
     ▼
Evaluación de riesgo
     │
     ▼
Selección de regresión
     │
     ▼
Automated Tests
     │
     ▼
Reporte QA
```

### Métricas

Como siguiente evolución se pueden incorporar métricas como:

* Cobertura por funcionalidad.
* Cobertura por criterio de aceptación.
* Tendencia de defectos.
* Riesgo por componente.
* Porcentaje de automatización.
* Casos duplicados.
* Casos obsoletos.
* Tiempo de ejecución de regresión.

---

# 🏗️ Estrategia de implementación

Para un equipo pequeño de QA, una adopción progresiva puede dividirse en fases:

### Fase 1 — Visibilidad

Centralizar:

* Casos.
* Requisitos.
* Resultados.
* Métricas.
* Riesgos.

### Fase 2 — Asistencia

Incorporar IA para:

* Generación de escenarios.
* Identificación de riesgos.
* Detección de duplicidades.
* Sugerencias de automatización.

### Fase 3 — Integración

Integrar con:

* Repositorio Git.
* CI/CD.
* Gestión de requerimientos.
* Herramientas de gestión de pruebas.

### Fase 4 — Optimización continua

Utilizar métricas históricas para identificar:

* Áreas con mayor número de regresiones.
* Pruebas con bajo valor.
* Áreas con baja cobertura.
* Oportunidades de automatización.

---

---

# 👨‍💻 Autor

### **Oscar Fernando Sánchez Gámez**

**Proyecto:** AI QA Test Optimizer
**Desafío:** ACME Corp — QA Challenge
**Versión:** 1.0
**Fecha:** OCtubre 2026
