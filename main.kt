import ui.Consola


fun main(){

    val con = Consola()


    val nombre1 = con.pedirInfo("Introduzca un nombre> ", "Nombre Incorrecto"){
        it.isNotEmpty() && it.length >= 5
    }


}