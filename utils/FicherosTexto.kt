package utils

import model.IExportable
import ui.IEntradaSalida
import java.io.File




class FicherosTexto(private val ui: IEntradaSalida): IUtilFicheros {


    override fun leerArchivo(ruta: String): List<String> {
        val archivo = File(ruta)
        if (!archivo.exists()) {
            ui.mostrarError("El archivo no existe")
            return emptyList()
        }
        return archivo.readLines()
    }




    override fun agregarLinea(ruta: String, linea: String): Boolean {
        return try {
            File(ruta).appendText("$linea\n")
            true
        } catch (e: Exception) {
            ui.mostrarError("Error al escribir: ${e.message ?: "Error desconocido"}")
            false
        }
    }


    override fun <T : IExportable> escribirArchivo(ruta: String, elementos: List<T>): Boolean {
        return try {
            File(ruta).writeText(elementos.joinToString("\n") { it.serializar() })
            true
        } catch (e: Exception) {
            ui.mostrarError("Error al guardar: ${e.message ?: "Error desconocido"}")
            false
        }
    }



    override fun existeFichero(ruta: String): Boolean = File(ruta).isFile


    override fun existeDirectorio(ruta: String): Boolean = File(ruta).isDirectory

}