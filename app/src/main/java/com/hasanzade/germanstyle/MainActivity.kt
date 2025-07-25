package com.hasanzade.germanstyle

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.hasanzade.germanstyle.ui.theme.GermanStyleTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ReceiptSplitterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GermanStyleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ReceiptSplitterAppWithPermissions(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ReceiptSplitterAppWithPermissions(
    viewModel: ReceiptSplitterViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Camera permission is required to scan receipts with AI", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Camera permission granted! Ready for AI scanning", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    ReceiptSplitterApp(
        viewModel = viewModel,
        hasCameraPermission = hasCameraPermission,
        onRequestCameraPermission = {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        },
        modifier = modifier
    )
}
