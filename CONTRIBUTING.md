# 🤝 Contributing

¡Gracias por tu interés en contribuir a **AI QA Test Optimizer**!

Este documento contiene unas reglas básicas para mantener el proyecto organizado y facilitar la colaboración.

---

## 📋 Requisitos

Antes de realizar cambios, asegúrate de tener instalado:

* ☕ **Java 17**
* 📦 **Maven**
* 🌿 **Git**

Si deseas utilizar la integración con OpenAI, también necesitarás configurar la variable de entorno:

```text
OPENAI_API_KEY
```

> ⚠️ **Importante:** nunca subas API Keys, contraseñas o archivos `.env` al repositorio.

---

## 🚀 Preparar el proyecto

Clona el repositorio:

```bash
git clone https://github.com/Oscarfsg19/qa-test-ai-optimizer
cd qa-test-ai-optimizer
```

Compila el proyecto:

```bash
mvn clean install
```

Ejecuta la aplicación:

```bash
mvn spring-boot:run
```

---

## 🤖 Modos de análisis

El proyecto permite trabajar con diferentes modos de análisis.

### 🔹 Análisis local

```properties
ai.enabled=false
ai.mock=false
```

Utiliza el analizador local sin depender de OpenAI.

### 🔹 Mock de IA

```properties
ai.enabled=true
ai.mock=true
```

Permite probar el flujo de análisis con IA sin realizar llamadas a la API de OpenAI.

### 🔹 OpenAI

```properties
ai.enabled=true
ai.mock=false
```

La API Key debe configurarse mediante la variable de entorno:

```text
OPENAI_API_KEY
```

---

## 🌿 Ramas

Se recomienda crear una rama independiente para cada cambio.

Ejemplo:

```bash
git checkout -b feature/nombre-del-cambio
```

Algunos ejemplos:

```text
feature/openai-analyzer
fix/respuesta-analisis
test/analizador-de-criterios
docs/actualizacion-readme
```

Esto ayuda a mantener separados los cambios y facilita su revisión.

---

## 🧪 Pruebas

Antes de realizar un Pull Request, ejecuta las pruebas:

```bash
mvn test
```

Si modificas una funcionalidad existente, procura actualizar o agregar las pruebas correspondientes.
