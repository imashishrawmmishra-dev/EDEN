package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import com.example.calculator.EnvironmentalCalculatorsExporter
import com.example.calculator.EnvironmentalUnitConverter
import com.example.calculator.GhgScenario
import com.example.calculator.GhgScenarioComparison
import com.example.calculator.GhgUnitSystem
import com.example.calculator.GhgUnitConverter
import com.example.ui.components.GhgSaveScenarioDialog
import com.example.ui.components.GhgScenarioComparisonCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.AirCalculatorEngine
import com.example.calculator.CarbonCalculatorEngine
import com.example.ui.components.GhgCsvExportModal
import com.example.util.GhgCsvExporter
import com.example.viewmodel.EdenViewModel
import java.util.Locale

@Composable
fun CalculatorsScreen(
    viewModel: EdenViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCalcIndex by remember { mutableIntStateOf(0) }
    val calcTabs = listOf("Live GPS Carbon", "GHG Calculator", "Air Stack & Flow", "Water & Wastewater", "Acoustics & Noise", "History")

    val carbonResult by viewModel.carbonResult.collectAsState()
    val airResult by viewModel.airResult.collectAsState()
    val waterResult by viewModel.waterResult.collectAsState()
    val noiseResult by viewModel.noiseResult.collectAsState()
    val history by viewModel.calculationHistory.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedCalcIndex,
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .testTag("calculator_tabs")
        ) {
            calcTabs.forEachIndexed { index, name ->
                Tab(
                    selected = selectedCalcIndex == index,
                    onClick = { selectedCalcIndex = index },
                    text = {
                        Text(
                            text = name,
                            fontWeight = if (selectedCalcIndex == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedCalcIndex) {
            0 -> LiveCarbonTrackerScreen(viewModel = viewModel)
            1 -> CarbonCalculatorView(viewModel = viewModel, result = carbonResult)
            2 -> AirCalculatorView(viewModel = viewModel, result = airResult)
            3 -> WaterCalculatorView(viewModel = viewModel, result = waterResult)
            4 -> NoiseCalculatorView(viewModel = viewModel, result = noiseResult)
            5 -> CalculationHistoryView(viewModel = viewModel, history = history)
        }
    }
}

@Composable
private fun CarbonCalculatorView(
    viewModel: EdenViewModel,
    result: CarbonCalculatorEngine.CarbonInventoryResult?
) {
    val context = LocalContext.current
    var fuelType by remember { mutableStateOf(CarbonCalculatorEngine.FuelType.DIESEL) }
    var fuelQty by remember { mutableStateOf("1500") }
    var electricityKwh by remember { mutableStateOf("25000") }
    var gridRegion by remember { mutableStateOf(CarbonCalculatorEngine.GridRegion.US_AVERAGE) }
    var transportMode by remember { mutableStateOf(CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT) }
    var transportVol by remember { mutableStateOf("8000") }

    val showExportModal by viewModel.showGhgCsvExportModal.collectAsState()
    val lastInputs by viewModel.lastCarbonInputs.collectAsState()
    val savedScenarios by viewModel.savedGhgScenarios.collectAsState()
    val selectedBaselineId by viewModel.selectedBaselineScenarioId.collectAsState()
    val isComparisonMode by viewModel.isComparisonModeActive.collectAsState()
    val showSaveDialog by viewModel.showSaveScenarioDialog.collectAsState()
    val unitSystem by viewModel.ghgUnitSystem.collectAsState()

    val activeBaseline = remember(savedScenarios, selectedBaselineId) {
        savedScenarios.find { it.id == selectedBaselineId }
            ?: savedScenarios.find { it.isBaseline }
            ?: savedScenarios.firstOrNull()
    }

    // Helper to compute and execute metric carbon calculation
    val executeCalculateCarbon: () -> Unit = {
        val parsedFuel = fuelQty.toDoubleOrNull() ?: 0.0
        val kwh = electricityKwh.toDoubleOrNull() ?: 0.0
        val parsedTransport = transportVol.toDoubleOrNull() ?: 0.0
        val metricFuel = GhgUnitConverter.convertFuelToMetric(parsedFuel, fuelType, unitSystem)
        val metricTransport = GhgUnitConverter.convertTransportToMetric(parsedTransport, transportMode, unitSystem)
        viewModel.calculateCarbon(fuelType, metricFuel, kwh, gridRegion, transportMode, metricTransport)
    }

    if (showExportModal && result != null) {
        GhgCsvExportModal(
            inputs = lastInputs,
            result = result,
            onDismissRequest = { viewModel.setGhgCsvExportModal(false) }
        )
    }

    if (showSaveDialog) {
        val parsedFuel = fuelQty.toDoubleOrNull() ?: 0.0
        val kwh = electricityKwh.toDoubleOrNull() ?: 0.0
        val parsedTransport = transportVol.toDoubleOrNull() ?: 0.0
        val metricFuel = GhgUnitConverter.convertFuelToMetric(parsedFuel, fuelType, unitSystem)
        val metricTransport = GhgUnitConverter.convertTransportToMetric(parsedTransport, transportMode, unitSystem)

        val currentCalcResult = result ?: CarbonCalculatorEngine.calculateInventory(
            fuelType,
            metricFuel,
            kwh,
            gridRegion,
            transportMode,
            metricTransport
        )
        val currentCalcInputs = GhgCsvExporter.GhgInputParameters(
            fuelType = fuelType,
            fuelQuantity = metricFuel,
            electricityKwh = kwh,
            gridRegion = gridRegion,
            transportMode = transportMode,
            transportVolume = metricTransport,
            timestamp = System.currentTimeMillis()
        )
        GhgSaveScenarioDialog(
            inputs = currentCalcInputs,
            result = currentCalcResult,
            onSave = { name, desc, setAsBaseline ->
                viewModel.saveCurrentCalculationAsScenario(name, desc, setAsBaseline)
                viewModel.setShowSaveScenarioDialog(false)
                Toast.makeText(context, "Scenario '$name' saved successfully!", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { viewModel.setShowSaveScenarioDialog(false) },
            unitSystem = unitSystem
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "GHG Protocol Corporate Carbon Inventory",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Deterministic quantification across Scope 1, 2, and 3 boundaries",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Global Unit Switcher (Metric vs Imperial)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ghg_unit_switcher")
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SwapHoriz,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Global Input Units",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = if (unitSystem == GhgUnitSystem.METRIC) "Metric (kg, km, L)" else "Imperial (lbs, miles, gal)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val isMetric = unitSystem == GhgUnitSystem.METRIC
                                Button(
                                    onClick = {
                                        if (!isMetric) {
                                            val curF = fuelQty.toDoubleOrNull()
                                            if (curF != null) {
                                                val convertedFuel = GhgUnitConverter.convertFuelToMetric(curF, fuelType, GhgUnitSystem.IMPERIAL)
                                                fuelQty = GhgUnitConverter.formatDisplayQuantity(convertedFuel)
                                            }
                                            val curT = transportVol.toDoubleOrNull()
                                            if (curT != null) {
                                                val convertedTransport = GhgUnitConverter.convertTransportToMetric(curT, transportMode, GhgUnitSystem.IMPERIAL)
                                                transportVol = GhgUnitConverter.formatDisplayQuantity(convertedTransport)
                                            }
                                            viewModel.setGhgUnitSystem(GhgUnitSystem.METRIC)
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp)
                                        .testTag("ghg_unit_switcher_metric"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = if (isMetric) {
                                        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                    } else {
                                        ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            contentColor = MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    border = if (!isMetric) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)) else null
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Straighten,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = if (isMetric) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Metric (kg / km)",
                                        fontSize = 12.sp,
                                        fontWeight = if (isMetric) FontWeight.Bold else FontWeight.Medium
                                    )
                                }

                                val isImperial = unitSystem == GhgUnitSystem.IMPERIAL
                                Button(
                                    onClick = {
                                        if (!isImperial) {
                                            val curF = fuelQty.toDoubleOrNull()
                                            if (curF != null) {
                                                val convertedFuel = GhgUnitConverter.convertFuelFromMetric(curF, fuelType, GhgUnitSystem.IMPERIAL)
                                                fuelQty = GhgUnitConverter.formatDisplayQuantity(convertedFuel)
                                            }
                                            val curT = transportVol.toDoubleOrNull()
                                            if (curT != null) {
                                                val convertedTransport = GhgUnitConverter.convertTransportFromMetric(curT, transportMode, GhgUnitSystem.IMPERIAL)
                                                transportVol = GhgUnitConverter.formatDisplayQuantity(convertedTransport)
                                            }
                                            viewModel.setGhgUnitSystem(GhgUnitSystem.IMPERIAL)
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp)
                                        .testTag("ghg_unit_switcher_imperial"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = if (isImperial) {
                                        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                    } else {
                                        ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            contentColor = MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    border = if (!isImperial) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)) else null
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Speed,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = if (isImperial) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Imperial (lbs / miles)",
                                        fontSize = 12.sp,
                                        fontWeight = if (isImperial) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Scope 1: Stationary & Fleet Fuel", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        items(CarbonCalculatorEngine.FuelType.values()) { fuel ->
                            FilterChip(
                                selected = fuelType == fuel,
                                onClick = { fuelType = fuel },
                                label = { Text(fuel.displayName, fontSize = 11.sp) }
                            )
                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = fuelQty,
                            onValueChange = { fuelQty = it },
                            label = { Text("Fuel Quantity (${GhgUnitConverter.getFuelUnit(fuelType, unitSystem)})") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = GhgUnitConverter.getFuelConversionHint(fuelType, unitSystem),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Scope 2: Purchased Electricity", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        items(CarbonCalculatorEngine.GridRegion.values()) { grid ->
                            FilterChip(
                                selected = gridRegion == grid,
                                onClick = { gridRegion = grid },
                                label = { Text(grid.displayName, fontSize = 11.sp) }
                            )
                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = electricityKwh,
                            onValueChange = { electricityKwh = it },
                            label = { Text("Electricity Consumed (kWh)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Universal unit: kWh across both Metric and Imperial electric utility systems",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Scope 3: Freight & Logistics", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        items(CarbonCalculatorEngine.TransportMode.values()) { mode ->
                            FilterChip(
                                selected = transportMode == mode,
                                onClick = { transportMode = mode },
                                label = { Text(mode.displayName, fontSize = 11.sp) }
                            )
                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = transportVol,
                            onValueChange = { transportVol = it },
                            label = { Text("Transport Volume (${GhgUnitConverter.getTransportUnit(transportMode, unitSystem)})") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = GhgUnitConverter.getTransportConversionHint(transportMode, unitSystem),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Primary Action Suite: Calculate, Save Scenario, Compare
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                executeCalculateCarbon()
                            },
                            modifier = Modifier
                                .weight(1.3f)
                                .height(44.dp)
                                .testTag("carbon_calculate_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Calculate GHG", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                executeCalculateCarbon()
                                viewModel.setShowSaveScenarioDialog(true)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("save_scenario_button"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = {
                                executeCalculateCarbon()
                                viewModel.toggleComparisonMode()
                            },
                            modifier = Modifier
                                .weight(1.2f)
                                .height(44.dp)
                                .testTag("toggle_ghg_comparison_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = if (isComparisonMode) {
                                ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                            } else {
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.CompareArrows,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isComparisonMode) Color.White else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isComparisonMode) "Comparing" else "Compare",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isComparisonMode) Color.White else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // Saved Scenarios Bar & Baseline Switcher
        if (savedScenarios.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("saved_scenarios_bar")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Saved Scenarios (${savedScenarios.size})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (activeBaseline != null) {
                                Text(
                                    text = "★ Baseline: ${activeBaseline.name.take(24)}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(savedScenarios) { scenario ->
                                val isCurrentBaseline = scenario.id == (activeBaseline?.id)
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isCurrentBaseline) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isCurrentBaseline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .clickable {
                                            val displayFuel = GhgUnitConverter.convertFuelFromMetric(scenario.inputs.fuelQuantity, scenario.inputs.fuelType, unitSystem)
                                            val displayTransport = GhgUnitConverter.convertTransportFromMetric(scenario.inputs.transportVolume, scenario.inputs.transportMode, unitSystem)
                                            fuelType = scenario.inputs.fuelType
                                            fuelQty = GhgUnitConverter.formatDisplayQuantity(displayFuel)
                                            electricityKwh = if (scenario.inputs.electricityKwh % 1.0 == 0.0) scenario.inputs.electricityKwh.toLong().toString() else scenario.inputs.electricityKwh.toString()
                                            gridRegion = scenario.inputs.gridRegion
                                            transportMode = scenario.inputs.transportMode
                                            transportVol = GhgUnitConverter.formatDisplayQuantity(displayTransport)
                                            viewModel.calculateCarbon(fuelType, scenario.inputs.fuelQuantity, scenario.inputs.electricityKwh, gridRegion, transportMode, scenario.inputs.transportVolume)
                                            Toast.makeText(context, "Loaded '${scenario.name}' into calculator", Toast.LENGTH_SHORT).show()
                                        }
                                        .testTag("saved_scenario_chip_${scenario.id}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        if (isCurrentBaseline) {
                                            Icon(Icons.Default.Star, contentDescription = "Baseline", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(12.dp))
                                        }
                                        Column {
                                            Text(
                                                text = scenario.name,
                                                fontSize = 11.sp,
                                                fontWeight = if (isCurrentBaseline) FontWeight.Bold else FontWeight.SemiBold,
                                                color = if (isCurrentBaseline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = if (unitSystem == GhgUnitSystem.IMPERIAL) {
                                                    "%.2f US tons • %s".format(Locale.US, scenario.result.totalTonnes * GhgUnitConverter.SHORT_TONS_PER_METRIC_TONNE, scenario.shortDate)
                                                } else {
                                                    "%.2f t CO₂e • %s".format(Locale.US, scenario.result.totalTonnes, scenario.shortDate)
                                                },
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Side-by-Side Comparison Card
        if (isComparisonMode) {
            item {
                val comparison = viewModel.getComparisonResult()
                if (comparison != null) {
                    GhgScenarioComparisonCard(
                        comparison = comparison,
                        savedScenarios = savedScenarios,
                        onSelectBaseline = { viewModel.setBaselineScenario(it) },
                        onLoadScenarioInputs = { sc ->
                            val displayFuel = GhgUnitConverter.convertFuelFromMetric(sc.inputs.fuelQuantity, sc.inputs.fuelType, unitSystem)
                            val displayTransport = GhgUnitConverter.convertTransportFromMetric(sc.inputs.transportVolume, sc.inputs.transportMode, unitSystem)
                            fuelType = sc.inputs.fuelType
                            fuelQty = GhgUnitConverter.formatDisplayQuantity(displayFuel)
                            electricityKwh = if (sc.inputs.electricityKwh % 1.0 == 0.0) sc.inputs.electricityKwh.toLong().toString() else sc.inputs.electricityKwh.toString()
                            gridRegion = sc.inputs.gridRegion
                            transportMode = sc.inputs.transportMode
                            transportVol = GhgUnitConverter.formatDisplayQuantity(displayTransport)
                            viewModel.calculateCarbon(fuelType, sc.inputs.fuelQuantity, sc.inputs.electricityKwh, gridRegion, transportMode, sc.inputs.transportVolume)
                        },
                        onDismiss = { viewModel.toggleComparisonMode(false) },
                        unitSystem = unitSystem
                    )
                }
            }
        }

        if (result != null) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .testTag("carbon_results_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "GHG Inventory Results",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (unitSystem == GhgUnitSystem.IMPERIAL) {
                            val totalLbs = result.totalKg * GhgUnitConverter.LBS_PER_KG
                            val totalUsTons = result.totalTonnes * GhgUnitConverter.SHORT_TONS_PER_METRIC_TONNE
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total Footprint:", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "%,.1f lbs CO₂e".format(Locale.US, totalLbs),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "%.2f US tons (%.2f t CO₂e)".format(Locale.US, totalUsTons, result.totalTonnes),
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total Footprint:", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "%.2f t CO₂e".format(result.totalTonnes),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(8.dp))

                        if (unitSystem == GhgUnitSystem.IMPERIAL) {
                            Text("• Scope 1 Direct: %,.1f lbs CO₂e (%.1f kg, %.1f%%)".format(Locale.US, result.scope1Kg * GhgUnitConverter.LBS_PER_KG, result.scope1Kg, result.scope1Percent), fontSize = 13.sp)
                            Text("• Scope 2 Indirect Grid: %,.1f lbs CO₂e (%.1f kg, %.1f%%)".format(Locale.US, result.scope2Kg * GhgUnitConverter.LBS_PER_KG, result.scope2Kg, result.scope2Percent), fontSize = 13.sp)
                            Text("• Scope 3 Logistics: %,.1f lbs CO₂e (%.1f kg, %.1f%%)".format(Locale.US, result.scope3Kg * GhgUnitConverter.LBS_PER_KG, result.scope3Kg, result.scope3Percent), fontSize = 13.sp)
                        } else {
                            Text("• Scope 1 Direct: %.1f kg CO₂e (%.1f%%)".format(result.scope1Kg, result.scope1Percent), fontSize = 13.sp)
                            Text("• Scope 2 Indirect Grid: %.1f kg CO₂e (%.1f%%)".format(result.scope2Kg, result.scope2Percent), fontSize = 13.sp)
                            Text("• Scope 3 Logistics: %.1f kg CO₂e (%.1f%%)".format(result.scope3Kg, result.scope3Percent), fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Dominant Driver: ${result.dominantScope}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Decarbonization Pathways:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        result.reductionScenarios.forEach { scenario ->
                            Text("➔ $scenario", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 2.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = result.methodologyNote,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Scenario Benchmarking Section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CompareArrows,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Scenario Benchmarking",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = "Side-by-Side",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.toggleComparisonMode(true)
                                },
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(44.dp)
                                    .testTag("result_compare_scenario_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CompareArrows,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Compare vs Baseline", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.setShowSaveScenarioDialog(true)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("result_save_scenario_button"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkAdd,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Save Scenario", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // CSV Export Actions Section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Reporting & Export",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = "RFC 4180 CSV",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2E7D32)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.setGhgCsvExportModal(true)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("export_ghg_csv_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FileDownload,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Export as CSV", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    try {
                                        val inputs = viewModel.lastCarbonInputs.value
                                        val csv = GhgCsvExporter.generateGhgCsv(inputs, result)
                                        val file = GhgCsvExporter.createExportFile(context, csv)
                                        val intent = GhgCsvExporter.createShareIntent(context, file)
                                        context.startActivity(android.content.Intent.createChooser(intent, "Share GHG Report CSV"))
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Could not share CSV: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .height(44.dp)
                                    .testTag("quick_share_ghg_csv_button"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share CSV",
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    val inputs = viewModel.lastCarbonInputs.value
                                    val csv = GhgCsvExporter.generateGhgCsv(inputs, result)
                                    GhgCsvExporter.copyToClipboard(context, csv)
                                    Toast.makeText(context, "✓ GHG Inventory CSV copied to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .height(44.dp)
                                    .testTag("quick_copy_ghg_csv_button"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy CSV",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AirCalculatorView(
    viewModel: EdenViewModel,
    result: AirCalculatorEngine.StackFlowResult?
) {
    val context = LocalContext.current
    val unitSystem by viewModel.ghgUnitSystem.collectAsState()
    val isImperial = unitSystem == GhgUnitSystem.IMPERIAL

    var velocity by remember { mutableStateOf("14.5") }
    var diameter by remember { mutableStateOf("1.8") }
    var temperature by remember { mutableStateOf("165.0") }
    var concentration by remember { mutableStateOf("45.0") }

    var showDiameterTooltip by remember { mutableStateOf(false) }
    var showTempTooltip by remember { mutableStateOf(false) }

    // Presets / Scenarios for Air Stack (Name, Velocity m/s, Diameter m, Temp °C)
    val airPresets = listOf(
        listOf("Thermal Coal Boiler", "18.5", "3.2", "165.0"),
        listOf("Gas Turbine Stack", "22.0", "2.4", "420.0"),
        listOf("Waste Incinerator", "12.0", "1.5", "210.0"),
        listOf("Industrial Kiln", "9.5", "1.2", "310.0")
    )

    // Stack Diameter Physical Plausibility Validation (EPA Method 1 & 2)
    val dVal = diameter.toDoubleOrNull()
    val diameterError: String? = when {
        diameter.isBlank() -> "Stack diameter is required"
        dVal == null -> "Please enter a valid numeric diameter"
        dVal <= 0.0 -> "Diameter must be strictly positive (> 0)"
        !isImperial && dVal < 0.10 -> "Physically implausible: Diameter < 0.10 m is too small for EPA Method 2 Pitot probe traverse"
        !isImperial && dVal > 15.0 -> "Physically implausible: Diameter > 15.0 m exceeds industrial chimney dimensions"
        isImperial && dVal < 0.33 -> "Physically implausible: Diameter < 0.33 ft is too small for EPA Method 2 Pitot probe traverse"
        isImperial && dVal > 49.2 -> "Physically implausible: Diameter > 49.2 ft exceeds industrial chimney dimensions"
        else -> null
    }
    val isDiameterError = diameterError != null

    // Stack Flue Gas Temperature Physical Plausibility Validation (EPA Method 2 Ts)
    val tVal = temperature.toDoubleOrNull()
    val tempError: String? = when {
        temperature.isBlank() -> "Stack flue temperature is required"
        tVal == null -> "Please enter a valid numeric temperature"
        !isImperial && tVal < -273.15 -> "Physically impossible: Below absolute zero (-273.15°C)"
        !isImperial && tVal < -50.0 -> "Physically implausible: Temperature < -50°C for stack flue exhaust"
        !isImperial && tVal > 1200.0 -> "Physically implausible: Temperature > 1,200°C exceeds thermocouple sensor limits"
        isImperial && tVal < -459.67 -> "Physically impossible: Below absolute zero (-459.67°F)"
        isImperial && tVal < -58.0 -> "Physically implausible: Temperature < -58°F for stack flue exhaust"
        isImperial && tVal > 2192.0 -> "Physically implausible: Temperature > 2,192°F exceeds thermocouple sensor limits"
        else -> null
    }
    val isTempError = tempError != null

    val hasPlausibilityError = isDiameterError || isTempError

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Industrial Chimney Flow & Emission Rate",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Compliant with EPA Method 2 (Pitot tube volumetric mass flow)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // GLOBAL UNIT SWITCHER
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.SwapHoriz,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Global Input Units",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = if (!isImperial) "Metric (m/s, m, °C, mg/Nm³)" else "Imperial (ft/s, ft, °F, gr/dscf)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (isImperial) {
                                            val v = velocity.toDoubleOrNull()
                                            if (v != null) velocity = "%.2f".format(v * EnvironmentalUnitConverter.METERS_PER_FT)
                                            val d = diameter.toDoubleOrNull()
                                            if (d != null) diameter = "%.2f".format(d * EnvironmentalUnitConverter.METERS_PER_FT)
                                            val t = temperature.toDoubleOrNull()
                                            if (t != null) temperature = "%.1f".format((t - 32.0) * 5.0 / 9.0)
                                            val c = concentration.toDoubleOrNull()
                                            if (c != null) concentration = "%.1f".format(c * EnvironmentalUnitConverter.MG_M3_PER_GRAINS_DSCF)
                                            viewModel.setGhgUnitSystem(GhgUnitSystem.METRIC)
                                        }
                                    },
                                    modifier = Modifier.weight(1f).height(38.dp).testTag("air_unit_metric"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = if (!isImperial) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                             else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface),
                                    border = if (isImperial) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)) else null
                                ) {
                                    Text("Metric (m, m/s, °C)", fontSize = 11.sp, fontWeight = if (!isImperial) FontWeight.Bold else FontWeight.Medium)
                                }

                                Button(
                                    onClick = {
                                        if (!isImperial) {
                                            val v = velocity.toDoubleOrNull()
                                            if (v != null) velocity = "%.2f".format(v * EnvironmentalUnitConverter.FT_PER_METER)
                                            val d = diameter.toDoubleOrNull()
                                            if (d != null) diameter = "%.2f".format(d * EnvironmentalUnitConverter.FT_PER_METER)
                                            val t = temperature.toDoubleOrNull()
                                            if (t != null) temperature = "%.1f".format(t * 1.8 + 32.0)
                                            val c = concentration.toDoubleOrNull()
                                            if (c != null) concentration = "%.4f".format(c * EnvironmentalUnitConverter.GRAINS_PER_DSCF_PER_MG_M3)
                                            viewModel.setGhgUnitSystem(GhgUnitSystem.IMPERIAL)
                                        }
                                    },
                                    modifier = Modifier.weight(1f).height(38.dp).testTag("air_unit_imperial"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = if (isImperial) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                             else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface),
                                    border = if (!isImperial) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)) else null
                                ) {
                                    Text("Imperial (ft, ft/s, °F)", fontSize = 11.sp, fontWeight = if (isImperial) FontWeight.Bold else FontWeight.Medium)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Scenario Presets
                    Text("Industry Stack Scenarios:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        items(airPresets) { preset ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                modifier = Modifier.clickable {
                                    val vMetric = preset[1].toDoubleOrNull() ?: 14.0
                                    val dMetric = preset[2].toDoubleOrNull() ?: 1.8
                                    val tMetric = preset[3].toDoubleOrNull() ?: 165.0
                                    velocity = if (isImperial) "%.2f".format(vMetric * EnvironmentalUnitConverter.FT_PER_METER) else preset[1]
                                    diameter = if (isImperial) "%.2f".format(dMetric * EnvironmentalUnitConverter.FT_PER_METER) else preset[2]
                                    temperature = if (isImperial) "%.1f".format(tMetric * 1.8 + 32.0) else preset[3]
                                    Toast.makeText(context, "Loaded ${preset[0]} preset", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(preset[0], fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = velocity,
                        onValueChange = { velocity = it },
                        label = { Text(if (isImperial) "Stack Gas Velocity (ft/s)" else "Stack Gas Velocity (m/s)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("air_input_velocity")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // ==========================================
                    // 1. STACK DIAMETER FIELD WITH ERROR STATE & TOOLTIP
                    // ==========================================
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Straighten,
                                    contentDescription = null,
                                    tint = if (isDiameterError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isImperial) "Stack Diameter (ft)" else "Stack Internal Diameter (m)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDiameterError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isDiameterError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                ) {
                                    Text(
                                        text = if (isDiameterError) "IMPLAUSIBLE" else if (isImperial) "0.33 – 49.2 ft" else "0.10 – 15.0 m",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDiameterError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { showDiameterTooltip = !showDiameterTooltip },
                                    modifier = Modifier.size(28.dp).testTag("air_diameter_tooltip_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Stack Diameter Validation Tooltip",
                                        tint = if (showDiameterTooltip) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        OutlinedTextField(
                            value = diameter,
                            onValueChange = { diameter = it },
                            label = { Text(if (isImperial) "Diameter (ft) [0.33 - 49.2]" else "Diameter (m) [0.10 - 15.0]") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = isDiameterError,
                            trailingIcon = {
                                if (isDiameterError) {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = "Diameter Error",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.testTag("air_diameter_error_icon")
                                    )
                                } else {
                                    IconButton(
                                        onClick = { showDiameterTooltip = !showDiameterTooltip },
                                        modifier = Modifier.testTag("air_diameter_info_icon")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.HelpOutline,
                                            contentDescription = "Diameter Physical Bounds Info",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            supportingText = {
                                if (isDiameterError) {
                                    Text(
                                        text = diameterError!!,
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.testTag("air_diameter_error_text")
                                    )
                                } else {
                                    Text(
                                        text = "EPA Method 1 & 2 plausible range: ${if (isImperial) "0.33 to 49.20 ft" else "0.10 to 15.00 m"}",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("air_input_diameter")
                        )

                        // Validation Tooltip Card for Diameter
                        if (showDiameterTooltip) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .testTag("air_diameter_tooltip_card")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(15.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("EPA Method 2 — Stack Diameter Criteria", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        }
                                        IconButton(onClick = { showDiameterTooltip = false }, modifier = Modifier.size(20.dp)) {
                                            Icon(Icons.Default.Close, contentDescription = "Close Tooltip", modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "• Physically Plausible Bounds: ${if (isImperial) "0.33 ft to 49.20 ft (4.0 in to 590 in)" else "0.10 m to 15.00 m (10 cm to 1,500 cm)"}\n" +
                                               "• Minimum Limit (0.10 m / 0.33 ft): Under EPA Method 1 Section 11.1, ducts below 0.1 m suffer severe flow blockage when a Type-S Pitot probe is inserted, distorting aerodynamic velocity profiles.\n" +
                                               "• Maximum Limit (15.0 m / 49.2 ft): Exceeds dimensions of the largest utility power generation exhaust stacks in the world. Stacks larger than 15 m are physically non-existent in stationary source operations.\n" +
                                               "• Typical Industrial Range: 0.8 m to 4.0 m (2.6 ft to 13.1 ft).",
                                        fontSize = 10.5.sp,
                                        lineHeight = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // ==========================================
                    // 2. STACK TEMPERATURE FIELD WITH ERROR STATE & TOOLTIP
                    // ==========================================
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Thermostat,
                                    contentDescription = null,
                                    tint = if (isTempError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isImperial) "Stack Flue Gas Temperature (Ts) (°F)" else "Stack Flue Gas Temperature (Ts) (°C)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isTempError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isTempError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                ) {
                                    Text(
                                        text = if (isTempError) "IMPLAUSIBLE" else if (isImperial) "-58 – 2,192 °F" else "-50 – 1,200 °C",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isTempError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { showTempTooltip = !showTempTooltip },
                                    modifier = Modifier.size(28.dp).testTag("air_temperature_tooltip_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Stack Temperature Validation Tooltip",
                                        tint = if (showTempTooltip) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        OutlinedTextField(
                            value = temperature,
                            onValueChange = { temperature = it },
                            label = { Text(if (isImperial) "Stack Temperature (°F) [-58 to 2,192]" else "Stack Temperature (°C) [-50 to 1,200]") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = isTempError,
                            trailingIcon = {
                                if (isTempError) {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = "Temperature Error",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.testTag("air_temperature_error_icon")
                                    )
                                } else {
                                    IconButton(
                                        onClick = { showTempTooltip = !showTempTooltip },
                                        modifier = Modifier.testTag("air_temperature_info_icon")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.HelpOutline,
                                            contentDescription = "Temperature Physical Bounds Info",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            supportingText = {
                                if (isTempError) {
                                    Text(
                                        text = tempError!!,
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.testTag("air_temperature_error_text")
                                    )
                                } else {
                                    Text(
                                        text = "EPA Method 2 plausible range: ${if (isImperial) "-58.0 °F to 2,192.0 °F" else "-50.0 °C to 1,200.0 °C"}",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("air_input_temperature")
                        )

                        // Validation Tooltip Card for Temperature
                        if (showTempTooltip) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .testTag("air_temperature_tooltip_card")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(15.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("EPA Method 2 — Stack Temperature Criteria", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        }
                                        IconButton(onClick = { showTempTooltip = false }, modifier = Modifier.size(20.dp)) {
                                            Icon(Icons.Default.Close, contentDescription = "Close Tooltip", modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "• Physically Plausible Bounds: ${if (isImperial) "-58.0°F to 2,192.0°F (222.0 K to 1,473.15 K)" else "-50.0°C to 1,200.0°C (223.15 K to 1,473.15 K)"}\n" +
                                               "• Absolute Physical Lower Bound: Exhaust flue gas cannot be colder than absolute zero (-273.15°C / -459.67°F). Ambient stacks and scrubbers rarely drop below -50°C.\n" +
                                               "• Sensor Limits (1,200°C / 2,192°F): EPA Method 2 Section 4.3 mandates calibrated Type-K/Type-S thermocouples. Flue temperatures > 1,200°C cause severe thermoelectric drift and probe damage.\n" +
                                               "• Standard Normalization: Ts is used in Method 2 to normalize actual flow Q to standard dry flow Q_std (20°C / 68°F NTP): Q_std = Q × (293.15 / (Ts + 273.15)).\n" +
                                               "• Typical Industrial Exhausts: Boilers: 120°C – 240°C; Gas Turbines: 380°C – 550°C; Incinerators: 850°C – 1,100°C.",
                                        fontSize = 10.5.sp,
                                        lineHeight = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = concentration,
                        onValueChange = { concentration = it },
                        label = { Text(if (isImperial) "Concentration (gr/dscf)" else "Concentration (mg/Nm³)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("air_input_concentration")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Validation Notice Banner
                    if (hasPlausibilityError) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.55f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth().testTag("air_validation_warning_banner")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Validation Alert",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "EPA Method 2 Physical Validation Alert",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        text = "Please enter physically plausible values for ${listOfNotNull(if (isDiameterError) "stack diameter" else null, if (isTempError) "temperature" else null).joinToString(" and ")} before calculating.",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Button(
                        onClick = {
                            if (hasPlausibilityError) {
                                Toast.makeText(context, "Please correct physically implausible values first", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val vRaw = velocity.toDoubleOrNull() ?: 0.0
                            val dRaw = diameter.toDoubleOrNull() ?: 0.0
                            val tRaw = temperature.toDoubleOrNull() ?: (if (isImperial) 329.0 else 165.0)
                            val cRaw = concentration.toDoubleOrNull() ?: 0.0
                            val vMetric = if (isImperial) vRaw * EnvironmentalUnitConverter.METERS_PER_FT else vRaw
                            val dMetric = if (isImperial) dRaw * EnvironmentalUnitConverter.METERS_PER_FT else dRaw
                            val tMetric = if (isImperial) (tRaw - 32.0) * 5.0 / 9.0 else tRaw
                            val cMetric = if (isImperial) cRaw * EnvironmentalUnitConverter.MG_M3_PER_GRAINS_DSCF else cRaw
                            viewModel.calculateAirStack(vMetric, dMetric, cMetric, tMetric)
                        },
                        enabled = !hasPlausibilityError,
                        modifier = Modifier.fillMaxWidth().height(46.dp).testTag("air_calculate_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Calculate Stack Dispersion Flow", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (result != null) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .testTag("air_results_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Deterministic Flow & Emission Results", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))

                        if (isImperial) {
                            val areaSqFt = result.ductAreaM2 * 10.7639
                            val flowCfm = result.flowRateM3Sec * EnvironmentalUnitConverter.CFM_PER_M3_PER_SEC
                            val flowDscfm = (result.normalizedFlowRateM3Hour / 60.0) * 35.3147
                            val tempF = result.stackTempCelsius * 1.8 + 32.0
                            val lbsPerHour = result.emissionRateKgHour * EnvironmentalUnitConverter.LBS_PER_KG
                            Text("• Stack Area: %,.2f sq ft (%.3f m²)".format(Locale.US, areaSqFt, result.ductAreaM2), fontSize = 13.sp)
                            Text("• Stack Flue Gas Temperature (Ts): %,.1f °F (%,.1f °C)".format(Locale.US, tempF, result.stackTempCelsius), fontSize = 13.sp)
                            Text("• Actual Volumetric Flow: %,.1f ACFM (%.2f m³/s)".format(Locale.US, flowCfm, result.flowRateM3Sec), fontSize = 13.sp)
                            Text("• Standard Flow Rate (Q_std @ 68°F): %,.1f DSCFM (%,.1f Nm³/h)".format(Locale.US, flowDscfm, result.normalizedFlowRateM3Hour), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.secondary)
                            Text("• Mass Emission Rate: %,.3f lbs/h (%.4f kg/h)".format(Locale.US, lbsPerHour, result.emissionRateKgHour), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        } else {
                            Text("• Stack Cross-sectional Area: %.3f m²".format(result.ductAreaM2), fontSize = 13.sp)
                            Text("• Stack Flue Gas Temperature (Ts): %.1f °C (%.1f °F)".format(result.stackTempCelsius, result.stackTempCelsius * 1.8 + 32.0), fontSize = 13.sp)
                            Text("• Actual Volumetric Flow Rate: %.2f m³/s (%.1f m³/h)".format(result.flowRateM3Sec, result.flowRateM3Hour), fontSize = 13.sp)
                            Text("• Standard Normalized Flow (EPA Method 2 @ 20°C): %.1f Nm³/h".format(result.normalizedFlowRateM3Hour), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.secondary)
                            Text("• Mass Emission Rate: %.4f kg/h (%.4f g/s)".format(result.emissionRateKgHour, result.emissionRateGSec), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(result.formulaExplanation, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Reporting & Export Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF0F6E43), modifier = Modifier.size(16.dp))
                                Text("Reporting & Export", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("RFC 4180 CSV", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F6E43))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val vRaw = velocity.toDoubleOrNull() ?: 0.0
                            val dRaw = diameter.toDoubleOrNull() ?: 0.0
                            val tRaw = temperature.toDoubleOrNull() ?: (if (isImperial) 329.0 else 165.0)
                            val cRaw = concentration.toDoubleOrNull() ?: 0.0
                            val vMetric = if (isImperial) vRaw * EnvironmentalUnitConverter.METERS_PER_FT else vRaw
                            val dMetric = if (isImperial) dRaw * EnvironmentalUnitConverter.METERS_PER_FT else dRaw
                            val tMetric = if (isImperial) (tRaw - 32.0) * 5.0 / 9.0 else tRaw
                            val cMetric = if (isImperial) cRaw * EnvironmentalUnitConverter.MG_M3_PER_GRAINS_DSCF else cRaw

                            Button(
                                onClick = {
                                    val csv = EnvironmentalCalculatorsExporter.generateAirCsv(vMetric, dMetric, cMetric, result, tMetric)
                                    EnvironmentalCalculatorsExporter.copyToClipboard(context, "Air Stack Audit CSV", csv)
                                    Toast.makeText(context, "✓ Air Stack Audit CSV exported to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f).height(44.dp).testTag("export_air_csv_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F6E43))
                            ) {
                                Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Export as CSV", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    try {
                                        val csv = EnvironmentalCalculatorsExporter.generateAirCsv(vMetric, dMetric, cMetric, result, tMetric)
                                        val file = EnvironmentalCalculatorsExporter.createExportFile(context, "air_stack_report", csv)
                                        val intent = EnvironmentalCalculatorsExporter.createShareIntent(context, file, "EDEN Air Stack Emission Audit")
                                        context.startActivity(android.content.Intent.createChooser(intent, "Share Air Stack Report"))
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Could not share CSV: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.height(44.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                            }

                            OutlinedButton(
                                onClick = {
                                    val csv = EnvironmentalCalculatorsExporter.generateAirCsv(vMetric, dMetric, cMetric, result, tMetric)
                                    EnvironmentalCalculatorsExporter.copyToClipboard(context, "Air Stack Audit", csv)
                                    Toast.makeText(context, "✓ CSV copied to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.height(44.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WaterCalculatorView(
    viewModel: EdenViewModel,
    result: com.example.calculator.WaterCalculatorEngine.WaterLoadResult?
) {
    val context = LocalContext.current
    val unitSystem by viewModel.ghgUnitSystem.collectAsState()
    val isImperial = unitSystem == GhgUnitSystem.IMPERIAL

    var flow by remember { mutableStateOf("2400") }
    var bod by remember { mutableStateOf("280") }
    var cod by remember { mutableStateOf("520") }
    var aerationVol by remember { mutableStateOf("800") }

    // Presets for Wastewater
    val waterPresets = listOf(
        Triple("Municipal WWTP", "3500", "220"),
        Triple("Brewery Effluent", "1200", "850"),
        Triple("Dairy Processing", "800", "1400"),
        Triple("Textile Dye Plant", "1800", "450")
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Wastewater Loading & Treatment Sizing",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Metcalf & Eddy standards: BOD/COD mass loads, Population Equivalent (PE), and HRT",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // GLOBAL UNIT SWITCHER
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.SwapHoriz,
                                        contentDescription = null,
                                        tint = Color(0xFF0288D1),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Global Input Units",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0288D1)
                                    )
                                }
                                Text(
                                    text = if (!isImperial) "Metric (m³/day, m³)" else "Imperial (MGD, cu ft)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (isImperial) {
                                            val f = flow.toDoubleOrNull()
                                            if (f != null) flow = "%.1f".format(f * EnvironmentalUnitConverter.M3_PER_MGD)
                                            val v = aerationVol.toDoubleOrNull()
                                            if (v != null) aerationVol = "%.1f".format(v * EnvironmentalUnitConverter.M3_PER_CU_FT)
                                            viewModel.setGhgUnitSystem(GhgUnitSystem.METRIC)
                                        }
                                    },
                                    modifier = Modifier.weight(1f).height(38.dp).testTag("water_unit_metric"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = if (!isImperial) ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                                             else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface),
                                    border = if (isImperial) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)) else null
                                ) {
                                    Text("Metric (m³/day, m³)", fontSize = 11.sp, fontWeight = if (!isImperial) FontWeight.Bold else FontWeight.Medium)
                                }

                                Button(
                                    onClick = {
                                        if (!isImperial) {
                                            val f = flow.toDoubleOrNull()
                                            if (f != null) flow = "%.3f".format(f * EnvironmentalUnitConverter.MGD_PER_M3)
                                            val v = aerationVol.toDoubleOrNull()
                                            if (v != null) aerationVol = "%.1f".format(v * EnvironmentalUnitConverter.CU_FT_PER_M3)
                                            viewModel.setGhgUnitSystem(GhgUnitSystem.IMPERIAL)
                                        }
                                    },
                                    modifier = Modifier.weight(1f).height(38.dp).testTag("water_unit_imperial"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = if (isImperial) ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                                             else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface),
                                    border = if (!isImperial) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)) else null
                                ) {
                                    Text("Imperial (MGD, cu ft)", fontSize = 11.sp, fontWeight = if (isImperial) FontWeight.Bold else FontWeight.Medium)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Scenario Presets
                    Text("Facility Wastewater Profiles:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        items(waterPresets) { preset ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                modifier = Modifier.clickable {
                                    val fMetric = preset.second.toDoubleOrNull() ?: 2400.0
                                    flow = if (isImperial) "%.3f".format(fMetric * EnvironmentalUnitConverter.MGD_PER_M3) else preset.second
                                    bod = preset.third
                                    Toast.makeText(context, "Loaded ${preset.first} profile", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(preset.first, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = flow,
                        onValueChange = { flow = it },
                        label = { Text(if (isImperial) "Daily Flow Rate (MGD)" else "Wastewater Daily Flow Rate (m³/day)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("water_input_flow")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = bod,
                            onValueChange = { bod = it },
                            label = { Text("BOD₅ (mg/L)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("water_input_bod")
                        )
                        OutlinedTextField(
                            value = cod,
                            onValueChange = { cod = it },
                            label = { Text("COD (mg/L)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("water_input_cod")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = aerationVol,
                        onValueChange = { aerationVol = it },
                        label = { Text(if (isImperial) "Aeration Tank Volume (cu ft)" else "Aeration Basin Volume (m³)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("water_input_volume")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val fRaw = flow.toDoubleOrNull() ?: 0.0
                            val b = bod.toDoubleOrNull() ?: 0.0
                            val c = cod.toDoubleOrNull() ?: 0.0
                            val vRaw = aerationVol.toDoubleOrNull() ?: 0.0
                            val fMetric = if (isImperial) fRaw * EnvironmentalUnitConverter.M3_PER_MGD else fRaw
                            val vMetric = if (isImperial) vRaw * EnvironmentalUnitConverter.M3_PER_CU_FT else vRaw
                            viewModel.calculateWater(fMetric, b, c, vMetric)
                        },
                        modifier = Modifier.fillMaxWidth().height(46.dp).testTag("water_calculate_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                    ) {
                        Text("Calculate Water Treatment Metrics", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (result != null) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF0288D1).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .testTag("water_results_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Wastewater Treatment Results", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0288D1))
                        Spacer(modifier = Modifier.height(8.dp))

                        if (isImperial) {
                            val bodLbs = result.bodLoadKgDay * EnvironmentalUnitConverter.LBS_PER_KG
                            val codLbs = result.codLoadKgDay * EnvironmentalUnitConverter.LBS_PER_KG
                            Text("• Daily BOD Load: %,.1f lbs BOD₅/day (%.1f kg)".format(Locale.US, bodLbs, result.bodLoadKgDay), fontSize = 13.sp)
                            Text("• Daily COD Load: %,.1f lbs COD/day (%.1f kg)".format(Locale.US, codLbs, result.codLoadKgDay), fontSize = 13.sp)
                        } else {
                            Text("• Daily BOD Load: %.1f kg BOD₅/day".format(result.bodLoadKgDay), fontSize = 13.sp)
                            Text("• Daily COD Load: %.1f kg COD/day".format(result.codLoadKgDay), fontSize = 13.sp)
                        }

                        Text("• Population Equivalent (PE): %,.0f capita".format(Locale.US, result.populationEquivalent), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("• Hydraulic Retention Time (HRT): %.1f hours".format(result.hrtHours), fontSize = 13.sp)
                        Text("• Biodegradability Ratio (BOD/COD): %.2f (%s)".format(result.bodCodRatio, result.biodegradabilityClass), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(result.treatmentRecommendation, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Reporting & Export
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF0288D1), modifier = Modifier.size(16.dp))
                                Text("Reporting & Export", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("RFC 4180 CSV", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0288D1))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val fRaw = flow.toDoubleOrNull() ?: 0.0
                            val b = bod.toDoubleOrNull() ?: 0.0
                            val c = cod.toDoubleOrNull() ?: 0.0
                            val vRaw = aerationVol.toDoubleOrNull() ?: 0.0
                            val fMetric = if (isImperial) fRaw * EnvironmentalUnitConverter.M3_PER_MGD else fRaw
                            val vMetric = if (isImperial) vRaw * EnvironmentalUnitConverter.M3_PER_CU_FT else vRaw

                            Button(
                                onClick = {
                                    val csv = EnvironmentalCalculatorsExporter.generateWaterCsv(fMetric, b, c, vMetric, result)
                                    EnvironmentalCalculatorsExporter.copyToClipboard(context, "Water Treatment Audit CSV", csv)
                                    Toast.makeText(context, "✓ Water Audit CSV exported to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f).height(44.dp).testTag("export_water_csv_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                            ) {
                                Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Export as CSV", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    try {
                                        val csv = EnvironmentalCalculatorsExporter.generateWaterCsv(fMetric, b, c, vMetric, result)
                                        val file = EnvironmentalCalculatorsExporter.createExportFile(context, "wastewater_treatment_report", csv)
                                        val intent = EnvironmentalCalculatorsExporter.createShareIntent(context, file, "EDEN Wastewater Treatment Audit")
                                        context.startActivity(android.content.Intent.createChooser(intent, "Share Wastewater Report"))
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Could not share CSV: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.height(44.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                            }

                            OutlinedButton(
                                onClick = {
                                    val csv = EnvironmentalCalculatorsExporter.generateWaterCsv(fMetric, b, c, vMetric, result)
                                    EnvironmentalCalculatorsExporter.copyToClipboard(context, "Water Treatment Audit", csv)
                                    Toast.makeText(context, "✓ CSV copied to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.height(44.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NoiseCalculatorView(
    viewModel: EdenViewModel,
    result: com.example.calculator.NoiseCalculatorEngine.AcousticResult?
) {
    val context = LocalContext.current
    val unitSystem by viewModel.ghgUnitSystem.collectAsState()
    val isImperial = unitSystem == GhgUnitSystem.IMPERIAL

    var sourceDb1 by remember { mutableStateOf("78.0") }
    var sourceDb2 by remember { mutableStateOf("75.0") }
    var dayLeq by remember { mutableStateOf("62.0") }
    var nightLeq by remember { mutableStateOf("52.0") }
    var refDist by remember { mutableStateOf("10.0") }
    var targetDist by remember { mutableStateOf("50.0") }

    // Presets for Noise
    val noisePresets = listOf(
        Triple("Highway Corridor", "82.0", "79.0"),
        Triple("Construction Crane", "88.0", "84.0"),
        Triple("Substation Transformer", "68.0", "65.0"),
        Triple("Railway Marshalling", "85.0", "80.0")
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Environmental Acoustics & Decibel Summation", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Logarithmic SPL addition, Ldn day-night penalty, and distance attenuation (ISO 9613)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(12.dp))

                    // GLOBAL UNIT SWITCHER (METERS VS FEET)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.SwapHoriz,
                                        contentDescription = null,
                                        tint = Color(0xFFE65100),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Distance Measurement Units",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFE65100)
                                    )
                                }
                                Text(
                                    text = if (!isImperial) "Metric (meters)" else "Imperial (feet)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (isImperial) {
                                            val r = refDist.toDoubleOrNull()
                                            if (r != null) refDist = "%.1f".format(r * EnvironmentalUnitConverter.METERS_PER_FT)
                                            val t = targetDist.toDoubleOrNull()
                                            if (t != null) targetDist = "%.1f".format(t * EnvironmentalUnitConverter.METERS_PER_FT)
                                            viewModel.setGhgUnitSystem(GhgUnitSystem.METRIC)
                                        }
                                    },
                                    modifier = Modifier.weight(1f).height(38.dp).testTag("noise_unit_metric"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = if (!isImperial) ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
                                             else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface),
                                    border = if (isImperial) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)) else null
                                ) {
                                    Text("Metric (meters)", fontSize = 11.sp, fontWeight = if (!isImperial) FontWeight.Bold else FontWeight.Medium)
                                }

                                Button(
                                    onClick = {
                                        if (!isImperial) {
                                            val r = refDist.toDoubleOrNull()
                                            if (r != null) refDist = "%.1f".format(r * EnvironmentalUnitConverter.FT_PER_METER)
                                            val t = targetDist.toDoubleOrNull()
                                            if (t != null) targetDist = "%.1f".format(t * EnvironmentalUnitConverter.FT_PER_METER)
                                            viewModel.setGhgUnitSystem(GhgUnitSystem.IMPERIAL)
                                        }
                                    },
                                    modifier = Modifier.weight(1f).height(38.dp).testTag("noise_unit_imperial"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = if (isImperial) ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
                                             else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface),
                                    border = if (!isImperial) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)) else null
                                ) {
                                    Text("Imperial (feet)", fontSize = 11.sp, fontWeight = if (isImperial) FontWeight.Bold else FontWeight.Medium)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Scenario Presets
                    Text("Acoustic Scenarios:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        items(noisePresets) { preset ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                modifier = Modifier.clickable {
                                    sourceDb1 = preset.second
                                    sourceDb2 = preset.third
                                    Toast.makeText(context, "Loaded ${preset.first} preset", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(preset.first, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = sourceDb1, onValueChange = { sourceDb1 = it }, label = { Text("Source 1 (dBA)") }, modifier = Modifier.weight(1f).testTag("noise_input_src1"))
                        OutlinedTextField(value = sourceDb2, onValueChange = { sourceDb2 = it }, label = { Text("Source 2 (dBA)") }, modifier = Modifier.weight(1f).testTag("noise_input_src2"))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = dayLeq, onValueChange = { dayLeq = it }, label = { Text("Day Leq (dBA)") }, modifier = Modifier.weight(1f).testTag("noise_input_day"))
                        OutlinedTextField(value = nightLeq, onValueChange = { nightLeq = it }, label = { Text("Night Leq (dBA)") }, modifier = Modifier.weight(1f).testTag("noise_input_night"))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = refDist, onValueChange = { refDist = it }, label = { Text(if (isImperial) "Ref Dist (ft)" else "Ref Dist (m)") }, modifier = Modifier.weight(1f).testTag("noise_input_ref_dist"))
                        OutlinedTextField(value = targetDist, onValueChange = { targetDist = it }, label = { Text(if (isImperial) "Target Dist (ft)" else "Target Dist (m)") }, modifier = Modifier.weight(1f).testTag("noise_input_target_dist"))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val s1 = sourceDb1.toDoubleOrNull() ?: 0.0
                            val s2 = sourceDb2.toDoubleOrNull() ?: 0.0
                            val d = dayLeq.toDoubleOrNull() ?: 0.0
                            val n = nightLeq.toDoubleOrNull() ?: 0.0
                            val rRaw = refDist.toDoubleOrNull() ?: 1.0
                            val tRaw = targetDist.toDoubleOrNull() ?: 1.0
                            val rMetric = if (isImperial) rRaw * EnvironmentalUnitConverter.METERS_PER_FT else rRaw
                            val tMetric = if (isImperial) tRaw * EnvironmentalUnitConverter.METERS_PER_FT else tRaw
                            viewModel.calculateNoise(listOf(s1, s2), d, n, rMetric, tMetric)
                        },
                        modifier = Modifier.fillMaxWidth().height(46.dp).testTag("noise_calculate_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
                    ) {
                        Text("Calculate Environmental Acoustics", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (result != null) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE65100).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .testTag("noise_results_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Acoustic Results", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Combined SPL (Sum): %.1f dBA".format(result.totalDecibels), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("• Day-Night Average Level (L_dn): %.1f dBA".format(result.dayNightLevelLdn), fontSize = 13.sp)
                        val distLabel = if (isImperial) "$targetDist ft" else "$targetDist m"
                        Text("• Attenuated Level at Receiver ($distLabel): %.1f dBA".format(result.attenuatedDbAtDistance), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(result.complianceStatus, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Reporting & Export
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(16.dp))
                                Text("Reporting & Export", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("RFC 4180 CSV", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFE65100))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val s1 = sourceDb1.toDoubleOrNull() ?: 0.0
                            val s2 = sourceDb2.toDoubleOrNull() ?: 0.0
                            val d = dayLeq.toDoubleOrNull() ?: 0.0
                            val n = nightLeq.toDoubleOrNull() ?: 0.0
                            val rRaw = refDist.toDoubleOrNull() ?: 1.0
                            val tRaw = targetDist.toDoubleOrNull() ?: 1.0
                            val rMetric = if (isImperial) rRaw * EnvironmentalUnitConverter.METERS_PER_FT else rRaw
                            val tMetric = if (isImperial) tRaw * EnvironmentalUnitConverter.METERS_PER_FT else tRaw

                            Button(
                                onClick = {
                                    val csv = EnvironmentalCalculatorsExporter.generateNoiseCsv(listOf(s1, s2), d, n, rMetric, tMetric, result)
                                    EnvironmentalCalculatorsExporter.copyToClipboard(context, "Acoustics Audit CSV", csv)
                                    Toast.makeText(context, "✓ Acoustics Audit CSV exported to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f).height(44.dp).testTag("export_noise_csv_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
                            ) {
                                Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Export as CSV", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    try {
                                        val csv = EnvironmentalCalculatorsExporter.generateNoiseCsv(listOf(s1, s2), d, n, rMetric, tMetric, result)
                                        val file = EnvironmentalCalculatorsExporter.createExportFile(context, "acoustics_decibel_report", csv)
                                        val intent = EnvironmentalCalculatorsExporter.createShareIntent(context, file, "EDEN Environmental Acoustics Audit")
                                        context.startActivity(android.content.Intent.createChooser(intent, "Share Acoustics Report"))
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Could not share CSV: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.height(44.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                            }

                            OutlinedButton(
                                onClick = {
                                    val csv = EnvironmentalCalculatorsExporter.generateNoiseCsv(listOf(s1, s2), d, n, rMetric, tMetric, result)
                                    EnvironmentalCalculatorsExporter.copyToClipboard(context, "Acoustic Audit", csv)
                                    Toast.makeText(context, "✓ CSV copied to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.height(44.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalculationHistoryView(
    viewModel: EdenViewModel,
    history: List<com.example.data.model.CalculationEntity>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Logged Calculations (${history.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            if (history.isNotEmpty()) {
                OutlinedButton(onClick = { viewModel.clearCalcHistory() }) {
                    Text("Clear All")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No calculations performed yet. Run any calculator to record verifiable engineering audit logs.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(history) { item ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item.calculatorType, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                                IconButton(onClick = { viewModel.deleteCalculation(item.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(18.dp))
                                }
                            }
                            Text(item.inputDescription, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(item.resultSummary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Standard/Formula: ${item.assumptions}", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
