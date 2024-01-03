package com.cc221002.mcfinalprojectmanuelprammer.ui

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CameraViewModel: ViewModel() {
	private val _cameraState = MutableStateFlow(CameraState())

	private val _forAdding = mutableStateOf(false)
	val forAdding: State<Boolean> = _forAdding

	private val _forEditing = mutableStateOf(false)
	val forEditing: State<Boolean> = _forEditing

	fun setCameraPermission(value: Boolean){
		_cameraState.update { it.copy(cameraPermissionGranted = value) }
	}

	fun setForAdding(value: Boolean) {
		_forAdding.value = value
	}

	fun setForEditing(value: Boolean) {
		_forEditing.value = value
	}

}