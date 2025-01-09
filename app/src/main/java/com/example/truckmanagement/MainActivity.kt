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
import android.icu.text.IDNA
import android.util.Log
import android.widget.Space
import androidx.collection.mutableObjectIntMapOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.example.truckmanagement.Data.Ent2
import com.example.truckmanagement.Data.EntKilometraza
import com.example.truckmanagement.Data.Entitet
//import com.example.truckmanagement.Data.entitiiiiii
//import com.example.truckmanagement.Data.TruckDatabase
//import com.example.truckmanagement.Data.TroskoviDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
        composable("Dugme/{tipKamiona}/{id}"){
            kamionIid->val tip = kamionIid.arguments?.getString("tipKamiona") ?: ""
            val aut = kamionIid.arguments?.getString("id") ?: ""
            Dugme("trosak",Modifier,navController,tip, bazaPodataka,aut)
        }
        /*
        composable("UnosTroskovaBD-BV257_truck"){
            Dugme("trosak",Modifier,navController,"BD-BV257 truck", bazaPodataka)
        }

         */
        composable("PrikazTroskova/{tipKamiona}"){
            backStackEntry->val tk = backStackEntry.arguments?.getString("tipKamiona")?:""
            PredstavaTroskova(Modifier,bazaPodataka, tk)
        }
        composable("PrikazTroskovaBrisanje/{tipKamiona}/{id_transporta}"){
            backStackEntry->val tip = backStackEntry.arguments?.getString("tipKamiona")?:""
            val id_transporta = backStackEntry.arguments?.getString("id_transporta")?:""
            ListaTroskovaZaBrisanje(bazaPodataka,tip,id_transporta)
        }
        composable("NavigacijaBD-BP688_truck"){
            NavigacioniEkran(bazaPodataka, "Truck BD-BP688", navController )
        }
        composable("NavigacijaBD-BV257_truck"){
            NavigacioniEkran(bazaPodataka,"Truck BD-BV257", navController)
        }
        composable("DodajTransport/{tipKamiona}") {
            backStackEntry->val tip = backStackEntry.arguments?.getString("tipKamiona") ?: ""
            DodajTransport(Modifier, bazaPodataka, tip, navController)
        }
        composable("Rute/{tipKamiona}"){
            backStackEntry->val tip = backStackEntry.arguments?.getString("tipKamiona")?:""
            Rute(Modifier,bazaPodataka,tip,navController)
        }
        composable("PregledRuta/{tipKamiona}"){
            kamion -> val tip = kamion.arguments?.getString("tipKamiona")?:""
            PregledRuta(Modifier,bazaPodataka,tip)
        }
        composable("Servis/{tipKamiona}"){
            tipkamiona -> var tip = tipkamiona.arguments?.getString("tipKamiona")?:""
            Servis(Modifier,bazaPodataka, tip)
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


@Composable
fun NavigacioniEkran(bazaPodataka: BazaPodataka, tipKamiona: String, navController: NavHostController){
    Column(
        Modifier
            .fillMaxSize()
            .padding(30.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
        Text(text="${tipKamiona}", style=MaterialTheme.typography.displayMedium)
        }
        Spacer(Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable { navController.navigate("Rute/$tipKamiona") },
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Aktuelan transport",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable { navController.navigate("DodajTransport/$tipKamiona") },
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
                    text = "Dodaj transport",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable { navController.navigate("PregledRuta/$tipKamiona") },
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
                    text = "Pregled okončanog transporta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable { navController.navigate("PrikazTroskova/$tipKamiona") },
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
                    text = "Prikaz troškova",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Card(
            modifier = Modifier.fillMaxWidth().height(50.dp).clickable {navController.navigate("Servis/$tipKamiona")}
        ){
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(text="Servis", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DodajTransport(modifier: Modifier,bazaPodataka: BazaPodataka, tipKamiona: String,navController: NavHostController){
    var mjesto_utovara by remember{ mutableStateOf("") }
    var mjesto_polaska by remember{ mutableStateOf("") }
    var datum_utovara by remember{ mutableStateOf("") }
    var datum_polaska by remember{ mutableStateOf("") }
    var km_utovara by remember{ mutableStateOf("") }
    var km_polaska by remember{ mutableStateOf("") }
    var usluge_prevoza by remember { mutableStateOf("") }
    var vrsta_robe by remember { mutableStateOf("") }
    var teret_robe_kg by remember { mutableStateOf("") }
    var mjesto_istovara by remember { mutableStateOf("") }
    var porukagreske by remember { mutableStateOf("pravo") }
    val coroutineScope= rememberCoroutineScope()
    var lista by remember { mutableStateOf<List<Ent2>>(emptyList()) }





Column(modifier = Modifier
    .fillMaxSize()
    .verticalScroll(rememberScrollState())) {
Card(
    modifier = Modifier.padding(10.dp),
    colors = CardDefaults.cardColors(
        containerColor = Color.White// Postavljanje pozadine na belu
       )

)
{
    Spacer(modifier.height(50.dp))
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Unesite transport", style = MaterialTheme.typography.displaySmall)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),

        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = mjesto_polaska,
            onValueChange = { novomjesto -> mjesto_polaska = novomjesto },
            label = { Text(text = "Mjesto polaska") }
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    Box(
        modifier = Modifier.fillMaxWidth(),

        contentAlignment = Alignment.Center
    )
    {
        OutlinedTextField(
            value = km_polaska,
            onValueChange = { novakm ->
                if (novakm.all { it.isDigit() }) {
                    km_polaska = novakm
                }
            },
            label = { Text(text = "Kilometraža") }
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
    Text(text = "Datum polaska:")
    Box(
        modifier = Modifier.fillMaxWidth(),

        contentAlignment = Alignment.Center
    )
    {
        DatePickerDocked(modifier = Modifier) { selektovan ->
            datum_polaska = selektovan
        }

    }
    Spacer(modifier = Modifier.height(5.dp))

}


Card(
    modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
    colors = CardDefaults.cardColors(
        containerColor = Color.White // Postavljanje pozadine na belu
    )
) {
    OutlinedTextField(
        value = mjesto_utovara,
        onValueChange = { novomjesto -> mjesto_utovara = novomjesto },
        label = { Text(text = "Mjesto utovara") }
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = km_utovara,
        onValueChange = { novakm ->
            if (novakm.all { it.isDigit() }) {
                km_utovara = novakm
            }
        },
        label = { Text(text = "Kilometraža na utovaru") }
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = usluge_prevoza,
        onValueChange = { u -> usluge_prevoza = u },
        label = { Text(text = "Usluga prevoza(firma,grad/drzava") }
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = vrsta_robe,
        onValueChange = { u -> vrsta_robe = u },
        label = { Text(text = "Vrsta robe") }
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = teret_robe_kg,
        onValueChange = { u ->
            if (u.all { it.isDigit() }) {
                teret_robe_kg = u
            }
        },
        label = { Text(text = "Teret u kg") }
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = mjesto_istovara,
        onValueChange = { u -> mjesto_istovara = u },
        label = { Text(text = "Mjesto istovara") }
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = "Datum utovara:")
    DatePickerDocked(modifier = Modifier) { selektovan ->
        datum_utovara = selektovan
    }
}

Card(
    modifier = Modifier
        .fillMaxWidth()
        .padding(50.dp)
        .clickable {
            if (mjesto_polaska.isNotEmpty() && km_polaska.isNotEmpty() &&
                datum_polaska.isNotEmpty() && mjesto_utovara.isNotEmpty() &&
                km_utovara.isNotEmpty() && vrsta_robe.isNotEmpty() && teret_robe_kg.isNotEmpty() &&
                usluge_prevoza.isNotEmpty() && mjesto_istovara.isNotEmpty()
            ) {
                porukagreske = ""

                val Transport = Ent2(
                    TipKamiona = tipKamiona,
                    Mjesto_Istovara = mjesto_istovara,
                    Mjesto_Utovara = mjesto_utovara,
                    Km_Utovar = km_utovara.toInt(),
                    Km_Polaska = km_polaska.toInt(),
                    Usluge_Prevoza_ZaIz = usluge_prevoza,
                    Vrsta_Robe = vrsta_robe,
                    Teret_Robe_Kg = teret_robe_kg.toInt(),
                    Datum_Polaska = datum_polaska,
                    Mjesto_Polaska = mjesto_polaska,
                    Zavrsi = false,
                    Datum_Utovara = datum_utovara
                )

                coroutineScope.launch(Dispatchers.IO) {
                    bazaPodataka
                        .dao2transport()
                        .InsertTransport(Transport)
                }
                coroutineScope.launch(Dispatchers.IO) {
                    lista = bazaPodataka
                        .dao2transport()
                        .TransportPodaci(tipKamiona)
                }
                lista.forEach {
                    println(
                        "Mjesto polaska:${it.Mjesto_Polaska}   Kilometraza polaska:${it.Km_Polaska}\\n\" +\n" +
                                "                        \"Mjesto utovara:${it.Mjesto_Utovara}   Kilometraza na utovaru:${it.Km_Utovar}\\n\" +\n" +
                                "                        \"Usluge prevoza:${it.Usluge_Prevoza_ZaIz}\\n\" +\n" +
                                "                        \"Vrsta robe:${it.Vrsta_Robe}   Teret u kg:${it.Teret_Robe_Kg}\\n\" +\n" +
                                "                        \"Datum:${it.Datum_Utovara}   Mjesto istovara:${it.Mjesto_Istovara}\""
                    )
                }
            } else {
                porukagreske = "Popunite svako polje!"
            }
        }
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        Text(text = "Dodaj transport", style = MaterialTheme.typography.bodyMedium)

    }
}
if(porukagreske.isNotEmpty() && porukagreske!="pravo"){
    Text(text=porukagreske)
}else if(porukagreske.isEmpty()){
    Text(text="Uspjesno dodat transport!")
}
}

}

@Composable
fun Rute(modifier: Modifier,bazaPodataka: BazaPodataka,tipKamiona: String, navController: NavHostController){
    var lista by remember { mutableStateOf<List<Ent2>>(emptyList()) }
    val coroutineScope= rememberCoroutineScope()
    var StringZaNavigaciju by remember { mutableStateOf("") }
    var kliknutoZavrsi by remember { mutableStateOf(false) }
    var Km_izlazna by remember { mutableStateOf("") }

    LaunchedEffect(tipKamiona) {
        coroutineScope.launch(Dispatchers.IO) {
            lista = bazaPodataka.dao2transport().AktuelneRute(tipKamiona)
        }
    }

    if(tipKamiona == "Truck BD-BP688"){
        StringZaNavigaciju = "NavigacijaBD-BP688_truck"
    }else if(tipKamiona == "Truck: BD-BV257"){
        StringZaNavigaciju = "NavigacijaBD-BV257_truck"
    }else{
        StringZaNavigaciju="home"
    }

    Column(modifier = Modifier.fillMaxWidth().padding(20.dp).verticalScroll(rememberScrollState()))
    {

            lista.forEach {

                    Spacer(modifier.height(10.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${it.Mjesto_Utovara} - ${it.Mjesto_Istovara}\n" +
                                    "Datum utovara: ${it.Datum_Utovara}\n" +
                                    "Usluge prevoza: ${it.Usluge_Prevoza_ZaIz}\n" +
                                    "Vrsta robe: ${it.Vrsta_Robe}\n"
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("Dugme/$tipKamiona/${it.id}") }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Dodaj troškove")
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                it.Zavrsi = true
                                kliknutoZavrsi = true
                            }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Kraj transporta")
                        }
                    }







                    Text(text = "\n")

                    if (kliknutoZavrsi == true && it.Zavrsi==true) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp).clickable {
                                    it.Km_Istovar = Km_izlazna.toInt()
                                    coroutineScope.launch(Dispatchers.IO) {
                                        bazaPodataka.dao2transport().update(it)
                                    }
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Unesite kilometrazu na istovaru:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                TextField(
                                    value = Km_izlazna,
                                    onValueChange = { izlazna ->
                                        if (izlazna.all() { it.isDigit() }) Km_izlazna = izlazna
                                    },
                                    label = { Text("Tekst polje") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
            }
/*
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .clickable { navController.navigate(StringZaNavigaciju) }
        ) {
            Box(
                modifier=Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Vrati se na Navigaciju", color = Color.White)
            }
        }

 */




    }


}

@Composable
fun PregledRuta(modifier: Modifier, bazaPodataka: BazaPodataka, tipKamiona: String){
    val courutineScope = rememberCoroutineScope()
    var lista_zavrsenih_ruta by remember { mutableStateOf<List<Ent2>>(emptyList()) }



    LaunchedEffect(tipKamiona) {
        courutineScope.launch(Dispatchers.IO) {
            lista_zavrsenih_ruta = bazaPodataka.dao2transport().ZavrsenTransport(tipKamiona)
        }
    }

    Column(modifier=Modifier.fillMaxSize().padding(20.dp).height(100.dp).verticalScroll(
        rememberScrollState()
    )) {
        lista_zavrsenih_ruta.forEach {
            var troskovi by remember{ mutableStateOf(0) }
            var id by remember{ mutableStateOf(0) }

            id = it.id
            LaunchedEffect(id,tipKamiona) {
                courutineScope.launch(Dispatchers.IO) {
                    troskovi = bazaPodataka.dao().TroskoviPuta(tipKamiona, id)
                }
            }

            Spacer(modifier.height(10.dp))
            Box(
                modifier=Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${it.Mjesto_Polaska} - ${it.Mjesto_Utovara} - ${it.Mjesto_Istovara}\n" +
                            "Usluge prevoza za: ${it.Usluge_Prevoza_ZaIz}.\n" +
                            "Datum polaska: ${it.Datum_Polaska}\n" +
                            "Datum utovara: ${it.Datum_Utovara}\n" +
                            "Vrsta robe: ${it.Vrsta_Robe}.\n" +
                            "Teret: ${it.Teret_Robe_Kg} kg\n" +
                            "Kilometraža na utovaru: ${it.Km_Utovar} km\n" +
                            "Kilometraža na istovaru: ${it.Km_Istovar} km\n" +
                            "Troškovi prevoza: ${troskovi} €\n"
                )
            }
        }
    }



}

@Composable
fun Servis(modifier: Modifier,bazaPodataka: BazaPodataka, tipKamiona: String){

    var kilometrazaMalogServisa by remember { mutableStateOf(50000) }
    var kilometrazaVelikogServisa by remember { mutableStateOf(130000) }
    var trenutnaKilometraza by remember { mutableStateOf("") }
    var PreostaloKilometaraDoMalog by remember { mutableStateOf(0) }
    var PreostaloKilometaraDoVelikog by remember { mutableStateOf(0) }

    var KilometrazaPoslednjeOdradjenogMalogServisa by remember { mutableStateOf("") }
    var OdradjeMaliKM by remember { mutableStateOf(0) }
    var OdradjenVelikiKM by remember { mutableStateOf(0) }
    var KilometrazaPoslednjeOdradjenogVelikogServisa by remember { mutableStateOf("") }
    var corutine = rememberCoroutineScope()
    var PorukaGreske by remember { mutableStateOf("") }
    var PorukaGreskeMali by remember { mutableStateOf("") }
    var PorukaGreskeVeliki by remember { mutableStateOf("") }

        //KilometrazaPoslednjeOdradjenogServisa = 880 000
    // trenutna 925 ---------> naredni mali na 930 000



    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        OutlinedTextField(
            value = trenutnaKilometraza,
            onValueChange = { novaKm ->
                if (novaKm.all { it.isDigit() }) {
                    trenutnaKilometraza = novaKm
                }
            },
            label = { Text(text = "Unesite trenutnu kilometrazu") }
        )
        val trenutna = if(trenutnaKilometraza.isNotEmpty()) trenutnaKilometraza.toInt() else 0
LaunchedEffect(tipKamiona) {
        corutine.launch(Dispatchers.IO) {
            OdradjenVelikiKM = bazaPodataka.daoKilometraza().PoslednjiOdradjenServis("Veliki")
            OdradjeMaliKM = bazaPodataka.daoKilometraza().PoslednjiOdradjenServis("Mali")
        }
}

        Button(onClick = {
            corutine.launch(Dispatchers.IO) {
                OdradjenVelikiKM = bazaPodataka.daoKilometraza().PoslednjiOdradjenServis("Veliki")
                OdradjeMaliKM = bazaPodataka.daoKilometraza().PoslednjiOdradjenServis("Mali")
            }

            PreostaloKilometaraDoMalog =
                kilometrazaMalogServisa - (trenutna - OdradjeMaliKM) % kilometrazaMalogServisa
            PreostaloKilometaraDoVelikog =
                kilometrazaVelikogServisa - (trenutna - OdradjenVelikiKM)% kilometrazaVelikogServisa



            if(trenutnaKilometraza.isEmpty()) {
                PorukaGreske = "Unesite trenutnu kilometrazu!"
            }else if(trenutna<OdradjeMaliKM && trenutna <OdradjenVelikiKM ){
                PorukaGreske="Unesite validnu kilometrazu\nMali servis odradjen na: $OdradjeMaliKM km.\n Veliki servis odradjen na: $OdradjenVelikiKM km.)"
                PorukaGreskeMali = ""
                PorukaGreskeVeliki=""
            }
            else if(trenutna<OdradjeMaliKM && trenutna>=OdradjenVelikiKM+kilometrazaVelikogServisa){
                PorukaGreske="Unesite validnu kilometrazu(Mali servis odradjen na: $OdradjeMaliKM km.)"
                PorukaGreskeMali = ""
                PorukaGreskeVeliki="Uradite Veliki Servis, prethodni odradjen na: $OdradjenVelikiKM km."
            }else if(trenutna <OdradjenVelikiKM && trenutna>=OdradjeMaliKM+kilometrazaMalogServisa){
                PorukaGreske="Unesite validnu kilometrazu(Veliki servis odradjen na $OdradjenVelikiKM km.)"
                PorukaGreskeMali = "Uradite Mali Servis, prethodni odradjen na: $OdradjeMaliKM km."
                PorukaGreskeVeliki=""
            }
            else if(trenutna >= OdradjeMaliKM+kilometrazaMalogServisa && trenutna<OdradjenVelikiKM+kilometrazaVelikogServisa){
                PorukaGreske=""
                PorukaGreskeVeliki=""
                PorukaGreskeMali="Uradite mali servis, prethodni odradjen na: $OdradjeMaliKM km."
            }else if(trenutna >= OdradjenVelikiKM+kilometrazaVelikogServisa && trenutna < OdradjeMaliKM+kilometrazaMalogServisa ){
                PorukaGreske=""
                PorukaGreskeVeliki="Uradite veliki servis, prethodni odradjen na: $OdradjenVelikiKM km."
                PorukaGreskeMali=""
            }else if(trenutna >= OdradjeMaliKM+kilometrazaMalogServisa && trenutna >= OdradjenVelikiKM+kilometrazaVelikogServisa){
                PorukaGreske="Istekli i veliki i mali, prethodni odradjeni:\nMali: $OdradjeMaliKM km.\nVeliki: $OdradjenVelikiKM km."
                PorukaGreskeMali = ""
                PorukaGreskeVeliki=""
            }else if(trenutna<OdradjeMaliKM ){
                PorukaGreske="Unesite validnu kilometrazu(Mali servis)"
                PorukaGreskeMali = ""
                PorukaGreskeVeliki=""
            }else if(trenutna<OdradjenVelikiKM){
                PorukaGreske="Unesite validnu kilometrazu(Veliki servis)"
                PorukaGreskeMali = ""
                PorukaGreskeVeliki=""
            }
            else{
                PorukaGreske = ""
                PorukaGreskeMali = ""
                PorukaGreskeVeliki=""
            }
        }) { Text(text = "Unesi") }

        if(PorukaGreske.isNotEmpty() && PorukaGreskeVeliki.isEmpty() && PorukaGreskeMali.isEmpty()){
            if(PorukaGreske=="Unesite validnu kilometrazu(Mali servis, Veliki servis)"){
                Text(text = "Do narednog malog servisa je preostalo: 0 km")
                Text(text = "Do narednog velikog servisa je preostalo: 0 km")
            }else if(PorukaGreske=="Unesite validnu kilometrazu(Mali servis)"){
                Text(text = "Do narednog velikog servisa je preostalo: $PreostaloKilometaraDoVelikog km")
            }else if(PorukaGreske=="Unesite validnu kilometrazu(Veliki servis)"){
                Text(text = "Do narednog malog servisa je preostalo: $PreostaloKilometaraDoMalog km")
            }else if(PorukaGreske=="Istekli i veliki i mali"){
                Text(text = "Do narednog malog servisa je preostalo: 0 km")
                Text(text = "Do narednog velikog servisa je preostalo: 0 km")
            }


        }else if(PorukaGreske.isEmpty() && PorukaGreskeVeliki.isEmpty() && PorukaGreskeMali.isNotEmpty()){
            Text(text = "Do narednog malog servisa je preostalo: 0 km")
            Text(text = "Do narednog velikog servisa je preostalo: $PreostaloKilometaraDoVelikog km")
        }else if(PorukaGreske.isEmpty() && PorukaGreskeVeliki.isNotEmpty() && PorukaGreskeMali.isEmpty()){
            Text(text = "Do narednog malog servisa je preostalo: $PreostaloKilometaraDoMalog km")
            Text(text = "Do narednog velikog servisa je preostalo: 0 km")
        }else if(PorukaGreske.isNotEmpty() && PorukaGreskeMali.isNotEmpty()){
            Text(text = "Uraditi mali servis | Ova kilometraza nije validna za veliki servis")
        }else if(PorukaGreske.isNotEmpty() && PorukaGreskeVeliki.isNotEmpty()){
            Text(text = "Uraditi veliki servis | Ova kilometraza nije validna za mali servis")
        }else{
            Text(text = "Do narednog malog servisa je preostalo: $PreostaloKilometaraDoMalog km")
            Text(text = "Do narednog velikog servisa je preostalo: $PreostaloKilometaraDoVelikog km")

        }




        OutlinedTextField(
            value = KilometrazaPoslednjeOdradjenogMalogServisa,
            onValueChange = { novaKm ->
                if (novaKm.all { it.isDigit() }) {
                    KilometrazaPoslednjeOdradjenogMalogServisa = novaKm
                }
            },
            label = { Text(text = "Kilometraza poslednjeg malog servisa", style = MaterialTheme.typography.bodyMedium)}
        )
        OutlinedTextField(
            value = KilometrazaPoslednjeOdradjenogVelikogServisa,
            onValueChange = { novaKm ->
                if (novaKm.all { it.isDigit() }) {
                    KilometrazaPoslednjeOdradjenogVelikogServisa = novaKm
                }
            },
            label = { Text(text = "Kilometraza poslednjeg velikog servisa", style = MaterialTheme.typography.bodyMedium)}
        )
        Button(onClick = {
            val KM_mali = EntKilometraza(
                Kilometraza = KilometrazaPoslednjeOdradjenogMalogServisa.toInt(),
                TipKilometraze = "Mali",
            )

            corutine.launch(Dispatchers.IO) {
                bazaPodataka.daoKilometraza().Insert(KM_mali)
            }
            corutine.launch(Dispatchers.IO) {
                OdradjeMaliKM = bazaPodataka.daoKilometraza().PoslednjiOdradjenServis("Mali")
            }

        }) { Text(text = "Unesite kilometrazu malog") }
        Spacer(modifier.height(10.dp) )
        Button(onClick = {
            val KM_veliki = EntKilometraza(
                Kilometraza = KilometrazaPoslednjeOdradjenogVelikogServisa.toInt(),
                TipKilometraze = "Veliki",
            )

            corutine.launch(Dispatchers.IO) {
                bazaPodataka.daoKilometraza().Insert(KM_veliki)
            }
            corutine.launch(Dispatchers.IO) {
                OdradjenVelikiKM = bazaPodataka.daoKilometraza().PoslednjiOdradjenServis("Veliki")
            }

        }) { Text(text = "Unesite kilometrazu velikog") }

        if(PorukaGreske.isNotEmpty()){

            Text(text = PorukaGreske, style = MaterialTheme.typography.bodyMedium, color = Color.Red)

        }
        if(PorukaGreske.isEmpty() && PorukaGreskeVeliki.isEmpty() && PorukaGreskeMali.isNotEmpty()){

            Text(text = PorukaGreskeMali, style = MaterialTheme.typography.bodyMedium, color = Color.Red)

        }
        if(PorukaGreske.isEmpty() && PorukaGreskeVeliki.isNotEmpty() && PorukaGreskeMali.isEmpty()){

            Text(text = PorukaGreskeVeliki, style = MaterialTheme.typography.bodyMedium, color = Color.Red)

        }
        if(PorukaGreske.isNotEmpty() && PorukaGreskeMali.isNotEmpty()){
            Text(text = PorukaGreske, style = MaterialTheme.typography.bodyMedium, color = Color.Red)
            Text(text = PorukaGreskeMali, style = MaterialTheme.typography.bodyMedium, color = Color.Red)
        }
        if(PorukaGreske.isNotEmpty() && PorukaGreskeVeliki.isNotEmpty()){
            Text(text = PorukaGreske, style = MaterialTheme.typography.bodyMedium, color = Color.Red)
            Text(text = PorukaGreskeVeliki, style = MaterialTheme.typography.bodyMedium, color = Color.Red)
        }
        
    }
}



@SuppressLint("NewApi")
@Composable
fun Dugme(naziv: String, modifier: Modifier = Modifier, navController: NavHostController,tipKamiona:String, bazaPodataka: BazaPodataka, iD:String){
var gorivo by remember { mutableStateOf("") }
var odrzavanje by remember { mutableStateOf("") }
var putarina by remember { mutableStateOf("") }
var datum by remember { mutableStateOf("") }
var PorukaOGresci by remember { mutableStateOf("puna") }
var lista_troskova by remember { mutableStateOf<List<Ent>>(emptyList()) }
var ukupan_trosak by remember { mutableStateOf(Int) }
val coroutineScope= rememberCoroutineScope()

    Column(modifier = modifier.padding(16.dp)){

        Spacer(modifier=modifier.height(40.dp))

        Box(
            modifier=Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
        Text(text="Unos troškova za transport ID:$iD\n",style = MaterialTheme.typography.headlineSmall)
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

        var datum_LocalDate by remember { mutableStateOf(LocalDate.now()) }

        Button(onClick = {

            if(datum.isNotEmpty()) {
                 datum_LocalDate = LocalDate.parse(datum, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    // uvijek koristiti LocalDate za rad sa poredjenjem datuma
            }

            if(gorivoInt==0 && odrzavanjeInt == 0 && putarinaInt==0) {
                println("Nepotrebna akcija!\nUnesite barem jedan trošak.")
                PorukaOGresci="Nepotrebna akcija!\nUnesite barem jedan trošak."

            }else if(datum.isEmpty()){
                PorukaOGresci="Morate unijeti datum!"

            }else if(datum_LocalDate>LocalDate.now()){
                PorukaOGresci="Unesite validan datum!"
            }
            else{

                PorukaOGresci=""

                    val noviTrosak = Ent(
                        TipKamiona = tipKamiona,
                        Gorivo = gorivoInt,
                        Odrzavanje = odrzavanjeInt,
                        Putarina = putarinaInt,
                        Datum = datum,
                        Id_Transporta = iD.toInt()
                    )

                coroutineScope.launch(Dispatchers.IO) {
                    bazaPodataka.dao().insertTrosak(noviTrosak)
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
            modifier= Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("PrikazTroskovaBrisanje/${tipKamiona}/${iD}") },
            shape= RoundedCornerShape(12.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(text="Obrišite unos", style = MaterialTheme.typography.bodyMedium, color = Color.Black
                )
            }
        }





        if(PorukaOGresci.isNotEmpty() && PorukaOGresci!="puna"){
            Text(
                text = PorukaOGresci,
                color = MaterialTheme.colorScheme.error
            )
        }else if(PorukaOGresci.isEmpty()){
            Text(
                text = "Uspješan unos!",
                color = Color.Green
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
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
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
val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
return formatter.format(Date(millis))
}



@Composable
fun PocetniEkran(navController: NavHostController){
Column(
modifier= Modifier
    .fillMaxSize()
    .padding(24.dp),
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
    modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .clickable { navController.navigate("NavigacijaBD-BP688_truck") },
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
            text = "BD-BP688 truck",
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
    modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .clickable { navController.navigate("NavigacijaBD-BV257_truck") },
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
            text = "BD-BV257 truck",
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
    gorivoDecembar = bazaPodataka.dao().GorivoDecembar(tipKamiona, trenutna_godina)
    odrzavanjeDecembar=bazaPodataka.dao().OdrzavanjeDecembar(tipKamiona,trenutna_godina)
    putarinaDecembar=bazaPodataka.dao().PutarinaDecembar(tipKamiona, trenutna_godina)
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



Column(modifier= Modifier
    .fillMaxSize()
    .padding(16.dp)
    .verticalScroll(rememberScrollState()))
{

Spacer(modifier.height(32.dp))

Box(
    modifier = Modifier
        .fillMaxSize(), // Ispuni ceo ekran
    contentAlignment = Alignment.Center // Poravnaj sadržaj u centru Box-a
) {
    Text(text ="Prikaz troškova", style = MaterialTheme.typography.displaySmall)
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
    Spacer(modifier.height(20.dp))

}
}

@Composable
fun Ispis(godina:String,gorivo:Int, odrzavanje:Int,putarina:Int,ukupno:Int,mjesec:String){
Column {
Text(
    text = "$mjesec $godina:",
    fontWeight = FontWeight.Bold // Bold tekst za januar
)
Text(text = "  Gorivo: ${gorivo} €")
Text(text = "  Održavanje: ${odrzavanje} €")
Text(text = "  Putarina: ${putarina} €")
Text(
    text = "  Suma---------------$ukupno €",
    color = Color.Red // Zelena boja za sumu
)
}
}

@Composable
fun ListaTroskovaZaBrisanje(bazaPodataka: BazaPodataka, tipKamiona: String, iD: String){
val coroutineScope= rememberCoroutineScope()
var lista by remember { mutableStateOf<List<Ent>>(emptyList()) }


LaunchedEffect(tipKamiona) {
coroutineScope.launch(Dispatchers.IO) {
    lista = bazaPodataka.dao().getTroskoviZaKamion(tipKamiona, iD)
}
}
Column(
    Modifier
        .fillMaxWidth()
        .padding(32.dp)
        .verticalScroll(rememberScrollState())) {
    lista.forEach {
        Text(text="Id_transporta:${it.Id_Transporta}, Id:${it.id_troskova}\n  Gorivo=${it.Gorivo}€\n  Održavanje=${it.Odrzavanje}€\n  Putarina=${it.Putarina}€     Datum:${it.Datum}")
        val entitet = Ent(TipKamiona = it.TipKamiona, Gorivo = it.Gorivo, Odrzavanje = it.Odrzavanje, Putarina = it.Putarina, Datum=it.Datum, id_troskova =it.id_troskova, Id_Transporta = it.Id_Transporta)
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    coroutineScope.launch(Dispatchers.IO) {
                        bazaPodataka
                            .dao()
                            .deleteTrosak(entitet)
                        lista = bazaPodataka
                            .dao()
                            .getTroskoviZaKamion(tipKamiona, iD)
                    }
                }

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