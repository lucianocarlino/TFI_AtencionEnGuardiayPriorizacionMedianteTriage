"use client"

import { useEffect, useState } from "react"
import AuthGuard from "@/components/auth-guard"
import Sidebar from "@/components/sidebar"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { listarAtencionesMedico } from "@/lib/api"
import type { AtencionResponse } from "@/lib/types"
import { useToast } from "@/hooks/use-toast"

function MisAtencionesPage() {
  const [atenciones, setAtenciones] = useState<AtencionResponse[]>([])
  const [loading, setLoading] = useState(true)
  const { toast } = useToast()

  useEffect(() => {
    const cargarAtenciones = async () => {
      try {
        const data = await listarAtencionesMedico()

        setAtenciones(data)
      } catch (error) {
        console.error("Error al cargar atenciones:", error)
      } finally {
        setLoading(false)
      }
    }

    cargarAtenciones()
  }, [toast])

  const getNivelColor = (nivel: string) => {
    switch (nivel) {
      case "CRITICA":
        return "bg-destructive/10 text-destructive border-destructive/20"
      case "EMERGENCIA":
        return "bg-accent/10 text-accent border-accent/20"
      case "URGENCIA":
        return "bg-warning/10 text-warning border-warning/20"
      case "URGENCIA_MENOR":
        return "bg-success/10 text-success border-success/20"
      case "SIN_URGENCIA":
        return "bg-primary/10 text-primary border-primary/20"
      default:
        return ""
    }
  }

  return (
    <div className="flex min-h-screen bg-background">
      <Sidebar />

      <main className="flex-1">
        <div className="border-b border-border bg-card">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
            <h1 className="text-3xl font-bold text-foreground">Mis Registros de Atención</h1>
            <p className="mt-2 text-muted-foreground">Historial de pacientes atendidos</p>
          </div>
        </div>

        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {loading ? (
            <Card>
              <CardContent className="p-6">
                <p className="text-center text-muted-foreground">Cargando registros de atención...</p>
              </CardContent>
            </Card>
          ) : atenciones.length === 0 ? (
            <Card>
              <CardContent className="p-6">
                <div className="text-center space-y-2">
                  <svg
                    className="w-16 h-16 mx-auto text-muted-foreground/50"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                    />
                  </svg>
                  <p className="text-lg font-medium text-foreground">No hay atenciones registradas</p>
                  <p className="text-sm text-muted-foreground">
                    Los pacientes que atiendas aparecerán aquí con sus informes de atención
                  </p>
                </div>
              </CardContent>
            </Card>
          ) : (
            <div className="grid gap-6">
              {atenciones.map((atencion) => (
                <Card key={atencion.id} className="overflow-hidden">
                  <CardHeader className="bg-muted/30">
                    <div className="flex items-start justify-between gap-4">
                      <div className="flex-1">
                        <CardTitle className="text-2xl mb-1">
                          {atencion.ingreso.paciente.nombre} {atencion.ingreso.paciente.apellido}
                        </CardTitle>
                        <CardDescription className="text-base">CUIL: {atencion.ingreso.paciente.cuil}</CardDescription>
                      </div>
                      <Badge className={`${getNivelColor(atencion.ingreso.nivelEmergencia)} border`}>
                        {atencion.ingreso.nivelEmergencia}
                      </Badge>
                    </div>
                  </CardHeader>
                  <CardContent className="space-y-6 pt-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div className="flex items-start gap-3 p-3 bg-muted/50 rounded-lg">
                        <svg
                          className="w-5 h-5 text-primary mt-0.5 shrink-0"
                          fill="none"
                          stroke="currentColor"
                          viewBox="0 0 24 24"
                        >
                          <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth={2}
                            d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"
                          />
                        </svg>
                        <div>
                          <p className="text-xs text-muted-foreground">Fecha de Ingreso</p>
                          <p className="font-semibold text-foreground">
                            {new Date(atencion.ingreso.fechaIngreso).toLocaleString("es-AR", {
                              dateStyle: "short",
                              timeStyle: "short",
                            })}
                          </p>
                        </div>
                      </div>
                      <div className="flex items-start gap-3 p-3 bg-success/5 border border-success/20 rounded-lg">
                        <svg
                          className="w-5 h-5 text-success mt-0.5 shrink-0"
                          fill="none"
                          stroke="currentColor"
                          viewBox="0 0 24 24"
                        >
                          <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth={2}
                            d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
                          />
                        </svg>
                        <div>
                          <p className="text-xs text-muted-foreground">Fecha de Atención</p>
                          <p className="font-semibold text-foreground">
                            {new Date(atencion.fechaAtencion).toLocaleString("es-AR", {
                              dateStyle: "short",
                              timeStyle: "short",
                            })}
                          </p>
                        </div>
                      </div>
                    </div>

                    <div>
                      <h3 className="text-sm font-semibold text-foreground mb-3">Signos Vitales del Ingreso</h3>
                      <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                        <div className="p-3 bg-muted rounded-lg">
                          <p className="text-xs text-muted-foreground">Temperatura</p>
                          <p className="text-lg font-semibold">{atencion.ingreso.temperatura}°C</p>
                        </div>
                        <div className="p-3 bg-muted rounded-lg">
                          <p className="text-xs text-muted-foreground">Freq. Cardíaca</p>
                          <p className="text-lg font-semibold">{atencion.ingreso.frecuenciaCardiaca.value} lpm</p>
                        </div>
                        <div className="p-3 bg-muted rounded-lg">
                          <p className="text-xs text-muted-foreground">Freq. Respiratoria</p>
                          <p className="text-lg font-semibold">{atencion.ingreso.frecuenciaRespiratoria.value} rpm</p>
                        </div>
                        <div className="p-3 bg-muted rounded-lg">
                          <p className="text-xs text-muted-foreground">Tensión Arterial</p>
                          <p className="text-lg font-semibold">
                            {atencion.ingreso.tensionArterial.frecuenciaSistolica}/
                            {atencion.ingreso.tensionArterial.frecuenciaDiastolica}
                          </p>
                        </div>
                      </div>
                    </div>

                    <div>
                      <h3 className="text-sm font-semibold text-foreground mb-2">Informe de Ingreso (Enfermería)</h3>
                      <div className="bg-muted/50 p-4 rounded-lg">
                        <p className="text-sm text-foreground leading-relaxed">{atencion.ingreso.informe}</p>
                      </div>
                    </div>

                    <div>
                      <h3 className="text-sm font-semibold text-foreground mb-2">Informe de Atención Médica</h3>
                      <div className="bg-primary/5 border border-primary/20 p-4 rounded-lg">
                        <p className="text-foreground leading-relaxed">{atencion.informe}</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>
      </main>
    </div>
  )
}

export default function Page() {
  return (
    <AuthGuard allowedRoles={["MEDICO"]}>
      <MisAtencionesPage />
    </AuthGuard>
  )
}
