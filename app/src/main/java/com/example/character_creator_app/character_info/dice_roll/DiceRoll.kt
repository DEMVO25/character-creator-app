package com.example.character_creator_app.character_info.dice_roll

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
fun DiceRoller() {
    val diceTypes = listOf(4, 6, 8, 10, 12, 20, 100)
    var diceCount by remember { mutableIntStateOf(1) }
    var rollResults by remember { mutableStateOf<List<Int>>(emptyList()) }
    var selectedDie by remember { mutableStateOf(20) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dice_animation))
    var isPlaying by remember { mutableStateOf(false) }
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
        if (progress == 1f) isPlaying = false
    }

    rememberShakeDetector { rollDice() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.dice_roller_label),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.dice_amount_label),
                style = MaterialTheme.typography.bodyLarge
            )
            (1..5).forEach { amount ->
                FilterChip(
                    selected = diceCount == amount,
                    onClick = { diceCount = amount },
                    label = { Text(amount.toString()) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                val cubesToShow =
                    if (isPlaying) diceCount else (if (rollResults.isEmpty()) 1 else rollResults.size)
                repeat(cubesToShow) { index ->
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                rollDice()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        LottieAnimation(
                            composition = composition,
                            progress = if (isPlaying) progress else 1f,
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(if (isPlaying) 1f else 0.7f)
                        )
                        if (!isPlaying && rollResults.isNotEmpty()) {
                            val result = rollResults.getOrNull(index) ?: 0
                            Text(
                                text = result.toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                color = if (result == selectedDie) Color(0xFFFFD700) else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            if (!isPlaying && rollResults.size > 1) {
                val total = rollResults.sum()
                Text(
                    text = stringResource(R.string.dice_total_label, total),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))
            Text(
                stringResource(R.string.select_dice),
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                diceTypes.forEach { sides ->
                    InputChip(
                        selected = selectedDie == sides,
                        onClick = {
                            selectedDie = sides
                            rollDice()
                        },
                        label = { Text(stringResource(R.string.dice_sides_label, sides)) }
                    )
                }
            }
        }
    }
}

@Composable
fun rememberShakeDetector(onShake: () -> Unit) {
    val context = LocalContext.current
    val sensorManager =
        remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    DisposableEffect(Unit) {
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
                        onShake()
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