package com.cc221002.mcfinalprojectmanuelprammer.ui.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.cc221002.mcfinalprojectmanuelprammer.R
import com.cc221002.mcfinalprojectmanuelprammer.data.model.SingleTrip
import com.cc221002.mcfinalprojectmanuelprammer.ui.CameraViewModel
import com.cc221002.mcfinalprojectmanuelprammer.ui.theme.adventureRed
import com.cc221002.mcfinalprojectmanuelprammer.ui.theme.backgroundGreen
import com.cc221002.mcfinalprojectmanuelprammer.ui.theme.backgroundWhite
import com.cc221002.mcfinalprojectmanuelprammer.ui.theme.orange
import com.cc221002.mcfinalprojectmanuelprammer.ui.view_model.AddingPageViewModel
import com.cc221002.mcfinalprojectmanuelprammer.ui.view_model.MainViewModel
import com.cc221002.mcfinalprojectmanuelprammer.ui.view_model.SharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.ExecutorService


// creating a sealed class for the Screens to navigate between
sealed class Screen(val route: String) {
    object SplashScreen: Screen("splashScreen")
    object ShowAllTrips: Screen("showTrips")
    object ShowSingleTrip: Screen("showSingleTrip")
    object AddingPage: Screen("addingPage")
    object CameraView: Screen("cameraView")
}


// this is the MainView Composable which is the first thing i navigate from the MainActivity
// here the routes of the screens are defined
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainView(
    mainViewModel: MainViewModel,
    cameraViewModel: CameraViewModel,
    previewView: PreviewView,
    imageCapture: ImageCapture,
    cameraExecutor: ExecutorService,
    directory: File
) {
    // creating instances of the ViewModels and the NavController
    val navController = rememberNavController()
    val sharedViewModel = SharedViewModel()
    val addingPageViewModel = AddingPageViewModel()

    // defining the routes to each Screen and what happens when that route is used
    Scaffold(
        ) {
            NavHost(
                navController = navController,
                modifier = Modifier.padding(it),
                // the starting screen is the SplashScreen
                startDestination = Screen.SplashScreen.route
            ) {
                composable(Screen.SplashScreen.route) {
                    mainViewModel.selectScreen(Screen.SplashScreen)
                    SplashScreen(
                        navController,
                    )
                }
                composable(Screen.ShowAllTrips.route) {
                    mainViewModel.selectScreen(Screen.ShowAllTrips)
                    mainViewModel.getTrips()
                    showTrips(
                        mainViewModel,
                        navController,
                    )
                }
                composable(Screen.ShowSingleTrip.route) {
                    mainViewModel.selectScreen(Screen.ShowSingleTrip)
                    showSingleTripModal(
                        mainViewModel,
                        navController,
                        cameraViewModel,
                        sharedViewModel
                    )
                }
                composable(Screen.AddingPage.route) {
                    mainViewModel.selectScreen(Screen.AddingPage)
                    addingPage(
                        sharedViewModel,
                        mainViewModel,
                        navController,
                        cameraViewModel,
                        addingPageViewModel,
                    )
                }
                composable(Screen.CameraView.route) {
                    mainViewModel.selectScreen(Screen.CameraView)
                    CameraView(
                        sharedViewModel,
                        cameraViewModel,
                        navController,
                        previewView,
                        imageCapture,
                        cameraExecutor,
                        directory,
                    )
                }
            }
        }
    }


