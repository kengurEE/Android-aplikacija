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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.room.Entity
import androidx.room.Room
import androidx.room.util.TableInfo
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
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
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
        composable("PrikazTroskova/{tipKamiona}"){
            backStackEntry->val tk = backStackEntry.arguments?.getString("tipKamiona")?:""
            PredstavaTroskova(Modifier,bazaPodataka, tk)
        }
        composable("PrikazTroskovaBrisanje/{tipKamiona}"){
            backStackEntry->val tip = backStackEntry.arguments?.getString("tipKamiona")?:""
            ListaTroskovaZaBrisanje(bazaPodataka,tip)
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

        Spacer(modifier=modifier.height(40.dp))

        Box(
            modifier=Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
        Text(text="Unos troškova za $tipKamiona:", style = MaterialTheme.typography.headlineSmall)
        }
        Spacer(modifier=modifier.height(60.dp))

        OutlinedTextField(
            value = gorivo,
            onValueChange = {if(it.all { char -> char.isDigit() }) gorivo = it},
            label = {Text(text = "Unesi $naziv za gorivo(€): ")},

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Spacer(modifier=modifier.height(8.dp))

        OutlinedTextField(
            value =odrzavanje,
            onValueChange = {if(it.all{char->char.isDigit()}) odrzavanje = it},
            label = {Text(text = "Unesi $naziv za odrzavanje(€): ")},

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier=modifier.height(8.dp))

        OutlinedTextField(
            value=putarina,
            onValueChange = {if(it.all{char ->char.isDigit()}) putarina = it},
            label={Text(text = "Unesi $naziv za putarinu(€): ")},

            keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier=modifier.height(8.dp))

        DatePickerDocked(modifier = Modifier){
            selecteddate->datum=selecteddate
        }

        Spacer(modifier=modifier.height(8.dp))

        val gorivoInt = if(gorivo.isNotEmpty()) gorivo.toInt() else 0
        val odrzavanjeInt = if(odrzavanje.isNotEmpty()) odrzavanje.toInt() else 0
        val putarinaInt = if(putarina.isNotEmpty()) putarina.toInt() else 0
        Button(onClick = {



            if(gorivoInt==0 && odrzavanjeInt == 0 && putarinaInt==0) {
                println("Nepotrebna akcija!\nUnesite barem jedan trošak.")
                PorukaOGresci="Nepotrebna akcija!\nUnesite barem jedan trošak."

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
                }









            }

        }) {
            Box(

                contentAlignment = Alignment.Center
            ){
            Text(text = "Dodaj troškove",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White)
            }
        }

        Spacer(modifier = modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth().height(50.dp).clickable { navController.navigate("PrikazTroskova/$tipKamiona") },
            shape = RoundedCornerShape(12.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Prikaz troškova za $tipKamiona",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = modifier.height(10.dp))

        Card(
            modifier=Modifier.fillMaxWidth().clickable { navController.navigate("PrikazTroskovaBrisanje/${tipKamiona}") },
            shape= RoundedCornerShape(12.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(text="Lista troškova za brisanje", style = MaterialTheme.typography.bodyMedium)
            }
        }

        



        if(PorukaOGresci.isNotEmpty()){
            Text(
                text = PorukaOGresci,
                color = MaterialTheme.colorScheme.error
            )
        }

    }

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(modifier: Modifier=Modifier, selektovan :(String) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    if(selectedDate.isNotEmpty()){
        selektovan(selectedDate)
    }
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
        modifier=Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
    Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(id = R.drawable.volvo_truck),
            contentDescription = "Volvo Truck",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp) // Prilagodi visinu slike po potrebi
                .clip(RoundedCornerShape(12.dp)), // Dodaje zaobljene uglove
            contentScale = ContentScale.Crop // Podesi kako se slika skalira
        )

        //Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Welcome back Radovan Bjelanovic", style = MaterialTheme.typography.bodyLarge
        , fontWeight = FontWeight.W200)

        //Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth().height(150.dp).clickable { navController.navigate("UnosTroskovaHladnjace") },
            shape = RoundedCornerShape(12.dp),
            //elevation = 8.dp,
            //backgroundColor = MaterialTheme.colors.primary

        ){
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                /*
                Icon(
                painter = painterResource(id = R.drawable.ic_truck_cool), // Replace with your icon
                contentDescription = "Hladnjača",
                modifier = Modifier.size(64.dp),
                tint = Color.White
                )
                */
                Text(
                    text = "Hladnjača",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.Black
                )
            }
        }
        /*
        Button(onClick = {
            navController.navigate("UnosTroskovaHladnjace")
        }) {
            Text(text="Hladnjača")
        }
        */
       // Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().height(150.dp).clickable { navController.navigate("UnosTroskovaObicog") },
            shape = RoundedCornerShape(12.dp)

        ){
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Šleper",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.Black)
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun PredstavaTroskova(modifier: Modifier, bazaPodataka: BazaPodataka, tipKamiona: String){
    var trenutna_godina = LocalDate.now().year.toString()
    val coroutineScope= rememberCoroutineScope()
    var lista_troskova by remember { mutableStateOf<List<Ent>>(emptyList()) }
    var ukupan_trosak by remember { mutableStateOf(0) }
    //Decembar:
    var gorivoDecembar by remember { mutableStateOf(0) }
    var odrzavanjeDecembar by remember { mutableStateOf(0) }
    var putarinaDecembar by remember { mutableStateOf(0) }
    var Dukupno by remember { mutableStateOf(0) }
    //Januar
    var Jgorivo by remember { mutableStateOf(0) }
    var Jodrzavanje by remember { mutableStateOf(0) }
    var Jputarina by remember { mutableStateOf(0) }
    var Jukupno by remember { mutableStateOf(0) }
    //Februar
    var Fgorivo by remember { mutableStateOf(0) }
    var Fodrzavanje by remember { mutableStateOf(0) }
    var Fputarina by remember { mutableStateOf(0) }
    var Fukupno by remember { mutableStateOf(0) }
    //Mart
    var Mgorivo by remember { mutableStateOf(0) }
    var Modrzavanje by remember { mutableStateOf(0) }
    var Mputarina by remember { mutableStateOf(0) }
    var Mukupno by remember { mutableStateOf(0) }
    //April
    var Aprilgorivo by remember { mutableStateOf(0) }
    var Aprilodrzavanje by remember { mutableStateOf(0) }
    var Aprilputarina by remember { mutableStateOf(0) }
    var Aprilukupno by remember { mutableStateOf(0) }
    //Maj
    var Majgorivo by remember { mutableStateOf(0) }
    var Majodrzavanje by remember { mutableStateOf(0) }
    var Majputarina by remember { mutableStateOf(0) }
    var Majukupno by remember { mutableStateOf(0) }
    //Jun
    var Jungorivo by remember { mutableStateOf(0) }
    var Junodrzavanje by remember { mutableStateOf(0) }
    var Junputarina by remember { mutableStateOf(0) }
    var Junukupno by remember { mutableStateOf(0) }
    //Jul
    var Julgorivo by remember { mutableStateOf(0) }
    var Julodrzavanje by remember { mutableStateOf(0) }
    var Julputarina by remember { mutableStateOf(0) }
    var Julukupno by remember { mutableStateOf(0) }
    //Avgust
    var Avgustgorivo by remember { mutableStateOf(0) }
    var Avgustodrzavanje by remember { mutableStateOf(0) }
    var Avgustputarina by remember { mutableStateOf(0) }
    var Avgustukupno by remember { mutableStateOf(0) }
    //Septembar
    var Septembargorivo by remember { mutableStateOf(0) }
    var Septembarodrzavanje by remember { mutableStateOf(0) }
    var Septembarputarina by remember { mutableStateOf(0) }
    var Septembarukupno by remember { mutableStateOf(0) }
    //Oktobar
    var Oktobargorivo by remember { mutableStateOf(0) }
    var Oktobarodrzavanje by remember { mutableStateOf(0) }
    var Oktobarputarina by remember { mutableStateOf(0) }
    var Oktobarukupno by remember { mutableStateOf(0) }
    //Novembar
    var Novembargorivo by remember { mutableStateOf(0) }
    var Novembarodrzavanje by remember { mutableStateOf(0) }
    var Novembarputarina by remember { mutableStateOf(0) }
    var Novembarukupno by remember { mutableStateOf(0) }
    var ukupni_troskovi_godina by remember { mutableStateOf(0) }


    LaunchedEffect(tipKamiona) {
        coroutineScope.launch(Dispatchers.IO) {
            ukupan_trosak = bazaPodataka.dao().getUkupniTroskoviZaKamion(tipKamiona)
            ukupni_troskovi_godina = bazaPodataka.dao().UkupniTroskoviGodina(tipKamiona, trenutna_godina)
        }
    }
    //Decembar
    LaunchedEffect(tipKamiona) {
        coroutineScope.launch(Dispatchers.IO) {
            gorivoDecembar = bazaPodataka.dao().GorivoDecembar(tipKamiona)
            odrzavanjeDecembar=bazaPodataka.dao().OdrzavanjeDecembar(tipKamiona,trenutna_godina)
            putarinaDecembar=bazaPodataka.dao().PutarinaDecembar(tipKamiona)
            Dukupno=bazaPodataka.dao().UkupnoDecembar(tipKamiona,trenutna_godina)
        }
    }

    //Januar
    LaunchedEffect(tipKamiona) {
        coroutineScope.launch(Dispatchers.IO) {
            Jgorivo = bazaPodataka.dao().JanuarGorivo(tipKamiona,trenutna_godina)
            Jodrzavanje = bazaPodataka.dao().JanuarOdrzavanje(tipKamiona,trenutna_godina)
            Jputarina = bazaPodataka.dao().JanuarPutarina(tipKamiona,trenutna_godina)
            Jukupno = bazaPodataka.dao().JanuarUkupno(tipKamiona,trenutna_godina)
        }
    }
    //Februar
    LaunchedEffect(tipKamiona) {
        coroutineScope.launch(Dispatchers.IO) {
            Fgorivo = bazaPodataka.dao().FebruarGorivo(tipKamiona,trenutna_godina)
            Fodrzavanje = bazaPodataka.dao().FebruarOdrzavanje(tipKamiona,trenutna_godina)
            Fputarina = bazaPodataka.dao().FebruarPutarina(tipKamiona,trenutna_godina)
            Fukupno = bazaPodataka.dao().FebruarUkupno(tipKamiona,trenutna_godina)
        }
    }
    //Mart
    LaunchedEffect(tipKamiona) {
        coroutineScope.launch(Dispatchers.IO) {
            Mgorivo = bazaPodataka.dao().MartGorivo(tipKamiona,trenutna_godina)
            Modrzavanje = bazaPodataka.dao().MartOdrzavanje(tipKamiona,trenutna_godina)
            Mputarina = bazaPodataka.dao().MartPutarina(tipKamiona,trenutna_godina)
            Mukupno = bazaPodataka.dao().MartUkupno(tipKamiona,trenutna_godina)
        }
    }
    //April
    LaunchedEffect(tipKamiona) {
        coroutineScope.launch(Dispatchers.IO) {
            Aprilgorivo = bazaPodataka.dao().AprilGorivo(tipKamiona,trenutna_godina)
            Aprilodrzavanje = bazaPodataka.dao().Aprildrzavanje(tipKamiona,trenutna_godina)
            Aprilputarina = bazaPodataka.dao().AprilPutarina(tipKamiona,trenutna_godina)
            Aprilukupno = bazaPodataka.dao().AprilUkupno(tipKamiona,trenutna_godina)
        }
    }
    //Maj
    LaunchedEffect(tipKamiona) {
        coroutineScope.launch(Dispatchers.IO) {
            Majgorivo = bazaPodataka.dao().MajGorivo(tipKamiona,trenutna_godina)
            Majodrzavanje = bazaPodataka.dao().Majdrzavanje(tipKamiona,trenutna_godina)
            Majputarina = bazaPodataka.dao().MajPutarina(tipKamiona,trenutna_godina)
            Majukupno = bazaPodataka.dao().MajUkupno(tipKamiona,trenutna_godina)
        }
    }
    //Jun
    LaunchedEffect(tipKamiona) {
        coroutineScope.launch(Dispatchers.IO) {
            Jungorivo = bazaPodataka.dao().JunGorivo(tipKamiona,trenutna_godina)
            Junodrzavanje = bazaPodataka.dao().Jundrzavanje(tipKamiona,trenutna_godina)
            Junputarina = bazaPodataka.dao().JunPutarina(tipKamiona,trenutna_godina)
            Junukupno = bazaPodataka.dao().JunUkupno(tipKamiona,trenutna_godina)

            //Jul
            Julgorivo = bazaPodataka.dao().JulGorivo(tipKamiona,trenutna_godina)
            Julodrzavanje = bazaPodataka.dao().Juldrzavanje(tipKamiona,trenutna_godina)
            Julputarina = bazaPodataka.dao().JulPutarina(tipKamiona,trenutna_godina)
            Julukupno = bazaPodataka.dao().JulUkupno(tipKamiona,trenutna_godina)

            //Avgust
            Avgustgorivo = bazaPodataka.dao().AvgustGorivo(tipKamiona,trenutna_godina)
            Avgustodrzavanje = bazaPodataka.dao().Avgustdrzavanje(tipKamiona,trenutna_godina)
            Avgustputarina = bazaPodataka.dao().AvgustPutarina(tipKamiona,trenutna_godina)
            Avgustukupno = bazaPodataka.dao().AvgustUkupno(tipKamiona,trenutna_godina)

            //Septembar
            Septembargorivo = bazaPodataka.dao().SeptembarGorivo(tipKamiona,trenutna_godina)
            Septembarodrzavanje = bazaPodataka.dao().Septembardrzavanje(tipKamiona,trenutna_godina)
            Septembarputarina = bazaPodataka.dao().SeptembarPutarina(tipKamiona,trenutna_godina)
            Septembarukupno = bazaPodataka.dao().SeptembarUkupno(tipKamiona,trenutna_godina)

            //Oktobar
            Oktobargorivo = bazaPodataka.dao().OktobarGorivo(tipKamiona,trenutna_godina)
            Oktobarodrzavanje = bazaPodataka.dao().Oktobardrzavanje(tipKamiona,trenutna_godina)
            Oktobarputarina = bazaPodataka.dao().OktobarPutarina(tipKamiona,trenutna_godina)
            Oktobarukupno = bazaPodataka.dao().OktobarUkupno(tipKamiona,trenutna_godina)

            //Novembar
            Novembargorivo = bazaPodataka.dao().NovembarGorivo(tipKamiona,trenutna_godina)
            Novembarodrzavanje = bazaPodataka.dao().Novembardrzavanje(tipKamiona,trenutna_godina)
            Novembarputarina = bazaPodataka.dao().NovembarPutarina(tipKamiona,trenutna_godina)
            Novembarukupno = bazaPodataka.dao().NovembarUkupno(tipKamiona,trenutna_godina)
        }
    }



    Column(modifier= Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()))
    {

        Spacer(modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxSize(), // Ispuni ceo ekran
            contentAlignment = Alignment.Center // Poravnaj sadržaj u centru Box-a
        ) {
            Text(text ="$tipKamiona:", style = MaterialTheme.typography.displaySmall)
        }

        Spacer(modifier.height(32.dp))
            Text(text="Ukupan trošak: ${ukupan_trosak}€")

        Spacer(modifier.height(8.dp))
        Text(text="Ukupan trošak za $trenutna_godina godinu: ${ukupni_troskovi_godina}€")

        Spacer(modifier.height(16.dp))
        Ispis(trenutna_godina,Jgorivo,Jodrzavanje,Jputarina,Jukupno,"Januar")

        Spacer(modifier.height(8.dp))
        Ispis(trenutna_godina,Fgorivo,Fodrzavanje,Fputarina,Fukupno,"Februar")

        Spacer(modifier.height(8.dp))
        Ispis(trenutna_godina,Mgorivo,Modrzavanje,Mputarina,Mukupno,"Mart")

        Spacer(modifier.height(8.dp))
        Ispis(trenutna_godina,Aprilgorivo,Aprilodrzavanje,Aprilputarina,Aprilukupno,"April")

        Spacer(modifier.height(8.dp))
        Ispis(trenutna_godina,Majgorivo,Majodrzavanje,Majputarina,Majukupno,"Maj")

        Spacer(modifier.height(8.dp))
        Ispis(trenutna_godina,Jungorivo,Junodrzavanje,Junputarina,Junukupno,"Jun")

        Spacer(modifier.height(8.dp))
        Ispis(trenutna_godina,Julgorivo,Julodrzavanje,Julputarina,Julukupno,"Jul")

        Spacer(modifier.height(8.dp))
        Ispis(trenutna_godina,Avgustgorivo,Avgustodrzavanje,Avgustputarina,Avgustukupno,"Avgust")

        Spacer(modifier.height(8.dp))
        Ispis(trenutna_godina,Septembargorivo,Septembarodrzavanje,Septembarputarina,Septembarukupno,"Septembar")

        Spacer(modifier.height(8.dp))
        Ispis(trenutna_godina,Oktobargorivo,Oktobarodrzavanje,Oktobarputarina,Oktobarukupno,"Oktobar")

        Spacer(modifier.height(8.dp))
        Ispis(trenutna_godina,Novembargorivo,Novembarodrzavanje,Novembarputarina,Novembarukupno,"Novembar")



        Spacer(modifier.height(8.dp))
        Ispis(trenutna_godina,gorivoDecembar,odrzavanjeDecembar,putarinaDecembar,Dukupno,"Decembar")


    }
}

