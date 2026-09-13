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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.AirCalculatorEngine
import com.example.calculator.CarbonCalculatorEngine
import com.example.viewmodel.EdenViewModel

@Composable
fun CalculatorsScreen(
    viewModel: EdenViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCalcIndex by remember { mutableIntStateOf(0) }
    val calcTabs = listOf("Live GPS Carbon", "Carbon Scope 1-3", "Air Stack & Flow", "Water & Wastewater", "Acoustics & Noise", "History")

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
    var fuelType by remember { mutableStateOf(CarbonCalculatorEngine.FuelType.DIESEL) }
    var fuelQty by remember { mutableStateOf("1500") }
    var electricityKwh by remember { mutableStateOf("25000") }
    var gridRegion by remember { mutableStateOf(CarbonCalculatorEngine.GridRegion.US_AVERAGE) }
    var transportMode by remember { mutableStateOf(CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT) }
    var transportVol by remember { mutableStateOf("8000") }

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

                    Text("Scope 1: Stationary & Fleet Fuel", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = fuelQty,
                            onValueChange = { fuelQty = it },
                            label = { Text("Fuel Quantity (${fuelType.unit})") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Scope 2: Purchased Electricity", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    OutlinedTextField(
                        value = electricityKwh,
                        onValueChange = { electricityKwh = it },
                        label = { Text("Electricity Consumed (kWh)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Scope 3: Freight & Logistics", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    OutlinedTextField(
                        value = transportVol,
                        onValueChange = { transportVol = it },
                        label = { Text("Transport Volume (${transportMode.unit})") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val fQty = fuelQty.toDoubleOrNull() ?: 0.0
                            val kwh = electricityKwh.toDoubleOrNull() ?: 0.0
                            val tVol = transportVol.toDoubleOrNull() ?: 0.0
                            viewModel.calculateCarbon(fuelType, fQty, kwh, gridRegion, transportMode, tVol)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("carbon_calculate_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Calculate GHG Inventory", fontWeight = FontWeight.Bold)
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

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Footprint:", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "%.2f t CO₂e".format(result.totalTonnes),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("• Scope 1 Direct: %.1f kg CO₂e (%.1f%%)".format(result.scope1Kg, result.scope1Percent), fontSize = 13.sp)
                        Text("• Scope 2 Indirect Grid: %.1f kg CO₂e (%.1f%%)".format(result.scope2Kg, result.scope2Percent), fontSize = 13.sp)
                        Text("• Scope 3 Logistics: %.1f kg CO₂e (%.1f%%)".format(result.scope3Kg, result.scope3Percent), fontSize = 13.sp)

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
    var velocity by remember { mutableStateOf("14.5") }
    var diameter by remember { mutableStateOf("1.8") }
    var concentration by remember { mutableStateOf("45.0") }

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
                        text = "Industrial Chimney Flow & Pollutant Emission Rate",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Compliant with EPA Method 2 (Pitot tube & volumetric mass flow)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = velocity,
                        onValueChange = { velocity = it },
                        label = { Text("Stack Gas Exit Velocity (m/s)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = diameter,
                        onValueChange = { diameter = it },
                        label = { Text("Stack Internal Diameter (m)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = concentration,
                        onValueChange = { concentration = it },
                        label = { Text("Pollutant Concentration (mg/Nm³)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val v = velocity.toDoubleOrNull() ?: 0.0
                            val d = diameter.toDoubleOrNull() ?: 0.0
                            val c = concentration.toDoubleOrNull() ?: 0.0
                            viewModel.calculateAirStack(v, d, c)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("air_calculate_button")
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
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Deterministic Flow & Emission Results", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Stack Cross-sectional Area: %.3f m²".format(result.ductAreaM2), fontSize = 13.sp)
                        Text("• Volumetric Flow Rate: %.2f m³/s (%.1f m³/h)".format(result.flowRateM3Sec, result.flowRateM3Hour), fontSize = 13.sp)
                        Text("• Mass Emission Rate: %.4f kg/h (%.4f g/s)".format(result.emissionRateKgHour, result.emissionRateGSec), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(result.formulaExplanation, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    var flow by remember { mutableStateOf("2400") }
    var bod by remember { mutableStateOf("280") }
    var cod by remember { mutableStateOf("520") }
    var aerationVol by remember { mutableStateOf("800") }

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
                        text = "BOD/COD mass rates, Population Equivalent (PE), and HRT",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = flow,
                        onValueChange = { flow = it },
                        label = { Text("Wastewater Daily Flow Rate (m³/day)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = bod,
                            onValueChange = { bod = it },
                            label = { Text("BOD₅ (mg/L)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = cod,
                            onValueChange = { cod = it },
                            label = { Text("COD (mg/L)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = aerationVol,
                        onValueChange = { aerationVol = it },
                        label = { Text("Aeration Basin Volume (m³)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val q = flow.toDoubleOrNull() ?: 0.0
                            val b = bod.toDoubleOrNull() ?: 0.0
                            val c = cod.toDoubleOrNull() ?: 0.0
                            val v = aerationVol.toDoubleOrNull() ?: 0.0
                            viewModel.calculateWater(q, b, c, v)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("water_calculate_button")
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
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Wastewater Treatment Results", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Daily BOD Load: %.1f kg BOD₅/day".format(result.bodLoadKgDay), fontSize = 13.sp)
                        Text("• Daily COD Load: %.1f kg COD/day".format(result.codLoadKgDay), fontSize = 13.sp)
                        Text("• Population Equivalent (PE): %.0f capita".format(result.populationEquivalent), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("• Hydraulic Retention Time (HRT): %.1f hours".format(result.hrtHours), fontSize = 13.sp)
                        Text("• Biodegradability Ratio (BOD/COD): %.2f (%s)".format(result.bodCodRatio, result.biodegradabilityClass), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(result.treatmentRecommendation, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    var sourceDb1 by remember { mutableStateOf("78.0") }
    var sourceDb2 by remember { mutableStateOf("75.0") }
    var dayLeq by remember { mutableStateOf("62.0") }
    var nightLeq by remember { mutableStateOf("52.0") }
    var refDist by remember { mutableStateOf("10.0") }
    var targetDist by remember { mutableStateOf("50.0") }

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
                    Text("Logarithmic SPL addition, Ldn day-night penalty, and distance attenuation", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = sourceDb1, onValueChange = { sourceDb1 = it }, label = { Text("Source 1 (dBA)") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = sourceDb2, onValueChange = { sourceDb2 = it }, label = { Text("Source 2 (dBA)") }, modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = dayLeq, onValueChange = { dayLeq = it }, label = { Text("Day Leq (dBA)") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = nightLeq, onValueChange = { nightLeq = it }, label = { Text("Night Leq (dBA)") }, modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = refDist, onValueChange = { refDist = it }, label = { Text("Ref Dist (m)") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = targetDist, onValueChange = { targetDist = it }, label = { Text("Target Dist (m)") }, modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val s1 = sourceDb1.toDoubleOrNull() ?: 0.0
                            val s2 = sourceDb2.toDoubleOrNull() ?: 0.0
                            val d = dayLeq.toDoubleOrNull() ?: 0.0
                            val n = nightLeq.toDoubleOrNull() ?: 0.0
                            val r = refDist.toDoubleOrNull() ?: 1.0
                            val t = targetDist.toDoubleOrNull() ?: 1.0
                            viewModel.calculateNoise(listOf(s1, s2), d, n, r, t)
                        },
                        modifier = Modifier.fillMaxWidth()
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
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Acoustic Results", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Combined SPL (Sum): %.1f dBA".format(result.totalDecibels), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("• Day-Night Average Level (L_dn): %.1f dBA".format(result.dayNightLevelLdn), fontSize = 13.sp)
                        Text("• Attenuated Level at Target Distance: %.1f dBA".format(result.attenuatedDbAtDistance), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(result.complianceStatus, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
