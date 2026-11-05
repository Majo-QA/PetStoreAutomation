# Plan de Pruebas y Casos de Prueba BDD: Sign Up (New Account)

## 1. Metadatos del Plan de Pruebas

| Atributo | Detalle |
| :--- | :--- |
| **Proyecto** | PetStoreAutomation |
| **Funcionalidad / Módulo** | Sign Up (New Account) |
| **Documento Funcional Base** | [`IA/documento_funcional_signup_v4.md`](file:///c:/Users/angro/IdeaProjects/PetStoreAutomation/IA/documento_funcional_signup_v4.md) (v4.0) |
| **Informe de Gaps y Riesgos** | [`IA/gaps_y_riesgos_signup_v4.md`](file:///c:/Users/angro/IdeaProjects/PetStoreAutomation/IA/gaps_y_riesgos_signup_v4.md) (Aprobado el 29/10/2026) |
| **Diseño / UI de Referencia** | `https://petstore.octoperf.com/account/new` (Formulario HTML de JPetStore) |
| **Versión del Plan** | 1.1 (iteración 3 sobre la versión 1.0) |
| **Fecha de Creación** | 2026-10-29 |
| **Fecha de Iteración** | 2026-11-03 |
| **Autor / Rol** | QA Lead / Senior QA Automation Architect |
| **Estado** | **Aprobado para Automatización** (pendiente de revisión, ver §9) |

---

## 2. Alcance y Objetivos de la Prueba

### 2.1. Objetivo General
Validar de forma exhaustiva, estructurada y automatizable el proceso de registro de nuevos usuarios en la aplicación JPetStore, verificando que la captura de datos, la aplicación de reglas de negocio, el rechazo de entradas inválidas, la persistencia en error y la redirección post-registro se comporten con total apego a la especificación funcional v4.0 y a los acuerdos de calidad establecidos.

### 2.2. Alcance Cubierto (En Alcance - Ref: §2)
* Alta de credenciales: unicidad y validación de `User ID`, política de complejidad de `password` y confirmación idéntica en `repeatedPassword`.
* Captura y validación de datos personales y de contacto: `firstName`, `lastName`, `email` y `phone`.
* Captura y validación de dirección física: `address1`, `address2` (opcional), `city`, `state`, `zip` y `country`.
* Configuración de preferencias de perfil: `languagePreference` (`english`, `japanese`), `favouriteCategoryId` (`FISH`, `DOGS`, `REPTILES`, `CATS`, `BIRDS`), `listOption` (MyList) y `bannerOption` (MyBanner), tanto con valores por defecto como personalizados.
* Validación de obligatoriedad en servidor para todos los campos (excepto `address2` y preferencias).
* Preservación del estado de campos válidos en el formulario ante errores de validación (vaciando únicamente contraseñas).
* Redirección post-registro exitoso a Home (`/catalog`) en sesión anónima (enlace *Sign In* visible en el encabezado).

### 2.3. Exclusiones Explícitas (Fuera de Alcance - Ref: §2)
* Autenticación de usuarios existentes (módulo Sign In).
* Recuperación de contraseñas olvidadas.
* Edición posterior de los datos de cuenta o perfil (módulo My Account).
* Verificación asíncrona de cuenta mediante correo electrónico con enlace o código de confirmación.
* Autenticación federada o social (OAuth2, SSO, Google, GitHub).
* Captura de métodos de pago, facturación o tarjetas de crédito.
* Casilla obligatoria de Términos y Condiciones o Política de Privacidad.
* Protección perimetral mediante CAPTCHA, reCAPTCHA o Rate Limiting.
* Creación de sesión activa o login automático tras el registro.

> **Nota:** los escenarios TC-SIGNUP-01 y TC-SIGNUP-02 usan el inicio de sesión (Sign In) como forma de confirmar la cuenta creada. Esto contradice la exclusión de este apartado. Ver §9, punto 1.

### 2.4. Criterios de Entrada y Criterios de Salida

| Criterio | Condiciones Requeridas |
| :--- | :--- |
| **Criterios de Entrada (Entry Criteria)** | 1. Documento funcional v4.0 y acuerdos de gaps/riesgos aprobados.<br>2. Ambiente web `https://petstore.octoperf.com/account/new` en línea y accesible.<br>3. Stack de automatización Java 21 + Gradle 9.6 + Selenium WebDriver + Cucumber configurado en `PetStoreAutomation`.<br>4. Cuenta existente con User ID `TESTUSER01` y email `usuario.existente@testpetstore.com` disponible en el ambiente (requerida por TC-SIGNUP-03, TC-SIGNUP-04 y TC-SIGNUP-11). |
| **Criterios de Salida (Exit Criteria)** | 1. 100% de los escenarios prioritarios (*Must*) ejecutados con resultado `PASSED`.<br>2. Cero defectos de severidad Bloqueante o Crítica abiertos.<br>3. Trazabilidad del 100% entre criterios de aceptación y escenarios Gherkin. |

---

## 3. Estrategia y Técnicas de Diseño de Pruebas (ISTQB)

### 3.1. Niveles y Tipos de Prueba

| Nivel de Prueba | Tipo de Prueba | Framework / Herramienta | Enfoque de Ejecución |
| :--- | :--- | :--- | :--- |
| **Pruebas de Sistema (E2E)** | Funcional (Web UI) | Selenium WebDriver + JUnit 5 | Navegación real en navegador (Chrome/Firefox headless), llenado de campos en el DOM y verificación de navegación/mensajes. |
| **Pruebas de Aceptación** | BDD / Comportamiento | Cucumber (Gherkin: palabras clave en inglés, texto en español) | Especificación ejecutable basada en los criterios de negocio definidos en §12 del documento funcional. |

### 3.2. Aplicación de los 7 Principios de Testing
1. **Pruebas tempranas (Shift-Left):** Detección previa de discrepancias arquitectónicas (eliminación oportuna de contratos JSON `REG_*` antes de codificar pruebas fallidas).
2. **Pruebas exhaustivas imposibles:** Muestreo representativo de combinaciones de caracteres y longitudes mediante Partición de Equivalencia y Valores Límite.
3. **Agrupación de defectos:** Focalización intensiva en los campos críticos con mayor probabilidad de falla: `User ID` (unicidad e insensibilidad a mayúsculas), `password` (4 reglas combinadas) y `phone` (formato con prefijo).
4. **Paradoja del pesticida:** Uso de datos sintéticos dinámicos generados en tiempo de ejecución para evitar pruebas con datos estáticos obsoletos o colisionados.
5. **Las pruebas demuestran la presencia de defectos:** Aserciones precisas sobre elementos visuales, URLs de destino y persistencia de valores en inputs.
6. **Las pruebas dependen del contexto:** Enfoque adaptado a una aplicación web server-side rendering tradicional con validación al presionar submit.
7. **Falacia de ausencia de errores:** Validación de que el usuario registrado queda en un estado limpio y consistente que le permita iniciar sesión con sus credenciales.

### 3.3. Técnicas de Caja Negra Utilizadas por Campo y Flujo

| Elemento / Campo | Técnica ISTQB | Clases de Equivalencia / Fronteras Evaluadas |
| :--- | :--- | :--- |
| **User ID (`username`)** | Partición de Equivalencia (EP) + Valores Límite (BVA) | **Válido:** Exactamente 10 alfanuméricos (`^[a-zA-Z0-9]{10}$`).<br>**Inválido:** 9 caracteres (límite inferior - 1), 11 caracteres (límite superior + 1), caracteres especiales (`user_12345`). |
| **Unicidad de User ID** | Ataque de Errores (Error Guessing) | Intento de registro con ID idéntico variando mayúsculas y minúsculas (insensibilidad a mayúsculas: `TESTUSER10` vs `testuser10`). |
| **Contraseña (`password`)** | Tabla de Decisión (Combinatoria) | Combinaciones de: Longitud [8-20], Minúscula (S/N), Mayúscula (S/N), Número (S/N), Carácter especial (S/N). |
| **Confirmación (`repeatedPassword`)** | Partición de Equivalencia (EP) | **Válido:** Idéntica a `password`.<br>**Inválido:** Diferente a `password` por 1 carácter o vacía. |
| **Teléfono (`phone`)** | BVA + Partición de Equivalencia | **Válido:** 9 dígitos numéricos (`123456789`), 9 dígitos con prefijo `+` (`+123456789`).<br>**Inválido:** 8 dígitos numéricos, 10 dígitos sin `+`, letras o caracteres especiales no autorizados. |
| **Correo (`email`)** | Partición de Equivalencia (EP) | **Válido:** Formato RFC 5322 (`test@petstore.com`).<br>**Inválido:** Sin `@`, sin dominio, con espacios o duplicado. |
| **Código Postal (`zip`)** | BVA + Partición de Equivalencia | **Válido:** 3 caracteres (mínimo), 10 caracteres (máximo), alfanumérico con guión o espacio.<br>**Inválido:** 2 caracteres, 11 caracteres. |
| **Campos de Texto Obligatorios** | Partición de Equivalencia (EP) | `firstName`, `lastName`, `address1`, `city`, `state`, `country` vacíos vs completos. |
| **Preferencias de Perfil** | Partición de Equivalencia (EP) | Valores por defecto (`english`, `FISH`, checkboxes desmarcados) vs selecciones personalizadas (`japanese`, `DOGS`, checkboxes activos). |
| **Persistencia en Error** | Transición de Estados | Verificación de que el formulario retiene la data ingresada tras un fallo de validación del servidor (excepto passwords). |

---

## 4. Matriz de Cobertura y Trazabilidad de Requisitos (RTM)

| ID Caso | Requisito Base | Nombre del Caso de Prueba | Técnica ISTQB | Tipo de Prueba | Prioridad MoSCoW | Tag Gherkin |
| :---: | :---: | :--- | :---: | :---: | :---: | :---: |
| `TC-SIGNUP-01` | §5, §12 (Esc. 1) | Registro exitoso con valores por defecto en preferencias (Flujo Feliz) | Partición de Equivalencia | Positiva (Smoke) | `Must` | `@smoke @positive` |
| `TC-SIGNUP-02` | §5, §6.1 | Registro exitoso personalizando idioma, categoría y checkboxes | Partición de Equivalencia | Positiva | `Should` | `@regression @positive` |
| `TC-SIGNUP-03` | §6.1, §7.1, §12 (Esc. 2) | Rechazo por User ID duplicado (insensible a mayúsculas) | Ataque de Errores | Negativa | `Must` | `@regression @negative` |
| `TC-SIGNUP-04` | §6.1, §12 (Esc. 3) | Rechazo por Email duplicado en el sistema | Ataque de Errores | Negativa | `Must` | `@regression @negative` |
| `TC-SIGNUP-05` | §6.1, §12 (Esc. 4) | Rechazo por discrepancia entre New Password y Repeat Password | Partición de Equivalencia | Negativa | `Must` | `@regression @negative` |
| `TC-SIGNUP-06` | §6.1, §7.2 | Validación de complejidad de contraseña (tabla de decisión) | Tabla de Decisión | Borde / Negativa | `Must` | `@regression @boundary` |
| `TC-SIGNUP-07` | §6.1, §12 (Esc. 5) | Validación de campos obligatorios vacíos | Partición de Equivalencia | Negativa | `Must` | `@regression @negative` |
| `TC-SIGNUP-08` | §6.1, Acuerdo 3 | Validación de formato y longitud de Teléfono con y sin prefijo '+' | Análisis de Valores Límite | Borde / Negativa | `Should` | `@regression @boundary` |
| `TC-SIGNUP-09` | §6.1 | Validación de longitud de User ID (fronteras 9, 10 y 11 caracteres) | Análisis de Valores Límite | Borde / Negativa | `Should` | `@regression @boundary` |
| `TC-SIGNUP-10` | §6.1 | Validación de longitud de Código Postal / Zip (fronteras 2, 3, 10 y 11) | Análisis de Valores Límite | Borde / Negativa | `Could` | `@regression @boundary` |
| `TC-SIGNUP-11` | §7.3 | Preservación del formulario y vaciado de contraseñas ante error | Transición de Estados | Negativa / UX | `Must` | `@regression @ux` |
| `TC-SIGNUP-12` | §2, §5 | Verificación de ausencia de sesión automática post-registro | Partición de Equivalencia | Seguridad / Flujo | `Must` | `@smoke @security` |

---

## 5. Especificación de Casos de Prueba en Gherkin (Cucumber)

> **Ubicación en el proyecto:** [`src/test/resources/Feature/Create_user/CreateUser.feature`](file:///c:/Users/angro/IdeaProjects/PetStoreAutomation/src/test/resources/Feature/Create_user/CreateUser.feature)
>
> **Convenciones de esta iteración:**
> * Palabras clave Gherkin en inglés (`Feature`, `Background`, `Scenario`, `Given`, `When`, `And`, `Then`). El resto del texto está en español.
> * Sin `Scenario Outline`: cada caso es un escenario independiente.
> * Cada escenario muestra la tabla completa de datos del formulario, con el valor del caso resaltado en la fila correspondiente. Así cada caso se lee sin consultar otra sección.
> * Los valores que dicen "generado" se crean en cada ejecución (ver §6.1).

```gherkin
# language: en
@web @signup
Feature: Registro de Nueva Cuenta de Usuario (Sign Up)
  Como un usuario nuevo de JPetStore
  Quiero registrar mis credenciales, datos de contacto y preferencias
  Para disponer de una cuenta activa y poder comprar en la tienda

  Background:
    Given ingresa en esta URL "https://petstore.octoperf.com/catalog"
    And da click en "Sign In"
    And da click en "Register Now"
    Then ingresa a la página de registro

  # ---------------------------------------------------------------------------
  # FLUJOS POSITIVOS (HAPPY PATH)
  # ---------------------------------------------------------------------------

  @smoke @positive
  Scenario: TC-SIGNUP-01 - Registro exitoso con valores por defecto en preferencias de perfil
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And deja las preferencias de perfil con sus valores por defecto
    And da click en "Save Account Information"
    Then el sistema redirige a la página de inicio "/catalog"
    And la página de inicio muestra el texto "docs"
    When da click en "Sign In"
    And ingresa el User ID registrado
    And ingresa la contraseña registrada
    And envía el formulario de inicio de sesión
    Then la página de inicio muestra el texto "docs"

  @regression @positive
  Scenario: TC-SIGNUP-02 - Registro exitoso con preferencias personalizadas y dirección complementaria
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | 987654321                                  |
      | Address 1       | Calle 45 # 12-34                           |
      | Address 2       | Apto 302                                   |
      | City            | Lima                                       |
      | State           | Lima                                       |
      | Zip             | 15001                                      |
      | Country         | Perú                                       |
    And selecciona el idioma "japanese"
    And selecciona la categoría favorita "DOGS"
    And marca la casilla "Enable MyList"
    And marca la casilla "Enable MyBanner"
    And da click en "Save Account Information"
    Then el sistema redirige a la página de inicio "/catalog"
    And la página de inicio muestra el texto "docs"
    When da click en "Sign In"
    And ingresa el User ID registrado
    And ingresa la contraseña registrada
    And envía el formulario de inicio de sesión
    Then la página de inicio muestra el texto "docs"

  # ---------------------------------------------------------------------------
  # FLUJOS DE EXCEPCIÓN: UNICIDAD Y DUPLICIDAD
  # ---------------------------------------------------------------------------

  @regression @negative
  Scenario: TC-SIGNUP-03 - Rechazo de registro por User ID duplicado sin distinguir mayúsculas
    Given existe una cuenta registrada con el User ID "TESTUSER01"
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | testuser01                                 |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y permanece en el formulario de registro
    And muestra un mensaje de error junto al campo User ID

  @regression @negative
  Scenario: TC-SIGNUP-04 - Rechazo de registro por correo electrónico duplicado
    Given existe una cuenta registrada con el correo "usuario.existente@testpetstore.com"
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | usuario.existente@testpetstore.com         |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y permanece en el formulario de registro
    And muestra un mensaje de error junto al campo Email

  # ---------------------------------------------------------------------------
  # FLUJOS DE EXCEPCIÓN: VALIDACIÓN DE CONTRASEÑAS
  # ---------------------------------------------------------------------------

  @regression @negative
  Scenario: TC-SIGNUP-05 - Rechazo de registro por confirmación de contraseña no coincidente
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | Passw0rd!+                                 |
      | Repeat password | Passw0rdDiferente!+                        |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta
    And muestra un mensaje de error junto al campo Repeat password

  @regression @boundary @negative
  Scenario: TC-SIGNUP-06-01 - Rechazo de contraseña menor a 8 caracteres
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | Pass1!                                     |
      | Repeat password | Pass1!                                     |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo New password

  @regression @boundary @negative
  Scenario: TC-SIGNUP-06-02 - Rechazo de contraseña mayor a 20 caracteres
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | Passw0rdSuperLarga2026!+Extra              |
      | Repeat password | Passw0rdSuperLarga2026!+Extra              |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo New password

  @regression @boundary @negative
  Scenario: TC-SIGNUP-06-03 - Rechazo de contraseña sin letra mayúscula
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | password123!+                              |
      | Repeat password | password123!+                              |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo New password

  @regression @boundary @negative
  Scenario: TC-SIGNUP-06-04 - Rechazo de contraseña sin letra minúscula
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | PASSWORD123!+                              |
      | Repeat password | PASSWORD123!+                              |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo New password

  @regression @boundary @negative
  Scenario: TC-SIGNUP-06-05 - Rechazo de contraseña sin número
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | PasswordSegura!+                           |
      | Repeat password | PasswordSegura!+                           |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo New password

  @regression @boundary @negative
  Scenario: TC-SIGNUP-06-06 - Rechazo de contraseña sin carácter especial
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | PasswordSegura123                          |
      | Repeat password | PasswordSegura123                          |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo New password

  # ---------------------------------------------------------------------------
  # FLUJOS DE EXCEPCIÓN: CAMPOS OBLIGATORIOS FALTANTES
  # ---------------------------------------------------------------------------

  @regression @negative
  Scenario: TC-SIGNUP-07-01 - Rechazo de registro con User ID vacío
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         |                                            |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no procesa el alta y permanece en el formulario marcando el campo User ID como obligatorio

  @regression @negative
  Scenario: TC-SIGNUP-07-02 - Rechazo de registro con New password vacía
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    |                                            |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no procesa el alta y permanece en el formulario marcando el campo New password como obligatorio

  @regression @negative
  Scenario: TC-SIGNUP-07-03 - Rechazo de registro con First name vacío
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      |                                            |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no procesa el alta y permanece en el formulario marcando el campo First name como obligatorio

  @regression @negative
  Scenario: TC-SIGNUP-07-04 - Rechazo de registro con Last name vacío
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       |                                            |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no procesa el alta y permanece en el formulario marcando el campo Last name como obligatorio

  @regression @negative
  Scenario: TC-SIGNUP-07-05 - Rechazo de registro con Email vacío
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           |                                            |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no procesa el alta y permanece en el formulario marcando el campo Email como obligatorio

  @regression @negative
  Scenario: TC-SIGNUP-07-06 - Rechazo de registro con Phone vacío
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           |                                            |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no procesa el alta y permanece en el formulario marcando el campo Phone como obligatorio

  @regression @negative
  Scenario: TC-SIGNUP-07-07 - Rechazo de registro con Address 1 vacía
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       |                                            |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no procesa el alta y permanece en el formulario marcando el campo Address 1 como obligatorio

  @regression @negative
  Scenario: TC-SIGNUP-07-08 - Rechazo de registro con City vacía
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            |                                            |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no procesa el alta y permanece en el formulario marcando el campo City como obligatorio

  @regression @negative
  Scenario: TC-SIGNUP-07-09 - Rechazo de registro con State vacío
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           |                                            |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no procesa el alta y permanece en el formulario marcando el campo State como obligatorio

  @regression @negative
  Scenario: TC-SIGNUP-07-10 - Rechazo de registro con Zip vacío
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             |                                            |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no procesa el alta y permanece en el formulario marcando el campo Zip como obligatorio

  @regression @negative
  Scenario: TC-SIGNUP-07-11 - Rechazo de registro con Country vacío
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         |                                            |
    And da click en "Save Account Information"
    Then el sistema no procesa el alta y permanece en el formulario marcando el campo Country como obligatorio

  # ---------------------------------------------------------------------------
  # VALORES LÍMITE: TELÉFONO, USER ID Y ZIP
  # ---------------------------------------------------------------------------

  @regression @boundary @negative
  Scenario: TC-SIGNUP-08-01 - Rechazo de teléfono con 8 dígitos numéricos
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | 12345678                                   |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo Phone

  @regression @boundary @negative
  Scenario: TC-SIGNUP-08-02 - Rechazo de teléfono con 10 dígitos numéricos sin prefijo internacional
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | 1234567890                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo Phone

  @regression @boundary @negative
  Scenario: TC-SIGNUP-08-03 - Rechazo de teléfono con prefijo "+" y 8 dígitos
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +12345678                                  |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo Phone

  @regression @boundary @negative
  Scenario: TC-SIGNUP-08-04 - Rechazo de teléfono con guiones intermedios
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | 123-456-789                                |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo Phone

  @regression @boundary @negative
  Scenario: TC-SIGNUP-08-05 - Rechazo de teléfono con caracteres alfabéticos
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | 12345ABCD                                  |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo Phone

  @regression @boundary @negative
  Scenario: TC-SIGNUP-09-01 - Rechazo de User ID de 9 caracteres
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | UserID009                                  |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo User ID

  @regression @boundary @negative
  Scenario: TC-SIGNUP-09-02 - Rechazo de User ID de 11 caracteres
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | UserID00011                                |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo User ID

  @regression @boundary @negative
  Scenario: TC-SIGNUP-09-03 - Rechazo de User ID de 10 caracteres con símbolos inválidos
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | User_ID!10                                 |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo User ID

  @regression @boundary @negative
  Scenario: TC-SIGNUP-10-01 - Rechazo de Zip de 2 caracteres
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | AB                                         |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo Zip

  @regression @boundary @negative
  Scenario: TC-SIGNUP-10-02 - Rechazo de Zip de 11 caracteres
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 12345678901                                |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema no crea la cuenta y muestra un mensaje de error junto al campo Zip

  # ---------------------------------------------------------------------------
  # EXPERIENCIA DE USUARIO Y SEGURIDAD
  # ---------------------------------------------------------------------------

  @regression @ux
  Scenario: TC-SIGNUP-11 - Preservación de datos válidos y vaciado de contraseñas tras error de correo duplicado
    Given existe una cuenta registrada con el correo "usuario.existente@testpetstore.com"
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | usuario.existente@testpetstore.com         |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema recarga el formulario y muestra un mensaje de error junto al campo Email
    And los campos User ID, First name, Last name, Address 1 y Phone conservan los datos ingresados
    And los campos New password y Repeat password quedan vacíos

  @smoke @security
  Scenario: TC-SIGNUP-12 - Verificación de ausencia de sesión automática post-registro
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | P@ssw0rd2026!+                             |
      | Repeat password | P@ssw0rd2026!+                             |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    And da click en "Save Account Information"
    Then el sistema redirige a la página de inicio "/catalog"
    And la cabecera muestra el enlace "Sign In"
    And la cabecera no muestra ningún indicador de sesión iniciada
```

---

## 6. Estrategia y Dataset de Datos de Prueba (Test Data Management)

### 6.1. Datasets Sintéticos Estandarizados

| Parámetro | Regla de Generación | Ejemplo Sintético | Propósito de Prueba |
| :--- | :--- | :--- | :--- |
| **`username` (Válido)** | Exactamente 10 caracteres alfanuméricos con prefijo | `"U" + RandomStringUtils.randomAlphanumeric(9)` | Garantizar unicidad y evitar colisiones de clave primaria en ambiente compartido. |
| **`username` (Límite 9)** | 9 caracteres alfanuméricos | `UserID009` | Verificar rechazo de frontera inferior. |
| **`username` (Límite 11)** | 11 caracteres alfanuméricos | `UserID00011` | Verificar rechazo de frontera superior. |
| **`password` (Válida)** | 8 a 20 chars con Mayús, Minús, Núm y Especial | `P@ssw0rd2026!+` | Cumplir política completa de robustez. |
| **`email` (Válido)** | Timestamp dinámico con dominio mock | `"qa_user_" + System.currentTimeMillis() + "@testpetstore.com"` | Unicidad asegurada por milisegundos. |
| **`phone` (Válido con +)** | 9 dígitos numéricos con prefijo `+` | `+123456789` | Formato internacional estándar. |
| **`phone` (Válido sin +)** | Exactamente 9 dígitos numéricos | `987654321` | Formato nacional estándar. |
| **`zip` (Válido)** | 3 a 10 caracteres alfanuméricos | `110111` o `75001-A` | Formato postal estándar. |

### 6.2. Reglas de Aislamiento y Trazabilidad
* **Independencia de pruebas:** Cada ejecución genera su propio conjunto de datos; ningún escenario depende de que otro caso haya corrido previamente.
* **Prefijo de Trazabilidad:** Todo registro generado por la automatización utiliza el prefijo `QA_` o `U` para facilitar su identificación y depuración en la base de datos de JPetStore.

---

## 7. Criterios de Suspensión y Reanudación

### 7.1. Criterios de Suspensión
La ejecución de la suite de pruebas se detendrá de inmediato si:
1. La URL base `https://petstore.octoperf.com` no responde o retorna errores HTTP 502/503/504 sostenidos.
2. Un cambio imprevisto en el backend o en la plantilla JSP altera los atributos `name` de los inputs HTML principales.
3. El caso crítico `@smoke` (`TC-SIGNUP-01`) falla de manera consistente en el flujo feliz.

### 7.2. Criterios de Reanudación
La ejecución se reanudará cuando:
1. Se verifique la estabilidad y accesibilidad del ambiente mediante una prueba manual exploratoria de 1 registro.
2. Se actualicen los localizadores del Page Object en caso de modificación confirmada de la UI.

---

## 8. Criterios de Aceptación y Aprobación de la Suite

* **Aprobación de la Suite Smoke:** 100% de los casos etiquetados con `@smoke` deben estar en estado `PASSED`.
* **Aprobación de la Suite de Regresión:** Mínimo 95% de casos aprobados en `@regression`, con 0 defectos abiertos de severidad `Must`.
* **Estabilidad de Automatización:** Ausencia de fallos intermitentes (flakiness) en 3 corridas consecutivas en el pipeline de CI.

---

## 9. Notas de Revisión de la Iteración 3

Puntos que requieren decisión antes de automatizar:

1. **Inicio de sesión fuera de alcance:** TC-SIGNUP-01 y TC-SIGNUP-02 validan la cuenta con un inicio de sesión posterior. El documento v4 excluye el inicio de sesión (Sign In) en §2. Decidir si se mantiene el caso (y el alcance se revisa) o si la validación queda solo con el texto de Home.
2. **Botón de envío del inicio de sesión:** el documento no define el nombre del botón ni los campos del formulario de Sign In. Por eso el paso "envía el formulario de inicio de sesión" queda sin nombre de botón.
3. **Texto "docs":** se usa como ejemplo de texto visible en Home. No está verificado en la página. Confirmar el texto exacto antes de ejecutar.
4. **Valores generados:** el documento original no fija un valor para `User ID` ni para `Email` de TC-SIGNUP-01 y TC-SIGNUP-02. Se dejan como generados en ejecución (ver §6.1). Confirmar si se prefiere un valor fijo.
5. **Datos de nombre en TC-SIGNUP-02:** el documento original no define nombre ni apellido para este caso. Se usan "Carlos" y "Tester" de TC-SIGNUP-01. Confirmar.
6. **Cuenta existente:** TC-SIGNUP-03, TC-SIGNUP-04 y TC-SIGNUP-11 requieren la cuenta `TESTUSER01` / `usuario.existente@testpetstore.com` en el ambiente. Confirmar que existe y quién la prepara (sin acceso a base de datos).
7. **Teléfono con prefijo (TC-SIGNUP-08-03):** depende de P15. Si el "+" cuenta dentro de los 9 caracteres, `+12345678` sería válido y el caso debe cambiar.
8. **Mensajes de error:** los escenarios verifican que aparece un mensaje en el campo indicado, sin texto exacto, porque el documento no define el texto. Completar cuando se confirme el mensaje.
9. **Campo "Repeat password" en obligatorios:** TC-SIGNUP-07 no incluye este campo, aunque §6.1 lo marca como obligatorio. Agregarlo es una decisión pendiente.
10. **Frontera válida de Zip:** TC-SIGNUP-10 cubre solo las fronteras inválidas (2 y 11 caracteres). Las fronteras válidas (3 y 10) no tienen escenario todavía.
11. **Longitud de las frases:** cada escenario tiene una tabla de 12 o 13 filas en una sola frase. Esto supera el límite de 10 líneas por frase que pediste. Decidir si se parte la tabla o si se acepta la excepción.
12. **Estado y rutas del plan:** la versión 1.0 figura como "Aprobado para Automatización" y las rutas de §1 y §5 apuntan a una carpeta de usuario distinta (`angro`). Revisar ambos datos antes de automatizar.
