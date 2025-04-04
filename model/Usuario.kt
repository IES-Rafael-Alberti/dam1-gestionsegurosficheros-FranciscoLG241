package model

class Usuario(
    val nombre: String,
    clave: String,
    val perfil: Perfil
): IExportable {


    var clave: String = clave
        private set

    companion object{
        fun crearUsuario(datos: List<String>): Usuario{
            return Usuario(
                nombre = datos[0],
                clave = datos[1],
                perfil = Perfil.getPerfil(datos[2])
            )
        }
    }


    fun cambiarClave(nuevaClaveEncriptada: String) {
        this.clave = nuevaClaveEncriptada
    }


    override fun serializar(separador: String): String {
        return "$nombre$separador$clave$separador$perfil"
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Usuario) return false

        return nombre == other.nombre && clave == other.clave && perfil == other.perfil
    }



    override fun hashCode(): Int {
        return 31 * nombre.hashCode() + clave.hashCode() + perfil.hashCode()
    }



    override fun toString(): String {
        return "Usuario(nombre='$nombre', clave='$clave', perfil=$perfil)"
    }

}