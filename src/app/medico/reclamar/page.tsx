"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import Sidebar from "@/components/sidebar"
import AuthGuard from "@/components/auth-guard"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { reclamarPaciente, obtenerProximoPaciente } from "@/lib/api"
import type { IngresoResponse } from "@/lib/types"
import { useToast } from "@/hooks/use-toast"

function ReclamarPacientePage() {
  const router = useRouter()
  const { toast } = useToast()
  const [loading, setLoading] = useState(false)
  const [loadingProximo, setLoadingProximo] = useState(true)
  const [proximoPaciente, setProximoPaciente] = useState<IngresoResponse | null>(null)
  const [pacienteReclamado, setPacienteReclamado] = useState<IngresoResponse | null>(null)

  useEffect(() => {
    const cargarProximoPaciente = async () => {
      try {
        const proximo = await obtenerProximoPaciente()
        console.log("[v0] Próximo paciente en la cola:", proximo)
        setProximoPaciente(proximo)
      } catch (error) {
        console.error("[v0] Error al obtener próximo paciente:", error)
      } finally {
        setLoadingProximo(false)
      }
    }

    cargarProximoPaciente()
  }, [])

  const handleReclamar = async () => {
    setLoading(true)
    try {
      console.log("[v0] Iniciando reclamar paciente...")
      const response = await reclamarPaciente()
      console.log("[v0] Respuesta del servidor:", response)
      setPacienteReclamado(response.ingreso)

      toast({
        title: "Paciente reclamado",
        description: "El paciente ha sido asignado exitosamente. Ahora puedes registrar la atención.",
        variant: "default",
      })

      // Guardar el ingreso reclamado en localStorage para usarlo en la página de atención
      console.log("[v0] Guardando ingreso en localStorage:", response.ingreso)
      localStorage.setItem("ingresoReclamado", JSON.stringify(response.ingreso))

      // Redirigir a la página de registrar atención
      setTimeout(() => {
        router.push("/medico/atencion")
      }, 1500)
    } catch (error) {
      console.error("[v0] Error en handleReclamar:", error)
      toast({
        title: "Error",
        description: error instanceof Error ? error.message : "Error al reclamar paciente",
        variant: "destructive",
      })
    } finally {
      setLoading(false)
    }
  }

  const getNivelColor = (nivel: string) => {
    switch (nivel) {
      case "CRITICA":
        return "bg-destructive/10 border-destructive text-destructive"
      case "EMERGENCIA":
        return "bg-accent/10 border-accent text-accent"
      case "URGENCIA":
        return "bg-warning/10 border-warning text-warning"
      case "URGENCIA_MENOR":
        return "bg-success/10 border-success text-success"
      case "SIN_URGENCIA":
        return "bg-primary/10 border-primary text-primary"
      default:
        return ""
    }
  }

  return (
    <div className="flex min-h-screen bg-background">
      <Sidebar />

      <main className="flex-1">
        <div className="border-b border-border bg-card">
          <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
            <h1 className="text-3xl font-bold text-foreground">Reclamar Paciente</h1>
            <p className="mt-2 text-muted-foreground">
              Reclama el próximo paciente en la lista de espera para iniciar su atención
            </p>
          </div>
        </div>

        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {!pacienteReclamado ? (
            <div className="space-y-6">
              {loadingProximo ? (
                <Card>
                  <CardContent className="py-8">
                    <p className="text-center text-muted-foreground">Cargando próximo paciente...</p>
                  </CardContent>
                </Card>
              ) : proximoPaciente ? (
                <>
                  <Card>
                    <CardHeader>
                      <CardTitle>Próximo Paciente en la Cola</CardTitle>
                      <CardDescription>Información del paciente que será asignado al reclamar</CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div className="space-y-2">
                          <p className="text-sm font-medium text-muted-foreground">Paciente</p>
                          <p className="text-lg font-semibold text-foreground">
                            {proximoPaciente.paciente.nombre} {proximoPaciente.paciente.apellido}
                          </p>
                        </div>

                        <div className="space-y-2">
                          <p className="text-sm font-medium text-muted-foreground">CUIL</p>
                          <p className="text-lg font-semibold text-foreground">{proximoPaciente.paciente.cuil}</p>
                        </div>

                        <div className="space-y-2">
                          <p className="text-sm font-medium text-muted-foreground">Fecha de Ingreso</p>
                          <p className="text-lg font-semibold text-foreground">
                            {new Date(proximoPaciente.fechaIngreso).toLocaleString("es-AR")}
                          </p>
                        </div>

                        <div className="space-y-2">
                          <p className="text-sm font-medium text-muted-foreground">Nivel de Emergencia</p>
                          <div
                            className={`inline-block px-4 py-2 rounded-lg border-2 font-semibold ${getNivelColor(proximoPaciente.nivelEmergencia)}`}
                          >
                            {proximoPaciente.nivelEmergencia}
                          </div>
                        </div>
                      </div>

                      <div className="space-y-2">
                        <p className="text-sm font-medium text-muted-foreground">Informe de Ingreso</p>
                        <p className="text-foreground bg-muted p-3 rounded-lg">{proximoPaciente.informe}</p>
                      </div>

                      <div className="space-y-2">
                        <p className="text-sm font-semibold text-foreground">Signos Vitales</p>
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                          <div className="p-3 bg-muted rounded-lg">
                            <p className="text-xs text-muted-foreground">Temperatura</p>
                            <p className="text-lg font-semibold">{proximoPaciente.temperatura}°C</p>
                          </div>
                          <div className="p-3 bg-muted rounded-lg">
                            <p className="text-xs text-muted-foreground">Freq. Cardíaca</p>
                            <p className="text-lg font-semibold">{proximoPaciente.frecuenciaCardiaca.value} lpm</p>
                          </div>
                          <div className="p-3 bg-muted rounded-lg">
                            <p className="text-xs text-muted-foreground">Freq. Respiratoria</p>
                            <p className="text-lg font-semibold">{proximoPaciente.frecuenciaRespiratoria.value} rpm</p>
                          </div>
                          <div className="p-3 bg-muted rounded-lg">
                            <p className="text-xs text-muted-foreground">Tensión Arterial</p>
                            <p className="text-lg font-semibold">
                              {proximoPaciente.tensionArterial.frecuenciaSistolica}/
                              {proximoPaciente.tensionArterial.frecuenciaDiastolica}
                            </p>
                          </div>
                        </div>
                      </div>
                    </CardContent>
                  </Card>

                  <Card>
                    <CardHeader>
                      <CardTitle>Reclamar Paciente</CardTitle>
                      <CardDescription>Confirma que deseas reclamar este paciente para atención</CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-6">
                      <Alert>
                        <AlertDescription>
                          <strong>Nota:</strong> Al reclamar este paciente, se te asignará automáticamente y saldrá de
                          la lista de espera. Deberás completar el informe de atención antes de poder reclamar otro
                          paciente.
                        </AlertDescription>
                      </Alert>

                      <div className="flex gap-4">
                        <Button onClick={handleReclamar} disabled={loading} className="bg-primary hover:bg-primary/90">
                          {loading ? "Reclamando..." : "Reclamar Este Paciente"}
                        </Button>
                        <Button type="button" variant="outline" onClick={() => router.push("/")}>
                          Volver al Inicio
                        </Button>
                      </div>
                    </CardContent>
                  </Card>
                </>
              ) : (
                <Card>
                  <CardHeader>
                    <CardTitle>No hay pacientes en espera</CardTitle>
                    <CardDescription>No hay pacientes disponibles para reclamar en este momento</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <Alert>
                      <AlertDescription>
                        La lista de espera está vacía. No hay pacientes pendientes de atención.
                      </AlertDescription>
                    </Alert>
                    <div className="mt-6">
                      <Button onClick={() => router.push("/")}>Volver al Inicio</Button>
                    </div>
                  </CardContent>
                </Card>
              )}
            </div>
          ) : (
            <Card>
              <CardHeader>
                <CardTitle>Paciente Reclamado Exitosamente</CardTitle>
                <CardDescription>Información del paciente asignado</CardDescription>
              </CardHeader>
              <CardContent className="space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <p className="text-sm font-medium text-muted-foreground">Paciente</p>
                    <p className="text-lg font-semibold text-foreground">
                      {pacienteReclamado.paciente.nombre} {pacienteReclamado.paciente.apellido}
                    </p>
                  </div>

                  <div className="space-y-2">
                    <p className="text-sm font-medium text-muted-foreground">CUIL</p>
                    <p className="text-lg font-semibold text-foreground">{pacienteReclamado.paciente.cuil}</p>
                  </div>

                  <div className="space-y-2">
                    <p className="text-sm font-medium text-muted-foreground">Fecha de Ingreso</p>
                    <p className="text-lg font-semibold text-foreground">
                      {new Date(pacienteReclamado.fechaIngreso).toLocaleString("es-AR")}
                    </p>
                  </div>

                  <div className="space-y-2">
                    <p className="text-sm font-medium text-muted-foreground">Nivel de Emergencia</p>
                    <div
                      className={`inline-block px-4 py-2 rounded-lg border-2 font-semibold ${getNivelColor(pacienteReclamado.nivelEmergencia)}`}
                    >
                      {pacienteReclamado.nivelEmergencia}
                    </div>
                  </div>
                </div>

                <div className="space-y-2">
                  <p className="text-sm font-medium text-muted-foreground">Informe de Ingreso</p>
                  <p className="text-foreground">{pacienteReclamado.informe}</p>
                </div>

                <div className="space-y-2">
                  <p className="text-sm font-semibold text-foreground">Signos Vitales</p>
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                    <div className="p-3 bg-muted rounded-lg">
                      <p className="text-xs text-muted-foreground">Temperatura</p>
                      <p className="text-lg font-semibold">{pacienteReclamado.temperatura}°C</p>
                    </div>
                    <div className="p-3 bg-muted rounded-lg">
                      <p className="text-xs text-muted-foreground">Freq. Cardíaca</p>
                      <p className="text-lg font-semibold">{pacienteReclamado.frecuenciaCardiaca.value} lpm</p>
                    </div>
                    <div className="p-3 bg-muted rounded-lg">
                      <p className="text-xs text-muted-foreground">Freq. Respiratoria</p>
                      <p className="text-lg font-semibold">{pacienteReclamado.frecuenciaRespiratoria.value} rpm</p>
                    </div>
                    <div className="p-3 bg-muted rounded-lg">
                      <p className="text-xs text-muted-foreground">Tensión Arterial</p>
                      <p className="text-lg font-semibold">
                        {pacienteReclamado.tensionArterial.frecuenciaSistolica}/
                        {pacienteReclamado.tensionArterial.frecuenciaDiastolica}
                      </p>
                    </div>
                  </div>
                </div>

                <Alert>
                  <AlertDescription>Redirigiendo a la página de registro de atención...</AlertDescription>
                </Alert>
              </CardContent>
            </Card>
          )}
        </div>
      </main>
    </div>
  )
}

export default function Page() {
  return (
    <AuthGuard allowedRoles={["MEDICO"]}>
      <ReclamarPacientePage />
    </AuthGuard>
  )
}
