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
    // Si solo envías ID, ajusta esto. Si envías objeto:
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
  direccion?: Domicilio // Puede ser null (según tu primer constructor)
}

export interface Enfermera {
  cuil: string
  nombre: string
  apellido: string
  email: string
  matricula: string
}

// --- Sub-estructuras ---
export interface ObraSocial {
  // Asumo que tu clase ObraSocial tiene estos campos
  identificador: string
  nombre: string
}

export interface Afiliado {
  obraSocial: ObraSocial
  numAfiliado: string // Si tu clase Afiliado lo tiene
}

export interface Domicilio {
  calle: string
  numero: number
  localidad: string
  // Agrega aquí lo que tenga tu clase Domicilio
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

// 3. La Interfaz Principal de Respuesta
export interface IngresoResponse {
  paciente: Paciente
  enfermera: Enfermera
  fechaIngreso: string // LocalDateTime viaja como string ISO "2023-11-26T18:00:00"
  informe: string
  nivelEmergencia: NivelEmergencia
  estado: EstadoIngreso

  // Aquí está la diferencia clave por tus Value Objects:
  temperatura: number
  frecuenciaCardiaca: FrecuenciaValue // Será un objeto, no un número directo
  frecuenciaRespiratoria: FrecuenciaValue // Será un objeto
  tensionArterial: TensionArterialValue // Será un objeto
}

export interface LoginRequest {
  email: string
  contrasena: string
}

export interface LoginResponse {
  email: string
  autoridad: string
  mensaje: string
}

export interface AuthUser {
  email: string
  autoridad: string
}
