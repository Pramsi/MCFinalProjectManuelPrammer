package com.cc221002.mcfinalprojectmanuelprammer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.cc221002.mcfinalprojectmanuelprammer.data.TripsDatabase
import com.cc221002.mcfinalprojectmanuelprammer.ui.CameraViewModel
import com.cc221002.mcfinalprojectmanuelprammer.ui.theme.MCFinalProjectManuelPrammerTheme
import com.cc221002.mcfinalprojectmanuelprammer.ui.view.MainView
import com.cc221002.mcfinalprojectmanuelprammer.ui.view_model.MainViewModel
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    //------------------
    // DATABASE
    //------------------

    // Creating a lazy Room Database
    private val db by lazy {
        Room.databaseBuilder(this, TripsDatabase::class.java, "TripsDatabase.db").build()
    /*.addMigrations(TripsDatabase.MIGRATION_1_2)     Only needed for migration of the database*/
    }
    // Creating the mainViewModel
    private val mainViewModel by viewModels<MainViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(db.dao) as T
                }
            }
        }
    )
    //------------------
    // CAMERA
    //------------------

    // initializing the variables
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private val imageCapture: ImageCapture = ImageCapture.Builder().build()
    private val preview: Preview = Preview.Builder().build()

    private val cameraViewModel = CameraViewModel()
    private val requestPermissionLauncher = registerForActivityResult(
        // requesting the Permission to use the camera
        ActivityResultContracts.RequestPermission()
    ){
        // setting if the Permission is granted or not
        cameraViewModel.setCameraPermission(it)
    }

    // Function to set up the Camera
    private fun setupCamera(){
        //getting the context for the CameraPreview and the cameraProvider
        previewView = PreviewView(this)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        //binding the CameraProvider to the lifecycle
        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            },
            ContextCompat.getMainExecutor(this))

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    // the function to bind the CameraProvider to the Lifecycle
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        cameraProvider.unbindAll()
        // binding the preview to the lifecycle
        cameraProvider.bindToLifecycle(
            this as LifecycleOwner,
            CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build(),
            preview, imageCapture
        )
        // setting the surfaceProvider for the preview
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    // overwritten function to define what happens when the activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        // it still deletes every remaining thing before destroying the activity but it also shuts down the cameraExecutor
        cameraExecutor.shutdown()
    }

    // the function to create the directory for the Images that are taken in the app
    private fun getOutputDirectory(): File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it,"TripDiary").apply { mkdirs() }
        }
        return if(mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    // the function that asks for permission to use the camera
    private fun requestPermission(){
        // it check if the Permission is already granted. If not it launches the function to ask for it
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA).let{ result ->
            if(result != PackageManager.PERMISSION_GRANTED){
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                //if it is already granted it sets the cameraPermission to true
                cameraViewModel.setCameraPermission(true)
            }
        }
    }

    //------------------
    // CREATING THE APP
    //------------------
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        // creating an instance
        super.onCreate(savedInstanceState)

        // ask for Permission to use the Camera
        requestPermission()


        // those are functions to delete everything in the database and to create predefined db entries
        //mainViewModel.wipeDatabase()
        //mainViewModel.insertPreTrips()


        setContent {
            MCFinalProjectManuelPrammerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // setting up the camera
                    setupCamera()
                    // directing to my MainView in the ComposableUiElement.kt file and passing everything that is needed
                    MainView(mainViewModel, cameraViewModel, previewView, imageCapture, cameraExecutor, getOutputDirectory())
                }
            }
        }
    }
}

