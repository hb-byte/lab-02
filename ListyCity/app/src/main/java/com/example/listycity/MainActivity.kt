package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        onDeleteCity = { cityRepository.deleteCity(it) },
                        modifier = Modifier.padding(paddingValues = innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onDeleteCity: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    var selectedCity by remember { mutableIntStateOf(-1) }
    var isCitySelected by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City name") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                },
                enabled = newCityName.isNotBlank()
            ) {
                Text("Add city")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (selectedCity >= 0) {
                        onDeleteCity(selectedCity)
                        isCitySelected = false
                        selectedCity = -1
                    } },
                enabled = selectedCity >= 0
            ) {
                Text("Delete city")
            }
        }
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(cities){ city ->
                val selected = selectedCity == cities.indexOf(city)
                Text(
                    text = city,
                    fontSize = 28.sp,
                    modifier = Modifier
                        .selectable(
                            selected = selected,
                            interactionSource = interactionSource,
                            onClick = {
                                selectedCity = cities.indexOf(city)
                            }
                        )
                        .background(color = (if (selected) DarkGray else LightGray))
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                )
            }

        // I wasn't able to get my method of selecting/deleting a row to work with the
        // CityRow @Composable, but I was able to get it to work like this. Also, it's hideous,
        // but it's feature complete. SHIP IT

//                CityRow(
//                    city = city,
//                    modifier = Modifier
//                        .selectable(
//                            selected = selected,
//                            interactionSource = interactionSource,
//                            onClick = {
//                                selectedCity = cities.indexOf(city)
//                            },
//                            indication = LocalIndication.current
//                        )
//                )
//            }
        }
    }
}

//@Composable
//fun CityRow(city: String, modifier: Modifier = Modifier){
//    Text(
//        text = city,
//        fontSize = 28.sp,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 18.dp, vertical = 14.dp)
//    )
//}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ListyCityTheme {
        Greeting("Android")
    }
}

class CityRepository {
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Osaka", "Hamilton", "London", "Nice"
    )

    val cities: List<String>
        get() = _cities

    fun addCity(city: String){
        _cities.add(city)
    }

    fun deleteCity(index: Int) {
        _cities.removeAt(index)
    }
}