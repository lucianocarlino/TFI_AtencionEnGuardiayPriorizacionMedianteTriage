"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { useFormik } from "formik"
import * as Yup from "yup"
import { registrarPaciente } from "@/lib/api"
import { useToast } from "@/hooks/use-toast"
import type { PacienteRegistroDTO } from "@/lib/types"

const validationSchema = Yup.object({
  cuil: Yup.string()
    .required("CUIL es obligatorio")
    .matches(/^\d{2}-\d{8}-\d$/, "Formato de CUIL inválido (XX-XXXXXXXX-X)"),
  nombre: Yup.string().required("Nombre es obligatorio").min(2, "Nombre debe tener al menos 2 caracteres"),
  apellido: Yup.string().required("Apellido es obligatorio").min(2, "Apellido debe tener al menos 2 caracteres"),
  calle: Yup.string().required("Calle es obligatorio").min(2,"Calle debe tener al menos 2 caracteres"),
  numero: Yup.string().required("Numero es obligatorio").matches(/^\d+$/, "Solo se permiten números (sin letras ni símbolos)"),
  localidad: Yup.string().required("Localidad es obligatorio"),
  provincia: Yup.string(),
  obraSocial: Yup.string(),
  numAfiliado: Yup.string(),
})

export default function PacienteForm() {
  const { toast } = useToast()

  const formik = useFormik<PacienteRegistroDTO>({
    initialValues: {
      cuil: "",
      nombre: "",
      apellido: "",
      calle: "",
      numero: "",
      localidad: "",
      provincia: "",
      obraSocial: "",
      numAfiliado: "",
    },
    validationSchema,
    onSubmit: async (values, { setSubmitting, resetForm }) => {
      try {
        await registrarPaciente(values)
        toast({
          title: "Éxito",
          description: "Paciente registrado exitosamente",
          variant: "default",
        })
        resetForm()
      } catch (error) {
        toast({
          title: "Error",
          description: error instanceof Error ? error.message : "Error al registrar paciente",
          variant: "destructive",
        })
      } finally {
        setSubmitting(false)
      }
    },
  })

  return (
    <Card>
      <CardHeader>
        <CardTitle>Información Personal</CardTitle>
        <CardDescription>Datos básicos del paciente</CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={formik.handleSubmit} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">CUIL *</label>
              <Input
                type="text"
                name="cuil"
                value={formik.values.cuil}
                onChange={(e) => formik.setFieldValue("cuil", e.target.value)}
                placeholder="20-12345678-9"
                className={formik.touched.cuil && formik.errors.cuil ? "border-destructive" : ""}
              />
              {formik.touched.cuil && formik.errors.cuil && (
                <p className="text-destructive text-sm mt-1">{formik.errors.cuil}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Nombre *</label>
              <Input
                type="text"
                {...formik.getFieldProps("nombre")}
                placeholder="Juan"
                className={formik.touched.nombre && formik.errors.nombre ? "border-destructive" : ""}
              />
              {formik.touched.nombre && formik.errors.nombre && (
                <p className="text-destructive text-sm mt-1">{formik.errors.nombre}</p>
              )}
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Apellido *</label>
              <Input
                type="text"
                {...formik.getFieldProps("apellido")}
                placeholder="Pérez"
                className={formik.touched.apellido && formik.errors.apellido ? "border-destructive" : ""}
              />
              {formik.touched.apellido && formik.errors.apellido && (
                <p className="text-destructive text-sm mt-1">{formik.errors.apellido}</p>
              )}
            </div>
          </div>

          <CardTitle className="text-base">Domicilio</CardTitle>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Calle</label>
              <Input type="text" {...formik.getFieldProps("calle")} placeholder="Av. Principal"
                     className={formik.touched.calle && formik.errors.calle ? "border-destructive" : ""}
              />
              {formik.touched.calle && formik.errors.calle && (
                  <p className="text-destructive text-sm mt-1">{formik.errors.calle}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Número</label>
              <Input type="text" {...formik.getFieldProps("numero")} placeholder="123" className={formik.touched.numero && formik.errors.numero ? "border-destructive" : ""}
              />
              {formik.touched.numero && formik.errors.numero && (
                  <p className="text-destructive text-sm mt-1">{formik.errors.numero}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Localidad</label>
              <Input type="text" {...formik.getFieldProps("localidad")} placeholder="Buenos Aires" className={formik.touched.localidad && formik.errors.localidad ? "border-destructive" : ""}
              />
              {formik.touched.localidad && formik.errors.localidad && (
                  <p className="text-destructive text-sm mt-1">{formik.errors.localidad}</p>
              )}
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-foreground mb-2">Provincia</label>
            <Input type="text" {...formik.getFieldProps("provincia")} placeholder="Buenos Aires" />
          </div>

          <CardTitle className="text-base">Obra Social (Opcional)</CardTitle>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Obra Social</label>
              <Input type="text" {...formik.getFieldProps("obraSocial")} placeholder="OSDE, PAMI, etc." />
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Número de Afiliado</label>
              <Input type="text" {...formik.getFieldProps("numAfiliado")} placeholder="123456789" />
            </div>
          </div>

          <div className="flex gap-4 pt-6">
            <Button type="submit" disabled={formik.isSubmitting} className="bg-primary hover:bg-primary/90">
              {formik.isSubmitting ? "Registrando..." : "Registrar Paciente"}
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
