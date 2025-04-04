package model

import java.time.LocalDate

class SeguroVida private constructor(
    numPoliza: Int,
    dniTitular: String,
    importe: Double,
    private val fechaNac: LocalDate,
    private val nivelRiesgo: Riesgo,
    private val indemnizacion: Double
) : Seguro(numPoliza, dniTitular, importe) {




    companion object {
        var numPolizasVida = 800000
        private const val PORCENTAJE_INCREMENTO_ANUAL = 0.0005



        fun crearSeguro(datos: List<String>): SeguroVida {
            require(datos.size == 5) { "Datos insuficientes para SeguroVida" }
            return SeguroVida(
                numPolizasVida++, datos[0], datos[1].toDouble(),
                LocalDate.parse(datos[2]), Riesgo.getRiesgo(datos[3]), datos[4].toDouble()
            )
        }
    }





    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        val edad = LocalDate.now().year - fechaNac.year
        return importe * (1 + interes + edad * PORCENTAJE_INCREMENTO_ANUAL + nivelRiesgo.interesAplicado / 100)
    }


    override fun serializar(separador: String): String {
        return "${super.serializar(separador)}$separador$fechaNac$separador$nivelRiesgo$separador$indemnizacion"
    }



    override fun toString(): String {
        return "Seguro Vida(numPoliza=$numPoliza, dniTitular=$dniTitular, importe=${"%.2f".format(importe)}, " +
                "fechaNac=$fechaNac, nivelRiesgo=$nivelRiesgo, indemnizacion=$indemnizacion)"
    }

}
