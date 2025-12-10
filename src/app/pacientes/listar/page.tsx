"use client"

import { useEffect, useState } from "react"
import AuthGuard from "@/components/auth-guard"
import Sidebar from "@/components/sidebar"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
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
        console.error()
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
                {pacientes.map((paciente) => {
                  const tieneObraSocial = paciente.afiliado && paciente.afiliado.obraSocial

                  return (
                    <Card key={paciente.cuil}>
                      <CardHeader>
                        <div className="flex items-start justify-between">
                          <div className="flex-1">
                            <CardTitle className="text-xl">{`${paciente.apellido} ${paciente.nombre}`}</CardTitle>
                            <CardDescription>CUIL: {paciente.cuil}</CardDescription>
                          </div>
                          {tieneObraSocial ? (
                            <Badge className="bg-success/10 text-success border-success/20">Con Obra Social</Badge>
                          ) : (
                            <Badge variant="outline" className="border-muted-foreground/20 text-muted-foreground">
                              Sin Obra Social
                            </Badge>
                          )}
                        </div>
                      </CardHeader>
                      <CardContent className="space-y-3">
                        {tieneObraSocial && (
                          <div className="bg-success/5 border border-success/20 rounded-lg p-4">
                            <p className="text-sm font-semibold text-foreground mb-2">Información de Obra Social</p>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
                              <div>
                                <span className="text-xs text-muted-foreground">Obra Social:</span>
                                <p className="font-medium text-foreground">{paciente.afiliado?.obraSocial.nombre}</p>
                              </div>
                              <div>
                                <span className="text-xs text-muted-foreground">Número de Afiliado:</span>
                                <p className="font-medium text-foreground">{paciente.afiliado?.numAfiliado}</p>
                              </div>
                            </div>
                          </div>
                        )}
                        {paciente.direccion && (
                          <div className="flex items-start gap-2">
                            <svg
                              className="w-4 h-4 mt-1 text-muted-foreground shrink-0"
                              fill="none"
                              stroke="currentColor"
                              viewBox="0 0 24 24"
                            >
                              <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth={2}
                                d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
                              />
                              <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth={2}
                                d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
                              />
                            </svg>
                            <div>
                              <span className="text-xs text-muted-foreground">Domicilio:</span>
                              <p className="text-sm text-foreground">
                                {paciente.direccion.calle} {paciente.direccion.numero}, {paciente.direccion.localidad}
                              </p>
                            </div>
                          </div>
                        )}
                      </CardContent>
                    </Card>
                  )
                })}
              </div>
            )}
          </div>
        </main>
      </div>
    </AuthGuard>
  )
}
