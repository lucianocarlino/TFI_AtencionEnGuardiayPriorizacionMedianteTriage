"use client"

import { useEffect, useState } from "react"
import AuthGuard from "@/components/auth-guard"
import Sidebar from "@/components/sidebar"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { listarPacientes } from "@/lib/api"
import type { PacienteResponse } from "@/lib/types"
import { useToast } from "@/hooks/use-toast"

export default function ListarPacientes() {
  const [pacientes, setPacientes] = useState<PacienteResponse[]>([])
  const [loading, setLoading] = useState(true)
  const { toast } = useToast()

  useEffect(() => {
    const cargarPacientes = async () => {
      try {
        const data = await listarPacientes()
        setPacientes(data)
      } catch (error) {
        toast({
          title: "Error",
          description: "No se pudieron cargar los pacientes",
          variant: "destructive",
        })
      } finally {
        setLoading(false)
      }
    }

    cargarPacientes()
  }, [toast])

  return (
    <AuthGuard>
      <div className="flex min-h-screen bg-background">
        <Sidebar />

        <main className="flex-1">
          <div className="border-b border-border bg-card">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
              <h1 className="text-3xl font-bold text-foreground">Pacientes Registrados</h1>
              <p className="mt-2 text-muted-foreground">Lista completa de pacientes en el sistema</p>
            </div>
          </div>

          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            {loading ? (
              <Card>
                <CardContent className="p-6">
                  <p className="text-center text-muted-foreground">Cargando pacientes...</p>
                </CardContent>
              </Card>
            ) : pacientes.length === 0 ? (
              <Card>
                <CardContent className="p-6">
                  <p className="text-center text-muted-foreground">No hay pacientes registrados</p>
                </CardContent>
              </Card>
            ) : (
              <div className="grid gap-4">
                {pacientes.map((paciente) => (
                  <Card key={paciente.cuil}>
                    <CardHeader>
                      <CardTitle className="text-xl">{`${paciente.apellido} ${paciente.nombre}`}</CardTitle>
                      <CardDescription>CUIL: {paciente.cuil}</CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-2">
                      {paciente.afiliado?.obraSocial && (
                        <div className="flex gap-2">
                          <span className="font-medium">Obra Social:</span>
                          <span className="text-muted-foreground">
                            {paciente.afiliado.obraSocial.nombre} - Afiliado N° {paciente.afiliado.numAfiliado}
                          </span>
                        </div>
                      )}
                      {paciente.direccion && (
                        <div className="flex gap-2">
                          <span className="font-medium">Domicilio:</span>
                          <span className="text-muted-foreground">
                            {paciente.direccion.calle} {paciente.direccion.numero}, {paciente.direccion.localidad}
                          </span>
                        </div>
                      )}
                    </CardContent>
                  </Card>
                ))}
              </div>
            )}
          </div>
        </main>
      </div>
    </AuthGuard>
  )
}
