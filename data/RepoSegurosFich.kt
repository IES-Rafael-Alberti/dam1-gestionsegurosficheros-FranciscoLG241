package data

import model.*
import utils.FicherosTexto


class RepoSegurosFich(
    private val rutaArchivo: String,
    private val ficheros: FicherosTexto
) : RepoSegurosMem(), ICargarSegurosIniciales {



    override fun agregar(seguro: Seguro): Boolean {
        return try {
            val linea = seguro.serializar(";")
            val guardadoOk = ficheros.agregarLinea(rutaArchivo, linea)

            if (guardadoOk) {
                super.agregar(seguro)
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }




    override fun eliminar(seguro: Seguro): Boolean {
        return try {
            if (super.eliminar(seguro)) {
                val seguros = obtenerTodos()
                // Conversión directa sin wrapper
                ficheros.escribirArchivo(rutaArchivo, seguros as List<IExportable>)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }






    private fun actualizarContadores(seguros: List<Seguro>) {

        val maxHogar = seguros
            .filter { it.tipoSeguro() == "SeguroHogar" }
            .maxOfOrNull { it.numPoliza } ?: 0
        SeguroHogar.numPolizasHogar = maxHogar


        val maxAuto = seguros
            .filter { it.tipoSeguro() == "SeguroAuto" }
            .maxOfOrNull { it.numPoliza } ?: 0
        SeguroAuto.numPolizasAuto = maxAuto


        val maxVida = seguros
            .filter { it.tipoSeguro() == "SeguroVida" }
            .maxOfOrNull { it.numPoliza } ?: 0
        SeguroVida.numPolizasVida = maxVida
    }



    override fun cargarSeguros(ficheroTexto: FicherosTexto): Boolean {
        return try {
            val lineas = ficheroTexto.leerArchivo(rutaArchivo)
            val seguros = mutableListOf<Seguro>()

            for (linea in lineas) {
                val datos = linea.split(";")
                val tipo = datos[0]

                val seguro = when (tipo) {
                    "SeguroHogar" -> SeguroHogar.crearSeguro(datos)
                    "SeguroAuto" -> SeguroAuto.crearSeguro(datos)
                    "SeguroVida" -> SeguroVida.crearSeguro(datos)
                    else -> null
                }

                if (seguro != null) {
                    seguros.add(seguro)
                }
            }


            seguros.forEach { super.agregar(it) }
            actualizarContadores(seguros)

            true
        } catch (e: Exception) {
            false
        }
    }








}

