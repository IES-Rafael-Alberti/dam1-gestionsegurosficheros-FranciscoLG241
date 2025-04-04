package ui

import jdk.internal.org.jline.reader.EndOfFileException
import jdk.internal.org.jline.reader.LineReaderBuilder
import jdk.internal.org.jline.reader.UserInterruptException
import jdk.internal.org.jline.terminal.TerminalBuilder




class Consola: IEntradaSalida {
    override fun mostrar(msj: String, salto: Boolean, pausa: Boolean) {
        print(msj)
        if (salto){
            println()
        }
        if (pausa){
            pausar()
        }
    }


    override fun mostrarError(msj: String, pausa: Boolean) {
        val msjError = if (!msj.startsWith("ERROR - ")) "ERROR - $msj" else msj
        mostrar(msjError, true, pausa)
    }


    override fun pedirInfo(msj: String): String {
        if (msj.isNotEmpty()) {
            mostrar(msj, salto = false)
        }
        return readln().trim() ?: ""
    }


    override fun pedirInfo(msj: String, error: String, debeCumplir: (String) -> Boolean): String {
        while (true) {
            val valor = pedirInfo(msj)
            try {
                require(debeCumplir(valor)) { error }
                return valor
            } catch (e: IllegalArgumentException) {
                mostrarError(e.message ?: error)
            }
        }
    }


    override fun pedirDouble(
        prompt: String,
        error: String,
        errorConv: String,
        debeCumplir: (Double) -> Boolean
    ): Double {
        while (true) {
            val input = pedirInfo(prompt).replace(',', '.')
            val valor = input.toDoubleOrNull()

            try {
                require(valor != null) { errorConv }
                require(debeCumplir(valor)) { error }
                return valor
            } catch (e: IllegalArgumentException) {
                mostrarError(e.message ?: error)
            }
        }
    }



    override fun pedirEntero(
        prompt: String,
        error: String,
        errorConv: String,
        debeCumplir: (Int) -> Boolean
    ): Int {
        while (true) {
            val input = pedirInfo(prompt)
            val valor = input.toIntOrNull()

            try {
                require(valor != null) { errorConv }
                require(debeCumplir(valor)) { error }
                return valor
            } catch (e: IllegalArgumentException) {
                mostrarError(e.message ?: error)
            }
        }
    }





    override fun pedirInfoOculta(prompt: String): String {
        return try {
            val terminal = TerminalBuilder.builder()
                .dumb(true) // Para entornos no interactivos como IDEs
                .build()

            val reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build()

            reader.readLine(prompt, '*') // Oculta la contraseña con '*'
        } catch (e: UserInterruptException) {
            mostrarError("Entrada cancelada por el usuario (Ctrl + C).", pausa = false)
            ""
        } catch (e: EndOfFileException) {
            mostrarError("Se alcanzó el final del archivo (EOF ó Ctrl+D).", pausa = false)
            ""
        } catch (e: Exception) {
            mostrarError("Problema al leer la contraseña: ${e.message}", pausa = false)
            ""
        }
    }



    override fun pausar(msj: String) {
        print(msj)
        readln()
    }


    override fun limpiarPantalla(numSaltos: Int) {
        if (System.console() != null) {
            mostrar("\u001b[H\u001b[2J", false)
            System.out.flush()
        } else {
            repeat(numSaltos) {
                mostrar("")
            }
        }
    }



    override fun preguntar(mensaje: String): Boolean {
        while (true) {
            print("$mensaje (s/n): ")
            when (readln().lowercase().trim()) {
                "s" -> return true
                "n" -> return false
                else -> println("Por favor, introduce 's' para sí o 'n' para no")
            }
        }
    }
}