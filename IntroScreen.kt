package com.lifesimulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lifesimulator.ui.components.PrimaryButton
import com.lifesimulator.ui.theme.*

@Composable
fun IntroScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Logo label
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.width(36.dp).height(1.dp).background(Accent.copy(alpha = 0.4f)))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "AI LIFE SIMULATOR",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Accent,
                    letterSpacing = 3.sp,
                    fontFamily = DmMono
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier.width(36.dp).height(1.dp).background(Accent.copy(alpha = 0.4f)))
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Headline
        Text(
            text = buildAnnotatedString {
                append("What if you\n")
                withStyle(SpanStyle(color = Accent, fontStyle = FontStyle.Italic, fontFamily = DmSerifDisplay)) {
                    append("changed")
                }
                append("\neverything?")
            },
            style = MaterialTheme.typography.displayLarge.copy(
                fontFamily = DmSerifDisplay,
                color = TextPrimary,
                lineHeight = 60.sp
            ),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Enter your details. Pick a direction.\nGet a brutally honest simulation of your future.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = TextMuted,
                lineHeight = 26.sp
            ),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Feature tags
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 48.dp)
        ) {
            listOf("5 min setup", "AI-powered", "Honest results").forEach { tag ->
                Box(
                    modifier = Modifier
                        .border(1.dp, Border, CircleShape)
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMuted,
                            fontFamily = DmMono
                        )
                    )
                }
            }
        }

        PrimaryButton(
            text = "Start your simulation →",
            onClick = onStart,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}
