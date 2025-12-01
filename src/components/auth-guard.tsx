"use client"

import type React from "react"
import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"
import type { AuthUser } from "@/lib/types"

function AuthGuard({ children }: { children: React.ReactNode }) {
  const router = useRouter()
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const checkAuth = () => {
      const userStr = localStorage.getItem("user")

      if (!userStr) {
        router.replace("/login")
        return
      }

      try {
        const user: AuthUser = JSON.parse(userStr)
        if (user.email && user.autoridad) {
          setIsAuthenticated(true)
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
  }, [router])

  if (isLoading || !isAuthenticated) {
    return (
        <div className="min-h-screen flex items-center justify-center bg-background">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        </div>
    )
  }

  return <>{children}</>
}

export default AuthGuard
export { AuthGuard }
