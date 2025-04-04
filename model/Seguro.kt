package model

abstract class Seguro(
    val numPoliza: Int,
    protected val dniTitular: String,
    protected var importe: Double
) : IExportable {



    abstract fun calcularImporteAnioSiguiente(interes: Double): Double



    override fun serializar(separador: String): String {
        return "$numPoliza$separador$dniTitular$separador${"%.2f".format(importe)}"
    }




    override fun toString(): String {
        return "Seguro(numPoliza=$numPoliza, dniTitular=$dniTitular, importe=${"%.2f".format(importe)})"
    }




    override fun hashCode(): Int = numPoliza.hashCode()




    override fun equals(other: Any?): Boolean {
        return other is Seguro && this.numPoliza == other.numPoliza
    }




    fun tipoSeguro(): String = this::class.simpleName ?: "Desconocido"
}
