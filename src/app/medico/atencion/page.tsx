"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import Sidebar from "@/components/sidebar"
import AuthGuard from "@/components/auth-guard"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { registrarAtencion } from "@/lib/api"
import type { IngresoResponse } from "@/lib/types"
import { useToast } from "@/hooks/use-toast"
import { useFormik } from "formik"
import * as Yup from "yup"

const validationSchema = Yup.object({
  informe: Yup.string()
    .required("El informe de atención es obligatorio")
    .min(20, "El informe debe tener al menos 20 caracteres"),
})

function AtencionPage() {
  const router = useRouter()
  const { toast } = useToast()
  const [ingresoReclamado, setIngresoReclamado] = useState<IngresoResponse | null>(null)

  useEffect(() => {
    const ingreso = localStorage.getItem("ingresoReclamado")


    if (ingreso && ingreso !== "undefined") {
      try {
        const parsed = JSON.parse(ingreso)
        setIngresoReclamado(parsed)
      } catch (error) {
        console.error("Error al parsear el ingreso del localStorage:", error)
        localStorage.removeItem("ingresoReclamado")
      }
    } else {
      console.warn("No hay ingreso reclamado en localStorage")
    }
  }, [])

  const formik = useFormik({
    initialValues: {
      informe: "",
    },
    validationSchema,
    onSubmit: async (values, { setSubmitting }) => {
      if (!ingresoReclamado) {
        toast({
          title: "Error",
          description: "No hay paciente reclamado para registrar atención",
          variant: "destructive",
        })
        return
      }

      const userStr = localStorage.getItem("user")
      if (!userStr) {
        toast({
          title: "Error",
          description: "No se encontró información del médico autenticado",
          variant: "destructive",
        })
        return
      }

      let user
      try {
        user = JSON.parse(userStr)
      } catch (error) {
        console.error("Error al parsear usuario:", error)
        toast({
          title: "Error",
          description: "Error al obtener información del médico",
          variant: "destructive",
        })
        return
      }

      try {
        const atencionData = {
          cuilPaciente: ingresoReclamado.paciente.cuil,
          medico: {
            cuil: user.email,
            nombre: user.nombre || "Médico",
            apellido: user.apellido || "Sistema",
            email: user.email,
          },
          informeAtencion: values.informe,
        }

        await registrarAtencion(atencionData)

        toast({
          title: "Atención registrada",
          description: "La atención médica se ha registrado exitosamente",
          variant: "default",
        })

        localStorage.removeItem("ingresoReclamado")

        setTimeout(() => {
          router.push("/")
        }, 1500)
      } catch (error) {
        console.error(" Error al registrar atención:", error)
        let errorMessage = "Error al registrar atención"

        if (error instanceof Error) {
          const message = error.message.toLowerCase()
          if (message.includes("500") || message.includes("internal server error")) {
            errorMessage =
              "Error del servidor al registrar la atención. Verifica que todos los datos sean correctos y que el paciente esté en estado válido."
          } else {
            errorMessage = error.message
          }
        }

        toast({
          title: "Error",
          description: errorMessage,
          variant: "destructive",
        })
      } finally {
        setSubmitting(false)
      }
    },
  })

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

  if (!ingresoReclamado) {
    return (
      <div className="flex min-h-screen bg-background">
        <main className="flex-1">
          <div className="border-b border-border bg-card">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
              <h1 className="text-3xl font-bold text-foreground">Registrar Atención</h1>
              <p className="mt-2 text-muted-foreground">Registra el informe de atención médica del paciente</p>
            </div>
          </div>
          <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <Alert variant="destructive">
              <AlertDescription>
                No hay un paciente reclamado. Debes reclamar un paciente primero antes de registrar una atención.
              </AlertDescription>
            </Alert>
            <div className="mt-6">
              <Button onClick={() => router.push("/medico/reclamar")}>Reclamar Paciente</Button>
            </div>
          </div>
        </main>
      </div>
    )
  }

  return (
    <div className="flex min-h-screen bg-background">

      <main className="flex-1">
        <div className="border-b border-border bg-card">
          <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
            <h1 className="text-3xl font-bold text-foreground">Registrar Atención</h1>
            <p className="mt-2 text-muted-foreground">Registra el informe de atención médica del paciente</p>
          </div>
        </div>

        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle>Información del Paciente</CardTitle>
                <CardDescription>Datos del ingreso reclamado</CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <p className="text-sm font-medium text-muted-foreground">Paciente</p>
                    <p className="text-lg font-semibold text-foreground">
                      {ingresoReclamado.paciente.nombre} {ingresoReclamado.paciente.apellido}
                    </p>
                  </div>

                  <div className="space-y-2">
                    <p className="text-sm font-medium text-muted-foreground">CUIL</p>
                    <p className="text-lg font-semibold text-foreground">{ingresoReclamado.paciente.cuil}</p>
                  </div>

                  <div className="space-y-2">
                    <p className="text-sm font-medium text-muted-foreground">Fecha de Ingreso</p>
                    <p className="text-lg font-semibold text-foreground">
                      {new Date(ingresoReclamado.fechaIngreso).toLocaleString("es-AR")}
                    </p>
                  </div>

                  <div className="space-y-2">
                    <p className="text-sm font-medium text-muted-foreground">Nivel de Emergencia</p>
                    <div
                      className={`inline-block px-4 py-2 rounded-lg border-2 font-semibold ${getNivelColor(ingresoReclamado.nivelEmergencia)}`}
                    >
                      {ingresoReclamado.nivelEmergencia}
                    </div>
                  </div>
                </div>

                <div className="space-y-2">
                  <p className="text-sm font-medium text-muted-foreground">Informe de Ingreso</p>
                  <p className="text-foreground bg-muted p-3 rounded-lg">{ingresoReclamado.informe}</p>
                </div>

                <div className="space-y-2">
                  <p className="text-sm font-semibold text-foreground">Signos Vitales</p>
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                    <div className="p-3 bg-muted rounded-lg">
                      <p className="text-xs text-muted-foreground">Temperatura</p>
                      <p className="text-lg font-semibold">{ingresoReclamado.temperatura}°C</p>
                    </div>
                    <div className="p-3 bg-muted rounded-lg">
                      <p className="text-xs text-muted-foreground">Freq. Cardíaca</p>
                      <p className="text-lg font-semibold">{ingresoReclamado.frecuenciaCardiaca.value} lpm</p>
                    </div>
                    <div className="p-3 bg-muted rounded-lg">
                      <p className="text-xs text-muted-foreground">Freq. Respiratoria</p>
                      <p className="text-lg font-semibold">{ingresoReclamado.frecuenciaRespiratoria.value} rpm</p>
                    </div>
                    <div className="p-3 bg-muted rounded-lg">
                      <p className="text-xs text-muted-foreground">Tensión Arterial</p>
                      <p className="text-lg font-semibold">
                        {ingresoReclamado.tensionArterial.frecuenciaSistolica}/
                        {ingresoReclamado.tensionArterial.frecuenciaDiastolica}
                      </p>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Informe de Atención Médica</CardTitle>
                <CardDescription>Registra el diagnóstico y tratamiento del paciente</CardDescription>
              </CardHeader>
              <CardContent>
                <form onSubmit={formik.handleSubmit} className="space-y-6">
                  <div>
                    <label className="block text-sm font-medium text-foreground mb-2">Informe de Atención *</label>
                    <textarea
                      {...formik.getFieldProps("informe")}
                      placeholder="Diagnóstico, tratamiento indicado, observaciones clínicas, medicamentos prescritos, etc."
                      rows={8}
                      className="w-full px-3 py-2 border border-input rounded-lg bg-card text-foreground placeholder-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                    />
                    {formik.touched.informe && formik.errors.informe && (
                      <p className="text-destructive text-sm mt-1">{formik.errors.informe}</p>
                    )}
                    <p className="text-xs text-muted-foreground mt-2">
                      El informe debe incluir detalles sobre el diagnóstico, tratamiento y cualquier observación
                      relevante
                    </p>
                  </div>

                  <Alert>
                    <AlertDescription>
                      <strong>Importante:</strong> Al registrar la atención, el estado del ingreso cambiará a FINALIZADO
                      y el paciente saldrá completamente del sistema de urgencias.
                    </AlertDescription>
                  </Alert>

                  <div className="flex gap-4 pt-4">
                    <Button type="submit" disabled={formik.isSubmitting} className="bg-primary hover:bg-primary/90">
                      {formik.isSubmitting ? "Registrando..." : "Registrar Atención"}
                    </Button>
                    <Button type="button" variant="outline" onClick={() => router.push("/")}>
                      Cancelar
                    </Button>
                  </div>
                </form>
              </CardContent>
            </Card>
          </div>
        </div>
      </main>
    </div>
  )
}

export default function Page() {
  return (
    <AuthGuard allowedRoles={["MEDICO"]}>
      <AtencionPage />
    </AuthGuard>
  )
}
