export enum NivelEmergencia {
    CRITICA = "CRITICA",
    EMERGENCIA = "EMERGENCIA",
    URGENCIA = "URGENCIA",
    URGENCIA_MENOR = "URGENCIA_MENOR",
    SIN_URGENCIA = "SIN_URGENCIA",
}

export interface IngresoDTO {
    cuilPaciente: string;
    informe: string;
    nivelEmergencia: NivelEmergencia;
    temperatura: number;
    frecCardiaca: number;
    frecRespiratoria: number;
    frecuenciaSistolica: number;
    frecuenciaDiastolica: number;
    enfermera: {
        // Si solo envías ID, ajusta esto. Si envías objeto:
        id?: number;
        nombre?: string;
    } | null;
}