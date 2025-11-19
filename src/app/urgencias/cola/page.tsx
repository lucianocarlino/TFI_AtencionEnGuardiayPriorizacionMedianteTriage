'use client'

import { useState, useEffect } from 'react'
import Sidebar from '@/components/sidebar'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'

interface Ingreso {
  id: string
  cuilPaciente: string
  nombrePaciente: string
  nombreEnfermera: string
  nivelEmergencia: 'CRITICO' | 'URGENTE' | 'SEMI_URGENTE' | 'NO_URGENTE'
  temperatura: number
  frecCardiaca: number
  frecRespiratoria: number
  sistolica: number
  diastolica: number
  informe: string
  fechaIngreso: string
  estado: 'PENDIENTE' | 'EN_ATENCION' | 'RESUELTO'
}

export default function ColaUrgencias() {
  const [ingresos, setIngresos] = useState<Ingreso[]>([])
  const [filtroNivel, setFiltroNivel] = useState<string>('todos')

  useEffect(() => {
    // Aquí iría la llamada al backend para obtener la lista de urgencias
    // Por ahora, usamos datos de demostración
    const datosDemo: Ingreso[] = [
      {
        id: '1',
        cuilPaciente: '20123456789',
        nombrePaciente: 'Juan Pérez',
        nombreEnfermera: 'María González',
        nivelEmergencia: 'CRITICO',
        temperatura: 39.5,
        frecCardiaca: 120,
        frecRespiratoria: 24,
        sistolica: 160,
        diastolica: 100,
        informe: 'Dolor torácico intenso con dificultad respiratoria',
        fechaIngreso: new Date().toISOString(),
        estado: 'PENDIENTE'
      },
      {
        id: '2',
        cuilPaciente: '20987654321',
        nombrePaciente: 'Ana Martínez',
        nombreEnfermera: 'Carlos López',
        nivelEmergencia: 'URGENTE',
        temperatura: 38.2,
        frecCardiaca: 95,
        frecRespiratoria: 18,
        sistolica: 135,
        diastolica: 85,
        informe: 'Fractura de brazo con hemorragia moderada',
        fechaIngreso: new Date(Date.now() - 600000).toISOString(),
        estado: 'PENDIENTE'
      }
    ]
    setIngresos(datosDemo)
  }, [])

  const getNivelInfo = (nivel: string) => {
    const info: Record<string, { label: string; color: string; bg: string; borderColor: string }> = {
      CRITICO: { label: 'CRÍTICO', color: 'text-destructive', bg: 'bg-destructive/10', borderColor: 'border-destructive/20' },
      URGENTE: { label: 'URGENTE', color: 'text-accent', bg: 'bg-accent/10', borderColor: 'border-accent/20' },
      SEMI_URGENTE: { label: 'SEMI-URGENTE', color: 'text-warning', bg: 'bg-warning/10', borderColor: 'border-warning/20' },
      NO_URGENTE: { label: 'NO URGENTE', color: 'text-primary', bg: 'bg-primary/10', borderColor: 'border-primary/20' }
    }
    return info[nivel]
  }

  const ingresosFiltrados = filtroNivel === 'todos'
    ? ingresos
    : ingresos.filter(i => i.nivelEmergencia === filtroNivel)

  // Ordena por nivel de emergencia (crítico primero)
  const nivelesOrden: Record<string, number> = {
    CRITICO: 0,
    URGENTE: 1,
    SEMI_URGENTE: 2,
    NO_URGENTE: 3
  }

  const ingresosOrdenados = [...ingresosFiltrados].sort(
    (a, b) => nivelesOrden[a.nivelEmergencia] - nivelesOrden[b.nivelEmergencia]
  )

  return (
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
              onClick={() => setFiltroNivel('todos')}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                filtroNivel === 'todos'
                  ? 'bg-primary text-primary-foreground'
                  : 'bg-muted text-muted-foreground hover:bg-muted/80'
              }`}
            >
              Todos ({ingresos.length})
            </button>
            <button
              onClick={() => setFiltroNivel('CRITICO')}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                filtroNivel === 'CRITICO'
                  ? 'bg-destructive text-destructive-foreground'
                  : 'bg-destructive/10 text-destructive hover:bg-destructive/20'
              }`}
            >
              Crítico ({ingresos.filter(i => i.nivelEmergencia === 'CRITICO').length})
            </button>
            <button
              onClick={() => setFiltroNivel('URGENTE')}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                filtroNivel === 'URGENTE'
                  ? 'bg-accent text-accent-foreground'
                  : 'bg-accent/10 text-accent hover:bg-accent/20'
              }`}
            >
              Urgente ({ingresos.filter(i => i.nivelEmergencia === 'URGENTE').length})
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
                  <Card key={ingreso.id} className={`border-2 ${nivelInfo.borderColor}`}>
                    <CardContent className="pt-6">
                      <div className="flex flex-col md:flex-row md:items-start md:justify-between gap-4">
                        <div className="flex-1">
                          <div className="flex items-center gap-3 mb-3">
                            <div className={`px-3 py-1 rounded-full text-sm font-bold ${nivelInfo.bg} ${nivelInfo.color}`}>
                              #{index + 1} - {nivelInfo.label}
                            </div>
                          </div>
                          <h3 className="text-lg font-bold text-foreground">{ingreso.nombrePaciente}</h3>
                          <p className="text-sm text-muted-foreground">CUIL: {ingreso.cuilPaciente}</p>
                          <p className="text-sm text-muted-foreground mt-2">Enfermera: {ingreso.nombreEnfermera}</p>
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
                                <p className="font-bold text-foreground">{ingreso.frecCardiaca} lpm</p>
                              </div>
                              <div>
                                <p className="text-muted-foreground">Frecuencia Respiratoria</p>
                                <p className="font-bold text-foreground">{ingreso.frecRespiratoria} rpm</p>
                              </div>
                              <div>
                                <p className="text-muted-foreground">Tensión Arterial</p>
                                <p className="font-bold text-foreground">{ingreso.sistolica}/{ingreso.diastolica}</p>
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
  )
}
