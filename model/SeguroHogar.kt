package model

class SeguroHogar private constructor(
    numPoliza: Int,
    dniTitular: String,
    importe: Double,
    private val metrosCuadrados: Int,
    private val valorContenido: Double,
    private val direccion: String,
    private val anioConstruccion: Int
) : Seguro(numPoliza, dniTitular, importe) {



    companion object {
        var numPolizasHogar = 100000
        private const val PORCENTAJE_INCREMENTO_ANIOS = 0.02
        private const val CICLO_ANIOS_INCREMENTO = 5



        fun crearSeguro(datos: List<String>): SeguroHogar {
            require(datos.size == 5) { "Datos insuficientes para SeguroHogar" }
            return SeguroHogar(
                numPolizasHogar++, datos[0], datos[1].toDouble(),
                datos[2].toInt(), datos[3].toDouble(), datos[4], datos[5].toInt()
            )
        }
    }




    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        val antiguedad = 2024 - anioConstruccion
        val incremento = (antiguedad / CICLO_ANIOS_INCREMENTO) * PORCENTAJE_INCREMENTO_ANIOS
        return importe * (1 + interes + incremento)
    }




    override fun toString(): String {
        return super.toString().replace("Seguro", "SeguroHogar") +
                ", metrosCuadrados=$metrosCuadrados, valorContenido=$valorContenido, direccion='$direccion', anioConstruccion=$anioConstruccion"
    }



    override fun serializar(separador: String): String {
        return super.serializar(separador) + "$separador$metrosCuadrados$separador$valorContenido$separador$direccion$separador$anioConstruccion"
    }
}

