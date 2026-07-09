package com.lifesimulator.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lifesimulator.data.CAREER_SCENARIOS
import com.lifesimulator.ui.components.AppCard
import com.lifesimulator.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(currentJob: String, scenarioId: String) {
    val loadingMessages = listOf(
        "Analyzing your profile…",
        "Cross-referencing career data…",
        "Modeling income trajectories…",
        "Running probability bands…",
        "Crafting your simulation…"
    )
    var messageIndex by remember { mutableStateOf(0) }
    val targetCareer = CAREER_SCENARIOS.find { it.id == scenarioId }?.title ?: scenarioId

    LaunchedEffect(Unit) {
        while (true) {
            delay(2200)
            messageIndex = (messageIndex + 1) % loadingMessages.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppCard {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated dots
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) { i ->
                        PulseDot(delayMs = i * 200)
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = loadingMessages[messageIndex],
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        fontFamily = DmMono,
                        letterSpacing = 2.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "$currentJob → $targetCareer",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                )
            }
        }
    }
}

@Composable
fun PulseDot(delayMs: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1400
                0.6f at 0
                1f at 560
                0.6f at 1120
                0.6f at 1400
            },
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(delayMs)
        ),
        label = "scale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1400
                0.3f at 0
                1f at 560
                0.3f at 1120
                0.3f at 1400
            },
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(delayMs)
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .size(10.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(Accent.copy(alpha = alpha))
    )
}
