export enum NivelEmergencia {
  CRITICA = "CRITICA",
  EMERGENCIA = "EMERGENCIA",
  URGENCIA = "URGENCIA",
  URGENCIA_MENOR = "URGENCIA_MENOR",
  SIN_URGENCIA = "SIN_URGENCIA",
}
export enum EstadoIngreso {
  PENDIENTE = "Pendiente",

  EN_PROCESO = "En proceso",

  FINALIZADO = "Finalizado",
}
export interface IngresoDTO {
  cuilPaciente: string
  informe: string
  nivelEmergencia: NivelEmergencia
  temperatura: number
  frecCardiaca: number
  frecRespiratoria: number
  frecuenciaSistolica: number
  frecuenciaDiastolica: number
  enfermera: {
    id?: number
    nombre?: string
  } | null
}

export interface FrecuenciaValue {
  value: number
}

export interface TensionArterialValue {
  frecuenciaSistolica: number
  frecuenciaDiastolica: number
}

export interface Paciente {
  cuil: string
  nombre: string
  apellido: string
  afiliado: Afiliado // Objeto anidado
  direccion?: Domicilio
}

export interface Enfermera {
  cuil: string
  nombre: string
  apellido: string
  email: string
  matricula: string
}

export interface ObraSocial {
  identificador: string
  nombre: string
}

export interface Afiliado {
  obraSocial: ObraSocial
  numAfiliado: string
}

export interface Domicilio {
  calle: string
  numero: number
  localidad: string
}

export interface PacienteRegistroDTO {
  cuil: string
  nombre: string
  apellido: string
  calle?: string
  numero?: string
  localidad?: string
  provincia?: string
  obraSocial?: string
  numAfiliado?: string
}

export interface PacienteResponse {
  cuil: string
  nombre: string
  apellido: string
  afiliado?: Afiliado
  direccion?: Domicilio
}

export interface IngresoResponse {
  paciente: Paciente
  enfermera: Enfermera
  fechaIngreso: string // LocalDateTime viaja como string ISO "2023-11-26T18:00:00"
  informe: string
  nivelEmergencia: NivelEmergencia
  estado: EstadoIngreso

  temperatura: number
  frecuenciaCardiaca: FrecuenciaValue
  frecuenciaRespiratoria: FrecuenciaValue
  tensionArterial: TensionArterialValue
}

export interface LoginRequest {
  email: string
  contrasena: string
}

export interface LoginResponse {
  email: string
  autoridad: string
  mensaje: string
  nombre : string
  apellido : string
}

export interface AuthUser {
  email: string
  autoridad: string
}

export interface ReclamarPacienteResponse {
  ingreso: IngresoResponse
}

export interface AtencionDTO {
  cuilPaciente: string
  medico: {
    cuil: string
    nombre: string
    apellido: string
    email: string
  }
  informeAtencion: string
}

export interface AtencionResponse {
  id: number
  ingreso: IngresoResponse
  informe: string
  medico: {
    cuil: string
    nombre: string
    apellido: string
    email: string
  }
  fechaAtencion: string
}
