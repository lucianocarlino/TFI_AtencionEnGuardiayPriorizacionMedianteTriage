import type { IngresoDTO } from "@/lib/types"
import type { IngresoResponse } from "@/lib/types"
import type { LoginRequest, LoginResponse } from "@/lib/types"
import type { PacienteRegistroDTO, PacienteResponse } from "@/lib/types"

const API_URL = "http://localhost:8080/sistema-urgencias/api/v1/urgencias"
const AUTH_URL = "http://localhost:8080/sistema-urgencias/api/v1/auth"
const PACIENTES_URL = "http://localhost:8080/sistema-urgencias/api/v1/pacientes"

export async function registrarIngreso(datos: IngresoDTO) {
  const response = await fetch(`${API_URL}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(datos),
  })
  if (!response.ok) {
    throw new Error("Error al registrar Urgencia: " + (await response.text()))
  }
  return await response.text()
}

export async function listarUrgencias(): Promise<IngresoResponse[]> {
  const response = await fetch(`${API_URL}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  })
  if (!response.ok) {
    throw new Error("Error al obtener lista de urgencias pendientes")
  }
  return await response.json()
}

export async function login(credentials: LoginRequest): Promise<LoginResponse> {
  const response = await fetch(`${AUTH_URL}/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(credentials),
  })

  if (!response.ok) {
    const error = await response.text()
    throw new Error(error || "Error al iniciar sesión")
  }

  return await response.json()
}

export async function getCurrentUser(): Promise<LoginResponse> {
  const response = await fetch(`${AUTH_URL}/me`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  })

  if (!response.ok) {
    throw new Error("No hay usuario autenticado")
  }

  return await response.json()
}

export async function registrarPaciente(datos: PacienteRegistroDTO): Promise<string> {
  const response = await fetch(`${PACIENTES_URL}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(datos),
  })
  if (!response.ok) {
    const error = await response.text()
    throw new Error(error || "Error al registrar paciente")
  }
  return await response.text()
}

export async function listarPacientes(): Promise<PacienteResponse[]> {
  const response = await fetch(`${PACIENTES_URL}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  })
  if (!response.ok) {
    throw new Error("Error al obtener lista de pacientes")
  }
  return await response.json()
}
