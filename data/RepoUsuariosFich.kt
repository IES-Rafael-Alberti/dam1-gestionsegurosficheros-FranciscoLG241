package data

import model.IExportable
import model.Usuario
import utils.FicherosTexto

class RepoUsuariosFich(private val rutaArchivo: String, private val fich: FicherosTexto) : RepoUsuariosMem(), ICargarUsuariosIniciales {
    override fun agregar(usuario: Usuario): Boolean {
        if (buscar(usuario.nombre) != null) return false
        if (fich.agregarLinea(rutaArchivo, usuario.serializar())) {
            return super.agregar(usuario)
        }
        return false
    }

    override fun eliminar(usuario: Usuario): Boolean {
        return try {
            val usuariosFiltrados = usuarios.filter { it != usuario }

            val usuariosExportables = usuariosFiltrados.map { usuario ->
                object : IExportable {
                    override fun serializar(separador: String) = usuario.serializar(separador)
                }
            }

            if (fich.escribirArchivo(rutaArchivo, usuariosExportables)) {
                super.eliminar(usuario)
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
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
