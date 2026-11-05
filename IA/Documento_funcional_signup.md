# Documento Funcional — Sign Up (New Account)

**Versión:** 4.0  
**Estado:** Propuesto / En Revisión (existen preguntas abiertas, ver Anexo A)  
**Última actualización:** 29 de octubre de 2026  
**Fuente de verdad:** `https://petstore.octoperf.com/account/new` (inspección del 29/10/2026)

**Changelog v4.0 (base: v3.0):**

- Alcance y exclusiones: sin cambios respecto a la v3.0 (se respetan íntegramente).  
- Flujo funcional: se alinea con la UI observada. Se elimina el enlace "¿Ya tienes cuenta? Inicia sesión dentro del formulario (la UI solo expone el enlace "Sign In" del encabezado). Se marca como no observado el botón de cancelar/reset y la validación en tiempo real.  
- `User ID` (confirmado por negocio): exactamente 10 caracteres, solo letras y números, sin guión bajo.  
- Contraseña (confirmado por negocio): 8 a 20 caracteres, con minúscula, mayúscula, número y carácter especial.  
- Obligatoriedad (confirmada por negocio, P3): son obligatorios todos los campos excepto *Address 2* y las preferencias de perfil. `phone`, `state` y `zip` pasan de opcionales a obligatorios.  
- Datos de entrada: se usan los nombres reales de los campos de la UI (`username`, `repeatedPassword`, `listOption`, `bannerOption`). Se documentan las opciones reales de `languagePreference` (`english`, `japanese`) y los valores por defecto observados en la página, que sustituyen a los de la v3 (confirmado por el negocio, P1 y P2). Obligatoriedad pendiente (P3).  
- Respuesta: se elimina `token` y la emisión de sesión (excluido en §2). La redirección post-registro se define como Home (`/catalog`) (P4).  
- Catálogo de errores: se marca como no verificado. Ningún código `REG_*` es visible en la UI actual.  
- Validaciones: se elimina la regla de autenticación inmediata (contradice la exclusión de sesión). El resto de reglas se marca según su verificación.  
- NFR: se eliminan Rate Limiting y CAPTCHA / anti-bot (excluidos en §2).  
- Integraciones: se elimina "Servicio de Sesión / JWT Issuer" (excluido en §2). Pendiente de confirmar (P9).  
- Privacidad: se elimina el checkbox de consentimiento de T\&C y Política de Privacidad (excluido en §2). Pendiente de confirmar (P10).  
- Criterios de aceptación: Escenario 1 ajustado (sin login automático, redirección a Home). Escenarios 2 a 5 se mantienen.  
- Sin cambios respecto a la v3.0: §1, §2, §3, §4, §10.  
- Se agregan Anexo A (preguntas abiertas) y Anexo B (hallazgos de la inspección frente a la v3).

---

## §1. Descripción General

La funcionalidad **Sign Up** (`https://petstore.octoperf.com/account/new`) permite a usuarios nuevos crear una cuenta en la aplicación registrando sus credenciales (`User ID`, contraseña y confirmación), sus datos personales, información de contacto y dirección, y sus preferencias de perfil. Al completarse, la cuenta queda disponible para iniciar sesión posteriormente desde Sign In.

---

## §2. Alcance

### Incluye

- Alta de credenciales (`User ID` único \+ contraseña \+ confirmación).  
- Captura y validación de datos personales, contacto y dirección.  
- Configuración de preferencias de perfil (idioma, categoría favorita, MyList, MyBanner).  
- Validación sintáctica y semántica de campos en backend. Validación en frontend según se confirme (ver P12).  
- Manejo estandarizado de errores y persistencia del estado del formulario.  
- Redirección post-registro.

### Excluye

- Autenticación de usuarios existentes (Sign In).  
- Recuperación de contraseña olvidada.  
- Edición posterior del perfil (administrado en módulo My Account).  
- Verificación asíncrona de cuenta por enlace/código enviado por email (se contempla para fases futuras).  
- Autenticación externa delegada (SSO, OAuth2 / Social Login).  
- Gestión de métodos de pago en esta pantalla.  
- Aceptación obligatoria de Términos y Condiciones y Política de Privacidad.  
- Protección perimetral básica (Rate Limiting y CAPTCHA / anti-bot).  
- Creación de sesión activa y autenticación automática post-registro.

