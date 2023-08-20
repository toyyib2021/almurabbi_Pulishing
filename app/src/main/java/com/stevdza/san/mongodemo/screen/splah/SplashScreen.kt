package com.stevdza.san.mongodemo.screen.splah

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.stevdza.san.mongodemo.R
import com.stevdza.san.mongodemo.navigation.Screen
import com.stevdza.san.mongodemo.data.dataStore.PassKey


@Composable
fun Splash(navController: NavHostController,
) {

    val context = LocalContext.current
    val passKey = PassKey(context)
    val getPassKey = passKey.getKey.collectAsState(initial = "")


    val degrees = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        degrees.animateTo(
            targetValue = 360f,
            animationSpec = tween(
                durationMillis = 1000,
                delayMillis = 200
            )
        )
        navController.popBackStack()
        if (getPassKey.value == "") {
//            navController.navigate(Screen.Authentication.route)
            navController.navigate(Screen.PinCreation.route)
        } else {
            navController.navigate(Screen.PinLogin.route)
        }
        Log.e(TAG, "Splash: ${getPassKey.value}", )
    }

    SplashUI(degrees = degrees.value)
}

@Composable
fun SplashUI(degrees: Float) {
    val modifier = if (isSystemInDarkTheme()) {
        Modifier.background(Color.Black)
    } else {
        Modifier.background(Color.White)
    }
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.rotate(degrees = degrees),
            painter = painterResource(id = R.drawable.logo_almurabbi),
            contentDescription = "o"

        )
    }

}
