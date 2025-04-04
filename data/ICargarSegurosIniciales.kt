package data

import utils.FicherosTexto

interface ICargarSegurosIniciales {
    fun cargarSeguros(mapa: FicherosTexto): Boolean
}
