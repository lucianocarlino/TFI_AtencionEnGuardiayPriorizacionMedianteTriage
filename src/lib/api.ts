import type { IngresoDTO } from "@/lib/types"
import type { IngresoResponse } from "@/lib/types"
import type { LoginRequest, LoginResponse } from "@/lib/types"
import type { PacienteRegistroDTO, PacienteResponse } from "@/lib/types"
import type { ReclamarPacienteResponse } from "@/lib/types"
import type { AtencionDTO, AtencionResponse } from "@/lib/types"

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


  try {
    const response = await fetch(`${API_URL}/reclamar`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    })


    if (!response.ok) {
      const error = await response.text()
      console.error("Error al reclamar paciente:", error)
      throw new Error(error || "Error al reclamar paciente")
    }

    const data = await response.json()
    if (!data.ingreso) {
      console.error(" Respuesta del servidor no contiene campo 'ingreso':", data)
      throw new Error("Respuesta inválida del servidor")
    }
    return data
  } catch (error) {
    console.error(" Exception en reclamarPaciente:", error)
    throw error
  }
}

export async function registrarAtencion(datos: AtencionDTO): Promise<string> {

  const response = await fetch(`${API_URL}/atencion`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(datos),
  })

  if (!response.ok) {
    const error = await response.text()
    console.error(" Error al registrar atención:", error)
    throw new Error(error || "Error al registrar atención")
  }

  const result = await response.text()
  return result
}



export async function listarAtencionesMedico(): Promise<AtencionResponse[]> {
  const userStr = localStorage.getItem("user")
  if (!userStr) {
    throw new Error("No hay usuario autenticado")
  }

  let user
  try {
    user = JSON.parse(userStr)
  } catch (error) {
    throw new Error("Error al obtener información del médico")
  }

  const response = await fetch(`${API_URL}/atenciones/medico?email=${encodeURIComponent(user.email)}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  })
  if (!response.ok) {
    const error = await response.text()
    throw new Error(error || "Error al obtener las atenciones del médico")
  }
  return await response.json()
}
