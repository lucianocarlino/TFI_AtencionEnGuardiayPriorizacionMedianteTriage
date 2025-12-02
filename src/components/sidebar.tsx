"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { cn } from "@/lib/utils"
import { useState, useEffect } from "react"
import type { AuthUser } from "@/lib/types"
import { hasAllowedRole } from "@/lib/role-utils"

export default function Sidebar() {
  const pathname = usePathname()
  const [user, setUser] = useState<AuthUser | null>(null)

  useEffect(() => {
    const userStr = localStorage.getItem("user")
    if (userStr) {
      const userData = JSON.parse(userStr)
      setUser(userData)
    }
  }, [])

  const isActive = (path: string) => {
    return pathname.startsWith(path)
  }

  const hasRole = (role: string) => {
    if (!user?.autoridad) return false
    return hasAllowedRole(user.autoridad, [role])
  }

  return (
    <aside className="w-64 bg-sidebar text-sidebar-foreground border-r border-sidebar-border">
      <div className="p-6 border-b border-sidebar-border">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-sidebar-primary rounded-lg flex items-center justify-center">
            <svg
              className="w-6 h-6 text-sidebar-primary-foreground"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
          </div>
          <div>
            <h1 className="font-bold text-lg">MedUrgencias</h1>
            <p className="text-xs text-sidebar-foreground/70">Sistema de Urgencias</p>
          </div>
        </div>
      </div>
      <div className="pt-2 pb-1">
        <p className="px-4 text-xs font-semibold text-sidebar-foreground/60 uppercase tracking-wide">
          Usuario: {user?.email || "Cargando..."}
        </p>
        {user && <p className="px-4 text-xs text-sidebar-foreground/50 mt-1">Rol: {user.autoridad}</p>}
      </div>

      <nav className="p-4 space-y-2">
        <Link
          href="/"
          className={cn(
            "flex items-center gap-3 px-4 py-3 rounded-lg transition-colors",
            isActive("/") && !isActive("/pacientes") && !isActive("/urgencias") && !isActive("/medico")
              ? "bg-sidebar-primary text-sidebar-primary-foreground"
              : "text-sidebar-foreground hover:bg-sidebar-accent/20",
          )}
        >
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M3 12l2-3m0 0l7-4 7 4M5 9v10a1 1 0 001 1h12a1 1 0 001-1V9m-9 9l-7-4"
            />
          </svg>
          <span className="font-medium">Inicio</span>
        </Link>

        {hasRole("ENFERMERA") && (
          <>
            <div className="pt-2 pb-1">
              <p className="px-4 text-xs font-semibold text-sidebar-foreground/60 uppercase tracking-wide">Pacientes</p>
            </div>

            <Link
              href="/pacientes/registrar"
              className={cn(
                "flex items-center gap-3 px-4 py-3 rounded-lg transition-colors",
                isActive("/pacientes/registrar")
                  ? "bg-sidebar-primary text-sidebar-primary-foreground"
                  : "text-sidebar-foreground hover:bg-sidebar-accent/20",
              )}
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z"
                />
              </svg>
              <span className="font-medium">Registrar Paciente</span>
            </Link>

            <Link
              href="/pacientes/listar"
              className={cn(
                "flex items-center gap-3 px-4 py-3 rounded-lg transition-colors",
                isActive("/pacientes/listar")
                  ? "bg-sidebar-primary text-sidebar-primary-foreground"
                  : "text-sidebar-foreground hover:bg-sidebar-accent/20",
              )}
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"
                />
              </svg>
              <span className="font-medium">Ver Pacientes</span>
            </Link>

            <div className="pt-2 pb-1">
              <p className="px-4 text-xs font-semibold text-sidebar-foreground/60 uppercase tracking-wide">Urgencias</p>
            </div>

            <Link
              href="/urgencias/registrar"
              className={cn(
                "flex items-center gap-3 px-4 py-3 rounded-lg transition-colors",
                isActive("/urgencias/registrar")
                  ? "bg-sidebar-primary text-sidebar-primary-foreground"
                  : "text-sidebar-foreground hover:bg-sidebar-accent/20",
              )}
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
                />
              </svg>
              <span className="font-medium">Registrar Urgencia</span>
            </Link>
          </>
        )}

        {hasRole("MEDICO") && (
          <>
            <div className="pt-2 pb-1">
              <p className="px-4 text-xs font-semibold text-sidebar-foreground/60 uppercase tracking-wide">
                Atención Médica
              </p>
            </div>

            <Link
              href="/medico/reclamar"
              className={cn(
                "flex items-center gap-3 px-4 py-3 rounded-lg transition-colors",
                isActive("/medico/reclamar")
                  ? "bg-sidebar-primary text-sidebar-primary-foreground"
                  : "text-sidebar-foreground hover:bg-sidebar-accent/20",
              )}
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"
                />
              </svg>
              <span className="font-medium">Reclamar Paciente</span>
            </Link>

            <Link
              href="/medico/atencion"
              className={cn(
                "flex items-center gap-3 px-4 py-3 rounded-lg transition-colors",
                isActive("/medico/atencion")
                  ? "bg-sidebar-primary text-sidebar-primary-foreground"
                  : "text-sidebar-foreground hover:bg-sidebar-accent/20",
              )}
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                />
              </svg>
              <span className="font-medium">Registrar Atención</span>
            </Link>
          </>
        )}

        <div className="pt-2 pb-1">
          <p className="px-4 text-xs font-semibold text-sidebar-foreground/60 uppercase tracking-wide">Consulta</p>
        </div>

        <Link
          href="/urgencias/cola"
          className={cn(
            "flex items-center gap-3 px-4 py-3 rounded-lg transition-colors",
            isActive("/urgencias/cola")
              ? "bg-sidebar-primary text-sidebar-primary-foreground"
              : "text-sidebar-foreground hover:bg-sidebar-accent/20",
          )}
        >
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"
            />
          </svg>
          <span className="font-medium">Cola de Urgencias</span>
        </Link>

        <Link
          href="/pacientes/listar"
          className={cn(
            "flex items-center gap-3 px-4 py-3 rounded-lg transition-colors",
            isActive("/pacientes/listar") && !hasRole("ENFERMERA")
              ? "bg-sidebar-primary text-sidebar-primary-foreground"
              : "text-sidebar-foreground hover:bg-sidebar-accent/20",
          )}
        >
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"
            />
          </svg>
          <span className="font-medium">Ver Pacientes</span>
        </Link>
      </nav>
    </aside>
  )
}
