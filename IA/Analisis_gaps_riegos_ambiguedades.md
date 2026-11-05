# Informe de Calidad de Requerimientos: Sign Up (New Account), documento v4.0

> **Alcance de este informe:** análisis estático del documento funcional v4.0. La columna "Diseño / UI Real Observado" usa únicamente lo que el propio documento declara en su Anexo B (inspección del 29/10/2026). No se verificó la página en esta revisión. Los hallazgos marcados como "por verificar" dependen de esa verificación.

---

## 1. Metadatos del Análisis

| Atributo | Detalle |
| :---- | :---- |
| **Proyecto** | PetStoreAutomation |
| **Funcionalidad** | Sign Up (New Account) |
| **Documento Analizado** | `IA/documento_funcional_signup_v4.md` |
| **Versión del Documento** | 4.0 |
| **Fecha de Análisis** | 2026-11-02 |
| **Analista QA** | QA Senior (análisis asistido por IA; pendiente de revisión humana) |
| **Fuente de Verdad de Diseño/UI** | `https://petstore.octoperf.com/account/new`, según Anexo B del documento (inspección del 29/10/2026). No re-verificada en este informe. |
| **Estado del Documento** | Propuesto / En Revisión (existen preguntas abiertas, Anexo A) |

---

## 2. Resumen Ejecutivo

### 2.1. Métricas de Calidad de la Especificación

| Categoría | Alta / Crítica | Media | Baja | Total |
| :---- | :---: | :---: | :---: | :---: |
| **Gaps Funcionales y de Diseño** | 4 | 4 | 2 | **10** |
| **Riesgos Identificados** | 5 | 2 | 0 | **7** |
| **Ambigüedades y Contradicciones** | 1 | 7 | 3 | **11** |
| **Total Hallazgos** | **10** | **13** | **5** | **28** |

### 2.2. Veredicto de QA

> **Dictamen:** Requiere Aclaración del Negocio

> **Síntesis:** El documento define con precisión los formatos y longitudes de la mayoría de los campos, y su alcance está claramente delimitado. Sin embargo, el mecanismo de envío documentado (respuestas JSON con códigos `REG_*`) no coincide con lo que el documento declara observar en la página (formulario HTML con `POST /account/new`, sin códigos visibles). Eso afecta directamente a los escenarios de error (§12, Escenarios 2, 3 y 5). Además, la validación en cliente sigue sin definirse y el resultado esperado de la sanitización no está especificado. Los dos riesgos críticos (RIE-01 y RIE-02) dependen de las preguntas P5 y P13. Hasta resolverlas, los escenarios de error no deberían considerarse listos para automatizar. Los escenarios de flujo exitoso y de formatos sí pueden avanzar con la información disponible.

---

## 3. Matriz de Gaps (Brechas Funcionales y de Diseño)

> **Criterios de Severidad:**

> * **Alta:** Bloquea el desarrollo o la automatización; discrepancia directa en campos obligatorios, mecanismo de envío o flujos principales.
> * **Media:** Discrepancia en reglas de formato secundarias, valores por defecto, textos de ayuda o controles visuales que no impiden el flujo principal.
> * **Baja:** Diferencias cosméticas menores, inconsistencias de etiquetado o mejoras sugeridas de usabilidad.

