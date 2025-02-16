package com.lancode.calculadoracompose.controller

import com.lancode.calculadoracompose.model.CalculadoraModel

class CalculadoraController {
    private val model = CalculadoraModel()

    fun realizarCalculo(operando1: String, operando2: String, operador: String): String {
        if (operando1.isEmpty() || operando2.isEmpty() || operador.isEmpty()) {
            return "Error"
        }

        return try {
            val num1 = operando1.toDouble()
            val num2 = operando2.toDouble()
            val resultado = model.calcular(num1, num2, operador)
            if (resultado.isNaN()) "Error" else resultado.toString()
        } catch (e: NumberFormatException) {
            "Error"
        }
    }
}