// This is the real Main Screen actually. It is the ShowTrips Composable and displays all the trips in the database
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun showTrips(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {

    // collecting the information for all the trips and creating a mutable List with that
    val tripsState by mainViewModel.trips.collectAsState()
    val trips = tripsState.toMutableList()

    // counting the number of trips to display on the screen
    val tripsCounter = trips.count()

    Scaffold(
        // a floating button on the bottom right to navigate to the Adding Page
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddingPage.route) },
                modifier = Modifier
                    .padding(16.dp)
                    .size(50.dp)
                    .shadow(7.dp, CircleShape)
                    .border(BorderStroke(1.dp, orange), shape = CircleShape),
                backgroundColor = backgroundWhite,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Trip", tint = orange)
            }
        }
    ) {

        // Defining the Layout of the Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGreen),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 80.dp)
                    .fillMaxWidth()
                , contentAlignment = Alignment.CenterStart
            )
            {
                // drawing a white circle with Canvas because I could not do it differently
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    translate(left = 0f, top = -450f) {
                        drawCircle(backgroundWhite, radius = 300.dp.toPx())
                        drawCircle(
                            Black,
                            radius = 298.dp.toPx(),
                            style = Stroke(10f),
                            alpha = 0.1f,
                        )
                        // imitating shadow with multiple circles with different size and stroke thickness
                        drawCircle(Black, radius = 299.dp.toPx(), style = Stroke(8f), alpha = 0.1f,)
                        drawCircle(Black, radius = 299.dp.toPx(), style = Stroke(3f), alpha = 0.1f,)
                        drawCircle(Black, radius = 300.dp.toPx(), style = Stroke(2f), alpha = 0.1f,)
                        drawCircle(Black, radius = 300.dp.toPx(), style = Stroke(1f), alpha = 0.1f,)
                    }
                }
                Column {
                    // Text inside of the white Circle
                    Text(
                        text = "Your Adventures!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        style = TextStyle(fontFamily = FontFamily.SansSerif),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 20.dp, 0.dp, 0.dp)

                    )
                    // The amount of Trips that are stored in the database
                    Text(
                        text = "Trips: $tripsCounter",
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        style = TextStyle(fontFamily = FontFamily.SansSerif),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 20.dp, 0.dp, 0.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))


            // with the LazyColumn
            LazyColumn(
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(items = trips, key = { trip -> trip.id }) { trip ->

                    val dismissState = rememberDismissState(
                        confirmValueChange = {
                            if (it == DismissValue.DismissedToStart) {
                                mainViewModel.openAlertDialog(trip.id.toString())
                            }
                            false
                        }
                    )
                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier
                            .padding(vertical = 1.dp)
                            .animateItemPlacement(),
                        background = {
                            SwipeBackground(dismissState = dismissState)
                        },
                        dismissContent = {
                            ItemUi(
                                mainViewModel = mainViewModel,
                                navController = navController,
                                trip = trip,
                            )
                        },

                    )

                    val openAlertDialogForTrip = mainViewModel.openAlertDialogForTrip.value

                    if (openAlertDialogForTrip == trip.id.toString()) {
                        showDeleteConfirmationDialog(
                            mainViewModel = mainViewModel,
                            trip = trip,
                            onDeleteConfirmed = {
                                mainViewModel.dismissAlertDialog()
                                mainViewModel.deleteTrip(it,navController)
                            })
                    }
                }
            }
        }
    }
}


