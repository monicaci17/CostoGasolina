package com.mexiti.costogasolina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mexiti.costogasolina.ui.theme.CostoGasolinaTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CostoGasolinaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFE8F5E9)
                ) {
                    CostGasLayout("Android")
                }
            }
        }
    }
}

@Composable
fun CostGasLayout(name: String, modifier: Modifier = Modifier) {
    var precioLitroEntrada by remember{
        mutableStateOf("")
    }
    var cantLitroEntrada by remember {
        mutableStateOf("")
    }
    var propinaEntrada by remember {
        mutableStateOf("")
    }
    var agregarPropina by remember {
        mutableStateOf(false)
    }

    val precioLitro = precioLitroEntrada.toDoubleOrNull() ?: 0.0
    val cantLitros = cantLitroEntrada.toDoubleOrNull() ?: 0.0
    val propina = propinaEntrada.toDoubleOrNull() ?: 0.0
    val monto = calcularMonto(precioLitro, cantLitros, propina, agregarPropina)
 // Cambio




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA5D6A7))
            .padding(10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calcular_monto),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .fillMaxWidth()
            )
        Spacer(modifier = Modifier.height(30.dp))
       EditNumberField(
           label = R.string.ingresa_gasolina,
           leadingIcon = R.drawable.money_gas ,
           keyboardsOptions = KeyboardOptions.Default.copy(
               keyboardType = KeyboardType.Number,
               imeAction = ImeAction.Next
           ),
           value = precioLitroEntrada,
           onValueChanged = {
               precioLitroEntrada = it
           }
       )
        EditNumberField(
            label = R.string.litros,
            leadingIcon = R.drawable.gas_24,
            keyboardsOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = cantLitroEntrada,
            onValueChanged = {
                cantLitroEntrada = it
            }
        )

        EditNumberField(
            label = R.string.propina,
            leadingIcon = R.drawable.propina_24,
            keyboardsOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = propinaEntrada,
            onValueChanged = {
                propinaEntrada = it
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(            modifier = modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "¿Deseas agregar propina?"
            )
            Switch(
                checked = agregarPropina,
                onCheckedChange = {
                    agregarPropina = it
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.monto_total, monto),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .fillMaxWidth()
        )

    }

}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardsOptions:KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
){
    TextField(
        label = { Text(text = stringResource(id = label))  },
        value = value,
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon) , contentDescription = null) },
        keyboardOptions = keyboardsOptions,
        modifier = modifier
            .fillMaxWidth(),
        onValueChange = onValueChanged
    )

}

@Preview(showBackground = true)
@Composable
fun CostGasLayoutPreview() {
    CostoGasolinaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFC8E6C9)
        ) {
            CostGasLayout("Android")
        }
    }
}

private fun calcularMonto(precio: Double, cantLitros: Double, propina: Double, agregarPropina: Boolean): String{
    val subtotal = precio * cantLitros
    val total = if (agregarPropina) {
        subtotal + propina
    } else {
        subtotal
    }
    return NumberFormat.getCurrencyInstance().format(total)
}