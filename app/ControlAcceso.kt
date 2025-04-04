package app

import data.IUtilFicheros
import model.Perfil
import service.IServUsuarios
import ui.IEntradaSalida






class ControlAcceso(
    private val rutaArchivo: String,
    private val gestorUsuarios: IServUsuarios,
    private val ui: IEntradaSalida,
    private val ficheros: utils.IUtilFicheros
) {

    fun autenticar(): Pair<String, Perfil>? {
        if (!verificarExistenciaUsuarios()) {
            return null
        }

        return procesarInicioSesion()
    }



    private fun verificarExistenciaUsuarios(): Boolean {
        val archivoExiste = ficheros.existeFichero(rutaArchivo)
        val tieneContenido = if (archivoExiste) ficheros.leerArchivo(rutaArchivo).isNotEmpty() else false

        if (!archivoExiste || !tieneContenido) {
            ui.mostrar("No hay usuarios registrados en el sistema.", true)
            ui.mostrar("Se requiere crear un usuario ADMINISTRADOR inicial.", true)

            if (!ui.preguntar("¿Desea crear un ADMINISTRADOR ahora?")) {
                ui.mostrarError("No se puede continuar sin usuarios.")
                return false
            }

            return crearUsuarioAdministrador()
        }
        return true
    }




    private fun crearUsuarioAdministrador(): Boolean {
        ui.mostrar("\nCREACIÓN DE USUARIO ADMINISTRADOR", true)


        val nombre = ui.pedirInfo("Nombre de usuario:")
        if (nombre.isBlank()) {
            ui.mostrarError("El nombre no puede estar vacío")
            return false
        }

        val clave = ui.pedirInfo("Contraseña (mínimo 8 caracteres):")
        if (clave.length < 8) {
            ui.mostrarError("La contraseña debe tener al menos 8 caracteres")
            return false
        }



        return try {
            val exito = gestorUsuarios.agregarUsuario(nombre, clave, Perfil.ADMIN)
            if (exito) {
                ui.mostrar("Usuario ADMINISTRADOR creado exitosamente!", true)
                true
            } else {
                ui.mostrarError("No se pudo crear el usuario ADMINISTRADOR")
                false
            }
        } catch (e: Exception) {
            ui.mostrarError("Error al crear usuario: ${e.message ?: "Error desconocido"}")
            false
        }
    }





    private fun procesarInicioSesion(): Pair<String, Perfil>? {
        var intentos = 3

        while (intentos > 0) {
            ui.mostrar("\nINICIO DE SESIÓN (Intentos restantes: $intentos)", true)

            val usuario = ui.pedirInfo("Usuario (deje vacío para cancelar):")
            if (usuario.isBlank()) {
                return null
            }

            val clave = ui.pedirInfo("Contraseña:")

            try {
                val perfil = gestorUsuarios.iniciarSesion(usuario, clave)
                if (perfil != null) {
                    ui.mostrar("¡Bienvenido $usuario!", true)
                    return Pair(usuario, perfil)
                }
            } catch (e: Exception) {
                ui.mostrarError("Error durante el inicio de sesión")
            }

            ui.mostrarError("Credenciales incorrectas")
            intentos--
        }

        ui.mostrarError("Demasiados intentos fallidos. Saliendo...")
        return null
    }
}