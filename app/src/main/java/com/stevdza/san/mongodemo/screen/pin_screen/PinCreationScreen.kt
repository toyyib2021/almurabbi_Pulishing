package com.stevdza.san.mongodemo.screen.pin_screen

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stevdza.san.mongodemo.data.dataStore.PassKey


@Composable
fun PinCreationScreen(navToDashBoard: () -> Unit) {

    val pinCreationScreenVM: PinCreationScreenVM = viewModel()
    val activeStep by pinCreationScreenVM.activeStep.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val passKey = PassKey(context)

    LaunchedEffect(key1 = true) {
        pinCreationScreenVM.onPasscodeConfirmed.collect {
            snackbarHostState.showSnackbar(
                message = "Passcode Successfully Saved"
            )
            val get = passKey.saveKey(pinCreationScreenVM.confirmPasscode.toString())
            navToDashBoard()
            Log.e(TAG, "confirmPasscode: ${get.toString()}", )

        }
    }
    Log.e(TAG, "confirmPasscode: ${pinCreationScreenVM.confirmedPassword.value}", )
    Log.e(TAG, "createPasscode: ${pinCreationScreenVM.createPasscode}", )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0F to Color.Transparent,
                        1.0F to MaterialTheme.colors.onBackground.copy(alpha = 0.045F)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Toolbar(activeStep = activeStep)
        Spacer(modifier = Modifier.height(6.dp))
        Headers(activeStep = activeStep)
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier.weight(1.0F),
            contentAlignment = Alignment.Center
        ) {
            PasscodeView(viewModel = pinCreationScreenVM)
        }
        Spacer(modifier = Modifier.height(6.dp))
        PasscodeKeys(
            viewModel = pinCreationScreenVM,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {

        }
    }

    SnackbarHost(hostState = snackbarHostState)
}