---

## §3. Objetivo

Permitir que un nuevo usuario registre una cuenta de forma segura, intuitiva y eficiente, previniendo registros con datos inconsistentes y dejando la cuenta lista para su uso posterior en el sistema.

---

## §4. Actores Involucrados

- **Usuario nuevo:** Persona que interactúa con la interfaz web.  
- **Frontend (Web App):** Interfaz responsable de la presentación del formulario, validaciones en cliente (según P12), UX y envío de datos.  
- **Backend / Account API:** Servicio encargado de reglas de negocio, sanitización, encriptación y persistencia.  
- **QA:** Equipo de aseguramiento de calidad y pruebas automatizadas.

---

## §5. Flujo Funcional

### Flujo Principal (Registro Exitoso)

1. El usuario navega a `/account/new` desde el enlace de registro disponible en la aplicación.  
2. El sistema presenta el formulario con las secciones *User Information*, *Account Information* y *Profile Information*. Las preferencias muestran sus valores por defecto observados (ver §6.1).  
3. El usuario completa credenciales, datos personales, dirección y selecciona sus preferencias.  
4. El usuario hace clic en el botón **Save Account Information** (envío `POST /account/new`).  
5. El sistema valida los datos de entrada (validación de cliente según P12; validación de servidor siempre).  
6. El backend válida unicidad de `User ID` y `email`, y aplica sanitización.  
7. El backend crea la cuenta con la contraseña cifrada (hash seguro) en estado `ACTIVE`.  
8. El sistema redirige al usuario a la página de inicio (**Home**, `/catalog`).

### Flujos Alternos

- **Acceso a Sign In:** El usuario selecciona el enlace **Sign In** del encabezado (`/account`). El formulario no contiene un enlace adicional (ver P7).  
- **Personalización de preferencias:** El usuario modifica el idioma, la categoría favorita o activa MyList / MyBanner entre las opciones disponibles.

*No observado en la UI:* botón de cancelar / reset del formulario (ver P8).

### Flujos de Excepción

- **User ID ya existente:** El sistema rechaza la solicitud, resalta el campo `User ID` con mensaje y mantiene el resto de datos ingresados (salvo contraseñas). Comportamiento por verificar (ver P5).  
- **Email ya existente:** El sistema informa que el correo ya se encuentra en uso. Comportamiento por verificar (ver P5).  
- **Contraseñas no coinciden o no cumplen política:** Validación y bloqueo de envío según las reglas de §7. Comportamiento por verificar (ver P5, P6).  
- **Campos obligatorios vacíos o formatos inválidos:** El sistema impide el registro y resalta los campos con error. Comportamiento por verificar (ver P3, P5).  
- **Falla de servicio o base de datos:** Mensaje genérico de error de sistema con código de trazabilidad (por confirmar, ver P13).

---

## §6. Datos y Contratos de Entrada/Salida

### 6.1. Entradas (Formulario de Registro)

Nombres de campo tal como aparecen en la UI. La columna *Nombre técnico* corresponde al atributo `name` del HTML.

