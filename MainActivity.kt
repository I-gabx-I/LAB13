package com.example.lab13

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab13.ui.theme.BMICalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BMICalculatorScreen()
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun BMICalculatorScreen() {
    var weightInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf<Double?>(null) }
    var bmiCategory by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculator") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFE6F4EA), Color(0xFFC2E9FB))
                        )
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text("Weight (lbs)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = heightInput,
                    onValueChange = { heightInput = it },
                    label = { Text("Height (cm)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        val weight = weightInput.toDoubleOrNull()
                        val heightCm = heightInput.toDoubleOrNull()

                        if (weight != null && heightCm != null && heightCm > 0) {
                            val heightInInches = heightCm / 2.54
                            val bmi = (weight / (heightInInches * heightInInches)) * 703
                            bmiResult = String.format("%.2f", bmi).toDouble()
                            bmiCategory = getBMICategory(bmi)
                        } else {
                            bmiResult = null
                            bmiCategory = "Invalid input"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calculate BMI")
                }

                Spacer(modifier = Modifier.height(24.dp))

                bmiResult?.let { bmi ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(8.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color.Green, shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Your BMI is:", fontSize = 14.sp)
                                Text(
                                    text = "$bmi",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(bmiCategory)
                            }
                        }
                    }
                }
            }
        }
    )
}

fun getBMICategory(bmi: Double): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi < 25 -> "Normal weight"
        bmi < 30 -> "Overweight"
        else -> "Obese"
    }
}
