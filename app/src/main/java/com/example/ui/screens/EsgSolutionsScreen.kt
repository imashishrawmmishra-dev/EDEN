package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solutions.model.AudienceType
import com.example.solutions.model.EsgDomainCategory
import com.example.solutions.model.EsgDomainItem
import com.example.solutions.model.EsgSolutionsRepository
import com.example.solutions.model.SustainabilityFormulaItem
import com.example.ui.components.EsgOtpDownloadModal
import com.example.viewmodel.EdenTab
import com.example.viewmodel.EdenViewModel

@Composable
fun EsgSolutionsScreen(
    viewModel: EdenViewModel,
    onNavigate: (EdenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedAudience by remember { mutableStateOf(AudienceType.ALL) }
    var selectedCategory by remember { mutableStateOf<EsgDomainCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var expandedDomainId by remember { mutableStateOf<String?>(null) }
    var showOtpDownloadModal by remember { mutableStateOf(false) }
    var showFormulasSection by remember { mutableStateOf(true) }

    if (showOtpDownloadModal) {
        EsgOtpDownloadModal(
            viewModel = viewModel,
            onDismiss = { showOtpDownloadModal = false }
        )
    }

    val filteredDomains = remember(searchQuery, selectedCategory) {
        EsgSolutionsRepository.domains.filter { domain ->
            val matchesCategory = selectedCategory == null || domain.category == selectedCategory
            val matchesSearch = if (searchQuery.isBlank()) true else {
                domain.title.contains(searchQuery, ignoreCase = true) ||
                domain.summary.contains(searchQuery, ignoreCase = true) ||
                domain.requirement.contains(searchQuery, ignoreCase = true) ||
                domain.jobs.any { it.title.contains(searchQuery, ignoreCase = true) } ||
                domain.toolsAndSoftware.any { it.name.contains(searchQuery, ignoreCase = true) }
            }
            matchesCategory && matchesSearch
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("esg_solutions_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // ==========================================
        // HERO HEADER & DIRECT DOWNLOAD CTA
        // ==========================================
        item {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF0F6E43),
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "ESG, EIA & Green Solutions Hub",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "20 Deep Environmental Domains • Professional Solutions",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF0F6E43)
                                )
                            }
                        }

                        // OTP Download Action Pill
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF0F6E43),
                            contentColor = Color.White,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { showOtpDownloadModal = true }
                                .testTag("hero_open_download_modal_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Downloads (OTP)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Statutory frameworks, academic roadmaps, and industry solutions curated for Students, Professionals, and Researchers. Includes direct links to UN, WHO, EPA, and in-app calculators.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF0F6E43).copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "Last Updated: September 15, 2026 • All 20 Domains Verified",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F6E43),
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = "v2.5 Release",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // ==========================================
        // AUDIENCE TARGET SELECTOR (Student, Pro, Researcher)
        // ==========================================
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = "Tailor Insights For Your Perspective:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                TabRow(
                    selectedTabIndex = selectedAudience.ordinal,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .testTag("audience_tab_row")
                ) {
                    AudienceType.values().forEach { aud ->
                        Tab(
                            selected = selectedAudience == aud,
                            onClick = { selectedAudience = aud },
                            text = {
                                Text(
                                    text = aud.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedAudience == aud) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }

        // ==========================================
        // SEARCH & CATEGORY FILTER CHIPS
        // ==========================================
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search ESG, EIA, GHG, AI, Lab skills, Jobs...", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Info, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("esg_search_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterPill(
                            label = "All Categories (${EsgSolutionsRepository.domains.size})",
                            isSelected = selectedCategory == null,
                            onClick = { selectedCategory = null }
                        )
                    }
                    items(EsgDomainCategory.values()) { cat ->
                        FilterPill(
                            label = cat.displayName,
                            isSelected = selectedCategory == cat,
                            onClick = { selectedCategory = if (selectedCategory == cat) null else cat }
                        )
                    }
                }
            }
        }

        // ==========================================
        // INTERACTIVE FORMULAS & INFOGRAPHICS SECTION
        // ==========================================
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showFormulasSection = !showFormulasSection },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Functions,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Sustainability Formulas & Indices",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Carbon Intensity • Circularity • Water Footprint • Eco-Efficiency",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        IconButton(onClick = { showFormulasSection = !showFormulasSection }) {
                            Icon(
                                imageVector = if (showFormulasSection) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null
                            )
                        }
                    }

                    AnimatedVisibility(visible = showFormulasSection) {
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                            Spacer(modifier = Modifier.height(10.dp))

                            EsgSolutionsRepository.formulas.forEach { formula ->
                                InteractiveFormulaCard(formula = formula)
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // 20 DEEP ENVIRONMENTAL DOMAIN CARDS
        // ==========================================
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Environmental Disciplines & Solutions (${filteredDomains.size})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tap to expand details",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        items(filteredDomains) { domain ->
            val isExpanded = expandedDomainId == domain.id
            EsgDomainCard(
                domain = domain,
                selectedAudience = selectedAudience,
                isExpanded = isExpanded,
                onToggleExpand = {
                    expandedDomainId = if (isExpanded) null else domain.id
                },
                onSearchJobs = {
                    viewModel.setJobSearchDialog(true)
                },
                onAskEden = {
                    viewModel.setAskQuery(domain.askEdenPrompt)
                    onNavigate(EdenTab.ASK_EDEN)
                    viewModel.askEdenQuestion(domain.askEdenPrompt)
                },
                onOpenCalculator = {
                    onNavigate(EdenTab.CALCULATORS)
                },
                onOpenProcedures = {
                    onNavigate(EdenTab.MONITORING_PROCEDURES)
                },
                onOpenExternalUrl = { url ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Could not open link: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // ==========================================
        // BOTTOM PROMINENT DOWNLOAD CTA CARD
        // ==========================================
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0F6E43).copy(alpha = 0.12f)
                ),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0F6E43).copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            tint = Color(0xFF0F6E43),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Download Regulatory Toolkits & Dossiers",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F6E43)
                            )
                            Text(
                                text = "EIA Kits • Scope 1-3 CSV • ISO 17025 SOPs • ML Notebooks",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Access exportable templates, Excel GHG ledgers, chemical compatibility tables, and Python climate scripts after simulated mobile/email OTP authentication.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showOtpDownloadModal = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F6E43)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Verify OTP & Open Downloads Section", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun EsgDomainCard(
    domain: EsgDomainItem,
    selectedAudience: AudienceType,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onSearchJobs: () -> Unit,
    onAskEden: () -> Unit,
    onOpenCalculator: () -> Unit,
    onOpenProcedures: () -> Unit,
    onOpenExternalUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isExpanded) 1.5.dp else 1.dp,
            color = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Top Row: Category + Short Name + Toggle Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = domain.category.displayName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isExpanded) "Hide" else "Details",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = domain.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable { onToggleExpand() }
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = domain.summary,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick High-Impact Tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                QuickTag(label = "Mandate", value = domain.requirement.take(38) + "...")
                QuickTag(label = "Purpose", value = domain.purpose.take(38) + "...")
            }

            // EXPANDED DEEP CONTENT
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))

                    // 1. AUDIENCE-SPECIFIC DEEP DIVE
                    val audienceGuide = domain.audienceGuides[selectedAudience]
                        ?: domain.audienceGuides[AudienceType.PROFESSIONALS]
                        ?: domain.audienceGuides.values.first()

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = when (selectedAudience) {
                                        AudienceType.STUDENTS -> Icons.Default.School
                                        AudienceType.RESEARCHERS -> Icons.Default.Science
                                        else -> Icons.Default.Work
                                    },
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${selectedAudience.label} Focus: ${audienceGuide.headline}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Key Core Concepts:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            audienceGuide.keyConcepts.forEach { concept ->
                                Text(text = "• $concept", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Actionable Steps:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            audienceGuide.actionItems.forEach { action ->
                                Text(text = "→ $action", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. STATUTORY SPECIFICATIONS
                    FieldRow(label = "Requirement", text = domain.requirement)
                    FieldRow(label = "Need", text = domain.need)
                    FieldRow(label = "Purpose", text = domain.purpose)
                    FieldRow(label = "Goal", text = domain.goal)
                    FieldRow(label = "Scope", text = domain.scope)
                    FieldRow(label = "Current Scenario", text = domain.currentScenario)
                    FieldRow(label = "Future Outlook", text = domain.futureOutlook)
                    FieldRow(label = "Necessity", text = domain.necessity)

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3. LANDMARK ARTICLES & PAPERS
                    Text(
                        text = "Key Articles & Landmark Literature:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    domain.articles.forEach { article ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clickable { onOpenExternalUrl(article.url) }
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = article.title,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.OpenInBrowser,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = "${article.sourceOrJournal} (${article.year})",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = article.keyTakeaway,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 4. JOBS IN THIS SECTION
                    Text(
                        text = "Jobs & Professional Opportunities:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    domain.jobs.forEach { job ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = job.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text(text = "${job.sector} • ${job.coreResponsibilities}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF0F6E43).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = job.salaryRange,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F6E43),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 5. APPROACH FOR JOB
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "Job Approach & Career Roadmap:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(text = "• Required Degrees: ${domain.jobApproach.recommendedDegrees.joinToString(", ")}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "• Key Certifications: ${domain.jobApproach.essentialCertifications.joinToString(", ")}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "• Portfolio Project: ${domain.jobApproach.handsOnPortfolioProjects.joinToString("; ")}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "• Interview Focus: ${domain.jobApproach.technicalInterviewFocus}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "• Career Path: ${domain.jobApproach.careerRoadmap}", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 6. TOOLS & SOFTWARE
                    Text(
                        text = "Essential Industry Tools & Software:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(domain.toolsAndSoftware) { tool ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.clickable { onOpenExternalUrl(tool.officialUrl) }
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(text = tool.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    Text(text = tool.category, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 7. DIRECT OFFICIAL SITE CONNECTIVITY
                    Text(
                        text = "Direct Official Regulatory Sites:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        domain.officialSites.forEach { site ->
                            OutlinedButton(
                                onClick = { onOpenExternalUrl(site.url) },
                                modifier = Modifier.weight(1f).height(36.dp)
                            ) {
                                Text(text = site.organization, fontSize = 10.sp, maxLines = 1)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 8. APP CONNECTIVITY ACTION BUTTONS
                    Text(
                        text = "Connected EDEN Actions:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = onSearchJobs,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F6E43)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(38.dp)
                        ) {
                            Icon(Icons.Default.Work, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Find Jobs", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onAskEden,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(38.dp)
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ask AI Advisor", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (domain.relatedCalculatorType != null || domain.relatedProcedureDomain != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (domain.relatedCalculatorType != null) {
                                OutlinedButton(
                                    onClick = onOpenCalculator,
                                    modifier = Modifier.weight(1f).height(36.dp)
                                ) {
                                    Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Open Calculator", fontSize = 10.sp)
                                }
                            }
                            if (domain.relatedProcedureDomain != null) {
                                OutlinedButton(
                                    onClick = onOpenProcedures,
                                    modifier = Modifier.weight(1f).height(36.dp)
                                ) {
                                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Open SOP Sheet", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InteractiveFormulaCard(formula: SustainabilityFormulaItem) {
    var p1 by remember(formula.id) { mutableDoubleStateOf(formula.param1Default) }
    var p2 by remember(formula.id) { mutableDoubleStateOf(formula.param2Default) }

    val calculatedResult = remember(p1, p2) {
        formula.calculate(p1, p2, null)
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formula.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF0F6E43),
                    contentColor = Color.White
                ) {
                    Text(
                        text = "%.3f %s".format(calculatedResult, formula.unit),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = formula.formulaSymbolic,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = formula.description,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Dynamic parameter sliders
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formula.param1Label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "%.1f".format(p1), fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = p1.toFloat(),
                onValueChange = { p1 = it.toDouble() },
                valueRange = ((formula.param1Default * 0.1).toFloat())..((formula.param1Default * 3.0).toFloat().coerceAtLeast(1f)),
                modifier = Modifier.height(26.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formula.param2Label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "%.1f".format(p2), fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = p2.toFloat(),
                onValueChange = { p2 = it.toDouble() },
                valueRange = ((formula.param2Default * 0.1).toFloat())..((formula.param2Default * 3.0).toFloat().coerceAtLeast(1f)),
                modifier = Modifier.height(26.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF0F6E43).copy(alpha = 0.1f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Interpretation: " + formula.interpretation(calculatedResult),
                    fontSize = 10.sp,
                    color = Color(0xFF0F6E43),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickTag(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$label: ", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(text = value, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun FieldRow(label: String, text: String) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Text(
            text = label.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 15.sp
        )
    }
}

@Composable
private fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
