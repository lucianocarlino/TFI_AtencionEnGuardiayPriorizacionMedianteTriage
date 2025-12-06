"use client"

import type React from "react"
import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"
import type { AuthUser } from "@/lib/types"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { Button } from "@/components/ui/button"
import { hasAllowedRole } from "@/lib/role-utils"

function AuthGuard({
  children,
  allowedRoles,
}: {
  children: React.ReactNode
  allowedRoles?: string[]
}) {
  const router = useRouter()
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [isLoading, setIsLoading] = useState(true)
  const [hasPermission, setHasPermission] = useState(false)
  const [user, setUser] = useState<AuthUser | null>(null)

  useEffect(() => {
    const checkAuth = () => {
      const userStr = localStorage.getItem("user")

      if (!userStr) {
        router.replace("/login")
        return
      }

      try {
        const userData: AuthUser = JSON.parse(userStr)
        if (userData.email && userData.autoridad) {
          setUser(userData)
          setIsAuthenticated(true)

          if (allowedRoles && allowedRoles.length > 0) {
            const hasRole = hasAllowedRole(userData.autoridad, allowedRoles)
            setHasPermission(hasRole)
          } else {
            setHasPermission(true)
          }
        } else {
          router.replace("/login")
        }
      } catch {
        router.replace("/login")
      } finally {
        setIsLoading(false)
      }
    }

    checkAuth()
  }, [router, allowedRoles])

  if (isLoading || !isAuthenticated) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    )
  }

  if (!hasPermission) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background p-4">
        <div className="max-w-md w-full">
          <Alert variant="destructive">
            <AlertDescription className="space-y-4">
              <div>
                <p className="font-semibold">Acceso Denegado</p>
                <p className="text-sm mt-2">No tienes permisos para acceder a esta página.</p>
                {allowedRoles && allowedRoles.length > 0 && (
                  <p className="text-sm mt-2">Esta página está restringida para: {allowedRoles.join(", ")}</p>
                )}
                {user && (
                  <p className="text-sm mt-2">
                    Tu rol actual es: <strong>{user.autoridad}</strong>
                  </p>
                )}
              </div>
              <Button onClick={() => router.push("/")} variant="outline" className="w-full">
                Volver al Inicio
              </Button>
            </AlertDescription>
          </Alert>
        </div>
      </div>
    )
  }

  return <>{children}</>
}

export default AuthGuard
export { AuthGuard }