@Composable
fun showDeleteConfirmationDialog(
    mainViewModel: MainViewModel,
    trip: SingleTrip,
    onDeleteConfirmed: (SingleTrip) -> Unit
) {
    AlertDialog(
        onDismissRequest = { mainViewModel.dismissAlertDialog() },
        title = { Text("Delete Trip?", color = Black, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
        text = { Text("Are you sure you want to delete this trip?", color = Black) },
        confirmButton = {
            Button(
                elevation = ButtonDefaults.buttonElevation(5.dp),
                border = BorderStroke(1.dp, orange),
                onClick = {
                    onDeleteConfirmed(trip) // Perform deletion on confirmation
                },
                colors = ButtonDefaults.buttonColors(backgroundWhite)
            ) {
                Text("Confirm", color = Black)
            }
        },
        dismissButton = {
            Button(
                onClick = { mainViewModel.dismissAlertDialog() },
                colors = ButtonDefaults.buttonColors(Transparent)
            ) {
                Text("Cancel", color = Black)
            }
        }
    )
}
@Composable
fun ItemUi(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    trip:SingleTrip
){
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(20.dp, 15.dp)
                .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp), clip = true)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(backgroundWhite)
                .border(1.dp, Color.Black, shape = RoundedCornerShape(20.dp))
                .background(backgroundWhite)
                .clickable {
                    mainViewModel.showTripWithID(trip.id)
                    navController.navigate(Screen.ShowSingleTrip.route)
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(
                modifier = Modifier
                    .padding(2.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .height(200.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(0.59f)
                    , verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Trip to:",
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        style = TextStyle(fontFamily = FontFamily.Monospace),
                        color = Black,
                        modifier = Modifier
                            .padding(5.dp, 15.dp, 0.dp, 5.dp)
                    )

                    Text(
                        text = trip.location,
                        textAlign = TextAlign.Start,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(fontFamily = FontFamily.Monospace).copy(lineBreak = LineBreak.Paragraph),
                        color = Black,
                        maxLines = 2,
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                    Text(
                        text = trip.date,
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        style = TextStyle(fontFamily = FontFamily.Monospace),
                        color = Black,
                        modifier = Modifier
                            .padding(5.dp, 25.dp, 0.dp, 15.dp)
                            .width(250.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                )
                {
                    Image(
                        modifier = Modifier.fillMaxHeight()
                        , painter = rememberImagePainter(trip.imageUri),
                        contentDescription = "Picture of the trip",
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackground(
    dismissState: DismissState
) {
    val direction = dismissState.dismissDirection ?: return

    if (direction != DismissDirection.EndToStart) {
        return // Only allow swipe from Start to End
    }
    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> Color.Transparent
            DismissValue.DismissedToEnd -> Color.Transparent
            DismissValue.DismissedToStart -> Color.Red
        }, label = "swipeanimate"
    )
    val alignment = when (direction) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
    }
    val icon = Icons.Default.Delete

    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f, label = "floatanimate"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        Icon(
            icon,
            contentDescription = "Localized description",
            modifier = Modifier.scale(scale)
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun showSingleTripModal(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    cameraViewModel: CameraViewModel,
    sharedViewModel: SharedViewModel
) {
    val selectedTrip = mainViewModel.selectedTrip.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGreen)
    ) {
            items(1) {    selectedTrip.value?.let { trip ->
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    translate(left = 0f, top = -550f) {
                        drawCircle(backgroundWhite, radius = 300.dp.toPx())
                        drawCircle(Black, radius = 298.dp.toPx(), style = Stroke(10f), alpha = 0.1f,)
                        drawCircle(Black, radius = 299.dp.toPx(), style = Stroke(8f), alpha = 0.1f,)
                        drawCircle(Black, radius = 299.dp.toPx(), style = Stroke(3f), alpha = 0.1f,)
                        drawCircle(Black, radius = 300.dp.toPx(), style = Stroke(2f), alpha = 0.1f,)
                        drawCircle(Black, radius = 300.dp.toPx(), style = Stroke(1f), alpha = 0.1f,)
                    }
                }

                Row (
                    modifier = Modifier
                        .padding(20.dp, 100.dp, 20.dp, 0.dp)
                        .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp), clip = true)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(backgroundWhite)
                        .border(1.dp, Black, shape = RoundedCornerShape(20.dp))
                        .background(backgroundWhite),
                    horizontalArrangement = Arrangement.Center
                ){
                    Spacer(
                        modifier = Modifier
                            .padding(2.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(200.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .height(250.dp)
                                .fillParentMaxWidth(0.6F),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Trip to:",
                                textAlign = TextAlign.Start,
                                fontSize = 35.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(fontFamily = FontFamily.Monospace),
                                color = Black,
                                modifier = Modifier
                                    .padding(5.dp, 15.dp, 0.dp, 5.dp)
                            )
                            Text(
                                text = trip.date,
                                textAlign = TextAlign.Start,
                                fontSize = 20.sp,
                                style = TextStyle(fontFamily = FontFamily.Monospace),
                                color = Color.Black,
                                modifier = Modifier
                                    .padding(5.dp, 25.dp, 0.dp, 15.dp)
                                    .width(250.dp)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillParentMaxHeight()
                                .fillParentMaxWidth(0.4F)
                        )
                        {
                            Image(
                                modifier = Modifier
                                    .fillParentMaxHeight()
                                    .fillParentMaxWidth(0.4F),
                                painter = rememberImagePainter(trip.imageUri),
                                contentDescription = "null",
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .height(450.dp)
                        .padding(20.dp, 0.dp)
                        .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp), clip = true)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(backgroundWhite)
                        .border(1.dp, Black, shape = RoundedCornerShape(20.dp))
                        .background(backgroundWhite),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(10.dp)
                            , verticalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = trip.location,
                                textAlign = TextAlign.Start,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(fontFamily = FontFamily.Monospace).copy(lineBreak = LineBreak.Paragraph),
                                color = Black,
                                maxLines = 3,
                                modifier = Modifier
                                    .padding(0.dp, 15.dp, 0.dp, 15.dp)
                                    .fillMaxWidth()

                            )

                            Text(
                                text = trip.details,
                                textAlign = TextAlign.Start,
                                fontSize = 20.sp,
                                style = TextStyle(fontFamily = FontFamily.Monospace),
                                color = Black,
                                modifier = Modifier
                                    .padding(5.dp, 15.dp, 25.dp, 15.dp)
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .verticalScroll(rememberScrollState())
                            )
                            Spacer(
                                modifier = Modifier
                                    .padding(10.dp)
                            )

                            Row(
                                modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                RatingStarsWithText(trip.rating)
                            }

                        }

                        Row (
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center

                        ){
                            Button(onClick = {
                                mainViewModel.deleteTrip(trip,navController)
                            },
                                shape = RectangleShape,
                                colors = ButtonColors(Transparent, White, Magenta, Gray),
                                modifier = Modifier
                                    .background(adventureRed)
                                    .fillParentMaxWidth(0.5f)
                            ) {
                                Text(text = "Delete")
                            }

                            Button(onClick = { mainViewModel.editTrip(trip)},
                                shape = RectangleShape,
                                colors = ButtonColors(Transparent, White, Magenta, Gray),
                                modifier = Modifier
                                    .background(backgroundGreen)
                                    .fillParentMaxWidth(0.5f)
                            ) {
                                Text(text = "Edit")
                            }
                        }
                    }
                }
            }
            }
        }

    Column {
        editTripModal(
            mainViewModel,
            navController,
            cameraViewModel,
            sharedViewModel
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(modifier = Modifier
            .padding(25.dp)
            .width(50.dp)
            .height(50.dp)
            .align(Alignment.BottomCenter)
        ) {
            val size = 100f
            val trianglePath = Path().apply {
                moveTo(size / 2f, 0f)
                lineTo(size * 0.8f, size )
                lineTo(size * 0.5f,size *0.8f)
                lineTo(size * 0.2f, size )
            }
            val triangleBorder= Path().apply {
                moveTo(size / 2f, 0f)
                lineTo(size * 0.8f, size )
                lineTo(size * 0.5f,size *0.8f)
                lineTo(size * 0.2f, size )
                lineTo(size / 2f, 0f)
        }
            Button(
                onClick = { navController.navigate(Screen.ShowAllTrips.route) },
                modifier = Modifier
                    .size(50.dp)
                    .shadow(7.dp, CircleShape),
                colors = ButtonDefaults.buttonColors(containerColor = backgroundWhite),
                shape = CircleShape,
                border = BorderStroke(1.dp, backgroundGreen),


                ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                      translate(left = -47f,-10f) {
                          drawPath(
                              color = orange,
                              path = trianglePath
                          )
                          drawPath(color = Black,
                              style = Stroke(2f),
                              path = triangleBorder)
                      }
                }
            }
        }
    }
}
    @Composable
    fun SplashScreen(
        navController: NavHostController
    ) {

        val loadingFinished = remember { mutableStateOf(false) }

        // Introduce a 2-second delay to simulate loading
        LaunchedEffect(Unit) {
            delay(2000)  // Delay for 2 seconds
            loadingFinished.value = true
        }

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
        if(loadingFinished.value) {
            navController.navigate(Screen.ShowAllTrips.route)
        }
    }



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()


    val datePicker = remember { // Use remember to ensure the dialog state is retained
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                onDateSelected(LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth))
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
    }

Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
)

