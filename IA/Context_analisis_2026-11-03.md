# Contexto técnico del proyecto: PetStoreAutomation

**Ruta real del proyecto:** `C:\Users\VICTUS\IdeaProjects\PetStoreAutomation`
**Fecha del análisis:** 2026-11-03
**Alcance:** todo el proyecto, excepto la carpeta `IA/`. Se incluyen archivos generados (`build/`, `.gradle/`) y configuración de IDE (`.idea/`).
**Origen del prompt:** `IA/prompt_context.md`

---

## Resumen

PetStoreAutomation es un proyecto de automatización de pruebas web en Java 21. Usa Cucumber 7 para escribir escenarios en Gherkin, Selenium 4 para interactuar con el navegador y Gradle 9.6 como herramienta de construcción.

El proyecto cubre la funcionalidad de registro de nuevos usuarios (Sign Up) de la aplicación JPetStore (`https://petstore.octoperf.com`). Su estado es inicial: el feature contiene un único escenario (TC-SIGNUP-01) y solo uno de sus pasos tiene implementación. Los demás pasos lanzan `PendingException`. No existe un runner de pruebas en el código fuente.

---

## Arquitectura y módulos

Proyecto de módulo único. No tiene subproyectos ni capas de servicio.

| Ruta | Tipo | Descripción |
| :--- | :--- | :--- |
| `build.gradle` | Configuración | Plugin `java`, dependencias y tarea `test` con `useJUnitPlatform()`. |
| `settings.gradle` | Configuración | Define `rootProject.name = "PetStoreAutomation"`. |
| `gradle/wrapper/` | Configuración | Wrapper de Gradle 9.6.0 (`gradle-wrapper.jar` y propiedades). |
| `gradlew`, `gradlew.bat` | Script | Lanzadores del wrapper para Unix y Windows. |
| `src/main/java/org/example/Main.java` | Código principal | Clase generada por la plantilla de IntelliJ. Imprime un saludo y un contador del 1 al 5. No participa en las pruebas. |
| `src/test/java/driver/DriverManager.java` | Código de pruebas | Administra el `WebDriver` por hilo mediante `ThreadLocal`. Crea un `ChromeDriver` con `--start-maximized` y espera implícita de 10 s. |
| `src/test/java/hook/Hook.java` | Código de pruebas | Hooks de Cucumber: `@Before` obtiene el driver y `@After` lo cierra. |
| `src/test/java/page/BasePage.java` | Código de pruebas | Clase base de Page Object. Métodos: `navigateTo`, `find` (espera explícita de 15 s), `click`, `type`, `getText`, `isDisplayed`. |
| `src/test/java/page/HomePage.java` | Código de pruebas | Page Object de la página de inicio. Solo tiene `accURL(String)`. No define localizadores. |
| `src/test/java/steps/CreateUserStep.java` | Código de pruebas | Definiciones de pasos Cucumber. Solo `ingresa en esta URL {string}` tiene código. Los demás lanzan `PendingException`. |
| `src/test/resources/Feature/CreateUser/CreateUser.feature` | Especificación | Feature de registro con Background y un escenario (TC-SIGNUP-01). |
| `build/` | Generado | Clases compiladas, copia del feature en recursos de prueba, reporte de problemas de Gradle y datos de compilación incremental. |
| `.gradle/` | Generado | Caché y estado de Gradle 9.6.0 (archivos binarios, bloqueos e historial de ejecución). |
| `.idea/` | Configuración de IDE | Configuración de IntelliJ IDEA: JDK, Gradle, compilador y configuraciones de ejecución. |
| `.gitignore` | Configuración | Excluye `build/`, `.gradle/`, `.idea/compiler.xml`, `.idea/modules.xml` y otros. |

Flujo de ejecución previsto por el código:

1. Cucumber lee el feature y localiza las definiciones de pasos en el paquete `steps`.
2. Al iniciar cada escenario, `Hook.setup()` obtiene el driver mediante `DriverManager.getDriver()`.
3. `CreateUserStep` crea un `HomePage` que usa ese driver.
4. Al terminar el escenario, `Hook.tearDown()` cierra el navegador.

---

## Tecnología y versiones

