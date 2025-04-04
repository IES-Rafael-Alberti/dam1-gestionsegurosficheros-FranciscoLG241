package app

import data.ICargarSegurosIniciales
import data.ICargarUsuariosIniciales
import ui.IEntradaSalida
import utils.FicherosTexto


class CargadorInicial(
    private val ui: IEntradaSalida,
    private val repoUsuarios: ICargarUsuariosIniciales,
    private val repoSeguros: ICargarSegurosIniciales,
    private val mapaCrearSeguros: FicherosTexto
) {

    fun cargarUsuarios() {
        try {
            val resultado = repoUsuarios.cargarUsuarios()
            if (resultado) {
                ui.mostrar("Usuarios cargados correctamente", salto = true)
            } else {
                ui.mostrarError("No se pudieron cargar los usuarios")
            }
        } catch (e: Exception) {
            ui.mostrarError("Error al cargar usuarios: ${e.message ?: "Error desconocido"}")
        }
    }




    fun cargarSeguros() {
        try {
            val resultado = repoSeguros.cargarSeguros(mapaCrearSeguros)
            if (resultado) {
                ui.mostrar("Seguros cargados correctamente", salto = true)
            } else {
                ui.mostrarError("No se pudieron cargar los seguros")
            }
        } catch (e: Exception) {
            ui.mostrarError("Error al cargar seguros: ${e.message ?: "Error desconocido"}")
        }
    }
}