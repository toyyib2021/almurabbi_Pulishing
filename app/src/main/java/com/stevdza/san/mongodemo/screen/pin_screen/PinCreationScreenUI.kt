package com.stevdza.san.mongodemo.screen.pin_screen

import android.content.Context
import android.os.*
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stevdza.san.mongodemo.R
import com.stevdza.san.mongodemo.MainActivity
import com.stevdza.san.mongodemo.ui.theme.Cyan
import com.stevdza.san.mongodemo.ui.theme.DarkBlue
import com.stevdza.san.mongodemo.ui.theme.PasscodeKeyButtonStyle
import com.stevdza.san.mongodemo.ui.theme.h2


@Composable
fun Toolbar(activeStep: PinCreationScreenVM.Step) {
    val mainActivity = LocalContext.current as MainActivity?
    val exitWarningDialogVisible = remember { mutableStateOf(false) }

    ExitWarningDialog(
        visible = exitWarningDialogVisible.value,
        onConfirm = {
            if (mainActivity == null) {
                exitWarningDialogVisible.value = false
            } else {
                mainActivity.finish()
            }
        },
        onDismiss = {
            exitWarningDialogVisible.value = false
        }
    )
    BackHandler {
        exitWarningDialogVisible.value = true
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        StepIndicator(
            modifier = Modifier.align(alignment = Alignment.Center),
            activeStep = activeStep
        )
        IconButton(
            modifier = Modifier.padding(all = 4.dp),
            onClick = {
                exitWarningDialogVisible.value = true
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Exit Button"
            )
        }
    }
}

@Composable
fun ExitWarningDialog(
    visible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (visible) {
        AlertDialog(
            shape = MaterialTheme.shapes.large,
            title = {
                Text(text = "Are you sure you want to exit?")
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = "Exit")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
            },
            onDismissRequest = onDismiss
        )
    }
}



@Composable
fun StepIndicator(
    modifier: Modifier = Modifier,
    activeStep: PinCreationScreenVM.Step
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 6.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        repeat(PinCreationScreenVM.STEPS_COUNT) { step ->
            val isActiveStep = step <= activeStep.index
            val stepColor = animateColorAsState(
                if (isActiveStep) {
                    DarkBlue
                } else {
                    Cyan
                }
            )

            Box(
                modifier = Modifier
                    .size(
                        width = 72.dp,
                        height = 4.dp
                    )
                    .background(
                        color = stepColor.value,
                        shape = MaterialTheme.shapes.medium
                    )
            )
        }
    }
}


@Composable
fun Headers(
    modifier: Modifier = Modifier,
    activeStep: PinCreationScreenVM.Step
) {
    val transitionState = remember { MutableTransitionState(activeStep) }
    transitionState.targetState = activeStep

    val transition: Transition<PinCreationScreenVM.Step> = updateTransition(
        transitionState = transitionState,
        label = "Headers Transition"
    )

    val offset = 200.0F
    val zeroOffset = Offset(x = 0.0F, y = 0.0F)
    val negativeOffset = Offset(x = -offset, y = 0.0F)
    val positiveOffset = Offset(x = offset, y = 0.0F)

    val xTransitionHeader1 by transition.animateOffset(label = "Transition Offset Header 1") {
        if (it == PinCreationScreenVM.Step.Create) zeroOffset else negativeOffset
    }
    val xTransitionHeader2 by transition.animateOffset(label = "Transition Offset Header 2") {
        if (it == PinCreationScreenVM.Step.Confirm) zeroOffset else positiveOffset
    }
    val alphaHeader1 by transition.animateFloat(label = "Transition Alpha Header 1") {
        if (it == PinCreationScreenVM.Step.Create) 1.0F else 0.0F
    }
    val alphaHeader2 by transition.animateFloat(label = "Transition Alpha Header 2") {
        if (it == PinCreationScreenVM.Step.Confirm) 1.0F else 0.0F
    }
    val scaleHeader1 by transition.animateFloat(label = "Transition Alpha Header 1") {
        if (it == PinCreationScreenVM.Step.Create) 1.0F else 0.5F
    }
    val scaleHeader2 by transition.animateFloat(label = "Transition Alpha Header 2") {
        if (it == PinCreationScreenVM.Step.Confirm) 1.0F else 0.5F
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .offset(x = xTransitionHeader1.x.dp)
                .alpha(alpha = alphaHeader1)
                .scale(scale = scaleHeader1),
            text = "Create passcode",
            style = h2
        )
        Text(
            modifier = Modifier
                .offset(x = xTransitionHeader2.x.dp)
                .alpha(alpha = alphaHeader2)
                .scale(scale = scaleHeader2),
            text = "Confirm passcode",
            style = h2
        )
    }
}


