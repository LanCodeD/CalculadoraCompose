package com.lancode.calculadoracompose.model

class CalculadoraModel {
    fun calcular(operando1: Double, operando2: Double, operador: String): Double {
        return when (operador) {
            "+" -> operando1 + operando2
            "-" -> operando1 - operando2
            "×" -> operando1 * operando2
            "÷" -> if (operando2 != 0.0) operando1 / operando2 else Double.NaN
            else -> Double.NaN
        }
    }
}
