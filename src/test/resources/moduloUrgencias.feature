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
      When Ingresa a urgencias el siguiente paciente:
        | Cuil         | Informe          | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Presion arterial |
        | 23-1234567-9 | Le agarro dengue | Emergencia          | 38          | 70                  | 15                      | 120/80           |
      Then La lista de espera esta ordenada por cuil de la siguiente manera:
        | 23-1234567-9 |