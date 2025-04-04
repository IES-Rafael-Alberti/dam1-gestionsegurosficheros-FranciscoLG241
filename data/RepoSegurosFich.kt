package data

import model.Seguro




class RepoSegurosFich(private val rutaArchivo: String, private val fich: IUtilFicheros) : RepoSegurosMem(), ICargarSegurosIniciales {
    override fun agregar(seguro: Seguro): Boolean {
        if (fich.agregarLinea(rutaArchivo, seguro.serializar())) {
            return super.agregar(seguro)
        }
        return false
    }

    override fun eliminar(seguro: Seguro): Boolean {
        if (fich.escribirArchivo(rutaArchivo, seguros.filter { it != seguro }.map { it.serializar() })) {
            return super.eliminar(seguro)
        }
        return false
    }

    override fun cargarSeguros(mapa: Map<String, (List<String>) -> Seguro>): Boolean {
        val lineas = fich.leerArchivo(rutaArchivo)

        for (linea in lineas) {
            val datos = linea.split(";")
            val tipoSeguro = datos.last()

            mapa[tipoSeguro]?.let { crearSeguro ->
                val seguro = crearSeguro(datos)
                super.agregar(seguro)
            }
        }

        return true
    }

}

