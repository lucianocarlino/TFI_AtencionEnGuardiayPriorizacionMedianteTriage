Feature: Autenticacion
  Como usuario del sistema
  Quiero poder registrarme e iniciar sesion en el sistema
  Para poder acceder a las actividades que me son otorgadas

  Background:
    Given Existen los siguientes usuarios:
      | Email             | Contrasena | Autoridad |
      | luciano@gmail.com | luciano123 | Medico    |
    And Que el usuario actual es:
      | Email | Contrasena | Autoridad |
      |       |            |           |

  Scenario: Inicio de sesion exitoso de un usuario
    When  Intenta iniciar sesion el siguiente usuario:
      | Email             | Contrasena |
      | enfermero@hospital.com | password1 |
    Then El usuario actual es:
      | Email             | Contrasena | Autoridad |
      | enfermero@hospital.com | password1 | Enfermero    |

  Scenario: Inicio de sesion fallido por usuario incorrecto
    When Intenta iniciar sesion el siguiente usuario:
      | Email            | Contrasena |
      | lucianogmail.com | luciano123 |
    Then El sistema muestra el mensaje de error "Usuario o contrasena invalido"

  Scenario: Inicio de sesion fallido por contrasena incorrecta
    When Intenta iniciar sesion el siguiente usuario:
      | Email             | Contrasena |
      | luciano@gmail.com | luciano12  |
    Then El sistema muestra el mensaje de error "Usuario o contrasena invalido"

  Scenario: Registro fallido de un usuario con email invalido
    When Intenta crearse el siguiente usuario:
      | Email        | Contrasena | Autoridad |
      | liomessi.com | lio10      |     Medico      |
    Then El sistema muestra el mensaje de error "Email invalido"

  Scenario: Regustro fallido de un usuario con email existente
    When Intenta crearse el siguiente usuario:
      | Email             | Contrasena | Autoridad |
      | luciano@gmail.com | lio10      |    Medico       |
    Then El sistema muestra el mensaje de error "Email existente"

  Scenario: Registro fallido de un usuario con contrasena invalida
    When Intenta crearse el siguiente usuario:
      | Email           | Contrasena | Autoridad |
      | liomessi@10.com | 1234567    |    Medico       |
    Then El sistema muestra el mensaje de error "Contrasena demasiado corta"

  Scenario: Registro exitoso de un usuario con autoridad vinculada
    When Intenta crearse el siguiente usuario:
      | Email           | Contrasena | Autoridad |
      | liomessi@10.com | 12345678   | Enfermero |
    Then La lista de usuarios es:
      | Email             | Contrasena | Autoridad |
      | luciano@gmail.com | luciano123 | Medico    |
      | liomessi@10.com   | 12345678   | Enfermero |

  Scenario: Registro exitoso de un usuario sin autoridad vinculada
    When Intenta crearse el siguiente usuario:
      | Email         | Contrasena | Autoridad |
      | franco@43.com | alpine43   |           |
    Then La lista de usuarios es:
      | Email             | Contrasena | Autoridad |
      | luciano@gmail.com | luciano123 | Medico    |
      | franco@43.com     | alpine43   |           |
