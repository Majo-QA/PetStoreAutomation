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