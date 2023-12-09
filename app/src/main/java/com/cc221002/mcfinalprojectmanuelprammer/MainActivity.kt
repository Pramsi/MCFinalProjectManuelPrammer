package com.cc221002.mcfinalprojectmanuelprammer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
    private val db by lazy {
        Room.databaseBuilder(this, TripsDatabase::class.java, "TripsDatabase.db") .addMigrations(TripsDatabase.MIGRATION_1_2).build()
    }

    private val mainViewModel by viewModels<MainViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(db.dao) as T
                }
            }
        }
    )

    private val cameraViewModel = CameraViewModel()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        cameraViewModel.setCameraPermission(it)
    }

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private val imageCapture: ImageCapture = ImageCapture.Builder().build()
    private val preview: Preview = Preview.Builder().build()

    private fun setupCamera(){
        previewView = PreviewView(this)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            },
            ContextCompat.getMainExecutor(this))

        cameraExecutor = Executors.newSingleThreadExecutor()
    }


    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            this as LifecycleOwner,
            CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build(),
            preview, imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }





    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermission()
        setupCamera()
//        mainViewModel.wipeDatabase()
//        mainViewModel.insertPreTrips()
        setContent {
            MCFinalProjectManuelPrammerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(mainViewModel, cameraViewModel, previewView, imageCapture, cameraExecutor, getOutputDirectory())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun getOutputDirectory(): File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it,"TripDiary").apply { mkdirs() }
        }
        return if(mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private fun requestPermission(){
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA).let{ result ->
            if(result != PackageManager.PERMISSION_GRANTED){
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                cameraViewModel.setCameraPermission(true)
            }
        }
    }


}

