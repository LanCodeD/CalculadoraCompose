package com.lancode.calculadoracompose.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lancode.calculadoracompose.controller.CalculadoraController


@OptIn(ExperimentalMaterial3Api::class) //para mostrar el TopBar
@Preview(showBackground = true, showSystemUi = true) // Mostramos un prewiew real de la app
@Composable //iniciamos compose
fun CalculadoraView() {
    var input by rememberSaveable { mutableStateOf("0") }
    var firstNumber by rememberSaveable { mutableStateOf("") }
    var secondNumber by rememberSaveable { mutableStateOf("") }
    var operator by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf<String?>(null) }
    val controlador = CalculadoraController() //importamos el controllador

    val maxDigits = 10

    val buttons = listOf(
        "7", "8", "9", "÷",
        "4", "5", "6", "×",
        "1", "2", "3", "-",
        "0", ".", "=", "+",
        "C", "⌫"
    ) //Declaramos nuestra lista de botones

    val buttonColors: Map<String, Color> = mapOf(
        "=" to Color(0xFF00C896),
        "+" to Color(0xFF007AFF),
        "-" to Color(0xFF007AFF),
        "×" to Color(0xFF007AFF),
        "÷" to Color(0xFF007AFF),
        "C" to Color(0xFFD32F2F),
        "⌫" to Color(0xFF616161)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Calculadora", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF007AFF), // Color de fondo del AppBar
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF121212) // Fondo general de la app
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pantalla de la calculadora
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (firstNumber.isNotEmpty() && operator.isNotEmpty()) {
                    Text(
                        text = "$firstNumber $operator",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = result ?: input,
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // **Botones**
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(buttons.size) { index ->
                    val button = buttons[index]

                    Button(
                        onClick = {
                            when (button) {
                                "C" -> {
                                    input = "0"
                                    firstNumber = ""
                                    secondNumber = ""
                                    operator = ""
                                    result = null

                                }
                                "⌫" -> {
                                    if (input.length > 1) {
                                        input = input.dropLast(1)
                                    } else {
                                        input = "0"
                                    }

                                    // validamos para el segndo numero
                                    if (operator.isNotEmpty()) {
                                        secondNumber = input
                                    } else {
                                        firstNumber = input
                                    }
                                }

                                "=" -> {
                                    if (firstNumber.isNotEmpty() && operator.isNotEmpty() && secondNumber.isNotEmpty()) {
                                        result = controlador.realizarCalculo(firstNumber, secondNumber, operator)

                                        // guardamos despues del iguak
                                        firstNumber = result ?: "0"
                                        secondNumber = ""
                                        operator = ""

                                        // Reflejamos el resultado
                                        input = firstNumber
                                    }
                                }

                                "+", "-", "×", "÷" -> {
                                    if (firstNumber.isEmpty()) {
                                        firstNumber = input
                                    } else if (operator.isNotEmpty() && secondNumber.isNotEmpty()) {
                                        //Validamos si está vació el input
                                        result = controlador.realizarCalculo(firstNumber, secondNumber, operator)
                                        firstNumber = result ?: "0" //Para un tercer valor
                                        secondNumber = ""
                                    }
                                    operator = button
                                    input = ""
                                    result = null // Limpiamos para otro valor
                                }



                                else -> {
                                    if (input.length < maxDigits) {
                                        if (operator.isNotEmpty() && secondNumber.isEmpty()) {
                                            // Operador del número, empezar a escribirlo
                                            input = button
                                        } else {
                                            //agrega si hay algo en pantalla
                                            input = if (input == "0") button else input + button
                                        }

                                        if (operator.isNotEmpty()) {
                                            secondNumber = input // Guarda el segundo número
                                        }
                                    }
                                }



                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .size(80.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColors[button] ?: Color(0xFF2E2E2E),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = button, fontSize = 28.sp)
                    }
                }
            }
        }
    }
}

