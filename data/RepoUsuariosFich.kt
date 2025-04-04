package data

import model.Usuario

class RepoUsuariosFich(private val rutaArchivo: String, private val fich: IUtilFicheros) : RepoUsuariosMem(), ICargarUsuariosIniciales {
    override fun agregar(usuario: Usuario): Boolean {
        if (buscar(usuario.nombre) != null) return false
        if (fich.agregarLinea(rutaArchivo, usuario.serializar())) {
            return super.agregar(usuario)
        }
        return false
    }

    override fun eliminar(usuario: Usuario): Boolean {
        if (fich.escribirArchivo(rutaArchivo, usuarios.filter { it != usuario }.map { it.serializar() })) {
            return super.eliminar(usuario)
        }
        return false
    }

    override fun cargarUsuarios(): Boolean {
        val lineas = fich.leerArchivo(rutaArchivo)
        lineas.forEach {
            val datos = it.split(";")
            val usuario = Usuario.crearUsuario(datos)
            super.agregar(usuario)
        }
        return true
    }
}
