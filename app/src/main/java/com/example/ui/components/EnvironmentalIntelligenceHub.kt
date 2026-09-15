package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EnvironmentalDomain
import com.example.data.model.EnvironmentalTopic
import com.example.data.model.QualityParameterBand
import com.example.data.repository.EnvironmentalKnowledgeRepository
import com.example.viewmodel.EdenViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EnvironmentalIntelligenceHub(
    viewModel: EdenViewModel,
    modifier: Modifier = Modifier
) {
    var selectedDomain by remember { mutableStateOf(EnvironmentalDomain.ALL) }
    var expandedTopicId by remember { mutableStateOf<String?>("air_quality") }
    val observations by viewModel.topicObservations.collectAsState()

    val topics = remember(selectedDomain) {
        EnvironmentalKnowledgeRepository.getTopicsForDomain(selectedDomain)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("environmental_intelligence_hub"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Header Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                    RoundedCornerShape(20.dp)
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = "Intelligence Hub",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Environmental Intelligence",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFE8F5E9)
                            ) {
                                Text(
                                    text = "BASIC TO ADVANCED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Comprehensive parameter charts, ISO/WHO management, ongoing global researches & field inputs",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Domain Filter Scroll Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EnvironmentalDomain.entries.forEach { domain ->
                        val isSelected = domain == selectedDomain
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedDomain = domain },
                            label = {
                                Text(
                                    text = domain.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = getDomainIcon(domain),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }
        }

        // Topic Cards List
        topics.forEach { topic ->
            val isExpanded = expandedTopicId == topic.id
            TopicComprehensiveCard(
                topic = topic,
                isExpanded = isExpanded,
                onToggleExpand = {
                    expandedTopicId = if (isExpanded) null else topic.id
                },
                viewModel = viewModel,
                observations = observations.filter { it.topicId == topic.id }
            )
        }
    }
}

@Composable
private fun TopicComprehensiveCard(
    topic: EnvironmentalTopic,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    viewModel: EdenViewModel,
    observations: List<com.example.data.model.TopicUserObservation>
) {
    val context = LocalContext.current
    val domainColor = getDomainThemeColor(topic.domain)

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isExpanded) domainColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                RoundedCornerShape(18.dp)
            )
            .testTag("topic_card_${topic.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
            ) {
                Surface(
                    shape = CircleShape,
                    color = domainColor.copy(alpha = 0.12f),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = getDomainIcon(topic.domain),
                            contentDescription = topic.title,
                            tint = domainColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = topic.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = topic.subtitle,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onToggleExpand) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Toggle",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Quick Badge Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = domainColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = topic.levelBadge,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = domainColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = topic.qualityManagement.certificationCode,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFE0F2F1)
                ) {
                    Text(
                        text = "${topic.ongoingResearches.size} Ongoing Researches",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00796B),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            // Expanded Full Content
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // 1. SECTION DESCRIPTION: BASIC & ADVANCED SCIENCE
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, tint = domainColor, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Fundamental Principle (Basic)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = topic.basicDescription,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Science, contentDescription = null, tint = domainColor, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Deep Scientific Mechanism (Advanced)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = domainColor
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = topic.advancedScience,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // 2. SECTION CHART & PARAMETER BANDS
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, domainColor.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Assessment, contentDescription = null, tint = domainColor, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Section Parameter Chart & Quality Benchmarks",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            topic.primaryIndicators.forEach { indicator ->
                                ParameterBarRow(indicator = indicator, domainColor = domainColor)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    // 3. QUALITY MANAGEMENT TYPE & STANDARDS
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFF1F8E9),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFF81C784).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Quality Management System (QMS)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color(0xFF1B5E20)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFC8E6C9)
                                ) {
                                    Text(
                                        text = topic.qualityManagement.governingBody,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B5E20),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Framework: ${topic.qualityManagement.frameworkName}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            Text(
                                text = topic.qualityManagement.scope,
                                fontSize = 11.sp,
                                color = Color(0xFF33691E),
                                lineHeight = 15.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Standard Compliance Protocols:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                            topic.qualityManagement.complianceChecklist.forEach { checkItem ->
                                Row(
                                    modifier = Modifier.padding(top = 3.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = checkItem, fontSize = 10.sp, color = Color(0xFF33691E), lineHeight = 14.sp)
                                }
                            }
                        }
                    }

                    // 4. ONGOING GLOBAL RESEARCHES SECTION
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Biotech, contentDescription = null, tint = Color(0xFF00796B), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Active Global Research & Breakthroughs",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = "Current academic, orbital & institutional investigations worldwide:",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            topic.ongoingResearches.forEach { research ->
                                ResearchCardItem(research = research)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    // 5. INTERACTIVE FIELD READING & RESEARCH NOTE INPUT
                    TopicFieldInputSection(
                        topic = topic,
                        viewModel = viewModel,
                        domainColor = domainColor,
                        observations = observations
                    )
                }
            }
        }
    }
}

