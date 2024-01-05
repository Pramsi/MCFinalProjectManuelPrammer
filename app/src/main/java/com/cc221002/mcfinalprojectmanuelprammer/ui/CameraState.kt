package com.cc221002.mcfinalprojectmanuelprammer.ui

import android.net.Uri

data class CameraState(
	val photosListState: List<Uri> = emptyList(),
	val enableCameraPreview: Boolean = false,
	val cameraPermissionGranted: Boolean = false
)