| Componente | Versión | Fuente |
| :--- | :--- | :--- |
| Java | 21 | `.idea/misc.xml` (`languageLevel="JDK_21"`), `.idea/compiler.xml` (`bytecodeTargetLevel 21`) |
| Gradle | 9.6.0 | `gradle/wrapper/gradle-wrapper.properties` |
| Cucumber (cucumber-java, cucumber-core, cucumber-junit) | 7.18.0 | `build.gradle` |
| Cucumber BOM | 7.13.0 | `build.gradle` (`platform("io.cucumber:cucumber-bom")`) |
| cucumber-junit-platform-engine, junit-platform-suite | Gestionado por BOM 7.13.0 | `build.gradle` |
| Selenium Java | 4.20.0 | `build.gradle` |
| JUnit BOM | 6.0.0 | `build.gradle` |
| JUnit Platform Launcher | Gestionado por JUnit BOM | `build.gradle` |
| TestNG | 7.10.1 | `build.gradle` |
| RestAssured | 5.3.1 | `build.gradle` |
| Allure JUnit5 | 2.23.0 | `build.gradle` |
| Hamcrest | 2.2 | `build.gradle` |
| AssertJ | 3.23.1 | `build.gradle` |
| Apache HttpClient | 4.5.14 | `build.gradle` |
| Jackson Databind | 2.14.2 | `build.gradle` |
| commons-lang3 | 3.0 | `build.gradle` |
| opencsv | 5.7.1 | `build.gradle` |
| vavr | 1.0.0-alpha-4 | `build.gradle` |
| Logback (classic y core) | 1.4.5 | `build.gradle` |
| Janino | 3.1.9 | `build.gradle` |

Uso observado de las dependencias: Cucumber, Selenium, AssertJ (declarada, sin uso visible en el código fuente) y Logback/Janino (solo como dependencia). Las demás no tienen uso visible en el código fuente.

Ejecución registrada en el IDE: la configuración `Cucumber Java` usa `io.cucumber.core.cli.Main` con la opción `--plugin teamcity`. Se encontró una configuración de ejecución para el escenario TC-SIGNUP-01.

---

## Contratos (si existen)

No existen contratos formales en el proyecto:

- No hay interfaces Java.
- No hay definiciones de API ni esquemas.
- La única especificación del comportamiento es el feature Gherkin en `src/test/resources`.

---

## Módulos de datos

No existen modelos de datos ni clases de entidad en el código fuente.

- Los datos de prueba se escriben directamente en el feature (tabla de campos del TC-SIGNUP-01).
- No hay archivos de datos externos (CSV, JSON, YAML o base de datos).
- `opencsv` y `Jackson` están declarados, pero no se observa su uso en el código.

---

## Estándares y convenciones del código

- **Paquetes:** en minúsculas, por responsabilidad (`driver`, `hook`, `page`, `steps`).
- **Clases:** PascalCase. Se usa el sufijo `Step` para las definiciones de pasos (`CreateUserStep`) y `Page` para Page Objects (`HomePage`, `BasePage`).
- **Métodos de pasos:** camelCase, en español, con tildes en los nombres (`ingresaALaPáginaDeRegistro`).
- **Frases de pasos:** en español. Palabras clave Gherkin en inglés (`Feature`, `Background`, `Scenario`, `Given`, `When`, `And`, `Then`).
- **Patrón Page Object:** `BasePage` contiene las operaciones comunes. Las clases de página heredan de ella.
- **Gestión del driver:** `ThreadLocal` en `DriverManager`, lo que permite ejecución paralela por hilos.
- **Esperas:** espera implícita de 10 s en el driver y espera explícita de 15 s en `BasePage`.
- **Documentación en código:** comentarios generados por la plantilla de IntelliJ (`// Write code here that turns the phrase above into concrete actions`). `Main.java` conserva los comentarios `TIP` de la plantilla.
- **Gherkin:** escenarios con etiquetas (`@smoke`, `@positive`), tabla de datos por campo y Background con los pasos de acceso.

---

## Riesgos, supuestos y limitaciones

### Riesgos

