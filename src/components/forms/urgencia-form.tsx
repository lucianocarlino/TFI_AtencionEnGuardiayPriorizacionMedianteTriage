'use client'

import {useState} from 'react'
import {Button} from '@/components/ui/button'
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from '@/components/ui/card'
import {Input} from '@/components/ui/input'
import {NivelEmergencia} from "@/lib/types";
import {registrarIngreso} from "@/lib/api";



export default function UrgenciaForm() {
  const [formData, setFormData] = useState({
    cuilPaciente: '',
    nombreEnfermera: '',
    informe: '',
    nivelEmergencia: NivelEmergencia.SIN_URGENCIA,
    temperatura: '',
    frecCardiaca: '',
    frecRespiratoria: '',
    frecuenciaSistolica: '',
    frecuenciaDiastolica: ''
  })

  const [errors, setErrors] = useState<Record<string, string>>({})
  const [success, setSuccess] = useState(false)
  const [loading, setLoading] = useState(false)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
    if (errors[name]) {
      setErrors(prev => { const { [name]: _, ...rest } = prev; return rest })
    }
  }

  const validateForm = () => {
    const newErrors: Record<string, string> = {}

    if (!formData.cuilPaciente.trim()) newErrors.cuilPaciente = 'CUIL es obligatorio'
    if (!formData.nombreEnfermera.trim()) newErrors.nombreEnfermera = 'Nombre de enfermera es obligatorio'
    if (!formData.informe.trim()) newErrors.informe = 'Informe es obligatorio'
    if (!formData.temperatura) newErrors.temperatura = 'Temperatura es obligatoria'
    if (!formData.frecCardiaca) newErrors.frecCardiaca = 'Frecuencia cardíaca es obligatoria'
    if (!formData.frecRespiratoria) newErrors.frecRespiratoria = 'Frecuencia respiratoria es obligatoria'
    if (!formData.frecuenciaSistolica) newErrors.frecuenciaSistolica = 'Presión sistólica es obligatoria'
    if (!formData.frecuenciaDiastolica) newErrors.frecuenciaDiastolica = 'Presión diastólica es obligatoria'

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
        const ingresoDTO = {
            cuilPaciente: formData.cuilPaciente,
            informe: formData.informe,
            nivelEmergencia: formData.nivelEmergencia,
            temperatura: parseFloat(formData.temperatura),
            frecCardiaca: parseFloat(formData.frecCardiaca),
            frecRespiratoria: parseFloat(formData.frecRespiratoria),
            frecuenciaSistolica: parseFloat(formData.frecuenciaSistolica),
            frecuenciaDiastolica: parseFloat(formData.frecuenciaDiastolica),
            // AJUSTE ENFERMERA: Tu DTO espera un objeto o null.
            // Por ahora mandamos null o un objeto mock. Lo ideal es tener la enfermera logueada real.
            enfermera: null
        }
        const mensajeBackend = await registrarIngreso(ingresoDTO)
      console.log( mensajeBackend)
      setSuccess(true)
      setFormData({
        cuilPaciente: '',
        nombreEnfermera: '',
        informe: '',
        nivelEmergencia: NivelEmergencia.SIN_URGENCIA,
        temperatura: '',
        frecCardiaca: '',
        frecRespiratoria: '',
        frecuenciaSistolica: '',
        frecuenciaDiastolica: ''
      })
      setTimeout(() => setSuccess(false), 5000) //para que sirve?
    } catch (error) {
      console.error('Error:', error)
    } finally {
      setLoading(false)
    }
  }

  const getNivelColor = (nivel: NivelEmergencia) => {
    switch (nivel) {
      case NivelEmergencia.CRITICA: return 'bg-destructive/10 border-destructive/20 text-destructive'
      case NivelEmergencia.EMERGENCIA: return 'bg-accent/10 border-accent/20 text-accent'
      case NivelEmergencia.URGENCIA: return 'bg-warning/10 border-warning/20 text-warning'
      case NivelEmergencia.URGENCIA_MENOR: return 'bg-primary/10 border-primary/20 text-primary'
      case NivelEmergencia.SIN_URGENCIA: return 'bg-primary/10 border-primary/20 text-primary' //CambiarCOLOR
      default: return ''
    }
  }

  return (
    <div className="space-y-6">
      {success && (
        <div className="bg-green-50 border border-green-200 rounded-lg p-4">
          <p className="text-green-800 font-medium">Urgencia registrada exitosamente</p>
        </div>
      )}

      <Card>
        <CardHeader>
          <CardTitle>Información de Urgencia</CardTitle>
          <CardDescription>Completa los datos del paciente y signos vitales</CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-foreground mb-2">CUIL Paciente *</label>
                <Input
                  type="text"
                  name="cuilPaciente"
                  value={formData.cuilPaciente}
                  onChange={handleChange}
                  placeholder=""
                  className={errors.cuilPaciente ? 'border-destructive' : ''}
                />
                {errors.cuilPaciente && <p className="text-destructive text-sm mt-1">{errors.cuilPaciente}</p>}
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-2">Nombre Enfermera *</label>
                <Input
                  type="text"
                  name="nombreEnfermera"
                  value={formData.nombreEnfermera}
                  onChange={handleChange}
                  placeholder=""
                  className={errors.nombreEnfermera ? 'border-destructive' : ''}
                />
                {errors.nombreEnfermera && <p className="text-destructive text-sm mt-1">{errors.nombreEnfermera}</p>}
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Nivel de Emergencia *</label>
              <div className={`p-4 rounded-lg border-2 ${getNivelColor(formData.nivelEmergencia as NivelEmergencia)}`}>
                <select
                  name="nivelEmergencia"
                  value={formData.nivelEmergencia}
                  onChange={handleChange}
                  className="w-full bg-transparent font-semibold outline-none"
                >
                  <option value="SIN_URGENCIA">SIN URGENCIA- Consulta ambulatoria</option>
                  <option value="URGENCIA_MENOR">URGENCIA MENOR- </option>
                  <option value="URGENCIA">URGENCIA - </option>
                  <option value="EMERGENCIA">EMERGENCIA - </option>
                  <option value="CRITICA">CRITICA- Riesgo de vida inmediato</option>

                </select>
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Informe Médico *</label>
              <textarea
                name="informe"
                value={formData.informe}
                onChange={handleChange}
                placeholder="Descripción de síntomas y observaciones clínicas..."
                rows={4}
                className="w-full px-3 py-2 border border-input rounded-lg bg-card text-foreground placeholder-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
              />
              {errors.informe && <p className="text-destructive text-sm mt-1">{errors.informe}</p>}
            </div>

            <div>
              <h3 className="text-base font-semibold text-foreground mb-4">Signos Vitales</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">Temperatura (°C) *</label>
                  <Input
                    type="number"
                    name="temperatura"
                    value={formData.temperatura}
                    onChange={handleChange}
                    placeholder=""
                    step="0.1"
                    className={errors.temperatura ? 'border-destructive' : ''}
                  />
                  {errors.temperatura && <p className="text-destructive text-sm mt-1">{errors.temperatura}</p>}
                </div>

                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">Frecuencia Cardíaca (lpm) *</label>
                  <Input
                    type="number"
                    name="frecCardiaca"
                    value={formData.frecCardiaca}
                    onChange={handleChange}
                    placeholder=""
                    className={errors.frecCardiaca ? 'border-destructive' : ''}
                  />
                  {errors.frecCardiaca && <p className="text-destructive text-sm mt-1">{errors.frecCardiaca}</p>}
                </div>

                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">Frecuencia Respiratoria (rpm) *</label>
                  <Input
                    type="number"
                    name="frecRespiratoria"
                    value={formData.frecRespiratoria}
                    onChange={handleChange}
                    placeholder=""
                    className={errors.frecRespiratoria ? 'border-destructive' : ''}
                  />
                  {errors.frecRespiratoria && <p className="text-destructive text-sm mt-1">{errors.frecRespiratoria}</p>}
                </div>

                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">Presión Sistólica (mmHg) *</label>
                  <Input
                    type="number"
                    name="frecuenciaSistolica"
                    value={formData.frecuenciaSistolica}
                    onChange={handleChange}
                    placeholder=""
                    className={errors.frecuenciaSistolica ? 'border-destructive' : ''}
                  />
                  {errors.frecuenciaSistolica && <p className="text-destructive text-sm mt-1">{errors.frecuenciaSistolica}</p>}
                </div>

                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">Presión Diastólica (mmHg) *</label>
                  <Input
                    type="number"
                    name="frecuenciaDiastolica"
                    value={formData.frecuenciaDiastolica}
                    onChange={handleChange}
                    placeholder=""
                    className={errors.frecuenciaDiastolica ? 'border-destructive' : ''}
                  />
                  {errors.frecuenciaDiastolica && <p className="text-destructive text-sm mt-1">{errors.frecuenciaDiastolica}</p>}
                </div>
              </div>
            </div>

            <div className="flex gap-4 pt-6">
              <Button type="submit" disabled={loading} className="bg-primary hover:bg-primary/90">
                {loading ? 'Registrando...' : 'Registrar Urgencia'}
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
