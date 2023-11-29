package com.cc221002.mcfinalprojectmanuelprammer.ui.view

import android.os.Build
import android.window.SplashScreen
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cc221002.mcfinalprojectmanuelprammer.R
import com.cc221002.mcfinalprojectmanuelprammer.data.model.SingleTrip
import com.cc221002.mcfinalprojectmanuelprammer.ui.theme.backgroundGreen
import com.cc221002.mcfinalprojectmanuelprammer.ui.theme.backgroundWhite
import com.cc221002.mcfinalprojectmanuelprammer.ui.view_model.MainViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate

sealed class Screen(val route: String) {
    object First: Screen("first")
    object Second: Screen("second")
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val loadingFinished = remember { mutableStateOf(false) }

    // Introduce a 2-second delay to simulate loading
    LaunchedEffect(Unit) {
        delay(2000)  // Delay for 2 seconds
        loadingFinished.value = true
    }
    if (loadingFinished.value) {
        Scaffold(
//            bottomBar = { BottomNavigationBar(navController, state.value.selectedScreen) }
        ) {
            NavHost(
                navController = navController,
                modifier = Modifier.padding(it),
                startDestination = Screen.First.route
            ) {
                composable(Screen.First.route) {
                    mainViewModel.selectScreen(Screen.First)
                    mainViewModel.getTrips()
                    showTrips(mainViewModel, navController)
                }

                composable(Screen.Second.route) {
                    mainViewModel.selectScreen(Screen.Second)
                    addingPage(mainViewModel,navController)

                }
            }
        }
    }
    else {
        SplashScreen()
    }

}

@Composable
fun BottomNavigationBar(navController: NavHostController, selectedScreen: Screen){
    BottomNavigation(
        backgroundColor = Color.Gray
    ) {
        NavigationBarItem(
            selected = (selectedScreen == Screen.First),
            onClick = { navController.navigate(Screen.First.route) },
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription ="" ) })

        NavigationBarItem(
            selected = (selectedScreen == Screen.Second),
            onClick = { navController.navigate(Screen.Second.route) },
            icon = { Icon(imageVector = Icons.Default.Add, contentDescription ="" ) })

    }
}


@Composable
fun showTrips(mainViewModel: MainViewModel,navController: NavHostController) {
    val trips = mainViewModel.trips.collectAsState()
    val state = mainViewModel.mainViewState.collectAsState()

    if(!state.value.openTripDialog){
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGreen)
    ) {
        item {
            Text(
                text = "Manuel Prammer",
                fontWeight = FontWeight.Bold,
                fontSize = 50.sp,
                style = TextStyle(fontFamily = FontFamily.SansSerif),
                color = Color.Black

            )
        }
        items(trips.value) { trip ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundWhite)
                    .height(200.dp)
                    .clickable { mainViewModel.showTripWithID(trip.id) },
                horizontalArrangement = Arrangement.Center
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .border(1.dp, Color.Black, RectangleShape)

                        .background(backgroundWhite),
//                        .clickable { mainViewModel.showTripWithID(trip.id) },
//                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Trip to:",
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        style = TextStyle(fontFamily = FontFamily.Monospace),
                        color = Color.Black,
                        modifier = Modifier
                            .padding(15.dp)
                            .width(250.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .border(1.dp, Color.Black, RectangleShape)

                        .background(backgroundWhite),
//                        .clickable { mainViewModel.showTripWithID(trip.id) },
//                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = trip.location,
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        style = TextStyle(fontFamily = FontFamily.Monospace),
                        color = Color.Black,
                        modifier = Modifier
                            .padding(15.dp)
                            .width(250.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .border(1.dp, Color.Black, RectangleShape)

                        .background(backgroundWhite),
//                        .clickable { mainViewModel.showTripWithID(trip.id) },
//                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = trip.date,
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        style = TextStyle(fontFamily = FontFamily.Monospace),
                        color = Color.Black,
                        modifier = Modifier
                            .padding(15.dp)
                            .width(250.dp)
                    )
                }
            }
        }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()){
        Box(modifier = Modifier
            .padding(50.dp)
            .width(50.dp)
            .height(50.dp)
            .background(color = Color.Green)
            .align(Alignment.BottomEnd)
            .clickable { navController.navigate(Screen.Second.route) }
    ){}
    }
    if (state.value.openTripDialog) {
        showSingleTripModal(mainViewModel, navController)
    }
}

@Composable
fun showSingleTripModal(mainViewModel: MainViewModel, navController: NavHostController) {
    val tripsState = mainViewModel.trips.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
    ) {
        items(tripsState.value) { trip ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
//                    .clickable { mainViewModel.editJoke(joke) },
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${trip.date}",
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    style = TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                    color = Color.White,
                    modifier = Modifier
                        .border(1.dp, Color.Yellow, RectangleShape)
                        .padding(15.dp)
                        .width(250.dp)
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(modifier = Modifier
            .padding(50.dp)
            .width(50.dp)
            .height(50.dp)
            .background(color = Color.Green)
            .align(Alignment.BottomCenter)
            .clickable { mainViewModel.dismissSingleTripModal() }
        ) {}
    }
}
    @Composable
    fun SplashScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = R.drawable.splashscreen),
                contentDescription = "Splashscreen",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var tempDate by remember { mutableStateOf(selectedDate ?: LocalDate.now().toEpochDay()) }

Row(
    modifier = Modifier
        .clickable { expanded = true }
) {
    OutlinedTextField(
        value = selectedDate?.toString() ?: "",
        onValueChange = { },
        label = { Text(text = "Date") },
        readOnly = true,
//        modifier = Modifier.clickable { expanded = true }
    )



    if (expanded) {
        Column {

            DatePicker(
                state = rememberDatePickerState(tempDate as Long),
                modifier = Modifier,
                dateFormatter = DatePickerDefaults.dateFormatter(),
                title = null,
                headline = null,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onDateSelected(LocalDate.ofEpochDay(tempDate as Long))

                    expanded = false
                }
            ) {
                Text("OK")
            }

        }
    }
}
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun addingPage(mainViewModel: MainViewModel,navController: NavHostController){
    var location by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    var details by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    var rating by rememberSaveable (stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }

    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.DarkGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ADD NEW TRIP",
            fontSize = 50.sp,
            style = TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Cursive),
            color = Color.White
        )
        
        TextField(
            modifier = Modifier.padding(top = 20.dp),
            value = location,
            onValueChange = {
                newText -> location = newText
            },
            label = {
                Text(text = "Location")
            }
        )
        DatePickerField(
            selectedDate = selectedDate,
            onDateSelected = {
                selectedDate = it
            }
        )
//        TextField(
//            modifier = Modifier
//                .padding(top = 20.dp),
//            value = date,
//            onValueChange = {
//                    newText -> date = newText
//            },
//            label = {
//                Text(text = "Date")
//            },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//        )
        TextField(
            modifier = Modifier.padding(top = 20.dp),
            value = details,
            onValueChange = {
                    newText -> details = newText
            },
            label = {
                Text(text = "Details")
            }
        )
        TextField(
            modifier = Modifier.padding(top = 20.dp),
            value = rating,
            onValueChange = {
                    newText -> rating = newText
            },
            label = {
                Text(text = "Rating in Number")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Button(
            onClick = { mainViewModel.saveButton(SingleTrip(location.text, selectedDate.toString(), /*date.text,*/ details.text, rating.text)); navController.navigate(Screen.First.route)},
            modifier = Modifier.padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(Color.Yellow),

            ) {
            Text(text = "Save", fontSize = 20.sp)
        }
    }
}
