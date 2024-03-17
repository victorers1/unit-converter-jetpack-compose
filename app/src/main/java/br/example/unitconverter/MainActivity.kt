package br.example.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.example.unitconverter.ui.theme.UnitConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UnitConverter()
                }
            }
        }
    }
}

@Composable
fun StyledText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
fun UnitConverterInputField(
    inputValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = inputValue,
        onValueChange = onValueChange,
        placeholder = { Text(text = "Enter value") },
        label = { Text(text = "Enter value") }
    )
}

@Composable
fun UnitDropDownButton(
    isExpanded: Boolean,
    label: String,
    onClick: () -> Unit,
    dropDownLabels: List<String>,
    onDropdownMenuItemClick: (String?) -> Unit
) {
    Box {
        Button(onClick = onClick) {
            Text(text = label.ifBlank { "Select" })
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
        }
        DropdownMenu(expanded = isExpanded, onDismissRequest = { onDropdownMenuItemClick(null) }) {
            dropDownLabels.map {
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = { onDropdownMenuItemClick(it) },
                )
            }
        }
    }
}

@Composable
fun UnitConverter() {
    val units: List<String> = listOf("Centimeters", "Meters", "Foot", "Millimeters")

    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("0.0") }
    var inputUnit by remember { mutableStateOf("") }
    var outputUnit by remember { mutableStateOf("") }
    var inputDropdownIsExpanded by remember { mutableStateOf(false) }
    var outputDropdownIsExpanded by remember { mutableStateOf(false) }

    fun calcResult() {
        val inputValueDouble: Double = inputValue.toDoubleOrNull() ?: 0.0
        val valueInMeters = convertToMeter(inputValueDouble, inputUnit)
        outputValue = convertFromMeter(valueInMeters, outputUnit).toString()
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp),
    ) {
        StyledText("Unit converter")
        UnitConverterInputField(
            inputValue,
            { inputValue = it; calcResult() },
            modifier = Modifier.padding(16.dp)
        )
        Row {
            UnitDropDownButton(
                inputDropdownIsExpanded,
                inputUnit,
                { inputDropdownIsExpanded = !inputDropdownIsExpanded },
                units,
                {
                    if (it != null) inputUnit = it
                    inputDropdownIsExpanded = false
                    calcResult()
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            UnitDropDownButton(
                outputDropdownIsExpanded,
                outputUnit,
                { outputDropdownIsExpanded = !outputDropdownIsExpanded },
                units,
                {
                    if (it != null) outputUnit = it
                    outputDropdownIsExpanded = false
                    calcResult()
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        StyledText(text = "Result: $outputValue $outputUnit")
    }
}

fun convertToMeter(value: Double, inputUnit: String): Double {
    return when (inputUnit) {
        "Centimeters" -> value * 0.01
        "Meters" -> value
        "Foot" -> value * 0.30479
        "Millimeters" -> value * 0.001
        else -> 0.0
    }
}

fun convertFromMeter(valueInMeters: Double, outputUnit: String): Double {
    return when (outputUnit) {
        "Centimeters" -> valueInMeters * 100
        "Meters" -> valueInMeters
        "Foot" -> valueInMeters * 3.28084
        "Millimeters" -> valueInMeters * 1000
        else -> 0.0
    }
}

@Preview(showBackground = true)
@Composable
fun UnitConverterPreview() {
    UnitConverterTheme {
        UnitConverter()
    }
}