| ID | Sección / Elemento | Especificación Funcional | Diseño / UI Real Observado | Severidad | Recomendación de QA |
| :---: | :---- | :---- | :---- | :---: | :---- |
| `GAP-01` | §5 (paso 4), §6.2 / Mecanismo de envío | Respuestas en formato JSON propuesto (`status`, `error.code`, `error.field`). | Formulario HTML con envío `POST /account/new`. No se observa contrato JSON (Anexo B y §6.2). | `Alta` | Confirmar mecanismo de envío y formato de respuesta (P5, P13). No asumir respuestas JSON hasta confirmarlo. |
| `GAP-02` | §6.3 / Catálogo de errores | Códigos `REG_001` a `REG_500` con HTTP y mensaje. | Ningún código `REG_*` es visible en la UI (§6.3 y Anexo B). | `Alta` | Validar el catálogo (P13) antes de escribir aserciones sobre códigos de error. |
| `GAP-03` | §2 Incluye, §4, §5 (paso 5), §7.4 / Validación en cliente | Validación en cliente con mensajes inline y feedback en tiempo real. | No se observa JavaScript ni atributos de validación (§10 y Anexo B: "Sí (tiempo real)" en v3, "No se observa JavaScript" en la UI). | `Alta` | Decidir si la validación de cliente es requisito o si basta con la de servidor (P12). |
| `GAP-04` | §6.1 / Obligatoriedad de campos | Obligatorios: todos los campos excepto *Address 2* y las preferencias de perfil (P3). | Sin indicación visual de obligatoriedad: sin asterisco ni atributo `required` (§6.1). | `Alta` | Definir si la UI debe indicar los campos obligatorios y cómo se verifica la obligatoriedad desde la UI. |
| `GAP-05` | §5 (Flujos Alternos) / Cancelar o reset | Botón de cancelar / reset que restablece valores por defecto (en v3). | Botón no observado en la UI (§5 y P8). | `Media` | Decidir si se agrega o se elimina de la especificación (P8). |
| `GAP-06` | §7.2 / Visibilidad de contraseña | Toggle "mostrar / ocultar contraseña". | No observado en la UI (§7.2 y P11). | `Media` | Decidir si es requisito (P11). |
| `GAP-07` | §5, §6.1 / Campos ocultos | No documentados en la especificación. | El formulario incluye campos ocultos `_sourcePage` y `__fp` (Anexo B). | `Media` | Decidir si forman parte del alcance de seguridad (P14). |
| `GAP-08` | §8 / Accesibilidad y diseño adaptable | WCAG 2.1 AA y diseño responsive (móvil, tablet, desktop). | Marcado en el documento como "No verificado" (§8). | `Media` | Verificar en ambiente autorizado o dejar explícito que queda fuera de la validación actual. |
| `GAP-09` | §5 (Flujos Alternos) / Enlace a Sign In | Enlace "¿Ya tienes cuenta? Inicia sesión" dentro del formulario (en v3). | Solo existe el enlace *Sign In* del encabezado (`/account`) (Anexo B). | `Baja` | El texto ya se ajustó en v4. Falta decidir si se agrega el enlace (P7). |
| `GAP-10` | §5 (Flujo Principal, paso 8) y §12 / Mensaje post-registro | Redirección a Home (`/catalog`). | Redirección confirmada por negocio (P4). No se define si se muestra un mensaje de confirmación. | `Baja` | Definir si la redirección debe acompañarse de un mensaje (P4, pendiente menor). |

---

## 4. Matriz de Riesgos

> **Nivel de Riesgo (Probabilidad x Impacto):**

> * **Crítico:** Probabilidad Alta / Impacto Alto (riesgo inminente de fallo en producción o bloqueo total de pruebas).
> * **Alto:** Probabilidad Alta / Impacto Medio, o Probabilidad Media / Impacto Alto.
> * **Medio:** Probabilidad Media / Impacto Medio.
> * **Bajo:** Probabilidad Baja / Impacto Bajo.

| ID | Categoría | Descripción del Riesgo | Probabilidad | Impacto | Nivel de Riesgo | Estrategia de Mitigación / Contingencia |
| :---: | :---: | :---- | :---: | :---: | :---: | :---- |
| `RIE-01` | `Técnico` | El catálogo de códigos `REG_*` no está verificado (GAP-02). Los escenarios 2, 3 y 5 de §12 podrían basarse en códigos que la aplicación no devuelve. | Alta | Alta | `Crítico` | Resolver P13 y P5 antes de automatizar los escenarios de error. Validar en ambiente autorizado. |
| `RIE-02` | `Automatización` | El envío es un formulario HTML y no una API JSON (GAP-01). Las validaciones basadas en códigos y campos JSON darían falsos negativos o no se podrían ejecutar. | Alta | Alta | `Crítico` | Confirmar el mecanismo real (P5, P13). Definir la estrategia de validación de respuesta según lo que devuelva la aplicación. |
| `RIE-03` | `Negocio / UX` | Pérdida de datos si la persistencia del formulario tras un error no se cumple (§7.3). Es un comportamiento "por verificar" (P5). | Media | Alta | `Alto` | Verificar el comportamiento en ambiente autorizado antes de cerrar el criterio. |
| `RIE-04` | `Técnico` | La sanitización y la prevención de inyecciones no define el resultado esperado: no se sabe si el dato se rechaza o se limpia (AMB-01). | Media | Alta | `Alto` | Definir el comportamiento esperado (AMB-01) y luego probarlo. Incluir pruebas de XSS y SQL Injection solo en ambiente autorizado. |
| `RIE-05` | `Técnico` | Datos de prueba y ambiente: el registro se prueba en un demo público y las cuentas creadas quedan persistidas, con `User ID` y `email` únicos. | Alta | Media | `Alto` | Requiere autorización (P5). Generar datos sintéticos únicos por corrida, dentro de las reglas de §6.1. |
| `RIE-06` | `Técnico` | Ausencia de Rate Limiting y CAPTCHA (excluidos en §2). El formulario queda expuesto a registros masivos. | Media | Media | `Medio` | Riesgo aceptado por exclusión de alcance. Documentarlo y revisarlo con negocio. |
| `RIE-07` | `Negocio / UX` | Cuentas en estado `ACTIVE` sin verificación de email (§10, excluido en §2). Pueden crearse cuentas con correos no válidos o de terceros. | Media | Media | `Medio` | Riesgo aceptado por exclusión de alcance. Revisar en la fase de verificación por email. |

