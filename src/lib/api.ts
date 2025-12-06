import type { IngresoDTO } from "@/lib/types"
import type { IngresoResponse } from "@/lib/types"
import type { LoginRequest, LoginResponse } from "@/lib/types"
import type { PacienteRegistroDTO, PacienteResponse } from "@/lib/types"
import type { ReclamarPacienteResponse } from "@/lib/types"
import type { AtencionDTO } from "@/lib/types"
import type { RegisterRequest, RegisterResponse } from "@/lib/types"

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

export async function obtenerProximoPaciente(): Promise<IngresoResponse | null> {
  const urgencias = await listarUrgencias()
  if (urgencias.length === 0) {
    return null
  }
  return urgencias[0]
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

export async function reclamarPaciente(): Promise<ReclamarPacienteResponse> {
  console.log("[v0] Llamando a reclamarPaciente...")
  console.log("[v0] URL del endpoint:", `${API_URL}/reclamar`)

  try {
    const response = await fetch(`${API_URL}/reclamar`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    })

    console.log("[v0] Response status:", response.status)
    console.log("[v0] Response ok:", response.ok)

    if (!response.ok) {
      const error = await response.text()
      console.error("[v0] Error al reclamar paciente:", error)
      throw new Error(error || "Error al reclamar paciente")
    }

    const data = await response.json()
    console.log("[v0] Paciente reclamado exitosamente:", data)
    if (!data.ingreso) {
      console.error("[v0] Respuesta del servidor no contiene campo 'ingreso':", data)
      throw new Error("Respuesta inválida del servidor")
    }
    return data
  } catch (error) {
    console.error("[v0] Exception en reclamarPaciente:", error)
    throw error
  }
}

export async function registrarAtencion(datos: AtencionDTO): Promise<string> {
  console.log("[v0] Llamando a registrarAtencion con datos:", datos)
  const response = await fetch(`${API_URL}/atencion`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(datos),
  })

  if (!response.ok) {
    const error = await response.text()
    console.error("[v0] Error al registrar atención:", error)
    throw new Error(error || "Error al registrar atención")
  }

  const result = await response.text()
  console.log("[v0] Atención registrada exitosamente:", result)
  return result
}

export async function register(datos: RegisterRequest): Promise<RegisterResponse> {
  const response = await fetch(`${AUTH_URL}/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(datos),
  })

  if (!response.ok) {
    const error = await response.text()
    throw new Error(error || "Error al registrar usuario")
  }

  return await response.json()
}
