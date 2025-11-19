'use client'

import Sidebar from '@/components/sidebar'
import UrgenciaForm from '@/components/forms/urgencia-form'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'

export default function RegistrarUrgencia() {
  return (
    <div className="flex min-h-screen bg-background">
      <Sidebar />
      
      <main className="flex-1">
        <div className="border-b border-border bg-card">
          <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
            <h1 className="text-3xl font-bold text-foreground">Registrar Urgencia</h1>
            <p className="mt-2 text-muted-foreground">Registra una nueva urgencia con signos vitales y nivel de prioridad</p>
          </div>
        </div>

        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <UrgenciaForm />
        </div>
      </main>
    </div>
  )
}