1. **Sin runner de pruebas.** No hay clase con `@Suite`, `@RunWith` ni `@CucumberOptions`. La tarea `gradle test` con `useJUnitPlatform()` no tiene punto de entrada para Cucumber. La ejecución depende de la configuración de IntelliJ (`io.cucumber.core.cli.Main`), lo que impide la ejecución por línea de comandos.
2. **Sin configuración de glue.** No hay `cucumber.glue` definido. Las definiciones de pasos se encuentran solo por el paquete por defecto.
3. **Firma incompatible en un paso.** `completa el formulario con los siguientes datos:` recibe una tabla en el feature, pero el método `completaElFormularioConLosSiguientesDatos()` no tiene parámetro `DataTable`. Cucumber fallará aunque el paso se implemente.
4. **Pasos sin implementar.** De los 10 pasos usados en el feature, solo `ingresa en esta URL {string}` tiene código. Los demás lanzan `PendingException`.
5. **Cobertura limitada.** El feature solo contiene TC-SIGNUP-01. Los casos TC-SIGNUP-02 a TC-SIGNUP-12 definidos en el plan de pruebas no están en el proyecto.
6. **Creación temprana del driver.** `CreateUserStep` crea `HomePage` en un inicializador de campo, lo que obtiene el driver al instanciar la clase de pasos. Esto puede ocurrir antes de `@Before`. Con `ThreadLocal` no debería haber conflicto, pero no está verificado.
7. **Duplicación y conflicto de versiones.** Janino, Logback y AssertJ aparecen en `implementation` y en `testImplementation`. Cucumber tiene la versión 7.13.0 en el BOM y la 7.18.0 en las declaraciones explícitas.
8. **Dependencias sin uso visible.** TestNG, RestAssured, Allure, Hamcrest, httpclient, Jackson, opencsv y vavr no aparecen en el código fuente.
9. **Dependencia con comentario obsoleto.** El comentario de commons-lang3 menciona `TestEventDataInitializer.java`, que no existe en el proyecto.
10. **Advertencias de Gradle.** El reporte de problemas (`build/reports/problems/problems-report.html`) indica que la notación de dependencias con cadena única (multi-string) está obsoleta y fallará en Gradle 10.
11. **Datos y URL fijos.** La URL base y los datos de prueba están escritos en el feature. No hay archivo de configuración ni variables de entorno.
12. **Información personal en el control de versiones.** `.idea/workspace.xml` contiene la ruta personal `C:/Users/VICTUS/...`. El archivo no está en `.gitignore`.
13. **Sin README.** No hay documentación de instalación, configuración ni ejecución.
14. **Sin repositorio Git.** No se observa el directorio `.git`.

### Supuestos

- El análisis parte de los archivos presentes en el disco en el momento del análisis. No se verificó su estado en un repositorio remoto.
- La versión de Gradle indicada en `gradle-wrapper.properties` es la que se usa para compilar.
- Las rutas de la configuración de IDE se refieren a la máquina del desarrollador actual.

### Limitaciones del análisis

- No se ejecutó la compilación ni las pruebas. Los riesgos se basan en la lectura del código.
- `.gradle/` contiene archivos binarios de caché (historial de ejecución, sumas de verificación y bloqueos). No son legibles como texto y no se analizó su contenido.
- En `build/`, las clases compiladas (`.class`) se listan por nombre y tamaño. No se decompilaron.
- La copia del feature en `build/resources/test/` tiene el mismo tamaño que el original (2196 bytes), pero no se comparó su contenido byte por byte.
- El reporte de problemas de Gradle es un HTML de 130 KB. Se revisaron sus advertencias de la notación multi-string. No se revisó el reporte completo.

---

## Artefactos generados y de configuración (detalle)

### `build/`

- `classes/java/main/org/example/Main.class`
- `classes/java/test/`: `DriverManager.class`, `Hook.class`, `BasePage.class`, `HomePage.class`, `CreateUserStep.class`. No hay clase runner compilada.
- `resources/test/Feature/CreateUser/CreateUser.feature`: copia del feature de `src/test/resources`.
- `reports/problems/problems-report.html`: reporte de problemas de Gradle.
- `tmp/`: datos de compilación incremental (`previous-compilation-data.bin`).

### `.gradle/`

- Versión de caché `9.6.0`: `checksums`, `executionHistory`, `fileHashes`, `fileChanges`, `expanded`, `kotlin-dsl-plugin-entries`, `vcsMetadata`.
- `buildOutputCleanup/`: archivos de limpieza de salidas.
- Archivos de bloqueo (`*.lock`) y `gc.properties`.

### `.idea/workspace.xml`

- Configuración personal del IDE: estado de cambios, historial de tareas, preferencias de Node.js y ajustes del editor.
- **Configuraciones de ejecución:**
  - `Feature: CreateUser` con `io.cucumber.core.cli.Main` y `--plugin teamcity`.
  - `Scenario: TC-SIGNUP-01 - Registro exitoso con valores por defecto en preferencias de perfil`, con filtro por nombre de escenario.
  - `Gradle.Build PetStoreAutomation`.
- **Punto de interrupción:** `src/main/java/org/example/Main.java`, línea 13.
- **Ruta personal:** `onboarding.tips.debug.path` apunta a `C:/Users/VICTUS/IdeaProjects/PetStoreAutomation/src/main/java/org/example/Main.java`.
- **JDK configurado:** 21.
- **Gradle:** cargado con el proyecto (`gradle.xml` apunta al directorio raíz).

---

## Mantenimiento

Este documento debe actualizarse después de cada implementación, corrección o modificación relevante del proyecto. Las secciones de riesgos y de tecnología son las que más cambian al implementar pasos o ajustar dependencias.
