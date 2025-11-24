import {IngresoDTO} from "@/lib/types";

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
        throw new Error("Error al registrar Urgencia")
    }
    return await response.text();
}