---

## 5. Matriz de Ambigüedades y Contradicciones

| ID | Sección | Requisito o Texto Analizado | Conflicto / Ambigüedad Detectada | Impacto Funcional | Propuesta de Redacción / Clarificación |
| :---: | :---: | :---- | :---- | :---: | :---- |
| `AMB-01` | §7.5, §8, §5 (paso 6) | "Todo campo alfanumérico debe pasar por stripping/escaping de etiquetas HTML (`<script>`, etc.) antes de ser procesado o almacenado." | No define si el sistema rechaza el dato con error o lo limpia en silencio. Tampoco define qué valor se guarda ni qué ve el usuario. | `Alto` | Definir una de estas opciones: (a) rechazo con error inline en el campo; (b) guardado sin las etiquetas. Indicar el valor almacenado y el mensaje visible en cada caso. |
| `AMB-02` | §6.1 / `phone` | "Exactamente 9 caracteres... el `+` cuenta dentro de los 9 caracteres, por confirmar en P15." Regex: `^(+[0-9]{8} \| [0-9]{9})$`. | La expresión regular, tal como está escrita, no es válida: el `+` inicial no está escapado y hay espacios alrededor de `\|`. La duda sobre el `+` figura como resuelta en el Anexo A, pero el texto sigue marcándola como "por confirmar". | `Medio` | Redactar la regla con una expresión válida. Confirmar si el `+` cuenta dentro de los 9 caracteres. |
| `AMB-03` | §6.1 / `zip` | "3 a 10 caracteres alfanuméricos (`^[a-zA-Z0-9\s-]{3,10}$`)." | El texto dice "alfanuméricos", pero la regex admite espacios (`\s`) y guiones. | `Medio` | Alinear el texto y la regex: definir si se aceptan espacios y guiones. |
| `AMB-04` | §6.1 / `firstName`, `lastName`, `address1`, `city` | "1 a 50 caracteres alfabéticos" y "3 a 100 caracteres alfanuméricos". | No se define si se aceptan espacios, tildes, guiones o apóstrofes (por ejemplo, en nombres compuestos o en direcciones con número). | `Medio` | Definir el conjunto exacto de caracteres permitidos por campo. |
| `AMB-05` | §6.1 / `country` | "2 a 50 caracteres (o código ISO alfa-2). Campo de texto libre en la UI." | No queda claro si ambos formatos son válidos en el mismo campo, ni si el valor se valida contra una lista. | `Medio` | Definir un formato único o la regla de validación para cada caso. |
| `AMB-06` | §5 (paso 6) | "El backend válida unicidad de `User ID` y `email`, y aplica sanitización." | No define el orden de las operaciones: si la unicidad se comprueba antes o después de la sanitización. | `Medio` | Definir el orden. La comparación de unicidad debería hacerse sobre el valor que se guarda. |
| `AMB-07` | §7.1 / Unicidad de email | "La comprobación de `User ID` se realiza de manera insensible a mayúsculas." | La regla de mayúsculas solo se define para `User ID`. No se define si el `email` es sensible a mayúsculas. | `Bajo` | Definir la regla de unicidad del email (sensible o insensible a mayúsculas). |
| `AMB-08` | §8 / Almacenamiento | "Hashing robusto adaptativo (Argon2id o BCrypt con factor de costo adecuado)." | Los términos "robusto" y "adecuado" no tienen criterio verificable. No se define el factor de costo. | `Medio` | Reemplazar por un criterio verificable o marcarlo como no verificable desde QA funcional. |
| `AMB-09` | Changelog v4.0 | "Sin cambios respecto a la v3.0: §1, §2, §3, §4, §10." | Comparado con la v3, §1, §2, §3 y §4 sí cambiaron de texto (por ejemplo, §2 pasó a "validación en backend" y §3 cambió su redacción). El changelog no lo refleja. | `Medio` | Corregir el changelog para listar los cambios reales de esas secciones. |
| `AMB-10` | Changelog v4.0 | "Obligatoriedad: ... Obligatoriedad pendiente (P3)." | El Anexo A y §6.1 indican que P3 está resuelta. El changelog la marca como pendiente. | `Bajo` | Corregir el changelog. |
| `AMB-11` | §9 / Base de datos | "Tablas `ACCOUNT`, `SIGNON` y `PROFILE` (según la implementación de referencia de JPetStore; no verificable desde la UI)." | Información de implementación que proviene de una referencia externa. No es verificable con el alcance del documento. | `Bajo` | Quitarla de la especificación funcional o marcarla como referencia no normativa. |

