"use client"

import type React from "react"

import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"
import type { AuthUser } from "@/lib/types"

export function AuthGuard({ children }: { children: React.ReactNode }) {
  const router = useRouter()
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const userStr = localStorage.getItem("user")

    if (!userStr) {
      router.push("/login")
    } else {
      try {
        const user: AuthUser = JSON.parse(userStr)
        if (user.email && user.autoridad) {
          setIsAuthenticated(true)
        } else {
          router.push("/login")
        }
      } catch {
        router.push("/login")
      }
    }

    setIsLoading(false)
  }, [router])

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    )
  }

  return isAuthenticated ? <>{children}</> : null
}
