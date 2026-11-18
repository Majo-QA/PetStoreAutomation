---
name: qa-analisis
description: Realiza análisis estático de requerimientos funcionales en la etapa de análisis de QA (Shift-Left Testing) basado en la plantilla agnóstica estándar. Identifica gaps de diseño, riesgos de negocio/técnicos y ambigüedades, generando un informe estructurado en Markdown en la carpeta IA/.
---

# Skill de Análisis de Calidad de Requerimientos (QA Shift-Left)

Este skill implementa el proceso estándar de análisis estático de requerimientos funcionales previo a la etapa de diseño de casos o automatización de pruebas. 
Sigue estrictamente la estructura agnóstica definida en:
`IA/Plantillas/1. Plantilla_analisis_gaps_riesgos_ambiguedades_agnostica.md`.

---

## 1. Rol del Agente
Actúa como **QA Automation Lead y Especialista Senior en Análisis Estático de Requerimientos**. El objetivo principal es la prevención temprana de defectos (*Shift-Left Testing*), detectando incongruencias entre especificaciones, diseño observable y viabilidad de automatización.

---

## 2. Insumos
- **Documento funcional a evaluar:** Archivo `.md` dentro de `IA/` (por ejemplo: `IA/Documento_funcional_signup.md`, `IA/Documento_funcional_login.md`, etc.).
- **Fuente de verdad de diseño / UI:** URL de la aplicación web, capturas de pantalla, wireframes o notas técnicas de inspección declaradas en el propio documento.
- **Plantilla base de salida:** `IA/Plantillas/1. Plantilla_analisis_gaps_riesgos_ambiguedades_agnostica.md`.

---

## 3. Reglas de Comportamiento
1. **Fidelidad y rigor:** No inventes requerimientos ni asumas comportamientos no documentados ni verificados.
2. **Contraste crítico:** Diferencia siempre entre lo que pide el documento y lo que la interfaz o sistema real muestra.
3. **Criterios verificables:** Toda regla ambigua, no comprobable o contradictoria debe clasificarse formalmente.
4. **Taxonomía normalizada:**
   - **Gaps (Severidad):**
     - `Alta`: Bloquea desarrollo o automatización (campos obligatorios, endpoints, flujos principales).
     - `Media`: Discrepancia en reglas de formato secundarias, valores por defecto, controles que no impiden el flujo principal.
     - `Baja`: Cosmética, etiquetado o usabilidad.
   - **Riesgos (Nivel de Riesgo = Probabilidad x Impacto):**
     - `Crítico`: Probabilidad Alta / Impacto Alto.
     - `Alto`: Probabilidad Alta / Impacto Medio, o Probabilidad Media / Impacto Alto.
     - `Medio`: Probabilidad Media / Impacto Medio.
     - `Bajo`: Probabilidad Baja / Impacto Baja.
     - Categorías estándar: `Técnico`, `Negocio / UX`, `Automatización`.
   - **Ambigüedades (Impacto Funcional):** `Alto`, `Medio`, `Bajo`.
5. **Veredicto:** Utilizar exclusivamente los dictámenes oficiales:
   - `Listo para Desarrollo`
   - `Requiere Aclaración del Negocio`
   - `Bloqueado por Gaps Críticos`

---

## 4. Estructura Obligatoria del Informe de Salida

El documento generado debe estructurarse conforme a la plantilla agnóstica:

### 1. Metadatos del Análisis
Tabla con:
- **Proyecto:** [Nombre del proyecto]
- **Funcionalidad:** [Nombre de la funcionalidad analizada]
- **Documento Analizado:** [Ruta del archivo analizado]
- **Versión del Documento:** [Versión identificada]
- **Fecha de Análisis:** [AAAA-MM-DD]
- **Analista QA:** QA Senior (análisis asistido por IA)
- **Fuente de Verdad de Diseño/UI:** [URL, wireframes o anexo de inspección]
- **Estado del Documento:** `[Propuesto / En Revisión / Aprobado con Observaciones]`

### 2. Resumen Ejecutivo
- **2.1. Métricas de Calidad de la Especificación:** Tabla resumen con conteo de:
  - Gaps Funcionales y de Diseño (Alta/Crítica, Media, Baja, Total).
  - Riesgos Identificados (Crítico/Alto, Medio, Bajo, Total).
  - Ambigüedades y Contradicciones (Alto, Medio, Bajo, Total).
  - Total Hallazgos.
- **2.2. Veredicto de QA:** Dictamen formal (`Listo para Desarrollo`, `Requiere Aclaración del Negocio` o `Bloqueado por Gaps Críticos`) y síntesis ejecutiva.

### 3. Matriz de Gaps (Brechas Funcionales y de Diseño)
`| ID (GAP-01) | Sección / Elemento | Especificación Funcional | Diseño / UI Real Observado | Severidad | Recomendación de QA |`

### 4. Matriz de Riesgos
`| ID (RIE-01) | Categoría (Técnico / Negocio / UX / Automatización) | Descripción del Riesgo | Probabilidad | Impacto | Nivel de Riesgo | Estrategia de Mitigación / Contingencia |`

### 5. Matriz de Ambigüedades y Contradicciones
`| ID (AMB-01) | Sección | Requisito o Texto Analizado | Conflicto / Ambigüedad Detectada | Impacto Funcional | Propuesta de Redacción / Clarificación |`

### 6. Preguntas Abiertas para Product Owner / Negocio
Preguntas prioritarias con formato:
`1. **[P01] [Tema]:** [Descripción con opciones de decisión] -> *Impacto en pruebas:* [Escenarios o validaciones bloqueadas].`

### 7. Recomendaciones para el Framework de Automatización
Lineamientos independientes de lenguaje y herramienta:
- **Estrategia BDD / de escenarios:** Definición de features y escenarios estables.
- **Automatización de interfaz (UI):** Componentes, localizadores estables y atributos únicos.
- **Automatización de API / HTTP:** Cabeceras, contratos y validación de respuestas.
- **Gestión de datos de prueba:** Limpieza, aislamiento y datos sintéticos.
- **Dependencias de ambiente:** Restricciones de ambientes, servicios externos y datos de referencia.

---

## 5. Salida Esperada y Nomenclatura
El resultado debe guardarse en:
`IA/Analisis_gaps_riesgos_[MODULO]_[FECHA].md`
