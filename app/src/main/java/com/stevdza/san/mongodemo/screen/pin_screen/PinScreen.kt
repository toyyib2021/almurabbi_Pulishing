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
import com.stevdza.san.mongodemo.ui.theme.h1


@Composable
fun PinScreen(navPinCreationScreen: () -> Unit, navDashboardScreen: () -> Unit) {
     val viewModel: PinScreenVM = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.getSavedPassword(context)
        viewModel.onPasscodeConfirmed.collect {
            snackbarHostState.showSnackbar(
                message = "Passcode is correct"
            )
            navDashboardScreen()
        }
    }
    Log.e(TAG, "confirmPasscode: ${viewModel.confirmPasscode}", )
    Log.e(TAG, "createPasscode: ${viewModel.password}", )

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
        Box(
            modifier = Modifier.weight(2f),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Enter Your Pin", style = h1)
        }
        Box(
            modifier = Modifier.weight(2f),
            contentAlignment = Alignment.Center
        ) {
            PasscodeViewP(
                viewModel = viewModel,
                navPinCreationScreen = { navPinCreationScreen() }
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        PasscodeKeysP(
            viewModel = viewModel,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Box(
            modifier = Modifier.weight(2f),
            contentAlignment = Alignment.Center
        ) {

        }
        SnackbarHost(hostState = snackbarHostState)

    }

}









