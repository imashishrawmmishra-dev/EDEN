package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompetencyEntity
import com.example.viewmodel.EdenViewModel

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val relatedCompetencyId: String
)

@Composable
fun LearningScreen(
    viewModel: EdenViewModel,
    modifier: Modifier = Modifier
) {
    val competencies by viewModel.competencies.collectAsState()

    var showQuiz by remember { mutableStateOf(false) }
    var currentQuestionIdx by remember { mutableIntStateOf(0) }
    var selectedOptionIdx by remember { mutableStateOf<Int?>(null) }
    var hasAnswered by remember { mutableStateOf(false) }

    val quizQuestions = remember {
        listOf(
            QuizQuestion(
                question = "What does a BOD₅/COD ratio greater than 0.5 typically indicate in wastewater treatment?",
                options = listOf(
                    "The wastewater is recalcitrant and requires advanced chemical oxidation",
                    "The wastewater is readily biodegradable and suitable for biological activated sludge",
                    "The wastewater contains high concentrations of toxic heavy metals",
                    "The wastewater has zero dissolved oxygen depletion potential"
                ),
                correctIndex = 1,
                explanation = "A BOD/COD ratio >= 0.5 indicates organic content that microorganisms can rapidly assimilate, making biological secondary treatment (like CAS or MBR) highly effective.",
                relatedCompetencyId = "comp_water_quality"
            ),
            QuizQuestion(
                question = "Under the GHG Protocol Corporate Standard, which category does electricity purchased from the regional utility grid fall into?",
                options = listOf(
                    "Scope 1 (Direct emissions)",
                    "Scope 2 (Indirect electricity emissions)",
                    "Scope 3 (Upstream value chain)",
                    "Out-of-Scope biogenic emissions"
                ),
                correctIndex = 1,
                explanation = "Scope 2 accounts for indirect GHG emissions from the generation of purchased or acquired electricity, steam, heating, or cooling consumed by the organization.",
                relatedCompetencyId = "comp_carbon_accounting"
            ),
            QuizQuestion(
                question = "According to the revised 2021 WHO Global Air Quality Guidelines, what is the recommended annual mean concentration limit for PM2.5?",
                options = listOf(
                    "35.0 µg/m³",
                    "25.0 µg/m³",
                    "10.0 µg/m³",
                    "5.0 µg/m³"
                ),
                correctIndex = 3,
                explanation = "WHO revised its annual PM2.5 threshold down to 5 µg/m³ (from 10 µg/m³) based on extensive epidemiological evidence showing adverse cardiovascular effects even at low ambient exposures.",
                relatedCompetencyId = "comp_air_dispersion"
            ),
            QuizQuestion(
                question = "In ISO 14040/14044 Life Cycle Assessment, what is the mandatory first phase of the study?",
                options = listOf(
                    "Life Cycle Inventory Analysis (LCI)",
                    "Goal and Scope Definition (including Functional Unit & System Boundaries)",
                    "Life Cycle Impact Assessment (LCIA)",
                    "Interpretation and Sensitivity Evaluation"
                ),
                correctIndex = 1,
                explanation = "ISO 14040 mandates defining the Goal and Scope first, establishing the functional unit, reference flows, system boundary (e.g. cradle-to-gate), and data quality requirements.",
                relatedCompetencyId = "comp_lca_assessment"
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Learning Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Environmental Learning Engine",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Adaptive 5-level professional competency roadmap",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "School",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            showQuiz = !showQuiz
                            selectedOptionIdx = null
                            hasAnswered = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (showQuiz) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("interactive_quiz_toggle_button")
                    ) {
                        Text(if (showQuiz) "Close Assessment Quiz" else "Start Scientific Assessment Quiz")
                    }
                }
            }
        }

        // Quiz Section
        if (showQuiz && currentQuestionIdx < quizQuestions.size) {
            val q = quizQuestions[currentQuestionIdx]
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        .testTag("quiz_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Question ${currentQuestionIdx + 1} of ${quizQuestions.size}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "Standard Verification",
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = q.question,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        q.options.forEachIndexed { idx, optionText ->
                            val isSelected = selectedOptionIdx == idx
                            val isCorrect = idx == q.correctIndex
                            val bg = when {
                                hasAnswered && isCorrect -> Color(0xFFE8F5E9)
                                hasAnswered && isSelected && !isCorrect -> Color(0xFFFFEBEE)
                                isSelected -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surface
                            }
                            val border = when {
                                hasAnswered && isCorrect -> Color(0xFF2E7D32)
                                hasAnswered && isSelected && !isCorrect -> Color(0xFFC62828)
                                isSelected -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(bg)
                                    .border(1.dp, border, RoundedCornerShape(10.dp))
                                    .clickable(enabled = !hasAnswered) { selectedOptionIdx = idx }
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = optionText,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (!hasAnswered) {
                            Button(
                                onClick = {
                                    if (selectedOptionIdx != null) {
                                        hasAnswered = true
                                        if (selectedOptionIdx == q.correctIndex) {
                                            viewModel.updateCompetency(q.relatedCompetencyId, 80)
                                        }
                                    }
                                },
                                enabled = selectedOptionIdx != null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Submit Answer")
                            }
                        } else {
                            // Explanation
                            Text(
                                text = if (selectedOptionIdx == q.correctIndex) "✓ Correct!" else "✗ Incorrect",
                                fontWeight = FontWeight.Bold,
                                color = if (selectedOptionIdx == q.correctIndex) Color(0xFF2E7D32) else Color(0xFFC62828),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = q.explanation,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedButton(
                                onClick = {
                                    if (currentQuestionIdx < quizQuestions.size - 1) {
                                        currentQuestionIdx++
                                        selectedOptionIdx = null
                                        hasAnswered = false
                                    } else {
                                        showQuiz = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (currentQuestionIdx < quizQuestions.size - 1) "Next Question" else "Finish Assessment")
                            }
                        }
                    }
                }
            }
        }

        // Competencies List
        item {
            Text(
                text = "Tracked Environmental Competencies",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(competencies) { comp ->
            CompetencyCard(competency = comp)
        }
    }
}

@Composable
private fun CompetencyCard(competency: CompetencyEntity) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = competency.category.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "Target: Level ${competency.targetLevel} (Regulatory)",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = competency.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mastery Progress", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${competency.progressPercent}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { competency.progressPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}
