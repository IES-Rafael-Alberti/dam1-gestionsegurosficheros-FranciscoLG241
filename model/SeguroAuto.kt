package model

class SeguroAuto private constructor(
    numPoliza: Int,
    dniTitular: String,
    importe: Double,
    private val descripcion: String,
    private val tipoAuto: Auto,
    private val cobertura: Cobertura,
    private val asistenciaCarretera: Boolean,
    private val numPartes: Int
) : Seguro(numPoliza, dniTitular, importe) {

    companion object {
        var numPolizasAuto: Int = 400000
        private const val PORCENTAJE_INCREMENTO_PARTES = 0.02


        fun crearSeguro(datos: List<String>): SeguroAuto {
            require(datos.size == 7) { "Datos insuficientes para SeguroAuto" }
            return SeguroAuto(
                numPolizasAuto++,
                datos[0],
                datos[1].toDouble(),
                datos[2],
                Auto.getAuto(datos[3]),
                Cobertura.getCobertura(datos[4]),
                datos[5].toBoolean(),
                datos[6].toInt()
            )
        }
    }


    private constructor(
        dniTitular: String,
        numPoliza: Int,
        importe: Double,
        descripcion: String,
        tipoAuto: Auto,
        cobertura: Cobertura,
        asistenciaCarretera: Boolean,
        numPartes: Int
    ) : this(numPoliza, dniTitular, importe, descripcion, tipoAuto, cobertura, asistenciaCarretera, numPartes)

    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        return importe * (1 + interes + numPartes * PORCENTAJE_INCREMENTO_PARTES)
    }

    override fun serializar(separador: String): String {
        return "${super.serializar(separador)}$separador$descripcion$separador$tipoAuto$separador$cobertura$separador$asistenciaCarretera$separador$numPartes"
    }


    override fun toString(): String {
        return "Seguro Auto(numPoliza=$numPoliza, dniTitular=$dniTitular, importe=${"%.2f".format(importe)}, " +
                "descripcion='$descripcion', tipoAuto=$tipoAuto, cobertura=$cobertura, asistenciaCarretera=$asistenciaCarretera, numPartes=$numPartes)"
    }
}
