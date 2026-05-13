package com.example.character_creator_app.character_info.dice_roll

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.character_creator_app.R
import kotlin.math.sqrt

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DiceRoller(
    diceCount: Int,
    onCountChange: (Int) -> Unit,
    selectedDie: Int,
    onDieChange: (Int) -> Unit,
    diceTypes: List<Int>
) {
    var rollResults by remember { mutableStateOf<List<Int>>(emptyList()) }
    var isPlaying by remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dice_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        restartOnPlay = true,
        speed = 1.5f
    )

    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    val rollDice = {
        if (!isPlaying) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            isPlaying = true
            rollResults = List(diceCount) { (1..selectedDie).random() }
        }
    }

    LaunchedEffect(progress) {
        if (progress == 1f) {
            isPlaying = false
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    rememberShakeDetector { rollDice() }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val cubesToShow =
                    if (isPlaying) diceCount else (if (rollResults.isEmpty()) 1 else rollResults.size)

                repeat(cubesToShow) { index ->
                    DiceBox(
                        result = rollResults.getOrNull(index),
                        isPlaying = isPlaying,
                        progress = progress,
                        composition = composition,
                        maxVal = selectedDie,
                        onClick = { rollDice() }
                    )
                }
            }

            if (!isPlaying && rollResults.size > 1) {
                Text(
                    text = stringResource(R.string.dice_total_label, rollResults.sum()),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            tonalElevation = 2.dp,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.dice_amount_label).uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    val options = listOf(1, 2, 3, 4, 5)
                    options.forEachIndexed { index, amount ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = {
                                onCountChange(amount)
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            },
                            selected = diceCount == amount,
                            label = { Text(amount.toString()) }
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.select_dice).uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement = Arrangement.spacedBy(
                        12.dp,
                        Alignment.CenterHorizontally
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(diceTypes) { sides ->
                        DiceTypeTile(
                            sides = sides,
                            isSelected = selectedDie == sides,
                            onClick = {
                                onDieChange(sides)
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DiceTypeTile(sides: Int, isSelected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Surface(
        modifier = Modifier
            .size(64.dp)
            .clip(shape)
            .clickable { onClick() },
        shape = shape,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = 0.5f
        ),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "d$sides",
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DiceBox(
    result: Int?,
    isPlaying: Boolean,
    progress: Float,
    composition: com.airbnb.lottie.LottieComposition?,
    maxVal: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = if (isPlaying) progress else 1f,
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (isPlaying) 1f else 0.6f)
        )
        if (!isPlaying && result != null) {
            Text(
                text = result.toString(),
                style = MaterialTheme.typography.headlineLarge,
                color = if (result == maxVal) Color(0xFFFFD700) else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun rememberShakeDetector(onShake: () -> Unit) {
    val context = LocalContext.current
    val currentOnShake by rememberUpdatedState(onShake)

    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    DisposableEffect(sensorManager) {
        val listener = object : SensorEventListener {
            private var lastShakeTime: Long = 0

            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val gForce = sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH
                if (gForce > 2.7f) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastShakeTime > 500) {
                        lastShakeTime = currentTime
                        currentOnShake()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }
}