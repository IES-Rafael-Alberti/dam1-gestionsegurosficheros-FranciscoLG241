package service

import data.IRepoSeguros
import model.*
import java.time.LocalDate

class GestorSeguros(private val repoSeguros: IRepoSeguros) : IServSeguros {


    override fun contratarSeguroHogar(
        dniTitular: String,
        importe: Double,
        metrosCuadrados: Int,
        valorContenido: Double,
        direccion: String,
        anioConstruccion: Int
    ): Boolean {
        val datos = listOf(
            dniTitular,
            importe.toString(),
            metrosCuadrados.toString(),
            valorContenido.toString(),
            direccion,
            anioConstruccion.toString()
        )
        val seguro = SeguroHogar.crearSeguro(datos)
        return repoSeguros.agregar(seguro)
    }




    override fun contratarSeguroAuto(
        dniTitular: String,
        importe: Double,
        descripcion: String,
        combustible: String,
        tipoAuto: Auto,
        cobertura: Cobertura,
        asistenciaCarretera: Boolean,
        numPartes: Int
    ): Boolean {
        val datos = listOf(
            dniTitular,
            importe.toString(),
            "$descripcion ($combustible)",
            tipoAuto.toString(),
            cobertura.toString(),
            asistenciaCarretera.toString(),
            numPartes.toString()
        )
        val seguro = SeguroAuto.crearSeguro(datos)
        return repoSeguros.agregar(seguro)
    }





    override fun contratarSeguroVida(
        dniTitular: String,
        importe: Double,
        fechaNacimiento: LocalDate,
        nivelRiesgo: Riesgo,
        indemnizacion: Double
    ): Boolean {
        val datos = listOf(
            dniTitular,
            importe.toString(),
            fechaNacimiento.toString(),
            nivelRiesgo.toString(),
            indemnizacion.toString()
        )
        val seguro = SeguroVida.crearSeguro(datos)
        return repoSeguros.agregar(seguro)
    }




    override fun eliminarSeguro(numPoliza: Int): Boolean {
        return repoSeguros.eliminar(numPoliza)
    }



    override fun consultarTodos(): List<Seguro> {
        return repoSeguros.obtenerTodos()
    }



    override fun consultarPorTipo(tipoSeguro: String): List<Seguro> {
        return repoSeguros.obtenerTodos().filter { it::class.simpleName == tipoSeguro }
    }
}