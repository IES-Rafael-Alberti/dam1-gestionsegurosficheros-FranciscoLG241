package data

import model.Seguro

open class RepoSegurosMem : IRepoSeguros {
    val seguros = mutableListOf<Seguro>()

    override fun agregar(seguro: Seguro): Boolean {
        seguros.add(seguro)
        return true
    }

    override fun buscar(numPoliza: Int): Seguro? {
        return seguros.find { it.numPoliza == numPoliza }
    }

    override fun eliminar(seguro: Seguro): Boolean {
        return seguros.remove(seguro)
    }

    override fun eliminar(numPoliza: Int): Boolean {
        val seguro = buscar(numPoliza)
        return if (seguro != null) eliminar(seguro) else false
    }

    override fun obtenerTodos(): List<Seguro> {
        return seguros
    }

    override fun obtener(tipoSeguro: String): List<Seguro> {
        return seguros.filter { it.tipoSeguro() == tipoSeguro }
    }
}

