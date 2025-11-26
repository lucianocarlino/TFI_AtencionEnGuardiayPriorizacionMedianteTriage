import {IngresoDTO} from "@/lib/types";
import {IngresoResponse} from "@/lib/types";
const API_URL = "http://localhost:8080/sistema-urgencias/api/v1/urgencias";
export async function registrarIngreso(datos: IngresoDTO){
    const response = await fetch(`${API_URL}`,{
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(datos),
    });
    if(!response.ok){
        throw new Error("Error al registrar Urgencia: " + await response.text())
    }
    return await response.text();
}

export async function listarUrgencias(): Promise<IngresoResponse[]>{
    const response = await fetch(`${API_URL}`,{
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    });
    if(!response.ok){
        throw new Error("Error al obtener lista de urgencias pendientes")
    }
    return await response.json();
}