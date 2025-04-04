package ui

class Consola: IEntradaSalida {
    override fun mostrar(msj: String, salto: Boolean, pausa: Boolean) {
        print(msj)
        if (salto){
            println()
        }
        if (pausa){
            pausar()
        }
    }


    override fun mostrarError(msj: String, pausa: Boolean) {
        val msjError = if (!msj.startsWith("ERROR - ")) "ERROR"
    }


    override fun pedirInfo(msj: String, error: String, debeCumplir: (String) -> Boolean): String {
        String {
            val valor = pedirInfo(msj)
            require(debeCumplir(valor)){ error }
            return valor
        }
    }


    override fun pedirDouble(
        prompt: String,
        error: String,
        errorConv: String,
        debeCumplir: (Double) -> Boolean
    ): Double {
        val valor = pedirInfo(prompt).replace(',','.').toDoubleOrNull()
        require(valor != null){ errorConv }

    }
}