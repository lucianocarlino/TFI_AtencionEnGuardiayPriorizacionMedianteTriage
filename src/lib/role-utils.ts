export type NormalizedRole = "ENFERMERA" | "MEDICO"

/**
 * Normaliza el rol del usuario para comparaciones consistentes
 * Convierte variaciones como "Enfermero", "enfermera", "ENFERMERA" a "ENFERMERA"
 */
export function normalizeRole(role: string): NormalizedRole {
  const normalized = role.toUpperCase().trim()

  // Manejar variaciones de enfermera/enfermero
  if (normalized === "ENFERMERA" || normalized === "ENFERMERO") {
    return "ENFERMERA"
  }

  // Manejar variaciones de médico
  if (normalized === "MEDICO" || normalized === "MÉDICO") {
    return "MEDICO"
  }

  // Por defecto devolver el rol normalizado a mayúsculas
  return normalized as NormalizedRole
}

/**
 * Verifica si el usuario tiene alguno de los roles permitidos
 */
export function hasAllowedRole(userRole: string, allowedRoles: string[]): boolean {
  const normalizedUserRole = normalizeRole(userRole)
  const normalizedAllowedRoles = allowedRoles.map((role) => normalizeRole(role))

  return normalizedAllowedRoles.includes(normalizedUserRole)
}