@Composable
fun Ispis(godina:String,gorivo:Int, odrzavanje:Int,putarina:Int,ukupno:Int,mjesec:String){
    Column {
        Text(
            text = "$mjesec $godina:",
            fontWeight = FontWeight.Bold // Bold tekst za januar
        )
        Text(text = "  Gorivo: ${gorivo}€")
        Text(text = "  Održavanje: ${odrzavanje}€")
        Text(text = "  Putarina: ${putarina}€")
        Text(
            text = "  Suma---------------$ukupno€",
            color = Color.Red // Zelena boja za sumu
        )
    }
}

@Composable
fun ListaTroskovaZaBrisanje(bazaPodataka: BazaPodataka, tipKamiona: String){
    val coroutineScope= rememberCoroutineScope()
    var lista by remember { mutableStateOf<List<Ent>>(emptyList()) }


    LaunchedEffect(tipKamiona) {
        coroutineScope.launch(Dispatchers.IO) {
            lista = bazaPodataka.dao().getTroskoviZaKamion(tipKamiona)
        }
    }
        Column(Modifier.fillMaxWidth().padding(32.dp).verticalScroll(rememberScrollState())) {
            lista.forEach {
                Text(text="Id:${it.id}\n  Gorivo=${it.Gorivo}€\n  Održavanje=${it.Odrzavanje}€\n  Putarina=${it.Putarina}€     Datum:${it.Datum}")
                Card(
                    modifier = Modifier.fillMaxSize().clickable {  }
                ){
                    Row(

                    ){
                        Text(text="Obrisi")
                    }
                }
                Text(text="\n")

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