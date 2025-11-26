'use client'

import Link from 'next/link'
import { usePathname } from 'next/navigation'
import { cn } from '@/lib/utils'

export default function Sidebar() {
  const pathname = usePathname()

  const isActive = (path: string) => {
    return pathname.startsWith(path)
  }

  return (
    <aside className="w-64 bg-sidebar text-sidebar-foreground border-r border-sidebar-border">
      <div className="p-6 border-b border-sidebar-border">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-sidebar-primary rounded-lg flex items-center justify-center">
            <svg className="w-6 h-6 text-sidebar-primary-foreground" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
          </div>
          <div>
            <h1 className="font-bold text-lg">MedUrgencias</h1>
            <p className="text-xs text-sidebar-foreground/70">Sistema de Urgencias</p>
          </div>
        </div>
      </div>
        <div className="pt-2 pb-1">
            <p className="px-4 text-xs font-semibold text-sidebar-foreground/60 uppercase tracking-wide">Enfermera: Jorgelina Ponce</p>
        </div>

      <nav className="p-4 space-y-2">
        <Link href="/" className={cn(
          'flex items-center gap-3 px-4 py-3 rounded-lg transition-colors',
          isActive('/') && !isActive('/pacientes') && !isActive('/urgencias')
            ? 'bg-sidebar-primary text-sidebar-primary-foreground'
            : 'text-sidebar-foreground hover:bg-sidebar-accent/20'
        )}>
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-3m0 0l7-4 7 4M5 9v10a1 1 0 001 1h12a1 1 0 001-1V9m-9 9l-7-4" />
          </svg>
          <span className="font-medium">Inicio</span>
        </Link>

        <div className="pt-2 pb-1">
          <p className="px-4 text-xs font-semibold text-sidebar-foreground/60 uppercase tracking-wide">Pacientes</p>
        </div>

        <Link href="/pacientes/registrar" className={cn(
          'flex items-center gap-3 px-4 py-3 rounded-lg transition-colors',
          isActive('/pacientes/registrar')
            ? 'bg-sidebar-primary text-sidebar-primary-foreground'
            : 'text-sidebar-foreground hover:bg-sidebar-accent/20'
        )}>
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
          </svg>
          <span className="font-medium">Registrar Paciente</span>
        </Link>

        <div className="pt-2 pb-1">
          <p className="px-4 text-xs font-semibold text-sidebar-foreground/60 uppercase tracking-wide">Urgencias</p>
        </div>

        <Link href="/urgencias/registrar" className={cn(
          'flex items-center gap-3 px-4 py-3 rounded-lg transition-colors',
          isActive('/urgencias/registrar')
            ? 'bg-sidebar-primary text-sidebar-primary-foreground'
            : 'text-sidebar-foreground hover:bg-sidebar-accent/20'
        )}>
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span className="font-medium">Registrar Urgencia</span>
        </Link>

        <Link href="/urgencias/cola" className={cn(
          'flex items-center gap-3 px-4 py-3 rounded-lg transition-colors',
          isActive('/urgencias/cola')
            ? 'bg-sidebar-primary text-sidebar-primary-foreground'
            : 'text-sidebar-foreground hover:bg-sidebar-accent/20'
        )}>
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 1112 2.944a11.954 11.954 0 018.618 3.04A11.955 11.955 0 1112 23.056" />
          </svg>
          <span className="font-medium">Cola de Urgencias</span>
        </Link>
      </nav>
    </aside>
  )
}