{
    TextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text(text = "Date") },
        readOnly = true,
        modifier = Modifier
            .width(230.dp)
    )

    Box(modifier = Modifier
        .width(50.dp)
        .height(55.dp)
        .shadow(5.dp, RoundedCornerShape(5.dp))
        .background(color = backgroundWhite)
        .clip(RoundedCornerShape(5.dp, 5.dp, 0.dp, 0.dp))
        .clickable {
            datePicker.show()
            Log.d("Just the Box", "TextField Clicked")
        },
        contentAlignment = Alignment.Center
    ){
        Icon(imageVector = Icons.Default.DateRange, contentDescription = "", tint = Color.Black, modifier = Modifier.fillMaxSize())
    }
}
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun addingPage(
    sharedViewModel: SharedViewModel,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    cameraViewModel: CameraViewModel,
    addingPageViewModel: AddingPageViewModel
) {



    var viewModelLocation by addingPageViewModel.location
    var viewModelDetails by addingPageViewModel.details
    var viewModelRating by addingPageViewModel.rating
    var viewModelDate by addingPageViewModel.date

    var imageUri = sharedViewModel.imageUri.collectAsState().value

    val maxLocationInputLength = 40
    val maxDetailsInputLength = 250
    val mContext = LocalContext.current
    val maxNumberInput= 5
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(backgroundGreen),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Box(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 80.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            )
            {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    translate(left = 0f, top = -390f) {
                        drawCircle(backgroundWhite, radius = 300.dp.toPx())
                        drawCircle(Black, radius = 298.dp.toPx(), style = Stroke(10f), alpha = 0.1f,)
                        drawCircle(Black, radius = 299.dp.toPx(), style = Stroke(8f), alpha = 0.1f,)
                        drawCircle(Black, radius = 299.dp.toPx(), style = Stroke(3f), alpha = 0.1f,)
                        drawCircle(Black, radius = 300.dp.toPx(), style = Stroke(2f), alpha = 0.1f,)
                        drawCircle(Black, radius = 300.dp.toPx(), style = Stroke(1f), alpha = 0.1f,)
                    }
                }
                Column {
                    Text(
                        text = "Add new Trip",
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        style = TextStyle(fontFamily = FontFamily.SansSerif),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 40.dp, 0.dp, 0.dp)
                    )
                }
            }
        Spacer(modifier = Modifier.size(50.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { focusManager.clearFocus() }
                    )
                }
            , verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    textStyle = TextStyle(fontFamily = FontFamily.Monospace).copy(lineBreak = LineBreak.Paragraph),
                    value = imageUri.toString(),
                    onValueChange = { newText ->
                        imageUri = newText
                    },
                    label = {
                        Text(text = "Image")
                    },
                    readOnly = true,
                    maxLines = 1,
                    modifier = Modifier
                        .width(230.dp)
                        .padding(top = 20.dp),

                    )
                Box(modifier = Modifier
                    .width(50.dp)
                    .padding(top = 20.dp)
                    .height(55.dp)
                    .shadow(5.dp, RoundedCornerShape(5.dp))
                    .background(color = backgroundWhite)
                    .clip(RoundedCornerShape(5.dp, 5.dp, 0.dp, 0.dp))
                    .clickable {
                        cameraViewModel.setForAdding(true)
                        navController.navigate(Screen.CameraView.route) // For adding a new trip
                    },
                    contentAlignment = Alignment.Center
                ){
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Take Picture", tint = Black, modifier = Modifier.fillMaxSize())
                }
            }

            TextField(
                modifier = Modifier.padding(top = 20.dp),
                value = viewModelLocation,
                onValueChange = { newText ->
                    if (newText.length <= maxLocationInputLength) addingPageViewModel.location.value = newText
                    else Toast.makeText(
                        mContext,
                        "Cannot be more than 100 Characters",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                label = {
                    Text(text = "Location")
                },
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            DatePickerField(
                selectedDate = viewModelDate
            ) {
                addingPageViewModel.date.value = it.toString()
            }
            TextField(
                modifier = Modifier.padding(top = 20.dp),
                value = viewModelDetails,
                onValueChange = { newText ->
                    if (newText.length <= maxDetailsInputLength) {
                        addingPageViewModel.details.value = newText
                    }
                    else Toast.makeText(
                        mContext,
                        "Cannot be more than 100 Characters",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                label = {
                    Text(text = "Details")
                }
            )
            TextField(
                modifier = Modifier.padding(top = 20.dp),
                value = viewModelRating,
                onValueChange = { newTextFieldValue ->
                    val newText = newTextFieldValue.trim()

                    if (newText.isEmpty()) {
                        addingPageViewModel.rating.value = newText // Update the value
                    } else {
                        val newValue = newText.toIntOrNull()
                        if (newValue in 0..maxNumberInput) {
                            addingPageViewModel.rating.value = newValue.toString()
                        } else {
                            Toast.makeText(
                                mContext,
                                "Please choose a number between 0 and 5",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Set the current value without changing it
                            addingPageViewModel.rating.value = viewModelRating
                        }
                    }
                },
                label = {
                    Text(text = "Rating in Number (0-5)")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )


            Spacer(modifier = Modifier.size(25.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Button(
                onClick = {navController.navigate(Screen.ShowAllTrips.route)},
                modifier = Modifier
                    .padding(top = 20.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.buttonColors(orange),
            ) {
                Text(text = "Back", fontSize = 20.sp)
            }
                Button(
                    onClick = {
                        mainViewModel.saveButton(
                            SingleTrip(
                                viewModelLocation,
                                viewModelDate,
                                viewModelDetails,
                                viewModelRating,
                                imageUri,
                            )
                        ); navController.navigate(Screen.ShowAllTrips.route)
                        sharedViewModel.setImageUri("")
                        addingPageViewModel.deleteAddingPageViewModelEntries()
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp)),
                    colors = ButtonDefaults.buttonColors(orange),
                    ) {
                    Text(text = "Save", fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun RatingStarsWithText(rating: String) {
    val numericRating = rating.toIntOrNull() ?: 0 // Convert String to Int, default to 0 if conversion fails

    Text(
        text = "Rating: ",
        textAlign = TextAlign.Start,
        fontSize = 20.sp,
        style = TextStyle(fontFamily = FontFamily.Monospace),
        color = Black,
        modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp)
    )
    Box() {
        // Draw five gray stars
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Gray Star",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp),
                    tint = Gray
                )
            }
        }
        // Overlay yellow stars based on the rating value
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(numericRating) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Yellow Star",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp),
                    tint = orange
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun editTripModal(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    cameraViewModel: CameraViewModel,
    sharedViewModel: SharedViewModel
){
    val state = mainViewModel.mainViewState.collectAsState()

    if(state.value.openEditDialog){
        var location by rememberSaveable {
            mutableStateOf(state.value.editSingleTrip.location)
        }
        var date by rememberSaveable {
            mutableStateOf(state.value.editSingleTrip.date)
        }
        var details by rememberSaveable {
            mutableStateOf(state.value.editSingleTrip.details)
        }
        var rating by rememberSaveable {
            mutableStateOf(state.value.editSingleTrip.rating)
        }
        var imageUri by rememberSaveable{
            mutableStateOf(state.value.editSingleTrip.imageUri)
        }
        var newImageUri = sharedViewModel.imageUri.collectAsState().value

        if(newImageUri =="") {
            newImageUri = imageUri
        }

        val maxLocationInputLength = 50
        val maxDetailsInputLength = 250
        val mContext = LocalContext.current
        val maxNumberInput= 5
        AlertDialog(
            onDismissRequest = {
                mainViewModel.dismissEditDialog()
            },
            backgroundColor = backgroundGreen,
            text = {
                Column {
                    Text(text ="Edit Trip",
                        lineHeight = 45.sp,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        style = TextStyle(fontFamily = FontFamily.SansSerif),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(),

                    )
                    Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        textStyle = TextStyle(fontFamily = FontFamily.Monospace).copy(lineBreak = LineBreak.Paragraph),
                        value = newImageUri.toString(),
                        onValueChange = {},
                        label = {
                            Text(text = "Image")
                        },
                        readOnly = true,
                        maxLines = 1,
                        modifier = Modifier
                            .width(230.dp)
                            .padding(top = 20.dp),

                        )
                    Box(modifier = Modifier
                        .width(50.dp)
                        .padding(top = 20.dp)
                        .height(55.dp)
                        .shadow(5.dp, RoundedCornerShape(5.dp))
                        .background(color = backgroundWhite)
                        .clip(RoundedCornerShape(5.dp, 5.dp, 0.dp, 0.dp))
                        .clickable {
                            cameraViewModel.setForEditing(true)
                            navController.navigate(Screen.CameraView.route) // For editing an existing trip
                        },
                        contentAlignment = Alignment.Center
                    ){
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Take Picture", tint = Black, modifier = Modifier.fillMaxSize())
                    }
                }
                    TextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = location,
                        onValueChange = { newText ->
                            if(newText.length <= maxLocationInputLength) location = newText
                            else Toast.makeText(mContext, "Cannot be more than 50 Characters", Toast.LENGTH_SHORT).show()},
                        label = { Text(text = "Location") },
                        textStyle = TextStyle(background = Transparent)
                    )
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                    DatePickerField(
                        selectedDate = date
                    ) {
                        date = it.toString()
                    }
                    TextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = details,
                        onValueChange = { newText ->
                            if(newText.length <= maxDetailsInputLength) details = newText
                            else Toast.makeText(mContext, "Cannot be more than 250 Characters", Toast.LENGTH_SHORT).show()
                                        },
                        label = { Text(text = "Details" ) }
                    )
                    TextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = rating,
                        onValueChange = { newText ->
                            if(newText.isEmpty() || newText.toIntOrNull() in 0..maxNumberInput){
                                rating = newText
                            } else {
                                Toast.makeText(mContext, "Please choose a number between 0 and 5", Toast.LENGTH_SHORT).show()
                            }
                        },
                        label = { Text(text = "Rating" ) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Log.d("Image_Debug", "save $newImageUri")
                        mainViewModel.saveEditedTrip(
                            SingleTrip(
                                location,
                                date,
                                details,
                                rating,
                                newImageUri,
                                state.value.editSingleTrip.id
                            )
                        )
                        sharedViewModel.setImageUri("")
                    },
                    colors = ButtonDefaults.buttonColors(orange),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 105.dp)
                        .padding(vertical = 16.dp)
                ) {
                    Text("Save", color = Color.Black)
                }
            }
        )
    }
}


@Composable
fun CameraView(
    sharedViewModel: SharedViewModel,
    cameraViewModel: CameraViewModel,
    navController: NavHostController,
    previewView: PreviewView,
    imageCapture: ImageCapture,
    cameraExecutor: ExecutorService,
    directory: File,
){
    val forAdding = cameraViewModel.forAdding.value
    val forEditing = cameraViewModel.forEditing.value

    val coroutineScope = rememberCoroutineScope()

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
        ){
        AndroidView({previewView}, modifier = Modifier.fillMaxSize())
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 5.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly

        ){
            Button(
                onClick = {if (forAdding) {
                    // Navigate back to addingPage with the captured image URI
                    navController.navigate(Screen.AddingPage.route)
                    cameraViewModel.setForAdding(false)
                } else if (forEditing) {
                    // Navigate back to editing with the captured image URI
                    navController.navigate(Screen.ShowSingleTrip.route)
                    cameraViewModel.setForEditing(false)
                }},
                colors = ButtonDefaults.buttonColors(orange),
                elevation = ButtonDefaults.elevatedButtonElevation(5.dp)
            ) {
            Text(text = "Back",color = Color.Black)
        }
            Button(
                modifier = Modifier,
                onClick = {
                    val photoFile = File(
                        directory,
                        SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.GERMANY).format(System.currentTimeMillis()) + ".jpg"
                    )

                    imageCapture.takePicture(
                        ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                        cameraExecutor,
                        object : ImageCapture.OnImageSavedCallback{
                            override fun onError(exception: ImageCaptureException){
                            }

                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                val imageUri = "file://${photoFile.absolutePath}"
                                sharedViewModel.setImageUri(imageUri)
                                coroutineScope.launch{
                                    if (forAdding) {
                                        // Navigate back to addingPage with the captured image URI
                                        navController.navigate(Screen.AddingPage.route)
                                        cameraViewModel.setForAdding(false)
                                    } else if (forEditing) {
                                        // Navigate back to editing with the captured image URI
                                        navController.navigate(Screen.ShowSingleTrip.route)
                                        cameraViewModel.setForEditing(false)
                                    }
                                }
                            }
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(orange),
                elevation = ButtonDefaults.elevatedButtonElevation(5.dp)
            ) {
                Icon(Icons.Default.AddCircle, "Take Photo", tint = backgroundWhite)
            }

        }

    }
}


