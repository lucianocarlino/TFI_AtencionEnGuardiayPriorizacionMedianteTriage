"use client"

import Sidebar from "@/components/sidebar"
import PacienteForm from "@/components/forms/paciente-form"
import AuthGuard from "@/components/auth-guard"

export default function RegistrarPaciente() {
  return (
    <AuthGuard>
      <div className="flex min-h-screen bg-background">
        <Sidebar />

        <main className="flex-1">
          <div className="border-b border-border bg-card">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
              <h1 className="text-3xl font-bold text-foreground">Registrar Paciente</h1>
              <p className="mt-2 text-muted-foreground">Completa el formulario para registrar un nuevo paciente</p>
            </div>
          </div>

          <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <PacienteForm />
          </div>
        </main>
      </div>
    </AuthGuard>
  )
}
