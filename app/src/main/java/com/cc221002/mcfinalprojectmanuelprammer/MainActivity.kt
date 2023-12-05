package com.cc221002.mcfinalprojectmanuelprammer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.cc221002.mcfinalprojectmanuelprammer.data.TripsDatabase
import com.cc221002.mcfinalprojectmanuelprammer.ui.theme.MCFinalProjectManuelPrammerTheme
import com.cc221002.mcfinalprojectmanuelprammer.ui.view.MainView
import com.cc221002.mcfinalprojectmanuelprammer.ui.view_model.MainViewModel

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(this, TripsDatabase::class.java, "TripsDatabase.db").build()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mainViewModel.wipeDatabase()
//        mainViewModel.insertPreTrips()
        setContent {
            MCFinalProjectManuelPrammerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(mainViewModel)                }
            }
        }
    }
}

