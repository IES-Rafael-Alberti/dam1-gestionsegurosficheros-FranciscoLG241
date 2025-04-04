package model

enum class Perfil {
    ADMIN, GESTION, CONSULTA;

    companion object {
        fun getPerfil(valor: String): Perfil {
            return values().find { it.name.equals(valor, ignoreCase = true) } ?: CONSULTA
        }
    }
}