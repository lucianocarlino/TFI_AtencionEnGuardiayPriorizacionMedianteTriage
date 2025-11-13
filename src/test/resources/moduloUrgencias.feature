Feature: Modulo de urgencias
  Esta feature esta relacionada al registro de ingresos en la sala de urgencias
  respetando su nivel de prioridad y el horario de llegada

  Background:
    Given Que la siguiente enfermera esta registrada:
      | Cuil         | Nombre | Apellido | E-mail           | Matricula
      | 23-9876543-6 | Susana | Gimenez  | susana@gmail.com | 12345
    And Las siguientes obras sociales están registradas:
      | Nombre            | Identificador |
      | Subsidio de salud | SS            |
      | Swiss medical     | SM            |
      | FBSA              | FB            |
      | OSDE              | OS            |

  Scenario: Ingreso del primer paciente a la lista de espera de urgencias
    Given que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 23-1234567-9 | Le agarro dengue | Emergencia          | 38          | 70                  | 15                      | 120/80           |
    Then La lista de espera esta ordenada por cuil de la siguiente manera:
      |Cuil          | Estado     |
      | 23-1234567-9 | Pendiente  |

  Scenario: Ingreso de un paciente de bajo nivel de emergencia y luego otro de mayor nivel
    Given que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
      | 27-4563390-3 | Estrella | Patricio  | FBSA              |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 27-4563390-3 | Le duele el ojo  | Sin Urgencia        | 38          | 70                  | 15                      | 120/80           |
      | 23-1234567-9 | Le agarro dengue | Emergencia          | 38          | 70                  | 15                      | 120/80           |
    Then La lista de espera esta ordenada por cuil de la siguiente manera:
      |Cuil          | Estado     |
      | 23-1234567-9 |  Pendiente |
      | 27-4563390-3 |  Pendiente |

  Scenario: Ingreso de dos pacientes criticos
    Given que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
      | 27-4563390-3 | Estrella | Patricio  | FBSA              |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe            | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 27-4563390-3 | Le agarro neumonia | Emergencia          | 37          | 70                  | 16                      | 120/80           |
      | 23-1234567-9 | Le agarro dengue   | Emergencia          | 38          | 70                  | 15                      | 120/80           |
    Then La lista de espera esta ordenada por cuil de la siguiente manera:
      |Cuil          | Estado     |
      | 27-4563390-3 |  Pendiente |
      | 23-1234567-9 |  Pendiente |

  Scenario: Ingreso un paciente sin urgencia y dos pacientes criticos
    Given que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
      | 27-4563390-2 | Estrella | Patricio  | FBSA              |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe            | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 23-1234567-9 | Le agarro dengue   | Emergencia          | 38          | 70                  | 15                      | 120/80           |
      | 27-4563390-2 | Le agarro neumonia | Sin Urgencia        | 37          | 70                  | 16                      | 120/80           |
      | 27-4567890-3 | Se cayo de un piso | Emergencia          | 39          | 90                  | 20                      | 130/90           |
    Then La lista de espera esta ordenada por cuil de la siguiente manera:
        |Cuil          | Estado     |
        | 23-1234567-9 | Pendiente |
        | 27-4567890-3 | Pendiente |
        | 27-4563390-2 | Pendiente |

    Scenario: registrar ingreso con valores negativos en frecuencia cardiaca
      Given que estan registrados los siguientes pacientes en el sistema:
        | Cuil         | Apellido | Nombre    | Obra social       |
        | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
        | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
      When Ingresan a urgencia los siguientes pacientes:
        | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
        | 23-1234567-9 | Le agarro dengue | Emergencia          | 38          | -70                 | 15                      | 120/80           |
      Then el sistema muestra el siguiente mensaje de error: "Frecuencia cardiaca no puede ser negativa"

    Scenario: registrar ingreso con valores negativos en frecuencia respiratoria
      Given que estan registrados los siguientes pacientes en el sistema:
        | Cuil         | Apellido | Nombre    | Obra social       |
        | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
        | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
      When Ingresan a urgencia los siguientes pacientes:
        | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
        | 23-1234567-9 | Le agarro dengue | Emergencia          | 38          | 70                 | -30                      | 120/80           |
      Then el sistema muestra el siguiente mensaje de error: "Frecuencia respiratoria no puede ser negativa"
      

    Scenario: ingreso de paciente no registrado
        Given que estan registrados los siguientes pacientes en el sistema:
            | Cuil         | Apellido | Nombre    | Obra social       |
            | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
            | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
        When Ingresan a urgencia los siguientes pacientes no registrados:
          | Cuil         | Apellido | Nombre | Obra social |
          | 23-1000000-9 | Pedro    | Perez  | OSDE        |
        Then el sistema registra los pacientes con sus respectivos datos en el sistema:
            | Cuil         | Apellido | Nombre | Obra social |
            | 23-1000000-9 | Pedro    | Perez  | OSDE        |
        And la lista de pacientes registrados en el sistema es la siguiente:
          | Cuil         | Apellido | Nombre    | Obra social       |
          | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
          | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
          | 23-1000000-9 | Pedro    | Perez     | OSDE              |

  Scenario: ingreso de un paciente pero Informe fue omitido
    Given que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 23-1234567-9 |         | Emergencia          | 38          | 100                 | 15                      | 120/80           |
    Then el sistema muestra el siguiente mensaje de error: "El informe es un campo obligatorio"

  Scenario: ingreso de un paciente pero Nivel de emergencia fue omitido
    Given que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe         | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 23-1234567-9 | Dolor de cabeza |                     | 38          | 100                 | 15                      | 120/80           |
    Then el sistema muestra el siguiente mensaje de error: "El nivel de emergencia es un campo obligatorio"

  Scenario: ingreso de un paciente pero Frecuencia Cardiaca fue omitida
        Given que estan registrados los siguientes pacientes en el sistema:
            | Cuil         | Apellido | Nombre    | Obra social       |
            | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
            | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
        When Ingresan a urgencia los siguientes pacientes:
          | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
          | 23-1234567-9 | Le agarro dengue | Emergencia          | 38          |                     | 15                      | 120/80           |
        Then el sistema muestra el siguiente mensaje de error: "Frecuencia cardiaca es un campo obligatorio"


  Scenario: ingreso de un paciente pero Frecuencia Respiratoria fue omitido
    Given que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 23-1234567-9 | Le agarro dengue | Emergencia          | 38          | 100                 |                         | 120/80           |
    Then el sistema muestra el siguiente mensaje de error: "Frecuencia respiratoria es un campo obligatorio"

  Scenario: ingreso de un paciente pero frecuencia sistolica fue omitida
    Given que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 23-1234567-9 | Le agarro dengue | Emergencia          | 38          | 100                 | 15                      | /80               |
    Then el sistema muestra el siguiente mensaje de error: "Frecuencia sistolica es un campo obligatorio"

  Scenario: ingreso de un paciente pero frecuencia diastolica fue omitida
    Given que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 23-1234567-9 | Le agarro dengue | Emergencia          | 38          | 100                 | 15                      | 120/             |
    Then el sistema muestra el siguiente mensaje de error: "Frecuencia diastolica es un campo obligatorio"

  Scenario: ingreso de un paciente pero la temperatura fue omitida
    Given que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 23-1234567-9 | Le agarro dengue | Emergencia          |             | 100                 | 15                      | 120/             |
    Then el sistema muestra el siguiente mensaje de error: "Temperatura es un campo obligatorio"
