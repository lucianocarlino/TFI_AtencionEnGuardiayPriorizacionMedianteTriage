Feature: Modulo de urgencias
  Esta feature esta relacionada al registro de ingresos en la sala de urgencias
  respetando su nivel de prioridad y el horario de llegada

  Background:
    Given Que la siguiente enfermera esta registrada:
      | Nombre | Apellido |
      | Susana | Gimenez  |


  Scenario: Ingreso del primer paciente a la lista de espera de urgencias
    Given Dado que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 23-1234567-9 | Le agarro dengue | Emergencia          | 38          | 70                  | 15                      | 120/80           |
    Then La lista de espera esta ordenada por cuil de la siguiente manera:
      | 23-1234567-9 |

  Scenario: Ingreso de un paciente de bajo nivel de emergencia y luego otro de mayor nivel
    Given Dado que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
      | 27-4563390-3 | Estrella | Patricio  | FBSA              |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 27-4563390-3 | Le duele el ojo  | Sin Urgencia        | 38          | 70                  | 15                      | 120/80           |
      | 23-1234567-9 | Le agarro dengue | Emergencia          | 38          | 70                  | 15                      | 120/80           |
    Then La lista de espera esta ordenada por cuil de la siguiente manera:
      | 23-1234567-9 |
      | 27-4563390-3 |

  Scenario: Ingreso de dos pacientes criticos
    Given Dado que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
      | 27-4563390-3 | Estrella | Patricio  | FBSA              |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe            | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 27-4563390-3 | Le agarro neumonia | Emergencia          | 37          | 70                  | 16                      | 120/80           |
      | 23-1234567-9 | Le agarro dengue   | Emergencia          | 38          | 70                  | 15                      | 120/80           |
    Then La lista de espera esta ordenada por cuil de la siguiente manera:
      | 27-4563390-3 |
      | 23-1234567-9 |

  Scenario: Ingreso un paciente sin urgencia y dos pacientes criticos
    Given Dado que estan registrados los siguientes pacientes en el sistema:
      | Cuil         | Apellido | Nombre    | Obra social       |
      | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
      | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
      | 27-4563390-3 | Estrella | Patricio  | FBSA              |
    When Ingresan a urgencia los siguientes pacientes:
      | Cuil         | Informe            | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
      | 27-4563390-3 | Le agarro neumonia | Sin Urgencia        | 37          | 70                  | 16                      | 120/80           |
      | 23-1234567-9 | Le agarro dengue   | Emergencia          | 38          | 70                  | 15                      | 120/80           |
      | 27-4567890-3 | Se cayo de un piso | Emergencia          | 39          | 90                  | 20                      | 130/90           |
    Then La lista de espera esta ordenada por cuil de la siguiente manera:
        | 23-1234567-9 |
        | 27-4567890-3 |
        | 27-4563390-3 |

    Scenario: registrar ingreso con valores negativos en frecuencia cardiaca
      Given Dado que estan registrados los siguientes pacientes en el sistema:
        | Cuil         | Apellido | Nombre    | Obra social       |
        | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
        | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
      When Ingresan a urgencia los siguientes pacientes:
        | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
        | 23-1234567-9 | Le agarro dengue | Emergencia          | 38          | -70                 | 15                      | 120/80           |
      Then el sistema muestra el siguiente mensaje de error: "Frecuencia cardiaca no puede ser negativa"

    Scenario: ingreso de paciente no registrado
        Given Dado que estan registrados los siguientes pacientes en el sistema:
            | Cuil         | Apellido | Nombre    | Obra social       |
            | 23-1234567-9 | Nunez    | Marcelo   | Subsidio de salud |
            | 27-4567890-3 | Dufour   | Alexandra | Swiss medical     |
        When Ingresan a urgencia los siguientes pacientes:
            | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
            | 30-1234567-9 | Le agarro dengue | Emergencia          | 38          | 70                  | 15                      | 120/80           |
        Then el sistema registra el paciente con el siguiente mensaje: "Paciente no registrado, se procede a su registro"
        And La lista de espera esta ordenada por cuil de la siguiente manera:
            | 30-1234567-9 |
