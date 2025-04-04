package service

import data.IRepoUsuarios
import model.Perfil
import model.Usuario
import utils.Seguridad

class GestorUsuarios(
    private val repoUsuarios: IRepoUsuarios,
    private val seguridad: Seguridad
): IServUsuarios{

    override fun iniciarSesion(nombre: String, clave: String): Perfil? {
        val usuario = repoUsuarios.buscar(nombre)

        return if (usuario != null && seguridad.verificarClave(clave, usuario.clave)){
            usuario.perfil
        } else{
            null
        }
    }

    override fun agregarUsuario(nombre: String, clave: String, perfil: Perfil): Boolean {
        val claveEncriptada = seguridad.encriptarClave(clave)
        val usuario = Usuario(nombre, claveEncriptada, perfil)
        return repoUsuarios.agregar(usuario)
    }


    override fun eliminarUsuario(nombre: String): Boolean {
        val usuario = repoUsuarios.buscar(nombre)
        return if (usuario != null){
            repoUsuarios.eliminar(usuario)
        }else{
            false
        }
    }


    override fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean {
        return repoUsuarios.cambiarClave(usuario, seguridad.encriptarClave(nuevaClave))
    }


    override fun buscarUsuario(nombre: String): Usuario? {
        return repoUsuarios.buscar(nombre)
    }


    override fun consultarTodos(): List<Usuario> {
        return repoUsuarios.obtenerTodos()
    }


    override fun consultarPorPerfil(perfil: Perfil): List<Usuario> {
        return repoUsuarios.obtener(perfil)
    }


}