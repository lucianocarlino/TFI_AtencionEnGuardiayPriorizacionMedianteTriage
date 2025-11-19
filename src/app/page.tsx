'use client'

import { useState } from 'react'
import Link from 'next/link'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import Sidebar from '@/components/sidebar'

export default function Home() {
  const [showAlert, setShowAlert] = useState(false)

  return (
    <div className="flex min-h-screen bg-background">
      <Sidebar />
      
      <main className="flex-1">
        <div className="border-b border-border bg-card">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
            <div className="flex items-center justify-between">
              <div>
                <h1 className="text-3xl font-bold text-foreground">Sistema de Urgencias Médicas</h1>
                <p className="mt-2 text-muted-foreground">Gestión integral de pacientes y urgencias</p>
              </div>
            </div>
          </div>
        </div>

        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
            <Link href="/pacientes/registrar" className="no-underline">
              <Card className="cursor-pointer hover:shadow-lg transition-shadow h-full">
                <CardHeader>
                  <div className="flex items-center gap-3">
                    <div className="p-3 bg-primary/10 rounded-lg">
                      <svg className="w-6 h-6 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                      </svg>
                    </div>
                    <div>
                      <CardTitle className="text-lg">Registrar Paciente</CardTitle>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <CardDescription>Registra un nuevo paciente en el sistema</CardDescription>
                </CardContent>
              </Card>
            </Link>

            <Link href="/urgencias/registrar" className="no-underline">
              <Card className="cursor-pointer hover:shadow-lg transition-shadow h-full">
                <CardHeader>
                  <div className="flex items-center gap-3">
                    <div className="p-3 bg-accent/10 rounded-lg">
                      <svg className="w-6 h-6 text-accent" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
                      </svg>
                    </div>
                    <div>
                      <CardTitle className="text-lg">Registrar Urgencia</CardTitle>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <CardDescription>Registra una nueva urgencia médica</CardDescription>
                </CardContent>
              </Card>
            </Link>

            <Link href="/urgencias/cola" className="no-underline">
              <Card className="cursor-pointer hover:shadow-lg transition-shadow h-full">
                <CardHeader>
                  <div className="flex items-center gap-3">
                    <div className="p-3 bg-primary/10 rounded-lg">
                      <svg className="w-6 h-6 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                      </svg>
                    </div>
                    <div>
                      <CardTitle className="text-lg">Cola de Urgencias</CardTitle>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <CardDescription>Visualiza la cola de urgencias ordenada por prioridad</CardDescription>
                </CardContent>
              </Card>
            </Link>
          </div>

          <div className="bg-gradient-to-r from-primary/5 to-accent/5 border border-border rounded-lg p-6">
            <h2 className="text-xl font-semibold text-foreground mb-2">Bienvenido al Sistema</h2>
            <p className="text-muted-foreground">
              Este sistema permite gestionar de manera eficiente el registro de pacientes y la administración de urgencias médicas con priorización automática.
            </p>
          </div>
        </div>
      </main>
    </div>
  )
}
