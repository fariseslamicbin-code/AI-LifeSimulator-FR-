package com.lifesimulator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lifesimulator.data.CAREER_SCENARIOS
import com.lifesimulator.ui.components.*
import com.lifesimulator.ui.theme.*

@Composable
fun ScenarioScreen(
    selected: String?,
    error: String?,
    onSelect: (String) -> Unit,
    onBack: () -> Unit,
    onRun: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppCard {
            SectionLabel("Step 02 — The Pivot")
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Where do you\nwant to go?",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontFamily = DmSerifDisplay,
                    color = TextPrimary,
                    lineHeight = 34.sp
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Pick the career direction you're considering. We'll run the numbers.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, lineHeight = 20.sp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2-column grid of scenario cards
            val rows = CAREER_SCENARIOS.chunked(2)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                rows.forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { scenario ->
                            ScenarioCard(
                                emoji = scenario.emoji,
                                title = scenario.title,
                                description = scenario.description,
                                isSelected = selected == scenario.id,
                                onClick = { onSelect(scenario.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Pad if odd
                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            if (error != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall.copy(color = Red),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GhostButton("← Back", onBack)
            PrimaryButton("Run simulation →", onRun, enabled = selected != null)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ScenarioCard(
    emoji: String,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) Accent.copy(alpha = 0.06f) else Surface2)
            .border(
                width = 1.dp,
                color = if (isSelected) Accent else Border,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(18.dp)
    ) {
        Column {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(Accent)
                        .align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(text = emoji, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary,
                    fontFamily = DmSans
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    lineHeight = 18.sp
                )
            )
        }
    }
}
