package com.cc221002.mcfinalprojectmanuelprammer.ui.view

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.camera2.internal.annotation.CameraExecutor
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cc221002.mcfinalprojectmanuelprammer.R
import com.cc221002.mcfinalprojectmanuelprammer.data.model.SingleTrip
import com.cc221002.mcfinalprojectmanuelprammer.ui.CameraViewModel
import com.cc221002.mcfinalprojectmanuelprammer.ui.theme.adventureRed
import com.cc221002.mcfinalprojectmanuelprammer.ui.theme.backgroundGreen
import com.cc221002.mcfinalprojectmanuelprammer.ui.theme.backgroundWhite
import com.cc221002.mcfinalprojectmanuelprammer.ui.theme.orange
import com.cc221002.mcfinalprojectmanuelprammer.ui.view_model.MainViewModel
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.ExecutorService

sealed class Screen(val route: String) {
    object First: Screen("first")
    object Second: Screen("second")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainView(mainViewModel: MainViewModel, cameraViewModel: CameraViewModel, previewView: PreviewView, imageCapture: ImageCapture, cameraExecutor: ExecutorService, directory: File) {
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
                    addingPage(mainViewModel,navController, cameraViewModel, previewView,imageCapture,cameraExecutor,directory, capturedImageUri = null)

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


@RequiresApi(Build.VERSION_CODES.O)
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
                    .padding(20.dp, 15.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp), clip = true)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(backgroundWhite)
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(20.dp))
                    .background(backgroundWhite)
                    .clickable {
                        mainViewModel.showTripWithID(trip.id)
                        Log.d("Show Trips with ID", " Clicked")
                        Log.d("Show Trips with ID", "$trip")
                    },
                horizontalArrangement = Arrangement.Center
            ){
                    Spacer(
                        modifier = Modifier
                            .padding(5.dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {

                        Text(
                            text = "Trip to:",
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            style = TextStyle(fontFamily = FontFamily.Monospace),
                            color = Color.Black,
                            modifier = Modifier
                                .padding(5.dp, 15.dp, 0.dp, 5.dp)
                                .width(250.dp)
                        )

                        Text(
                            text = trip.location,
                            textAlign = TextAlign.Start,
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(fontFamily = FontFamily.Monospace),
                            color = Color.Black,
                            modifier = Modifier
                                .padding(5.dp)
                                .width(250.dp)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun showSingleTripModal(mainViewModel: MainViewModel, navController: NavHostController) {
    val selectedTrip = mainViewModel.selectedTrip.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGreen)
    ) {
        items(1) {    selectedTrip.value?.let { trip ->

            Row(
                modifier = Modifier
                    .padding(20.dp, 50.dp, 20.dp, 0.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp), clip = true)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(backgroundWhite)
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(20.dp))
                    .background(backgroundWhite),
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(5.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {

                    Text(
                        text = "Trip to:",
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        style = TextStyle(fontFamily = FontFamily.Monospace),
                        color = Color.Black,
                        modifier = Modifier
                            .padding(5.dp, 15.dp, 0.dp, 5.dp)
                            .width(250.dp)
                    )

                    Text(
                        text = trip.location,
                        textAlign = TextAlign.Start,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(fontFamily = FontFamily.Monospace),
                        color = Color.Black,
                        modifier = Modifier
                            .padding(5.dp)
                            .width(250.dp)
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
            }
            Row(
                modifier = Modifier
                    .height(500.dp)
                    .padding(20.dp, 0.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp), clip = true)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(backgroundWhite)
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(20.dp))
                    .background(backgroundWhite),
                horizontalArrangement = Arrangement.Center
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "${trip.details}",
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        style = TextStyle(fontFamily = FontFamily.Monospace),
                        color = Color.Black,
                        modifier = Modifier
                            .padding(25.dp, 15.dp, 25.dp, 15.dp)
                            .fillMaxWidth()
                    )

                    RatingStarsWithText(trip.rating)



                    Row (
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center


                    ){
                        Button(onClick = {
                            mainViewModel.deleteTrip(trip)
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
        editTripModal(mainViewModel)
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
fun addingPage(mainViewModel: MainViewModel,navController: NavHostController, cameraViewModel: CameraViewModel, previewView: PreviewView, imageCapture: ImageCapture, cameraExecutor: ExecutorService, directory: File,capturedImageUri: String?) {
    var location by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue("")
        )
    }
    var details by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue("")
        )
    }
    var rating by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue("")
        )
    }
    var capturedImageUri by remember { mutableStateOf<String?>("") }
    val handleImageCaptured: (String) -> Unit = { imageUri ->
        capturedImageUri = imageUri
    }

    val camState = cameraViewModel.cameraState.collectAsState()

    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }

    val maxLocationInputLength = 50
    val maxDetailsInputLength = 250
    val mContext = LocalContext.current
    val maxNumberInput= 5


    if(!camState.value.enableCameraPreview){
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
                .fillMaxWidth()
                .height(200.dp)
//                .clip(RoundedCornerShape(100))
//                .background(backgroundWhite)
//            .shadow(5.dp, RoundedCornerShape(100))
            , contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                translate(left = 0f, top = -125f) {
                    drawCircle(backgroundWhite, radius = 225.dp.toPx())
                }
            }
            Text(
                text = "ADD NEW TRIP",
                fontWeight = FontWeight.Bold,
                fontSize = 50.sp,
                style = TextStyle(fontFamily = FontFamily.SansSerif),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.size(100.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .background(backgroundGreen),
            , verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextField(
                modifier = Modifier.padding(top = 20.dp),
                value = location,
                onValueChange = { newText ->
                    if (newText.text.length <= maxLocationInputLength) location = newText
                    else Toast.makeText(
                        mContext,
                        "Cannot be more than 100 Characters",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                label = {
                    Text(text = "Location")
                }
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            DatePickerField(
                selectedDate = selectedDate.toString()
            ) {
                selectedDate = it
            }
            TextField(
                modifier = Modifier.padding(top = 20.dp),
                value = details,
                onValueChange = { newText ->
                    if (newText.text.length <= maxDetailsInputLength) details = newText
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
                value = rating,
                onValueChange = { newTextFieldValue ->
                    val newText = newTextFieldValue.text

                    if (newText.isEmpty() || newText.toIntOrNull() in 0..maxNumberInput) {
                        rating = newTextFieldValue
                    } else {
                        Toast.makeText(
                            mContext,
                            "Please choose a number between 0 and 5",
                            Toast.LENGTH_SHORT
                        ).show()
                        rating = rating.takeUnless { it.text.isEmpty() } ?: TextFieldValue("")
                    }
                },
                label = {
                    Text(text = "Rating in Number (0-5)")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                modifier = Modifier.padding(top = 20.dp),
                value = capturedImageUri.toString(),
                onValueChange = { newText ->
                    capturedImageUri = newText
                },
                label = {
                    Text(text = "Image")
                }
            )
            Spacer(modifier = Modifier.size(100.dp))
            Button(
                onClick = {
                    mainViewModel.saveButton(
                        SingleTrip(
                            location.text,
                            selectedDate.toString(), /*date.text,*/
                            details.text,
                            rating.text
                        )
                    ); navController.navigate(Screen.First.route)
                },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.buttonColors(orange),

                ) {
                Text(text = "Save", fontSize = 20.sp)
            }
        }
        Button(
            modifier = Modifier.padding(25.dp),
            onClick = { cameraViewModel.enableCameraPreview(true) }
        ) {
            Icon(Icons.Default.Add, "Open Camera Preview")
        }
    }
    } else {
        CameraView(
            cameraViewModel = cameraViewModel,
            previewView = previewView,
            imageCapture = imageCapture,
            cameraExecutor = cameraExecutor,
            directory = directory,
            onImageCaptured = handleImageCaptured
        )
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
        color = Color.Black,
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
                        .padding(end = 4.dp), // Adjust padding as needed
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
                        .padding(end = 4.dp), // Adjust padding as needed
                    tint = orange
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editTripModal(mainViewModel: MainViewModel){
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
                        mainViewModel.saveEditedTrip(
                            SingleTrip(
                                location,
                                date,
                                details,
                                rating,
                                state.value.editSingleTrip.id
                            )
                        )
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
fun CameraView(cameraViewModel: CameraViewModel, previewView: PreviewView, imageCapture: ImageCapture, cameraExecutor: ExecutorService, directory: File, onImageCaptured:(String)-> Unit){


    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
        ){
        AndroidView({previewView}, modifier = Modifier.fillMaxSize())
        Button(
            modifier = Modifier.padding(25.dp),
            onClick = {
                val photoFile = File(
                    directory,
                    SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.GERMANY).format(System.currentTimeMillis())
                )

                imageCapture.takePicture(
                    ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                    cameraExecutor,
                    object : ImageCapture.OnImageSavedCallback{
                        override fun onError(exception: ImageCaptureException){
                            Log.e("PICTURE","Errer when capturing image")
                        }

                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            val imageUri = photoFile.absolutePath
                            onImageCaptured(imageUri)
                            cameraViewModel.setNewUri(Uri.fromFile(photoFile))
                            Log.d("PICTURE", "SAVED in ${Uri.fromFile(photoFile)}")
                            Log.d("PICTURE", "SAVED in $photoFile")

                        }
                    }
                )
            }
        ) {
            Icon(Icons.Default.AddCircle, "Take Photo", tint = backgroundWhite)
        }
    }
}


