"use client"

import { useState, useEffect } from "react"
import Sidebar from "@/components/sidebar"
import { Card, CardContent } from "@/components/ui/card"
import type { NivelEmergencia, IngresoResponse } from "@/lib/types"
import { listarUrgencias } from "@/lib/api"
import AuthGuard from "@/components/auth-guard"

export default function ColaUrgencias() {
  const [ingresos, setIngresos] = useState<IngresoResponse[]>([])
  const [filtroNivel, setFiltroNivel] = useState<string>("todos")

  const [loading, setLoading] = useState(true)
  const [error, setError] = useState("")

  const cargarDatos = async () => {
    setLoading(true)
    try {
      const data = await listarUrgencias()
      setIngresos(data)
      setError("")
    } catch (err: any) {
      setError("Error al cargar la lista de espera. Verifique que el servidor esté encendido.")
    } finally {
      setLoading(false)
    }
  }

  // Cargar datos al iniciar la página
  useEffect(() => {
    cargarDatos()
  }, [])

  const getNivelInfo = (nivel: NivelEmergencia) => {
    const info: Record<NivelEmergencia, { label: string; color: string; bg: string; borderColor: string }> = {
      CRITICA: {
        label: "CRITICA",
        color: "text-destructive",
        bg: "bg-destructive/10",
        borderColor: "border-destructive/20",
      },
      EMERGENCIA: { label: "EMERGENCIA", color: "text-accent", bg: "bg-accent/10", borderColor: "border-accent/20" },
      URGENCIA: { label: "URGENCIA", color: "text-warning", bg: "bg-warning/10", borderColor: "border-warning/20" },
      URGENCIA_MENOR: {
        label: "URGENCIA_MENOR",
        color: "text-primary",
        bg: "bg-primary/10",
        borderColor: "border-primary/20",
      },
      SIN_URGENCIA: {
        label: "SIN_URGENCIA",
        color: "text-primary",
        bg: "bg-primary/10",
        borderColor: "border-primary/20",
      },
    }
    return info[nivel]
  }

  const ingresosFiltrados =
    filtroNivel === "todos" ? ingresos : ingresos.filter((i) => i.nivelEmergencia === filtroNivel)

  // Ordena por nivel de emergencia (crítico primero)
  const nivelesOrden: Record<string, number> = {
    CRITICA: 0,
    EMERGENCIA: 1,
    URGENCIA: 2,
    URGENCIA_MENOR: 3,
    SIN_URGENCIA: 4,
  }

  const ingresosOrdenados = [...ingresosFiltrados].sort(
    (a, b) => nivelesOrden[a.nivelEmergencia] - nivelesOrden[b.nivelEmergencia],
  )

  return (
    <AuthGuard>
      <div className="flex min-h-screen bg-background">
        <Sidebar />

        <main className="flex-1">
          <div className="border-b border-border bg-card">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
              <h1 className="text-3xl font-bold text-foreground">Cola de Urgencias</h1>
              <p className="mt-2 text-muted-foreground">Visualiza todos los ingresos ordenados por prioridad</p>
            </div>
          </div>

          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div className="mb-6 flex gap-2 flex-wrap">
              <button
                onClick={() => setFiltroNivel("todos")}
                className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                  filtroNivel === "todos"
                    ? "bg-primary text-primary-foreground"
                    : "bg-muted text-muted-foreground hover:bg-muted/80"
                }`}
              >
                Todos ({ingresos.length})
              </button>
              <button
                onClick={() => setFiltroNivel("CRITICA")}
                className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                  filtroNivel === "CRITICA"
                    ? "bg-destructive text-destructive-foreground"
                    : "bg-destructive/10 text-destructive hover:bg-destructive/20"
                }`}
              >
                Crítico ({ingresos.filter((i) => i.nivelEmergencia === "CRITICA").length})
              </button>
              <button
                onClick={() => setFiltroNivel("EMERGENCIA")}
                className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                  filtroNivel === "EMERGENCIA"
                    ? "bg-accent text-accent-foreground"
                    : "bg-accent/10 text-accent hover:bg-accent/20"
                }`}
              >
                Emergencia ({ingresos.filter((i) => i.nivelEmergencia === "EMERGENCIA").length})
              </button>
            </div>

            <div className="space-y-4">
              {ingresosOrdenados.length === 0 ? (
                <Card>
                  <CardContent className="pt-8">
                    <p className="text-center text-muted-foreground">No hay ingresos registrados</p>
                  </CardContent>
                </Card>
              ) : (
                ingresosOrdenados.map((ingreso, index) => {
                  const nivelInfo = getNivelInfo(ingreso.nivelEmergencia)
                  return (
                    <Card key={ingreso.paciente.cuil} className={`border-2 ${nivelInfo.borderColor}`}>
                      <CardContent className="pt-6">
                        <div className="flex flex-col md:flex-row md:items-start md:justify-between gap-4">
                          <div className="flex-1">
                            <div className="flex items-center gap-3 mb-3">
                              <div
                                className={`px-3 py-1 rounded-full text-sm font-bold ${nivelInfo.bg} ${nivelInfo.color}`}
                              >
                                #{index + 1} - {nivelInfo.label}
                              </div>
                            </div>
                            <h3 className="text-lg font-bold text-foreground">{`${ingreso.paciente.nombre} ${ingreso.paciente.apellido}`}</h3>
                            <h3 className="text-lg text-foreground">Estado: {ingreso.estado}</h3>
                            <p className="text-sm text-muted-foreground">CUIL: {ingreso.paciente.cuil}</p>
                            <p className="text-sm text-muted-foreground mt-2">
                              Enfermera: {ingreso.enfermera.nombre} {ingreso.enfermera.apellido}
                            </p>
                            <p className="text-sm text-foreground mt-3 leading-relaxed">{ingreso.informe}</p>
                          </div>

                          <div className="md:w-80">
                            <div className="bg-muted/50 rounded-lg p-4 space-y-2">
                              <h4 className="font-semibold text-foreground mb-3">Signos Vitales</h4>
                              <div className="grid grid-cols-2 gap-3 text-sm">
                                <div>
                                  <p className="text-muted-foreground">Temperatura</p>
                                  <p className="font-bold text-foreground">{ingreso.temperatura}°C</p>
                                </div>
                                <div>
                                  <p className="text-muted-foreground">Frecuencia Cardíaca</p>
                                  <p className="font-bold text-foreground">
                                    {`${ingreso.frecuenciaCardiaca.value}`} lpm
                                  </p>
                                </div>
                                <div>
                                  <p className="text-muted-foreground">Frecuencia Respiratoria</p>
                                  <p className="font-bold text-foreground">
                                    {`${ingreso.frecuenciaRespiratoria.value}`} rpm
                                  </p>
                                </div>
                                <div>
                                  <p className="text-muted-foreground">Tensión Arterial</p>
                                  <p className="font-bold text-foreground">
                                    {`${ingreso.tensionArterial.frecuenciaSistolica}`}/
                                    {`${ingreso.tensionArterial.frecuenciaDiastolica}`}
                                  </p>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  )
                })
              )}
            </div>
          </div>
        </main>
      </div>
    </AuthGuard>
  )
}
