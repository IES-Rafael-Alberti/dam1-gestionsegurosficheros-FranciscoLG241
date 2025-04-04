package app

import model.Perfil
import service.IServSeguros
import service.IServUsuarios
import ui.IEntradaSalida

class GestorMenu(
    private val nombreUsuario: String,
    private val perfilUsuario: Perfil,
    private val ui: IEntradaSalida,
    private val gestorUsuarios: IServUsuarios,
    private val gestorSeguros: IServSeguros
) {

    fun iniciarMenu(indice: Int = 0) {
        val (opciones, acciones) = ConfiguracionesApp.obtenerMenuYAcciones(perfilUsuario.toString(), indice)
        ejecutarMenu(opciones, acciones)
    }

    private fun formatearMenu(opciones: List<String>): String {
        var menu = ""
        for ((index, opcion) in opciones.withIndex()) {
            menu += "${index + 1}. $opcion\n"
        }
        return menu
    }

    private fun mostrarMenu(opciones: List<String>) {
        ui.limpiarPantalla()
        ui.mostrar(formatearMenu(opciones), salto = false)
    }

    private fun ejecutarMenu(opciones: List<String>, ejecutar: Map<Int, (GestorMenu) -> Boolean>) {
        while (true) {
            mostrarMenu(opciones)
            val opcion = ui.pedirInfo("Elige opción > ").toIntOrNull()

            if (opcion != null && opcion in 1..opciones.size) {
                val accion = ejecutar[opcion]
                if (accion != null && accion(this)) {
                    return
                }
            } else {
                ui.mostrarError("Opción no válida!")
            }
        }
    }




    fun nuevoUsuario(): Boolean {
        ui.mostrar("\nNUEVO USUARIO", true)

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



        val perfil = when (ui.pedirInfo("Perfil (A=Admin, G=Gestion, C=Consulta):").uppercase()) {
            "A" -> Perfil.ADMIN
            "G" -> Perfil.GESTION
            else -> Perfil.CONSULTA
        }



        return try {
            val exito = gestorUsuarios.agregarUsuario(nombre, clave, perfil)
            if (exito) {
                ui.mostrar("Usuario creado con éxito!", true)
            } else {
                ui.mostrarError("No se pudo crear el usuario")
            }
            ui.pausar()
            exito
        } catch (e: Exception) {
            ui.mostrarError("Error: ${e.message}")
            false
        }
    }



    fun eliminarUsuario(): Boolean {
        ui.mostrar("\nELIMINAR USUARIO", true)
        val nombre = ui.pedirInfo("Nombre de usuario a eliminar:")

        return try {
            val exito = gestorUsuarios.eliminarUsuario(nombre)
            if (exito) {
                ui.mostrar("Usuario eliminado", true)
            } else {
                ui.mostrarError("No se pudo eliminar el usuario")
            }
            ui.pausar()
            exito
        } catch (e: Exception) {
            ui.mostrarError("Error: ${e.message}")
            false
        }
    }



    fun cambiarClaveUsuario(): Boolean {
        ui.mostrar("\nCAMBIAR CONTRASEÑA", true)
        val nuevaClave = ui.pedirInfo("Nueva contraseña (mínimo 8 caracteres):")

        if (nuevaClave.length < 8) {
            ui.mostrarError("La contraseña debe tener al menos 8 caracteres")
            return false
        }


        return try {
            val usuario = gestorUsuarios.buscarUsuario(nombreUsuario)
            if (usuario != null) {
                val exito = gestorUsuarios.cambiarClave(usuario, nuevaClave)
                if (exito) {
                    ui.mostrar("Contraseña cambiada con éxito", true)
                } else {
                    ui.mostrarError("No se pudo cambiar la contraseña")
                }
                ui.pausar()
                exito
            } else {
                ui.mostrarError("Usuario no encontrado")
                false
            }
        } catch (e: Exception) {
            ui.mostrarError("Error: ${e.message}")
            false
        }
    }




    fun consultarUsuarios(): Boolean {
        ui.mostrar("\nLISTA DE USUARIOS", true)

        val usuarios = gestorUsuarios.consultarTodos()
        if (usuarios.isEmpty()) {
            ui.mostrar("No hay usuarios registrados", true)
        } else {
            usuarios.forEach { usuario ->
                ui.mostrar("- ${usuario.nombre} (${usuario.perfil})", true)
            }
        }

        ui.pausar()
        return false
    }





    fun contratarSeguroHogar(): Boolean {
        ui.mostrar("\nNUEVO SEGURO HOGAR", true)
        ui.mostrar("Seguro de hogar contratado", true)
        ui.pausar()
        return false
    }

    fun contratarSeguroAuto(): Boolean {
        ui.mostrar("\nNUEVO SEGURO AUTO", true)
        ui.mostrar("Seguro de auto contratado", true)
        ui.pausar()
        return false
    }

    fun contratarSeguroVida(): Boolean {
        ui.mostrar("\nNUEVO SEGURO VIDA", true)
        ui.mostrar("Seguro de vida contratado", true)
        ui.pausar()
        return false
    }

    fun eliminarSeguro(): Boolean {
        ui.mostrar("\nELIMINAR SEGURO", true)
        val numPoliza = ui.pedirInfo("Número de póliza a eliminar:").toIntOrNull()

        if (numPoliza == null) {
            ui.mostrarError("Número de póliza inválido")
            return false
        }

        return try {
            val exito = gestorSeguros.eliminarSeguro(numPoliza)
            if (exito) {
                ui.mostrar("Seguro eliminado", true)
            } else {
                ui.mostrarError("No se pudo eliminar el seguro")
            }
            ui.pausar()
            exito
        } catch (e: Exception) {
            ui.mostrarError("Error: ${e.message}")
            false
        }
    }

    fun consultarSeguros(): Boolean {
        ui.mostrar("\nTODOS LOS SEGUROS", true)
        val seguros = gestorSeguros.consultarTodos()

        if (seguros.isEmpty()) {
            ui.mostrar("No hay seguros registrados", true)
        } else {
            seguros.forEach { seguro ->
                ui.mostrar("- ${seguro.toString()}", true)
            }
        }

        ui.pausar()
        return false
    }


    private fun pedirDni(): String {
        while (true) {
            val dni = ui.pedirInfo("DNI (8 números y 1 letra):").uppercase()
            if (dni.matches(Regex("\\d{8}[A-Z]"))) {
                return dni
            }
            ui.mostrarError("Formato de DNI incorrecto")
        }
    }

    private fun pedirImporte(): Double {
        while (true) {
            val importe = ui.pedirInfo("Importe:").replace(',', '.').toDoubleOrNull()
            if (importe != null && importe > 0) {
                return importe
            }
            ui.mostrarError("Importe no válido")
        }
    }
}