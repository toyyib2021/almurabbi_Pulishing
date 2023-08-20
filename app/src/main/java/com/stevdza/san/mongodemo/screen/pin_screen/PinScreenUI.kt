package com.stevdza.san.mongodemo.screen.pin_screen

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.stevdza.san.mongodemo.R
import com.stevdza.san.mongodemo.ui.theme.Cyan
import com.stevdza.san.mongodemo.ui.theme.DarkBlue
import com.stevdza.san.mongodemo.ui.theme.PasscodeKeyButtonStyle


const val VIBRATE_FEEDBACK_DURATION = 300L
var RESET_PASSCODE = 1


@Composable
fun PasscodeViewP(
    modifier: Modifier = Modifier,
    viewModel: PinScreenVM,
    navPinCreationScreen: () -> Unit
) {
    val context = LocalContext.current
    val filledDots by viewModel.filledDots.collectAsState()
    val xShake = remember { Animatable(initialValue = 0.0F) }
    val passcodeRejectedDialogVisible = remember { mutableStateOf(false) }


    LaunchedEffect(key1 = true) {
        viewModel.onPasscodeRejected.collect {
            if (RESET_PASSCODE == 4){
                navPinCreationScreen()
            }else{
                passcodeRejectedDialogVisible.value = true
                vibrateFeedback(context)
                RESET_PASSCODE++
                Log.e(TAG, "RESET_PASSCODE: $RESET_PASSCODE", )
            }


//            xShake.animateTo(
//                targetValue = 0.dp.value,
//                animationSpec = keyframes {
//                    0.0F at 0
//                    20.0F at 80
//                    -20.0F at 120
//                    10.0F at 160
//                    -10.0F at 200
//                    5.0F at 240
//                    0.0F at 280
//                }
//            )
        }
    }

    PasscodeRejectedDialogP(
        visible = passcodeRejectedDialogVisible.value,
        onDismiss = {
            passcodeRejectedDialogVisible.value = false

            viewModel.restart()
        }
    )

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.offset(x = xShake.value.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 26.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            repeat(PinScreenVM.PASSCODE_LENGTH) { dot ->
                val isFilledDot = dot + 1 <= filledDots
                val dotColor = animateColorAsState(
                    if (isFilledDot) {
                        DarkBlue
                    } else {
                        Cyan
                    }
                )

                Box(
                    modifier = modifier
                        .size(size = 14.dp)
                        .background(
                            color = dotColor.value,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun PasscodeRejectedDialogP(
    visible: Boolean,
    onDismiss: () -> Unit
) {
    if (visible) {
        AlertDialog(
            shape = MaterialTheme.shapes.large,
            title = {
                when(RESET_PASSCODE){
                    2 -> { Text(text = "You have enter the wrong pin! " +
                            "2 more attempt left") }
                    3 -> { Text(text = "You have enter the wrong pin! " +
                            "1 more attempt left") }
                    else -> {
                        Text(text = "This is your last attempt")
                    }
                }
                },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    when(RESET_PASSCODE){
                        4 -> {
                            Text(text = "Last attempt")
                        }else -> {
                            Text(text = "Try again")
                        }
                    }
                }
            },
            onDismissRequest = onDismiss
        )
    }
}



@Composable
fun PasscodeKeysP(
    viewModel: PinScreenVM,
    modifier: Modifier = Modifier,
) {
    val onEnterKeyClick = { keyTitle: String ->
        viewModel.enterKey(keyTitle)
    }

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                PasscodeKeyP(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "1",
                    onClick = onEnterKeyClick
                )
                PasscodeKeyP(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "2",
                    onClick = onEnterKeyClick
                )
                PasscodeKeyP(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "3",
                    onClick = onEnterKeyClick
                )
            }
            Spacer(modifier = Modifier.height(height = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PasscodeKeyP(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "4",
                    onClick = onEnterKeyClick
                )
                PasscodeKeyP(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "5",
                    onClick = onEnterKeyClick
                )
                PasscodeKeyP(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "6",
                    onClick = onEnterKeyClick
                )
            }
            Spacer(modifier = Modifier.height(height = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PasscodeKeyP(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "7",
                    onClick = onEnterKeyClick
                )
                PasscodeKeyP(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "8",
                    onClick = onEnterKeyClick
                )
                PasscodeKeyP(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "9",
                    onClick = onEnterKeyClick
                )
            }
            Spacer(modifier = Modifier.height(height = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PasscodeKeyP(modifier = Modifier.weight(weight = 1.0F))
                PasscodeKeyP(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "0",
                    onClick = onEnterKeyClick
                )
                PasscodeKeyP(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyIcon = ImageVector.vectorResource(id = R.drawable.ic_delete),
                    keyIconContentDescription = "Delete Passcode Key Button",
                    onClick = {
                        viewModel.deleteKey()
                    },

                )
            }
        }
    }
}


@Composable
private fun PasscodeKeyP(
    modifier: Modifier = Modifier,
    keyTitle: String = "",
    keyIcon: ImageVector? = null,
    keyIconContentDescription: String = "",
    onClick: ((String) -> Unit)? = null,
    onLongClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CombinedClickableIconButton(
            modifier = Modifier.padding(all = 4.dp),
            onClick = {
                onClick?.invoke(keyTitle)
            },
            onLongClick = {
                onLongClick?.invoke()
            }
        ) {
            if (keyIcon == null) {
                Text(
                    text = keyTitle,
                    style = PasscodeKeyButtonStyle
                )
            } else {
                Icon(
                    imageVector = keyIcon,
                    contentDescription = keyIconContentDescription
                )
            }
        }
    }
}







