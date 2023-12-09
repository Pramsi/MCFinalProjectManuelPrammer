package com.cc221002.mcfinalprojectmanuelprammer.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CameraViewModel: ViewModel() {
	private val _cameraState = MutableStateFlow(CameraState())
	val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()

	fun setCameraPermission(value: Boolean){
		_cameraState.update { it.copy(cameraPermissionGranted = value) }
	}

	fun enableCameraPreview(value: Boolean){
		_cameraState.update { it.copy(enableCameraPreview = value) }
	}

	fun setNewUri(value: Uri){
		_cameraState.update { it.copy(photosListState = it.photosListState + value) }
		enableCameraPreview(false)
	}

}