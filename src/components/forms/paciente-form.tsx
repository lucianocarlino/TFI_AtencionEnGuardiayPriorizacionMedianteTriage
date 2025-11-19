'use client'

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'

export default function PacienteForm() {
  const [formData, setFormData] = useState({
    cuil: '',
    nombre: '',
    apellido: '',
    calle: '',
    numero: '',
    localidad: '',
    provincia: '',
    numAfiliado: '',
    obraSocial: ''
  })

  const [errors, setErrors] = useState<Record<string, string>>({})
  const [success, setSuccess] = useState(false)
  const [loading, setLoading] = useState(false)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
    // Clear error for this field
    if (errors[name]) {
      setErrors(prev => { const { [name]: _, ...rest } = prev; return rest })
    }
  }

  const validateForm = () => {
    const newErrors: Record<string, string> = {}

    if (!formData.cuil.trim()) newErrors.cuil = 'CUIL es obligatorio'
    if (!formData.nombre.trim()) newErrors.nombre = 'Nombre es obligatorio'
    if (!formData.apellido.trim()) newErrors.apellido = 'Apellido es obligatorio'

    return newErrors
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    const newErrors = validateForm()
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return
    }

    setLoading(true)
    try {
      // Aquí iría la llamada al backend con los datos del paciente
      console.log('Paciente registrado:', formData)
      setSuccess(true)
      setFormData({
        cuil: '',
        nombre: '',
        apellido: '',
        calle: '',
        numero: '',
        localidad: '',
        provincia: '',
        numAfiliado: '',
        obraSocial: ''
      })
      setTimeout(() => setSuccess(false), 5000)
    } catch (error) {
      console.error('Error:', error)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      {success && (
        <div className="bg-green-50 border border-green-200 rounded-lg p-4">
          <p className="text-green-800 font-medium">Paciente registrado exitosamente</p>
        </div>
      )}

      <Card>
        <CardHeader>
          <CardTitle>Información Personal</CardTitle>
          <CardDescription>Datos básicos del paciente</CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-foreground mb-2">CUIL *</label>
                <Input
                  type="text"
                  name="cuil"
                  value={formData.cuil}
                  onChange={handleChange}
                  placeholder="20123456789"
                  className={errors.cuil ? 'border-destructive' : ''}
                />
                {errors.cuil && <p className="text-destructive text-sm mt-1">{errors.cuil}</p>}
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-2">Nombre *</label>
                <Input
                  type="text"
                  name="nombre"
                  value={formData.nombre}
                  onChange={handleChange}
                  placeholder="Juan"
                  className={errors.nombre ? 'border-destructive' : ''}
                />
                {errors.nombre && <p className="text-destructive text-sm mt-1">{errors.nombre}</p>}
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-foreground mb-2">Apellido *</label>
                <Input
                  type="text"
                  name="apellido"
                  value={formData.apellido}
                  onChange={handleChange}
                  placeholder="Pérez"
                  className={errors.apellido ? 'border-destructive' : ''}
                />
                {errors.apellido && <p className="text-destructive text-sm mt-1">{errors.apellido}</p>}
              </div>

              <div></div>
            </div>

            <CardTitle className="text-base">Domicilio</CardTitle>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label className="block text-sm font-medium text-foreground mb-2">Calle</label>
                <Input
                  type="text"
                  name="calle"
                  value={formData.calle}
                  onChange={handleChange}
                  placeholder="Av. Principal"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-2">Número</label>
                <Input
                  type="text"
                  name="numero"
                  value={formData.numero}
                  onChange={handleChange}
                  placeholder="123"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-2">Localidad</label>
                <Input
                  type="text"
                  name="localidad"
                  value={formData.localidad}
                  onChange={handleChange}
                  placeholder="Buenos Aires"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Provincia</label>
              <Input
                type="text"
                name="provincia"
                value={formData.provincia}
                onChange={handleChange}
                placeholder="Buenos Aires"
              />
            </div>

            <CardTitle className="text-base">Obra Social (Opcional)</CardTitle>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-foreground mb-2">Obra Social</label>
                <Input
                  type="text"
                  name="obraSocial"
                  value={formData.obraSocial}
                  onChange={handleChange}
                  placeholder="OSDE, IOMA, etc."
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-2">Número de Afiliado</label>
                <Input
                  type="text"
                  name="numAfiliado"
                  value={formData.numAfiliado}
                  onChange={handleChange}
                  placeholder="123456789"
                />
              </div>
            </div>

            <div className="flex gap-4 pt-6">
              <Button type="submit" disabled={loading} className="bg-primary hover:bg-primary/90">
                {loading ? 'Registrando...' : 'Registrar Paciente'}
              </Button>
              <Button type="button" variant="outline" onClick={() => window.history.back()}>
                Cancelar
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}
