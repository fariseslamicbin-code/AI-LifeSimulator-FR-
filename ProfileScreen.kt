package com.lifesimulator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lifesimulator.data.SKILLS_LIST
import com.lifesimulator.data.UserProfile
import com.lifesimulator.ui.components.*
import com.lifesimulator.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profile: UserProfile,
    onUpdate: (UserProfile.() -> UserProfile) -> Unit,
    onToggleSkill: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val isValid = profile.age.isNotBlank() && profile.currentJob.isNotBlank() && profile.salary.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppCard {
            SectionLabel("Step 01 — Your Profile")
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Tell us where you are",
                style = MaterialTheme.typography.displaySmall.copy(fontFamily = DmSerifDisplay, color = TextPrimary)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "This is the baseline. The more accurate, the sharper the simulation.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, lineHeight = 20.sp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Age + Country
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    FieldLabel("Age")
                    AppTextField(
                        value = profile.age,
                        onValueChange = { onUpdate { copy(age = it) } },
                        placeholder = "28",
                        keyboardType = KeyboardType.Number
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    FieldLabel("Country")
                    AppTextField(
                        value = profile.country,
                        onValueChange = { onUpdate { copy(country = it) } },
                        placeholder = "United States"
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            FieldLabel("Current Job Title")
            AppTextField(
                value = profile.currentJob,
                onValueChange = { onUpdate { copy(currentJob = it) } },
                placeholder = "Marketing Manager"
            )

            Spacer(modifier = Modifier.height(14.dp))

            FieldLabel("Annual Salary (USD)")
            AppTextField(
                value = profile.salary,
                onValueChange = { onUpdate { copy(salary = it) } },
                placeholder = "65000",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(14.dp))

            FieldLabel("Education Level")
            DropdownField(
                selected = profile.education,
                options = listOf(
                    "highschool" to "High School",
                    "some-college" to "Some College",
                    "bachelor" to "Bachelor's Degree",
                    "master" to "Master's Degree",
                    "phd" to "PhD / Doctorate",
                    "bootcamp" to "Bootcamp / Self-taught"
                ),
                onSelect = { onUpdate { copy(education = it) } }
            )

            Spacer(modifier = Modifier.height(14.dp))

            FieldLabel("Skills")
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(SKILLS_LIST, profile.skills, onToggleSkill)

            Spacer(modifier = Modifier.height(14.dp))

            FieldLabel("Risk Tolerance")
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("low" to "🛡️ Conservative", "medium" to "⚖️ Balanced", "high" to "🔥 Bold").forEach { (v, label) ->
                    ToggleButton(
                        text = label,
                        selected = profile.risk == v,
                        onClick = { onUpdate { copy(risk = v) } },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            FieldLabel("Time Horizon")
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("1" to "1 Year", "3" to "3 Years", "5" to "5 Years").forEach { (v, label) ->
                    ToggleButton(
                        text = label,
                        selected = profile.horizon == v,
                        onClick = { onUpdate { copy(horizon = v) } },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GhostButton("← Back", onBack)
            PrimaryButton("Choose scenario →", onNext, enabled = isValid)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(
            color = TextMuted,
            fontFamily = DmMono,
            letterSpacing = 0.5.sp
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Surface2,
            unfocusedContainerColor = Surface2,
            focusedBorderColor = Accent,
            unfocusedBorderColor = Border2,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = Accent
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontFamily = DmSans)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    selected: String,
    options: List<Pair<String, String>>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.find { it.first == selected }?.second ?: selected

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Surface2,
                unfocusedContainerColor = Surface2,
                focusedBorderColor = Accent,
                unfocusedBorderColor = Border2,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontFamily = DmSans)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Surface)
        ) {
            options.forEach { (value, label) ->
                DropdownMenuItem(
                    text = { Text(label, style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)) },
                    onClick = { onSelect(value); expanded = false },
                    modifier = Modifier.background(Surface)
                )
            }
        }
    }
}

@Composable
fun FlowRow(items: List<String>, selected: List<String>, onToggle: (String) -> Unit) {
    var rowItems = items.chunked(4)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rowItems.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { skill ->
                    Chip(text = skill, selected = skill in selected, onClick = { onToggle(skill) })
                }
            }
        }
    }
}
