package com.cc221002.mcfinalprojectmanuelprammer.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CameraViewModel: ViewModel() {
	private val _cameraState = MutableStateFlow(CameraState())

	fun setCameraPermission(value: Boolean){
		_cameraState.update { it.copy(cameraPermissionGranted = value) }
	}


}