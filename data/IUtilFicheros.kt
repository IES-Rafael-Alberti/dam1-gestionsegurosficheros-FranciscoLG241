package data

interface IUtilFicheros {
    fun agregarLinea(ruta: String, linea: String): Boolean
    fun escribirArchivo(ruta: String, lineas: List<String>): Boolean
    fun leerArchivo(ruta: String): List<String>
}
