package com.lifesimulator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lifesimulator.data.SimulationResult
import com.lifesimulator.ui.components.*
import com.lifesimulator.ui.theme.*

@Composable
fun ResultsScreen(result: SimulationResult, onReset: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Disclaimer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Accent.copy(alpha = 0.06f))
                .border(1.dp, Accent.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚠  Simulation · Not a prediction · Probabilistic modeling",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Accent,
                    fontFamily = DmMono,
                    letterSpacing = 0.5.sp
                )
            )
        }

        // VERDICT
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    androidx.compose.ui.graphics.Brush.linearGradient(
                        listOf(Accent.copy(alpha = 0.08f), Accent2.copy(alpha = 0.04f))
                    )
                )
                .border(1.dp, Accent.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "THE HONEST TAKE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Accent,
                        fontFamily = DmMono,
                        letterSpacing = 2.sp
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = result.verdict,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontFamily = DmSerifDisplay,
                        fontStyle = FontStyle.Italic,
                        color = TextPrimary,
                        lineHeight = 32.sp,
                        fontSize = 20.sp
                    )
                )
            }
        }

        // TIMELINE
        AppCard {
            SectionLabel("Income Timeline")
            Spacer(modifier = Modifier.height(20.dp))
            TimelineView(result)
        }

        // METRICS (2x2 grid)
        val metricDefs = listOf(
            Triple("💰", "INCOME GROWTH", listOf(
                Triple("Optimistic", result.metrics.income.optimistic, Green),
                Triple("Realistic", result.metrics.income.realistic, Green),
                Triple("Pessimistic", result.metrics.income.pessimistic, Green)
            )),
            Triple("😌", "STRESS LEVEL", listOf(
                Triple("Optimistic", result.metrics.stress.optimistic, Red),
                Triple("Realistic", result.metrics.stress.realistic, Red),
                Triple("Pessimistic", result.metrics.stress.pessimistic, Red)
            )),
            Triple("⏳", "FREE TIME", listOf(
                Triple("Optimistic", result.metrics.freeTime.optimistic, Blue),
                Triple("Realistic", result.metrics.freeTime.realistic, Blue),
                Triple("Pessimistic", result.metrics.freeTime.pessimistic, Blue)
            )),
            Triple("📈", "CAREER GROWTH", listOf(
                Triple("Optimistic", result.metrics.growth.optimistic, Purple),
                Triple("Realistic", result.metrics.growth.realistic, Purple),
                Triple("Pessimistic", result.metrics.growth.pessimistic, Purple)
            ))
        )

        val rows = metricDefs.chunked(2)
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { (emoji, name, bands) ->
                    MetricCard(emoji, name, bands, modifier = Modifier.weight(1f))
                }
            }
        }

        // SKILL GAPS
        AppCard {
            SectionLabel("Skill Gaps to Bridge")
            Spacer(modifier = Modifier.height(16.dp))
            FlowTagRow(result.skillGaps)
        }

        // PROBABILITY
        AppCard {
            SectionLabel("Outcome Probability")
            Spacer(modifier = Modifier.height(16.dp))
            result.probability.forEach { (_, band) ->
                val bandColor = parseHexColor(band.color)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = band.label,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted, fontFamily = DmMono
                        ),
                        modifier = Modifier.width(110.dp)
                    )
                    AnimatedBar(
                        targetValue = band.bar / 100f,
                        color = bandColor,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = band.value,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = bandColor, fontFamily = DmMono
                        ),
                        modifier = Modifier.width(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = band.detail,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted, lineHeight = 18.sp
                    ),
                    modifier = Modifier.padding(start = 110.dp, bottom = 14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            GhostButton("← Run another simulation", onReset)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TimelineView(result: SimulationResult) {
    if (result.timeline.isEmpty()) return
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        result.timeline.forEachIndexed { index, point ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (index > 0) Accent else Surface2)
                        .border(2.dp, if (index > 0) Accent else Border2, CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = point.year,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted, fontFamily = DmMono
                    )
                )
                Text(
                    text = point.income,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary, fontFamily = DmMono
                    )
                )
                if (point.delta.isNotBlank()) {
                    Text(
                        text = point.delta,
                        style = MaterialTheme.typography.bodySmall.copy(color = Green, fontSize = 11.sp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = point.note,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted, fontSize = 10.sp, lineHeight = 14.sp
                    ),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun MetricCard(
    emoji: String,
    name: String,
    bands: List<Triple<String, Int, Color>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .border(1.dp, Border, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Text(text = emoji, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextMuted, fontFamily = DmMono, letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        bands.forEachIndexed { i, (label, value, color) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label.take(4),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted, fontFamily = DmMono, fontSize = 9.sp
                    ),
                    modifier = Modifier.width(32.dp)
                )
                AnimatedBar(
                    targetValue = value / 100f,
                    color = color,
                    delay = i * 200,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$value%",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = color, fontFamily = DmMono, fontSize = 9.sp
                    ),
                    modifier = Modifier.width(28.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
fun FlowTagRow(tags: List<String>) {
    val rows = tags.chunked(2)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { tag ->
                    MonoTag(text = tag, color = Blue)
                }
            }
        }
    }
}

fun parseHexColor(hex: String): Color {
    return try {
        val cleaned = hex.removePrefix("#")
        val value = cleaned.toLong(16)
        when (cleaned.length) {
            6 -> Color(0xFF000000 or (value and 0xFFFFFF))
            8 -> Color(value)
            else -> Accent
        }
    } catch (e: Exception) {
        Accent
    }
}
