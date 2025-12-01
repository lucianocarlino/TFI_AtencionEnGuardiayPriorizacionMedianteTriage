"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { NivelEmergencia } from "@/lib/types"
import { registrarIngreso } from "@/lib/api"
import { useFormik } from "formik"
import * as Yup from "yup"
import { useToast } from "@/hooks/use-toast"
import { useEffect, useState } from "react"

const validationSchema = Yup.object({
  cuilPaciente: Yup.string()
    .required("CUIL es obligatorio")
    .matches(/^\d{2}-\d{8}-\d$/, "Formato de CUIL inválido (XX-XXXXXXXX-X)"),
  informe: Yup.string().required("Informe es obligatorio").min(10, "El informe debe tener al menos 10 caracteres"),
  nivelEmergencia: Yup.string().required("Nivel de emergencia es obligatorio"),
  temperatura: Yup.number()
    .required("Temperatura es obligatoria")
    .min(35, "Temperatura debe ser mayor a 35°C")
    .max(43, "Temperatura debe ser menor a 43°C"),
  frecCardiaca: Yup.number()
    .required("Frecuencia cardíaca es obligatoria")
    .min(30, "Frecuencia cardíaca debe ser mayor a 30 lpm")
    .max(250, "Frecuencia cardíaca debe ser menor a 250 lpm"),
  frecRespiratoria: Yup.number()
    .required("Frecuencia respiratoria es obligatoria")
    .min(8, "Frecuencia respiratoria debe ser mayor a 8 rpm")
    .max(60, "Frecuencia respiratoria debe ser menor a 60 rpm"),
  frecuenciaSistolica: Yup.number()
    .required("Presión sistólica es obligatoria")
    .min(70, "Presión sistólica debe ser mayor a 70 mmHg")
    .max(250, "Presión sistólica debe ser menor a 250 mmHg"),
  frecuenciaDiastolica: Yup.number()
    .required("Presión diastólica es obligatoria")
    .min(40, "Presión diastólica debe ser mayor a 40 mmHg")
    .max(150, "Presión diastólica debe ser menor a 150 mmHg"),
})

