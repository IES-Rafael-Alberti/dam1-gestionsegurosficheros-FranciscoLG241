import app.CargadorInicial
import app.ControlAcceso
import app.GestorMenu
import data.*
import service.GestorSeguros
import service.GestorUsuarios
import ui.Consola
import utils.FicherosTexto
import utils.Seguridad




fun main() {

    val rutaUsuarios = "usuarios.txt"
    val rutaSeguros = "seguros.txt"


    val ui = Consola()
    val ficheros = FicherosTexto(ui)
    val seguridad = Seguridad(ui)


    ui.limpiarPantalla()


    val modoSimulacion = ui.preguntar("¿Desea iniciar en modo simulación? (S/N)")


    val repoUsuarios: IRepoUsuarios
    val repoSeguros: IRepoSeguros


    if (modoSimulacion) {
        ui.mostrar("Iniciando en modo simulación (datos en memoria)", true)
        repoUsuarios = RepoUsuariosMem()
        repoSeguros = RepoSegurosMem()
    } else {
        ui.mostrar("Iniciando en modo persistente (datos en archivos)", true)
        repoUsuarios = RepoUsuariosFich(rutaUsuarios, ficheros)
        repoSeguros = RepoSegurosFich(rutaSeguros, ficheros)


        val cargador = CargadorInicial(ui, repoUsuarios, repoSeguros, ficheros)
        cargador.cargarUsuarios()
        cargador.cargarSeguros()
    }


    val gestorUsuarios = GestorUsuarios(repoUsuarios, seguridad)
    val gestorSeguros = GestorSeguros(repoSeguros)



    val controlAcceso = ControlAcceso(rutaUsuarios, gestorUsuarios, ui, ficheros)
    val credenciales = controlAcceso.autenticar()


    if (credenciales != null) {
        val (nombreUsuario, perfilUsuario) = credenciales

        // Iniciar menú principal
        val menu = GestorMenu(nombreUsuario, perfilUsuario, ui, gestorUsuarios, gestorSeguros)
        menu.iniciarMenu(0)
    } else {
        ui.mostrar("No se pudo iniciar sesión. Saliendo...", true)
    }
}