@Composable
fun PasscodeView(
    modifier: Modifier = Modifier,
    viewModel: PinCreationScreenVM
) {
    val context = LocalContext.current
    val filledDots by viewModel.filledDots.collectAsState()
    val xShake = remember { Animatable(initialValue = 0.0F) }
    val passcodeRejectedDialogVisible = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.onPasscodeRejected.collect {
            passcodeRejectedDialogVisible.value = true
            vibrateFeedback(context)

            xShake.animateTo(
                targetValue = 0.dp.value,
                animationSpec = keyframes {
                    0.0F at 0
                    20.0F at 80
                    -20.0F at 120
                    10.0F at 160
                    -10.0F at 200
                    5.0F at 240
                    0.0F at 280
                }
            )
        }
    }

    PasscodeRejectedDialog(
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
            repeat(PinCreationScreenVM.PASSCODE_LENGTH) { dot ->
                val isFilledDot = dot + 1 <= filledDots
                val dotColor = animateColorAsState(
                    if (isFilledDot) {
                        MaterialTheme.colors.primary
                    } else {
                        MaterialTheme.colors.secondary
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
fun PasscodeRejectedDialog(
    visible: Boolean,
    onDismiss: () -> Unit
) {
    if (visible) {
        AlertDialog(
            shape = MaterialTheme.shapes.large,
            title = { Text(text = "Passcodes do not match!") },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "Try again")
                }
            },
            onDismissRequest = onDismiss
        )
    }
}

@Composable
fun PasscodeKeys(
    viewModel: PinCreationScreenVM,
    modifier: Modifier = Modifier,
) {
    val onEnterKeyClick = { keyTitle: String ->
        viewModel.enterKey(keyTitle)
    }

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "1",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "2",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "3",
                    onClick = onEnterKeyClick
                )
            }
            Spacer(modifier = Modifier.height(height = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "4",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "5",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "6",
                    onClick = onEnterKeyClick
                )
            }
            Spacer(modifier = Modifier.height(height = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "7",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "8",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "9",
                    onClick = onEnterKeyClick
                )
            }
            Spacer(modifier = Modifier.height(height = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PasscodeKey(modifier = Modifier.weight(weight = 1.0F))
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "0",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyIcon = ImageVector.vectorResource(id = R.drawable.ic_delete),
                    keyIconContentDescription = "Delete Passcode Key Button",
                    onClick = {
                        viewModel.deleteKey()
                    },
                    onLongClick = {
                        viewModel.deleteAllKeys()
                    }
                )
            }
        }
    }
}


@Composable
private fun PasscodeKey(
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CombinedClickableIconButton(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    rippleRadius: Dp = 36.dp,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(size = size)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    radius = rippleRadius,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colors.onBackground
                    } else {
                        MaterialTheme.colors.primary
                    }
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha, content = content)
    }
}



@Suppress("DEPRECATION")
fun vibrateFeedback(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).vibrate(
            CombinedVibration.createParallel(
                VibrationEffect.createOneShot(
                    VIBRATE_FEEDBACK_DURATION,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        )
    } else {
        (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(
                    VibrationEffect.createOneShot(
                        VIBRATE_FEEDBACK_DURATION,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                it.vibrate(VIBRATE_FEEDBACK_DURATION)
            }
        }
    }
}