export default function UrgenciaForm() {
  const { toast } = useToast()
  const [userEmail, setUserEmail] = useState<string>("")

  useEffect(() => {
    const user = localStorage.getItem("user")
    if (user) {
      const userData = JSON.parse(user)
      setUserEmail(userData.email)
    }
  }, [])

  const formik = useFormik({
    initialValues: {
      cuilPaciente: "",
      informe: "",
      nivelEmergencia: NivelEmergencia.SIN_URGENCIA,
      temperatura: "",
      frecCardiaca: "",
      frecRespiratoria: "",
      frecuenciaSistolica: "",
      frecuenciaDiastolica: "",
    },
    validationSchema,
    onSubmit: async (values, { setSubmitting, resetForm }) => {
      try {
        const ingresoDTO = {
          cuilPaciente: values.cuilPaciente,
          informe: values.informe,
          nivelEmergencia: values.nivelEmergencia,
          temperatura: Number.parseFloat(values.temperatura),
          frecCardiaca: Number.parseFloat(values.frecCardiaca),
          frecRespiratoria: Number.parseFloat(values.frecRespiratoria),
          frecuenciaSistolica: Number.parseFloat(values.frecuenciaSistolica),
          frecuenciaDiastolica: Number.parseFloat(values.frecuenciaDiastolica),
          enfermera: { cuil: 1, nombre: userEmail },
        }

        await registrarIngreso(ingresoDTO)
        toast({
          title: "Éxito",
          description: "Urgencia registrada exitosamente",
          variant: "default",
        })
        resetForm()
      } catch (error) {
        toast({
          title: "Error",
          description: error instanceof Error ? error.message : "Error al registrar urgencia",
          variant: "destructive",
        })
      } finally {
        setSubmitting(false)
      }
    },
  })

  const getNivelColor = (nivel: NivelEmergencia) => {
    switch (nivel) {
      case NivelEmergencia.CRITICA:
        return "bg-destructive/10 border-destructive/20 text-destructive"
      case NivelEmergencia.EMERGENCIA:
        return "bg-accent/10 border-accent/20 text-accent"
      case NivelEmergencia.URGENCIA:
        return "bg-warning/10 border-warning/20 text-warning"
      case NivelEmergencia.URGENCIA_MENOR:
        return "bg-primary/10 border-primary/20 text-primary"
      case NivelEmergencia.SIN_URGENCIA:
        return "bg-primary/10 border-primary/20 text-primary"
      default:
        return ""
    }
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Información de Urgencia</CardTitle>
        <CardDescription>Completa los datos del paciente y signos vitales</CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={formik.handleSubmit} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">CUIL Paciente *</label>
              <Input
                type="text"
                {...formik.getFieldProps("cuilPaciente")}
                placeholder="20-12345678-9"
                className={formik.touched.cuilPaciente && formik.errors.cuilPaciente ? "border-destructive" : ""}
              />
              {formik.touched.cuilPaciente && formik.errors.cuilPaciente && (
                <p className="text-destructive text-sm mt-1">{formik.errors.cuilPaciente}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Enfermero/a *</label>
              <Input type="text" value={userEmail} readOnly className="bg-muted" />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-foreground mb-2">Nivel de Emergencia *</label>
            <div
              className={`p-4 rounded-lg border-2 ${getNivelColor(formik.values.nivelEmergencia as NivelEmergencia)}`}
            >
              <select
                {...formik.getFieldProps("nivelEmergencia")}
                className="w-full bg-transparent font-semibold outline-none"
              >
                <option value="SIN_URGENCIA">SIN URGENCIA - Consulta ambulatoria</option>
                <option value="URGENCIA_MENOR">URGENCIA MENOR</option>
                <option value="URGENCIA">URGENCIA</option>
                <option value="EMERGENCIA">EMERGENCIA</option>
                <option value="CRITICA">CRITICA - Riesgo de vida inmediato</option>
              </select>
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-foreground mb-2">Informe Médico *</label>
            <textarea
              {...formik.getFieldProps("informe")}
              placeholder="Descripción de síntomas y observaciones clínicas..."
              rows={4}
              className="w-full px-3 py-2 border border-input rounded-lg bg-card text-foreground placeholder-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
            />
            {formik.touched.informe && formik.errors.informe && (
              <p className="text-destructive text-sm mt-1">{formik.errors.informe}</p>
            )}
          </div>

          <div>
            <h3 className="text-base font-semibold text-foreground mb-4">Signos Vitales</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-foreground mb-2">Temperatura (°C) *</label>
                <Input
                  type="number"
                  {...formik.getFieldProps("temperatura")}
                  placeholder="36.5"
                  step="0.1"
                  className={formik.touched.temperatura && formik.errors.temperatura ? "border-destructive" : ""}
                />
                {formik.touched.temperatura && formik.errors.temperatura && (
                  <p className="text-destructive text-sm mt-1">{formik.errors.temperatura}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-2">Frecuencia Cardíaca (lpm) *</label>
                <Input
                  type="number"
                  {...formik.getFieldProps("frecCardiaca")}
                  placeholder="80"
                  className={formik.touched.frecCardiaca && formik.errors.frecCardiaca ? "border-destructive" : ""}
                />
                {formik.touched.frecCardiaca && formik.errors.frecCardiaca && (
                  <p className="text-destructive text-sm mt-1">{formik.errors.frecCardiaca}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-2">
                  Frecuencia Respiratoria (rpm) *
                </label>
                <Input
                  type="number"
                  {...formik.getFieldProps("frecRespiratoria")}
                  placeholder="16"
                  className={
                    formik.touched.frecRespiratoria && formik.errors.frecRespiratoria ? "border-destructive" : ""
                  }
                />
                {formik.touched.frecRespiratoria && formik.errors.frecRespiratoria && (
                  <p className="text-destructive text-sm mt-1">{formik.errors.frecRespiratoria}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-2">Presión Sistólica (mmHg) *</label>
                <Input
                  type="number"
                  {...formik.getFieldProps("frecuenciaSistolica")}
                  placeholder="120"
                  className={
                    formik.touched.frecuenciaSistolica && formik.errors.frecuenciaSistolica ? "border-destructive" : ""
                  }
                />
                {formik.touched.frecuenciaSistolica && formik.errors.frecuenciaSistolica && (
                  <p className="text-destructive text-sm mt-1">{formik.errors.frecuenciaSistolica}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-2">Presión Diastólica (mmHg) *</label>
                <Input
                  type="number"
                  {...formik.getFieldProps("frecuenciaDiastolica")}
                  placeholder="80"
                  className={
                    formik.touched.frecuenciaDiastolica && formik.errors.frecuenciaDiastolica
                      ? "border-destructive"
                      : ""
                  }
                />
                {formik.touched.frecuenciaDiastolica && formik.errors.frecuenciaDiastolica && (
                  <p className="text-destructive text-sm mt-1">{formik.errors.frecuenciaDiastolica}</p>
                )}
              </div>
            </div>
          </div>

          <div className="flex gap-4 pt-6">
            <Button type="submit" disabled={formik.isSubmitting} className="bg-primary hover:bg-primary/90">
              {formik.isSubmitting ? "Registrando..." : "Registrar Urgencia"}
            </Button>
            <Button type="button" variant="outline" onClick={() => window.history.back()}>
              Cancelar
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  )
}
