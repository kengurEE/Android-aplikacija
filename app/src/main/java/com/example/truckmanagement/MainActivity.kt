package com.example.truckmanagement

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.truckmanagement.ui.theme.TruckManagementTheme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import java.util.concurrent.ConcurrentSkipListMap
import androidx.compose.material3.OutlinedTextField

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Popup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.room.Entity
import androidx.room.Room
import com.example.truckmanagement.Data.BazaPodataka
import com.example.truckmanagement.Data.BazaPodataka_Impl
import com.example.truckmanagement.Data.Ent
import com.example.truckmanagement.Data.Entitet
//import com.example.truckmanagement.Data.entitiiiiii
//import com.example.truckmanagement.Data.TruckDatabase
//import com.example.truckmanagement.Data.TroskoviDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var baza: BazaPodataka

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baza = Room.databaseBuilder(
            applicationContext, // `this` ovde referiše na MainActivity
            BazaPodataka::class.java,
            "truck"
        ).fallbackToDestructiveMigration()
            .build()

        enableEdgeToEdge()
        setContent {
            TruckManagementTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )/*
                    PocetniEkran(
                        navController = rememberNavController()
                    )*/
                    TruckManagementApp(baza)
/*
                    Dugme(
                        naziv="trosak",
                        modifier = Modifier.padding(innerPadding),
                        navController = rememberNavController(),
                        tipKamiona = ""
                    )
*/


                }
            }

        }
    }
}


@Composable
fun TruckManagementApp(bazaPodataka: BazaPodataka){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
    ){
        composable("home"){
            PocetniEkran(navController)
        }
        composable("UnosTroskovaHladnjace"){
            Dugme("trosak",Modifier,navController,"Hladnjača", bazaPodataka )
        }
        composable("UnosTroskovaObicog"){
            Dugme("trosak",Modifier,navController,"Šleper", bazaPodataka)
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "",
        modifier = modifier
    )
}


@SuppressLint("NewApi")
@Composable
fun Dugme(naziv: String, modifier: Modifier = Modifier, navController: NavHostController,tipKamiona:String, bazaPodataka: BazaPodataka){
    var gorivo by remember { mutableStateOf("") }
    var odrzavanje by remember { mutableStateOf("") }
    var putarina by remember { mutableStateOf("") }
    var datum by remember { mutableStateOf("") }
    var PorukaOGresci by remember { mutableStateOf("") }
    var lista_troskova by remember { mutableStateOf<List<Ent>>(emptyList()) }
    var ukupan_trosak by remember { mutableStateOf(Int) }
    val coroutineScope= rememberCoroutineScope()



    //DatePicker
    val context = LocalContext.current
    val datePickerDialog=android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                datum = "$dayOfMonth/${month + 1}/$year"  //format datuma
            },
            2024, 5, 1
    )

    Column(modifier = modifier.padding(16.dp)){
        Text(text="Unos troskova za $tipKamiona:")


        OutlinedTextField(
            value = gorivo,
            onValueChange = {if(it.all { char -> char.isDigit() }) gorivo = it},
            label = {Text(text = "Unesi $naziv za gorivo: ")},

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Spacer(modifier=modifier.height(8.dp))

        OutlinedTextField(
            value =odrzavanje,
            onValueChange = {if(it.all{char->char.isDigit()}) odrzavanje = it},
            label = {Text(text = "Unesi $naziv za odrzavanje: ")},

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier=modifier.height(8.dp))

        OutlinedTextField(
            value=putarina,
            onValueChange = {if(it.all{char ->char.isDigit()}) putarina = it},
            label={Text(text = "Unesi $naziv za putarinu: ")},

            keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier=modifier.height(8.dp))

        datum = DatePickerDocked(Modifier).toString()

        Spacer(modifier=modifier.height(8.dp))

        val gorivoInt = if(gorivo.isNotEmpty()) gorivo.toInt() else 0
        val odrzavanjeInt = if(odrzavanje.isNotEmpty()) odrzavanje.toInt() else 0
        val putarinaInt = if(putarina.isNotEmpty()) putarina.toInt() else 0
        Button(onClick = {



            if(gorivoInt==0 && odrzavanjeInt == 0 && putarinaInt==0) {
                println("Greska! Nepotrebna akcija!\nUnesite barem jedan trosak.")
                PorukaOGresci="Greska! Nepotrebna akcija!\nUnesite barem jedno polje."

            }else if(datum.isEmpty()){
                PorukaOGresci="Morate unijeti datum!"


            }
            else{
                println("Gorivo: $gorivoInt, Odrzavanje: $odrzavanjeInt, Putarina: $putarinaInt")
                PorukaOGresci=""

                    val noviTrosak = Ent(
                        TipKamiona = tipKamiona,
                        Gorivo = gorivoInt,
                        Odrzavanje = odrzavanjeInt,
                        Putarina = putarinaInt,
                        Datum = datum
                    )

                coroutineScope.launch(Dispatchers.IO) {
                    bazaPodataka.dao().insertTrosak(noviTrosak)
                }
                coroutineScope.launch(Dispatchers.IO) {
                    lista_troskova = bazaPodataka.dao().getTroskoviZaKamion(tipKamiona)
                    lista_troskova.forEach {
                        println("Gorivo:${it.Gorivo} | Odrzavanje:${it.Odrzavanje} | Putarina:${it.Putarina} | Datum:${it.Datum} dadadadadaaddada")
                    }
                }








            }

        }){
            Text(text="Dodaj troskove")
        }
        Button(onClick = {
            coroutineScope.launch(Dispatchers.IO) {
                //bazaPodataka.dao().getUkupniTroskoviZaKamion(tipKamiona)
                println("Ukupan trosak '${tipKamiona}':${bazaPodataka.dao().getUkupniTroskoviZaKamion(tipKamiona)}")
            }

        }) { Text(text="Ukupni troskovi za ${tipKamiona}")}

        if(PorukaOGresci.isNotEmpty()){
            Text(
                text = PorukaOGresci,
                color = MaterialTheme.colorScheme.error
            )
        }else {
            Text(text = "Gorivo:${gorivoInt} | Odrzavanje:${odrzavanjeInt} | Putarina:${putarinaInt} | Datum: ${datum} za kamion tipa ${tipKamiona}")


/*
                        LaunchedEffect(tipKamiona) {
                         CoroutineScope(Dispatchers.IO).launch {
                                lista_troskova = bazaPodataka.dao().getTroskoviZaKamion(tipKamiona)
                            }
                        }
                        */




        }

    }

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(modifier: Modifier=Modifier) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
            OutlinedTextField(
                value = selectedDate,
                onValueChange = { },
                readOnly = true,
                trailingIcon = { IconButton( onClick = {showDatePicker = !showDatePicker}){
                    Icon(imageVector = Icons.Default.DateRange,
                        contentDescription = "Unesi datum")
                }
                },
                modifier = Modifier.fillMaxWidth().height(64.dp)
            )


        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}



@Composable
fun PocetniEkran(navController: NavHostController){
    Column(
        modifier=Modifier.fillMaxSize().padding(16.dp)
    ){
        Button(onClick = {
            navController.navigate("UnosTroskovaHladnjace")
        }) {
            Text(text="Hladnjača")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("UnosTroskovaObicog")
        }){
            Text(text = "Šleper")
        }

    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TruckManagementTheme {
        Greeting("Android")
    }
}