@Composable
private fun ParameterBarRow(
    indicator: QualityParameterBand,
    domainColor: Color
) {
    val progress = (indicator.currentBaseline / indicator.maxChartScale).coerceIn(0f, 1f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = indicator.parameterName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "Standard: ${indicator.regulatoryStandard} (${indicator.standardLimit} ${indicator.unit})", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${indicator.currentBaseline} ${indicator.unit}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = domainColor
                )
                Text(text = "Target: ${indicator.optimalRange}", fontSize = 9.sp, color = Color(0xFF2E7D32))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = domainColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun ResearchCardItem(research: com.example.data.model.OngoingResearch) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = research.title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFE0F2F1)
                ) {
                    Text(
                        text = research.stage,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00796B),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "${research.institution} • ${research.leadLocation}",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF00796B)
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = research.summary,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Reference: ${research.publicationOrRef}",
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TopicFieldInputSection(
    topic: EnvironmentalTopic,
    viewModel: EdenViewModel,
    domainColor: Color,
    observations: List<com.example.data.model.TopicUserObservation>
) {
    val context = LocalContext.current
    var inputValue by remember { mutableStateOf("") }
    var locationInput by remember { mutableStateOf("") }
    var notesInput by remember { mutableStateOf("") }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, domainColor.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AddCircle, contentDescription = null, tint = domainColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Field Reading & User Research Input",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = topic.thresholdGuideline,
                fontSize = 10.sp,
                color = domainColor,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = inputValue,
                onValueChange = { inputValue = it },
                label = { Text(topic.defaultInputPrompt, fontSize = 11.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_val_${topic.id}"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = locationInput,
                    onValueChange = { locationInput = it },
                    label = { Text("Field Station / City", fontSize = 11.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_loc_${topic.id}"),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = notesInput,
                onValueChange = { notesInput = it },
                label = { Text("Observation notes or research hypothesis", fontSize = 11.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_notes_${topic.id}"),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (inputValue.isBlank()) {
                        Toast.makeText(context, "Please enter a reading value", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val status = evaluateCompliance(topic.id, inputValue)
                    viewModel.addTopicObservation(
                        topicId = topic.id,
                        parameterValue = "$inputValue ${topic.inputUnit}",
                        location = locationInput,
                        notes = notesInput,
                        complianceStatus = status
                    )
                    inputValue = ""
                    locationInput = ""
                    notesInput = ""
                    Toast.makeText(context, "✓ Logged to Research Records! +10 EcoPoints", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .testTag("btn_log_observation_${topic.id}"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = domainColor)
            ) {
                Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Log Field Research Record (+10 Pts)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            // Display Logged Observations for this Topic
            if (observations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Saved Field & Research Logs (${observations.size}):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))

                observations.forEach { obs ->
                    ObservationItemRow(obs = obs, onDelete = { viewModel.deleteTopicObservation(obs.id) })
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
private fun ObservationItemRow(
    obs: com.example.data.model.TopicUserObservation,
    onDelete: () -> Unit
) {
    val dateStr = remember(obs.timestamp) {
        val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        sdf.format(Date(obs.timestamp))
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = obs.parameterValue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (obs.complianceStatus == "Compliant") Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                    ) {
                        Text(
                            text = obs.complianceStatus,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (obs.complianceStatus == "Compliant") Color(0xFF2E7D32) else Color(0xFFE65100),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = dateStr, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(text = "📍 ${obs.location}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = obs.observationNotes, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
            }
        }
    }
}

private fun evaluateCompliance(topicId: String, valueStr: String): String {
    val num = valueStr.filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: return "Recorded"
    return when (topicId) {
        "air_quality" -> if (num <= 15.0) "Compliant" else "Advisory"
        "water_quality" -> if (num >= 6.5) "Compliant" else "Advisory"
        "animals_biodiversity" -> if (num >= 2.5) "Compliant" else "Advisory"
        "physical_qualities" -> if (num <= 2.0) "Compliant" else "Advisory"
        "soil_control" -> if (num >= 3.0) "Compliant" else "Advisory"
        "noise_quality" -> if (num <= 55.0) "Compliant" else "Advisory"
        "food_microbiology" -> if (num <= 4.0) "Compliant" else "Advisory"
        "pollution_testing" -> if (num in 90.0..110.0) "Compliant" else "Advisory"
        "pollution_monitoring" -> if (num >= 95.0) "Compliant" else "Advisory"
        "pollution_remediation" -> if (num >= 85.0) "Compliant" else "Advisory"
        else -> "Compliant"
    }
}

private fun getDomainIcon(domain: EnvironmentalDomain): ImageVector {
    return when (domain) {
        EnvironmentalDomain.ALL -> Icons.Default.Public
        EnvironmentalDomain.AIR_QUALITY -> Icons.Default.Air
        EnvironmentalDomain.WATER_QUALITY -> Icons.Default.WaterDrop
        EnvironmentalDomain.ANIMALS_BIODIVERSITY -> Icons.Default.Pets
        EnvironmentalDomain.PHYSICAL_QUALITIES -> Icons.Default.WbSunny
        EnvironmentalDomain.SOIL_CONTROL -> Icons.Default.Landscape
        EnvironmentalDomain.NOISE_QUALITY -> Icons.Default.Hearing
        EnvironmentalDomain.FOOD_MICROBIOLOGY -> Icons.Default.Restaurant
        EnvironmentalDomain.POLLUTION_TESTING -> Icons.Default.Science
        EnvironmentalDomain.POLLUTION_MONITORING -> Icons.Default.Sensors
        EnvironmentalDomain.POLLUTION_REMEDIATION -> Icons.Default.Recycling
    }
}

private fun getDomainThemeColor(domain: EnvironmentalDomain): Color {
    return when (domain) {
        EnvironmentalDomain.ALL -> Color(0xFF0F6E43)
        EnvironmentalDomain.AIR_QUALITY -> Color(0xFF0288D1)
        EnvironmentalDomain.WATER_QUALITY -> Color(0xFF00796B)
        EnvironmentalDomain.ANIMALS_BIODIVERSITY -> Color(0xFF388E3C)
        EnvironmentalDomain.PHYSICAL_QUALITIES -> Color(0xFFF57C00)
        EnvironmentalDomain.SOIL_CONTROL -> Color(0xFF795548)
        EnvironmentalDomain.NOISE_QUALITY -> Color(0xFF7B1FA2)
        EnvironmentalDomain.FOOD_MICROBIOLOGY -> Color(0xFFD32F2F)
        EnvironmentalDomain.POLLUTION_TESTING -> Color(0xFF512DA8)
        EnvironmentalDomain.POLLUTION_MONITORING -> Color(0xFF0097A7)
        EnvironmentalDomain.POLLUTION_REMEDIATION -> Color(0xFF2E7D32)
    }
}