| Campo (UI) | Nombre técnico | Tipo | Obligatorio | Formato / Restricciones | Valor por defecto |
| :---- | :---- | :---- | :---: | :---- | :---- |
| User ID | `username` | string | Sí | Exactamente 10 caracteres. Solo letras y números, sin guion bajo ni otros símbolos (`^[a-zA-Z0-9]{10}$`). Case-insensitive para unicidad. | Vacío |
| New password | `password` | string | Sí | Entre 8 y 20 caracteres. Al menos 1 mayúscula, 1 minúscula, 1 número y 1 carácter especial. Lista de caracteres especiales pendiente (P6). | Vacío |
| Repeat password | `repeatedPassword` | string | Sí | Debe coincidir exactamente con *New password*. | Vacío |
| First name | `firstName` | string | Sí | 1 a 50 caracteres alfabéticos. Sanitizado de etiquetas HTML/scripts. | Vacío |
| Last name | `lastName` | string | Sí | 1 a 50 caracteres alfabéticos. Sanitizado de etiquetas HTML/scripts. | Vacío |
| Email | `email` | string | Sí | Formato RFC 5322\. Longitud máx. 100 caracteres. Único en el sistema. | Vacío |
| Phone | `phone` | string | Sí | Exactamente 9 caracteres. Solo números, con prefijo `+` opcional al inicio (el `+` cuenta dentro de los 9 caracteres, por confirmar en P15). No admite espacios, letras ni guiones (\`^(+\[0-9\]{8} | \[0-9\]{9})\$\`). |
| Address 1 | `address1` | string | Sí | 3 a 100 caracteres alfanuméricos. | Vacío |
| Address 2 | `address2` | string | No | Hasta 100 caracteres alfanuméricos. | Vacío |
| City | `city` | string | Sí | 2 a 50 caracteres. | Vacío |
| State | `state` | string | Sí | 2 a 50 caracteres. | Vacío |
| Zip | `zip` | string | Sí | 3 a 10 caracteres alfanuméricos (`^[a-zA-Z0-9\s-]{3,10}$`). | Vacío |
| Country | `country` | string | Sí | 2 a 50 caracteres (o código ISO alfa-2). Campo de texto libre en la UI. | Vacío |
| Language Preference | `languagePreference` | enum | No (preferencia con valor por defecto) | Opciones: `english`, `japanese` (P1). | `english` (P2) |
| Favourite Category | `favouriteCategoryId` | enum | No (preferencia con valor por defecto) | Opciones: `FISH`, `DOGS`, `REPTILES`, `CATS`, `BIRDS`. | `FISH` (P2) |
| Enable MyList | `listOption` (checkbox, valor `true`) | boolean | No (preferencia con valor por defecto) | Marcado \= `true`, desmarcado \= `false`. | `false` (desmarcado) |
| Enable MyBanner | `bannerOption` (checkbox, valor `true`) | boolean | No (preferencia con valor por defecto) | Marcado \= `true`, desmarcado \= `false`. | `false` (desmarcado) |

Obligatoriedad confirmada por negocio (P3): todos los campos son obligatorios excepto *Address 2* y las preferencias de perfil (idioma, categoría favorita, MyList, MyBanner), que tienen valor por defecto. La UI no lo indica visualmente (sin asterisco ni atributo `required`).

**Acción de envío:** botón *Save Account Information* (`name="newAccount"`), `POST /account/new`.

### 6.2. Salidas

La UI actual es un formulario HTML con envío tradicional (`POST /account/new`). No se observa un contrato JSON. Los siguientes formatos son propuestos y deben validarse (ver P5).

#### Respuesta Exitosa (propuesta)

- Creación de la cuenta con estado `ACTIVE`.  
- Redirección a Home (`/catalog`).  
- Sin emisión de sesión ni token (excluido en §2).

#### Respuesta de Error (formato propuesto)

```json
{
  "status": "error",
  "error": {
    "code": "REG_001",
    "message": "The selected User ID is already in use.",
    "field": "username",
    "timestamp": "2026-10-29T21:10:00Z"
  }
}
```

### 6.3. Catálogo de Códigos de Error

*Ningún código `REG_*` es visible en la UI actual. Catálogo propuesto por validar (ver P13).*

| Código | HTTP | Descripción / Mensaje al Usuario |
| :---- | :---: | :---- |
| `REG_001` | 409 | Duplicate User ID. El nombre de usuario ya está registrado. |
| `REG_002` | 400 | Missing required fields. Faltan uno o más campos obligatorios. |
| `REG_003` | 400 | Password mismatch. La confirmación de contraseña no coincide. |
| `REG_004` | 400 | Invalid email format. El formato del correo electrónico no es válido. |
| `REG_005` | 409 | Duplicate Email. El correo electrónico ya se encuentra registrado. |
| `REG_006` | 400 | Weak password. La contraseña no cumple con los requisitos de seguridad. |
| `REG_500` | 500 | Internal server error. Ocurrió un error inesperado al procesar la solicitud. |

---

## §7. Validaciones y Reglas de Negocio

1. **Unicidad:** Tanto `User ID` como `email` deben ser únicos a nivel de base de datos. La comprobación de `User ID` se realiza de manera insensible a mayúsculas (`johndoe` \== `JohnDoe`). *Por verificar (P5).*  
2. **Robustez de Contraseñas:** (reglas confirmadas por negocio, P6)  
   - Entre 8 y 20 caracteres.  
   - Requiere al menos 1 mayúscula, 1 minúscula, 1 número y 1 carácter especial.  
   - El `+` es un carácter especial válido. Lista completa de caracteres especiales pendiente de confirmar (P6).  
   - Enmascaramiento por defecto con toggle visual ("mostrar/ocultar contraseña"). *No observado en la UI actual (ver P12).*  
3. **Persistencia del Formulario en Error:** Si la petición falla (por ejemplo, `User ID` duplicado), el formulario **no debe blanquearse**. Debe conservar los datos ingresados, excepto `password` y `repeatedPassword`, que se vacían por seguridad. *Por verificar (P5).*  
4. **Mensajería de Error en Interfaz:** Los errores de validación de campo deben mostrarse de manera inline junto al campo correspondiente, con borde rojo de advertencia y foco en el primer campo inválido. *Por verificar (P5, P12).*  
5. **Sanitización y Prevención de Inyecciones:** Todo campo alfanumérico debe pasar por stripping/escaping de etiquetas HTML (`<script>`, etc.) antes de ser procesado o almacenado.  
6. **~~Autenticación Inmediata~~** *Eliminada en v4.0.* La creación de sesión tras el registro está excluida en §2.

---

## §8. Requisitos No Funcionales (NFR)

* **Seguridad y Cifrado:**  
  * Tránsito 100% bajo HTTPS (TLS 1.3). *La página se sirve por HTTPS.*  
  * Almacenamiento de contraseñas mediante hashing robusto adaptativo (Argon2id o BCrypt con factor de costo adecuado).  
  * Sanitización contra XSS y parametrización estricta de consultas contra SQL Injection.  
* **Rendimiento:**  
  * Tiempo de respuesta del endpoint de registro menor a 1.5 segundos en el percentil 95 (P95).  
* **Usabilidad y Accesibilidad:**  
  * Cumplimiento con pautas WCAG 2.1 nivel AA (contraste de color, navegación por teclado, atributos `aria-*` en mensajes de error). *No verificado.*  
  * Diseño adaptable (Responsive Web Design) compatible con pantallas móviles, tablets y desktop. *No verificado.*

*Excluido en v4.0: Rate Limiting y Protección Anti-Bot (ver §2).*

---

## §9. Integraciones

- **Backend Account API:** Controlador REST / de formulario para orquestar la creación de cuenta. La UI envía `POST /account/new`.  
- **Base de Datos Relacional:** Tablas `ACCOUNT`, `SIGNON` y `PROFILE` (según la implementación de referencia de JPetStore; no verificable desde la UI).  
- **Servicio de Catálogo:** Consumo de listas maestras para categorías favoritas e idiomas (observado en la UI como listas fijas).

*Excluido en v4.0: Servicio de Sesión / JWT Issuer y Servicio de Seguridad / Anti-Bot (ver §2). Pendiente de confirmar (P9).*

---

## §10. Supuestos y Restricciones

- La verificación de cuenta vía correo electrónico queda diferida para una versión posterior; las cuentas se activan en estado `ACTIVE` de inmediato.  
- Se asume disponibilidad continua de los servicios de base de datos.  
- No se capturan datos de facturación ni pasarelas de pago durante el registro inicial.  
- El formulario actual no incluye validaciones de cliente observables en el HTML (no se detectó JavaScript ni atributos de validación). Ver P12.

---

## §11. Privacidad y Cumplimiento Normativo

- **Minimización de Datos:** No se recopila información personal sensible innecesaria para el proceso de compra y entrega.  
- **Almacenamiento Conforme:** Las contraseñas en texto plano nunca se registran en logs ni se exponen en respuestas HTTP.

*Excluido en v4.0: casilla de consentimiento de Términos y Condiciones / Política de Privacidad (ver §2). Pendiente de confirmar (P10).*

---

## §12. Criterios de Aceptación (Gherkin)

### Escenario 1: Registro exitoso

**Dado** que un usuario nuevo ingresa al formulario de registro  
**Cuando** completa todos los campos obligatorios con formatos válidos y contraseñas coincidentes  
**Y** envía el formulario  
**Entonces** el sistema crea la cuenta en base de datos con contraseña cifrada en estado `ACTIVE`  
**Y** no crea una sesión activa ni autentica automáticamente al usuario  
**Y** lo redirige a la página de inicio (Home, `/catalog`).

---

### Escenario 2: User ID duplicado

**Dado** que existe una cuenta registrada con el `User ID` "petlover"  
**Cuando** un nuevo usuario intenta registrarse usando "PetLover" o "petlover"  
**Entonces** el backend rechaza el registro con error `REG_001` (código HTTP 409, por verificar)  
**Y** el frontend resalta el campo `User ID` con un mensaje inline  
**Y** mantiene en pantalla el resto de los datos completados, vaciando únicamente los campos de contraseña.

---

### Escenario 3: Email duplicado

**Dado** que existe una cuenta registrada con el correo "[test@petstore.com](mailto:test@petstore.com)"  
**Cuando** otro usuario intenta registrarse utilizando dicho correo  
**Entonces** el sistema bloquea el registro con error `REG_005` (código HTTP 409, por verificar)  
**Y** muestra el mensaje inline correspondiente en el campo de email.

---

### Escenario 4: Contraseñas no cumplen política o no coinciden

**Dado** que el usuario ingresa una contraseña que no cumple con el estándar de seguridad (ej. menor a 8 caracteres o sin números) o que no coincide con *Repeat password*  
**Cuando** intenta enviar el formulario  
**Entonces** el sistema bloquea el registro  
**Y** indica los requisitos de contraseña incumplidos (por verificar, ver P5 y P12).

---

### Escenario 5: Campos obligatorios vacíos

**Dado** que el usuario se encuentra en el formulario de registro  
**Cuando** deja uno o más campos obligatorios vacíos e intenta enviar el formulario  
**Entonces** el sistema impide el registro  
**Y** resalta de manera inline, con borde rojo, los campos con error y sitúa el foco en el primer campo inválido (por verificar, P5)  
**Y** el backend rechaza cualquier solicitud con campos obligatorios faltantes con código HTTP 400 y error `REG_002`.

---

## Anexo A. Preguntas Abiertas

| \# | Pregunta | Impacto |
| :---: | :---- | :---- |
| P1 | **~~Idioma~~** **Resuelta:** se usan las opciones de la página (`english`, `japanese`). Se elimina `spanish`. | §6.1 |
| P2 | **~~Valores por defecto~~** **Resuelta:** se usan los valores de la página (`english`, `FISH`). | §5, §6.1 |
| P3 | **~~Campos obligatorios~~** **Resuelta:** son obligatorios todos los campos excepto *Address 2* y las preferencias de perfil (idioma, categoría, MyList, MyBanner). `phone`, `state` y `zip` pasan a obligatorios. | §6.1, §12 (Escenario 5\) |
| P4 | **~~Redirección post-registro~~** **Resuelta:** tras un registro correcto el sistema redirige a Home (`/catalog`). Pendiente menor: ¿se muestra algún mensaje de confirmación? | §5, §6.2, §12 (Escenario 1\) |
| P5 | **Pruebas en ambiente:** ¿autorizas crear cuentas de prueba en el demo público para validar mensajes de error, redirección, persistencia del formulario y formatos? Sin esto, §6.2, §6.3 y §7 quedan sin verificar. | Múltiples |
| P6 | **~~Reglas de contraseña y User ID~~** **Resuelta:** contraseña de 8 a 20 caracteres con mayúscula, minúscula, número y carácter especial, donde el `+` es un carácter especial válido. `User ID` de exactamente 10 caracteres alfanuméricos. Pendiente menor: ¿cuál es el listado completo de caracteres especiales aceptados (v3: `!@#$%^&*`)? Y ¿la regla de formato de zip (§6.1) es requisito nuevo o se mantiene el comportamiento actual? | §6.1, §7, §12 |
| P7 | **Enlace a Sign In:** la v3 indica un enlace dentro del formulario ("¿Ya tienes cuenta? Inicia sesión") hacia `/account/signonForm`. La UI solo tiene el enlace *Sign In* del encabezado (`/account`). ¿Se agrega el enlace o se mantiene así? | §5 |
| P8 | **Cancelar / reset:** la UI no tiene botón de cancelar ni reset. ¿Se agrega o se elimina de la especificación? | §5 |
| P9 | **Sesión / JWT:** la v3 excluye la sesión, pero §6.2, §7 y §9 de la v3 todavía la incluían. En la v4 se retiró. ¿Confirmas? | §2, §6.2, §9 |
| P10 | **Consentimiento T\&C:** la v3 excluye la aceptación de T\&C, pero §11 de la v3 todavía la incluía. En la v4 se retiró. ¿Confirmas? | §2, §11 |
| P11 | **Visibilidad de contraseña:** ¿el toggle "mostrar/ocultar" es requisito? No aparece en el HTML actual. | §7.2 |
| P12 | **Validación en cliente:** la página no tiene JavaScript ni atributos de validación visibles. ¿La validación en tiempo real (§7.2, §7.4 y Escenario 4\) es requisito, o basta con validación de servidor? | §4, §5, §7, §10, §12 |
| P13 | **Códigos de error `REG_*`:** ¿existen en la aplicación actual o son una propuesta nueva? | §6.2, §6.3, §12 |
| P15 | **~~Teléfono~~** **Resuelta:** longitud de 9 caracteres (mínimo y máximo), sin espacios, solo números con `+` opcional al inicio. Se asume que el `+` cuenta dentro de los 9 caracteres; confirmar si es correcto (si el `+` no cuenta, la longitud sería 9 dígitos más el `+`). | §6.1 |
| P14 | **Formulario con tokens ocultos:** el formulario incluye campos ocultos `_sourcePage` y `__fp`. ¿Se considera parte del alcance de seguridad (protección contra envíos forjados) o es un detalle de implementación fuera de alcance? | §8 |

---

## Anexo B. Hallazgos de la Inspección frente a la v3.0

| Aspecto | v3.0 | Observado en `/account/new` (29/10/2026) | Tratamiento en v4.0 |
| :---- | :---- | :---- | :---- |
| Nombres de campo | `userId`, `confirmPassword`, `myList`, `myBanner` | `username`, `repeatedPassword`, `listOption`, `bannerOption` | Se usan nombres de UI y se documentan nombres técnicos |
| Idioma | Solo `spanish` | `english`, `japanese` | Resuelto, P1: se usa lo observado |
| Valores por defecto | `spanish`, `DOGS` | `english`, `FISH` (primera opción) | Resuelto, P2: se usa lo observado |
| Campos obligatorios | `phone`, `state`, `zip` opcionales | No indicados en la UI | Confirmado por negocio: todos obligatorios excepto *Address 2* y preferencias, P3. `phone`, `state` y `zip` pasan a obligatorios |
| Enlace a Sign In en el formulario | Sí, a `/account/signonForm` | No, solo enlace del encabezado a `/account` | Se ajusta, P7 |
| Cancelar / reset | Sí | No | Se marca como no observado, P8 |
| Toggle de contraseña | Sí | No observado | Se marca como por verificar, P11 |
| Validación en cliente | Sí (tiempo real) | No se observa JavaScript | Por verificar, P12 |
| Checkbox de T\&C | Excluido en §2, pero presente en §11 | No existe en la UI | Eliminado de §11, P10 |
| Token / sesión | Excluido en §2, pero presente en §6.2, §7 y §9 | No aplica (sin sesión en la UI) | Eliminado, P9 |
| Rate limiting / CAPTCHA | Excluido en §2, pero presente en §8 | No existe en la UI | Eliminado de §8 |
| Códigos `REG_*` | Catálogo definido | No visibles | Marcados como por validar, P13 |
| Protección de formulario | No documentada | Campos ocultos `_sourcePage` y `__fp` | Consulta, P14 |
| Protocolo | HTTPS | HTTPS | Confirmado |

&nbsp;