---

## 6. Preguntas Abiertas para Product Owner / Negocio

Preguntas pendientes del Anexo A del documento v4.0. Se incluyen solo las que siguen abiertas (P1, P2, P3, P4, P6 y P15 están resueltas en el documento):

1. **[P5] Pruebas en ambiente:** ¿Se autoriza crear cuentas de prueba en el demo público para validar mensajes de error, redirección, persistencia del formulario y formatos?
   * *Impacto en pruebas:* sin esta autorización, §6.2, §6.3 y §7 quedan sin verificar (GAP-01, GAP-02, RIE-03, RIE-05).
2. **[P7] Enlace a Sign In:** ¿Se agrega el enlace "¿Ya tienes cuenta? Inicia sesión" dentro del formulario o se mantiene solo el enlace del encabezado?
   * *Impacto en pruebas:* define el escenario de navegación desde el formulario al inicio de sesión (§5, GAP-09).
2. **[P8] Cancelar / reset:** ¿Se agrega un botón de cancelar o reset, o se elimina de la especificación?
   * *Impacto en pruebas:* define si existe un caso de restablecimiento del formulario (GAP-05).
3. **[P9] Sesión / JWT:** ¿Se confirma que la sesión queda excluida y que §6.2, §7 y §9 de la v3 se retiran?
   * *Impacto en pruebas:* define si se prueba la ausencia de sesión tras el registro (§2, §6.2, §9).
4. **[P10] Consentimiento de T&C:** ¿Se confirma que la aceptación de Términos y Condiciones queda excluida de esta versión?
   * *Impacto en pruebas:* define si existe un caso de consentimiento en el formulario (§2, §11).
5. **[P11] Visibilidad de contraseña:** ¿El toggle "mostrar / ocultar contraseña" es requisito?
   * *Impacto en pruebas:* define si se prueba el control de visibilidad (§7.2, GAP-06).
6. **[P12] Validación en cliente:** ¿La validación en tiempo real (§7.2, §7.4 y Escenario 4) es requisito, o basta con la validación de servidor?
   * *Impacto en pruebas:* define si los escenarios de campos vacíos y formatos inválidos se prueban en la pantalla, en el servidor o en ambos (GAP-03).
7. **[P13] Códigos de error `REG_*`:** ¿Existen en la aplicación actual o son una propuesta nueva?
   * *Impacto en pruebas:* bloquea las aserciones sobre códigos en los escenarios 2, 3 y 5 de §12 (GAP-02, RIE-01).
8. **[P14] Formulario con tokens ocultos:** ¿Los campos `_sourcePage` y `__fp` forman parte del alcance de seguridad o son un detalle de implementación fuera de alcance?
   * *Impacto en pruebas:* define si se prueban envíos forjados o manipulación de estos campos (§8, GAP-07).

---

## 7. Recomendaciones para el Framework de Automatización

Lineamientos basados en la información del documento v4.0:

* **Estrategia BDD / de escenarios:** Usar los escenarios de §12 como base. Los escenarios 1 y 4 pueden avanzar con la información disponible. Los escenarios 2, 3 y 5 quedan bloqueados hasta resolver P5 y P13.
* **Automatización de interfaz (UI):** Los campos del formulario tienen un atributo `name` documentado en §6.1 (`username`, `password`, `repeatedPassword`, `firstName`, `lastName`, `email`, `phone`, `address1`, `address2`, `city`, `state`, `zip`, `country`, `languagePreference`, `favouriteCategoryId`, `listOption`, `bannerOption`). El botón de envío es `name="newAccount"` (§6.1). Antes de usarlos como localizadores, verificar que existan en la página real (GAP-03, Anexo B).
* **Automatización de API / HTTP:** No asumir respuestas JSON. El envío documentado es `POST /account/new` con respuesta de formulario HTML. Las validaciones de código de estado y de contrato deben esperar a la confirmación del mecanismo real (GAP-01, RIE-02).
* **Gestión de datos de prueba:** Los valores de §6.1 definen las reglas de formato (por ejemplo, `User ID` de 10 caracteres alfanuméricos y contraseña de 8 a 20 caracteres con las cuatro clases de caracteres). Generar datos sintéticos que cumplan esas reglas y sean únicos por ejecución. No usar datos reales de personas (RIE-05).
* **Dependencias de ambiente:** La verificación en la UI depende de un ambiente de demo público. No ejecutar pruebas que creen cuentas hasta contar con la autorización de P5.
