package utils

import data.IUtilFicheros
import ui.IEntradaSalida
import java.io.File

class FicherosTexto(private val ui: IEntradaSalida): IUtilFicheros {


    override fun leerArchivo(ruta: String): List<String> {
        val archivo = File(ruta)
        try {
            archivo.appendText("$linea\n")
            return true
        }catch (e: IDException){
            ui.mostrarError("Se produjo el siguiente error al leer el archivo")
            return emptyList()

        }
        archivo.readLines()
    }


    override fun agregarLinea(ruta: String, linea: String): Boolean {
        val archivo = File(ruta)
        try {
            archivo.appendText("$linea\n")
            return true
        } catch (e: IDException){
            ui.mostrarError("Se produjo el siguiente error al escribir en el archivo ")
        }
    }


    override fun escribirArchivo(ruta: String, lineas: List<String>): Boolean {
        return File(ruta).exists()
    }


    override fun existeDirectorio









}