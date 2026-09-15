package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.calculator.AirCalculatorEngine
import com.example.calculator.CarbonCalculatorEngine
import com.example.calculator.WaterCalculatorEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("EDEN", appName)
    }

    @Test
    fun `test deterministic carbon calculation`() {
        val result = CarbonCalculatorEngine.calculateInventory(
            fuelType = CarbonCalculatorEngine.FuelType.DIESEL,
            fuelQuantity = 1000.0,
            electricityKwh = 10000.0,
            gridRegion = CarbonCalculatorEngine.GridRegion.US_AVERAGE,
            transportMode = CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT,
            transportVolume = 5000.0
        )
        // Diesel: 1000 * 2.687 = 2687 kg CO2e
        assertEquals(2687.0, result.scope1Kg, 0.01)
        // Electricity: 10000 * 0.386 = 3860 kg CO2e
        assertEquals(3860.0, result.scope2Kg, 0.01)
        // Freight: 5000 * 0.105 = 525 kg CO2e
        assertEquals(525.0, result.scope3Kg, 0.01)
        assertEquals(7.072, result.totalTonnes, 0.01)
        assertTrue(result.reductionScenarios.isNotEmpty())
    }

    @Test
    fun `test deterministic wastewater loading`() {
        // Q = 1000 m3/d, BOD = 300 mg/L
        // BOD load = 1000 * 300 * 10^-3 = 300 kg/day
        // PE = 300 / 0.06 = 5000 population equivalent
        val result = WaterCalculatorEngine.calculateWastewaterMetrics(
            flowM3Day = 1000.0,
            bodMgL = 300.0,
            codMgL = 600.0,
            tankVolumeM3 = 500.0
        )
        assertEquals(300.0, result.bodLoadKgDay, 0.01)
        assertEquals(600.0, result.codLoadKgDay, 0.01)
        assertEquals(5000.0, result.populationEquivalent, 0.01)
        assertEquals(12.0, result.hrtHours, 0.01)
        assertEquals(0.5, result.bodCodRatio, 0.01)
        assertTrue(result.biodegradabilityClass.contains("Readily"))
    }

    @Test
    fun `test air stack volumetric flow calculation`() {
        // Velocity = 10 m/s, Diameter = 2 m
        // Area = pi * 1^2 = 3.14159 m2
        // Flow = 10 * 3.14159 * 3600 = 113097.3 m3/h
        val result = AirCalculatorEngine.calculateStackFlow(
            velocityMPerSec = 10.0,
            diameterMeters = 2.0,
            pollutantConcMgPerM3 = 50.0
        )
        assertEquals(3.1416, result.ductAreaM2, 0.01)
        assertEquals(31.416, result.flowRateM3Sec, 0.01)
        assertEquals(5.655, result.emissionRateKgHour, 0.01)
    }

    @Test
    fun `test Ask EDEN offline grounded query`() = kotlinx.coroutines.runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = com.example.data.repository.EdenRepository(context)
        repository.ensureSeeded()
        val aiService = com.example.data.remote.EdenAiService(repository)

        val answer = aiService.answerQuestion("Scope 1 emissions GHG Protocol", forceOffline = true)
        assertTrue("Answer text should not be empty", answer.text.isNotBlank())
        assertTrue("Answer should reference Scope or GHG", answer.text.contains("Scope", ignoreCase = true) || answer.text.contains("GHG", ignoreCase = true))
        assertTrue("Should have citations or retrieved nodes", answer.citations.isNotEmpty() || answer.retrievedNodes.isNotEmpty())
    }

    @Test
    fun `test user auth profile and reward points`() {
        val user = com.example.data.model.UserAuthProfile(
            isLoggedIn = true,
            authMethod = "PURPOSE_DESIGNATION",
            displayName = "Ashish Mishra",
            purpose = "Carbon Auditing",
            designation = "Lead Engineer",
            rewardPoints = 100
        )
        assertEquals(100, user.rewardPoints)
        assertEquals("🌱 Green Sentinel", user.badgeTitle)

        val updated = user.copy(rewardPoints = 250)
        assertEquals("🍃 Eco Pioneer", updated.badgeTitle)

        val champion = user.copy(rewardPoints = 550)
        assertEquals("🌿 Net-Zero Champion", champion.badgeTitle)
    }

    @Test
    fun `test device GHG profile calculation`() {
        // 5 Watts * 6 hours = 30 Wh = 0.03 kWh / day
        // Operational GHG = 0.03 * 0.385 = 0.01155 kg CO2e / day
        val device = com.example.data.model.DeviceGhgProfile(
            deviceType = "Smartphone",
            make = "Google",
            model = "Pixel",
            powerWatts = 5.0,
            dailyScreenHours = 6.0,
            gridCarbonIntensityKgKwh = 0.385,
            embodiedCarbonKg = 55.0
        )
        assertEquals(0.01155, device.dailyOperationalCarbonKg, 0.001)
        assertEquals(1.925, device.operationalGramsPerHour, 0.01)
        assertTrue(device.annualTotalKg > 0)
    }

    @Test
    fun `test environmental sensors WHO AQI categorization`() {
        val goodSensors = com.example.data.model.EnvironmentalSensorsState(pm25 = 10.0, pm10 = 25.0)
        assertTrue(goodSensors.whoAqiStatus.contains("Good"))

        val moderateSensors = com.example.data.model.EnvironmentalSensorsState(pm25 = 20.0)
        assertTrue(moderateSensors.whoAqiStatus.contains("Moderate"))

        val unhealthySensors = com.example.data.model.EnvironmentalSensorsState(pm25 = 45.0)
        assertTrue(unhealthySensors.whoAqiStatus.contains("Unhealthy"))
    }

    @Test
    fun `test app auto-update model and version comparison`() {
        val updateInfo = com.example.data.model.AppUpdateInfo(
            currentVersionCode = 1,
            currentVersionName = "1.0.0",
            latestVersionCode = 2,
            latestVersionName = "1.1.0",
            autoCheckEnabled = true,
            autoDownloadOnWifi = true
        )
        assertTrue(updateInfo.latestVersionCode > updateInfo.currentVersionCode)
        assertTrue(updateInfo.isUpdateAvailable)
        assertEquals("1.1.0", updateInfo.latestVersionName)
        assertTrue(updateInfo.releaseNotes.isNotEmpty())

        val updated = updateInfo.copy(
            currentVersionCode = 2,
            currentVersionName = "1.1.0",
            isUpdateAvailable = false,
            status = com.example.data.model.UpdateState.UP_TO_DATE
        )
        assertFalse(updated.isUpdateAvailable)
        assertEquals(com.example.data.model.UpdateState.UP_TO_DATE, updated.status)
    }

    @Test
    fun `test environmental knowledge repository all 10 topics loaded`() {
        val allTopics = com.example.data.repository.EnvironmentalKnowledgeRepository.allTopics
        assertEquals(10, allTopics.size)

        // Verify key required topics exist
        val domains = allTopics.map { it.domain }
        assertTrue(domains.contains(com.example.data.model.EnvironmentalDomain.AIR_QUALITY))
        assertTrue(domains.contains(com.example.data.model.EnvironmentalDomain.WATER_QUALITY))
        assertTrue(domains.contains(com.example.data.model.EnvironmentalDomain.ANIMALS_BIODIVERSITY))
        assertTrue(domains.contains(com.example.data.model.EnvironmentalDomain.PHYSICAL_QUALITIES))
        assertTrue(domains.contains(com.example.data.model.EnvironmentalDomain.SOIL_CONTROL))
        assertTrue(domains.contains(com.example.data.model.EnvironmentalDomain.NOISE_QUALITY))
        assertTrue(domains.contains(com.example.data.model.EnvironmentalDomain.FOOD_MICROBIOLOGY))
        assertTrue(domains.contains(com.example.data.model.EnvironmentalDomain.POLLUTION_TESTING))
        assertTrue(domains.contains(com.example.data.model.EnvironmentalDomain.POLLUTION_MONITORING))
        assertTrue(domains.contains(com.example.data.model.EnvironmentalDomain.POLLUTION_REMEDIATION))

        // Check each topic has charts, QMS, ongoing researches, and threshold guidelines
        allTopics.forEach { topic ->
            assertTrue("Primary indicators must not be empty for ${topic.id}", topic.primaryIndicators.isNotEmpty())
            assertTrue("QMS checklist must not be empty for ${topic.id}", topic.qualityManagement.complianceChecklist.isNotEmpty())
            assertTrue("Researches must not be empty for ${topic.id}", topic.ongoingResearches.isNotEmpty())
            assertTrue("Threshold guideline must be set for ${topic.id}", topic.thresholdGuideline.isNotBlank())
        }
    }

    @Test
    fun `test air quality monitoring metrics and repository stations`() {
        val stations = com.example.data.repository.AirQualityRepository.defaultStations
        assertTrue(stations.isNotEmpty())

        val firstStation = stations.first()
        assertTrue(firstStation.pm25 > 0)
        assertTrue(firstStation.pm10 > 0)
        assertTrue(firstStation.no2 > 0)
        assertTrue(firstStation.o3 > 0)

        // Verify all 4 required pollutants have hourly history data
        val pollutants = listOf(
            com.example.data.model.AirPollutantType.PM25,
            com.example.data.model.AirPollutantType.PM10,
            com.example.data.model.AirPollutantType.NO2,
            com.example.data.model.AirPollutantType.O3
        )
        pollutants.forEach { pollutant ->
            val history = firstStation.hourlyHistory[pollutant]
            assertNotNull(history)
            assertTrue(history!!.isNotEmpty())
            assertTrue(pollutant.whoGuideline24h > 0)
            assertTrue(pollutant.epaStandardLimit > 0)
        }

        // Test status evaluation logic
        val goodPm25 = com.example.data.model.AirPollutantType.PM25.evaluateStatus(10.0)
        assertTrue(goodPm25 == com.example.data.model.AirQualityStatus.EXCELLENT || goodPm25 == com.example.data.model.AirQualityStatus.GOOD)

        val spikePm25 = com.example.data.model.AirPollutantType.PM25.evaluateStatus(40.0)
        assertTrue(spikePm25 == com.example.data.model.AirQualityStatus.UNHEALTHY || spikePm25 == com.example.data.model.AirQualityStatus.UNHEALTHY_SENSITIVE)
    }

    @Test
    fun `test air quality threshold notification system for PM25 and NO2`() {
        val stations = com.example.data.repository.AirQualityRepository.defaultStations
        val industrialStation = stations.find { it.id == "station_industrial_corridor" }
        assertNotNull(industrialStation)

        // Harbor Industrial station naturally breaches PM2.5 (>15) and NO2 (>25)
        assertTrue("PM2.5 should exceed 15 in industrial zone", industrialStation!!.isPm25Exceeded)
        assertTrue("NO2 should exceed 25 in industrial zone", industrialStation.isNo2Exceeded)
        assertTrue("Industrial station must flag threshold alert", industrialStation.hasThresholdAlert)

        val botanicalStation = stations.find { it.id == "station_green_belt" }
        assertNotNull(botanicalStation)
        assertFalse("PM2.5 must be within safe limits in botanical reserve", botanicalStation!!.isPm25Exceeded)
        assertFalse("NO2 must be within safe limits in botanical reserve", botanicalStation.isNo2Exceeded)
        assertFalse("Botanical reserve must not flag breach", botanicalStation.hasThresholdAlert)

        // Threshold evaluation checks
        val pm25SafeLimit = com.example.data.model.AirPollutantType.PM25.whoGuideline24h
        val no2SafeLimit = com.example.data.model.AirPollutantType.NO2.whoGuideline24h
        assertEquals(15.0, pm25SafeLimit, 0.001)
        assertEquals(25.0, no2SafeLimit, 0.001)
    }

    @Test
    fun `test topbar alert indicator state reactivity on sensor threshold breach`() {
        val context = ApplicationProvider.getApplicationContext<Context>() as android.app.Application
        val viewModel = com.example.viewmodel.EdenViewModel(context)

        // Default station (Urban Alpha) is within WHO guidelines
        val initialAlert = viewModel.isAirQualityThresholdExceeded.value
        assertFalse("Initial urban alpha station should not breach WHO thresholds", initialAlert)

        // When switching to Harbor Logistics Industrial Belt, values exceed WHO guidelines (PM2.5=28.5 > 15, NO2=34 > 25)
        viewModel.selectAirStation("station_industrial_corridor")
        val industrialAlert = viewModel.isAirQualityThresholdExceeded.value
        assertTrue("Industrial corridor station must trigger topbar alert indicator", industrialAlert)

        val activeStation = viewModel.currentAirQualityStation.value
        assertEquals("station_industrial_corridor", activeStation.id)
        assertTrue(activeStation.hasThresholdAlert)
        assertTrue(activeStation.isPm25Exceeded)
        assertTrue(activeStation.isNo2Exceeded)

        // Switch to pristine Botanical Reserve
        viewModel.selectAirStation("station_green_belt")
        val greenBeltAlert = viewModel.isAirQualityThresholdExceeded.value
        assertFalse("Botanical reserve station must be safe with alert indicator off", greenBeltAlert)

        // Trigger a simulated spike in Botanical Reserve
        viewModel.simulateAirSpike(com.example.data.model.AirPollutantType.PM25)
        val spikedAlert = viewModel.isAirQualityThresholdExceeded.value
        assertTrue("Simulated spike in PM2.5 must immediately trigger topbar alert indicator", spikedAlert)

        // Dialog toggle control
        assertFalse(viewModel.showAirQualityAlertDialog.value)
        viewModel.setAirQualityAlertDialog(true)
        assertTrue(viewModel.showAirQualityAlertDialog.value)
        viewModel.setAirQualityAlertDialog(false)
        assertFalse(viewModel.showAirQualityAlertDialog.value)
    }

    @Test
    fun `test GHG results CSV generation and RFC 4180 compliance`() {
        val inputs = com.example.util.GhgCsvExporter.GhgInputParameters(
            fuelType = CarbonCalculatorEngine.FuelType.DIESEL,
            fuelQuantity = 2000.0,
            electricityKwh = 15000.0,
            gridRegion = CarbonCalculatorEngine.GridRegion.US_AVERAGE,
            transportMode = CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT,
            transportVolume = 10000.0,
            timestamp = 1715000000000L
        )

        val result = CarbonCalculatorEngine.calculateInventory(
            fuelType = inputs.fuelType,
            fuelQuantity = inputs.fuelQuantity,
            electricityKwh = inputs.electricityKwh,
            gridRegion = inputs.gridRegion,
            transportMode = inputs.transportMode,
            transportVolume = inputs.transportVolume
        )

        val csv = com.example.util.GhgCsvExporter.generateGhgCsv(inputs, result)
        assertNotNull(csv)
        assertTrue(csv.isNotBlank())

        // Verify key RFC 4180 sections exist
        assertTrue("Must include GHG Protocol Compliance Header", csv.contains("GHG Protocol Corporate Accounting and Reporting Standard"))
        assertTrue("Must include Section 1: Summary", csv.contains("SECTION 1: GHG INVENTORY QUANTIFICATION SUMMARY"))
        assertTrue("Must include Section 2: Activity Data", csv.contains("SECTION 2: ACTIVITY DATA & EMISSION FACTOR AUDIT TRAIL"))
        assertTrue("Must include Section 3: Mitigation Roadmap", csv.contains("SECTION 3: ANALYTICAL FINDINGS & MITIGATION ROADMAP"))
        assertTrue("Must include Section 4: Methodology", csv.contains("SECTION 4: METHODOLOGY & ASSURANCE STATEMENT"))

        // Verify numeric calculations match in CSV
        // Diesel 2000 * 2.687 = 5374.00 kg
        assertTrue(csv.contains("5374.00"))
        // Electricity 15000 * 0.386 = 5790.00 kg
        assertTrue(csv.contains("5790.00"))
        // Transport 10000 * 0.105 = 1050.00 kg
        assertTrue(csv.contains("1050.00"))
        // Total = 12214.00 kg = 12.2140 t
        assertTrue(csv.contains("12214.00"))
        assertTrue(csv.contains("12.2140"))

        // Verify fuel units and activity units are present
        assertTrue(csv.contains("litres"))
        assertTrue(csv.contains("kWh"))
        assertTrue(csv.contains("tonne-km"))
    }

    @Test
    fun `test GHG CSV export file creation and stream writing`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testCsv = "\"EDEN Test GHG Export\"\n\"Scope 1\",\"1000.0\"\n"

        // Test physical file creation
        val file = com.example.util.GhgCsvExporter.createExportFile(context, testCsv, "test_ghg_export")
        assertTrue("Exported file must exist", file.exists())
        assertTrue("Exported file must contain bytes", file.length() > 0)
        assertEquals(testCsv, file.readText())

        // Test stream writing for SAF CreateDocument
        val outputStream = java.io.ByteArrayOutputStream()
        val success = com.example.util.GhgCsvExporter.writeCsvToStream(outputStream, testCsv)
        assertTrue(success)
        assertEquals(testCsv, outputStream.toString("UTF-8"))

        // Test share intent creation
        val shareIntent = com.example.util.GhgCsvExporter.createShareIntent(context, file)
        assertEquals(android.content.Intent.ACTION_SEND, shareIntent.action)
        assertEquals("text/csv", shareIntent.type)
        assertNotNull(shareIntent.getParcelableExtra(android.content.Intent.EXTRA_STREAM, android.net.Uri::class.java))
    }

    @Test
    fun `test ViewModel GHG calculation updates inputs and export state`() {
        val context = ApplicationProvider.getApplicationContext<Context>() as android.app.Application
        val viewModel = com.example.viewmodel.EdenViewModel(context)

        // Trigger calculation
        viewModel.calculateCarbon(
            fuel = CarbonCalculatorEngine.FuelType.NATURAL_GAS,
            qty = 500.0,
            kwh = 12000.0,
            grid = CarbonCalculatorEngine.GridRegion.EU_AVERAGE,
            mode = CarbonCalculatorEngine.TransportMode.TRAIN_COMMUTER,
            travelVol = 4000.0
        )

        val result = viewModel.carbonResult.value
        assertNotNull(result)

        val lastInputs = viewModel.lastCarbonInputs.value
        assertEquals(CarbonCalculatorEngine.FuelType.NATURAL_GAS, lastInputs.fuelType)
        assertEquals(500.0, lastInputs.fuelQuantity, 0.001)
        assertEquals(12000.0, lastInputs.electricityKwh, 0.001)
        assertEquals(CarbonCalculatorEngine.GridRegion.EU_AVERAGE, lastInputs.gridRegion)
        assertEquals(CarbonCalculatorEngine.TransportMode.TRAIN_COMMUTER, lastInputs.transportMode)
        assertEquals(4000.0, lastInputs.transportVolume, 0.001)

        // Modal toggle
        assertFalse(viewModel.showGhgCsvExportModal.value)
        viewModel.setGhgCsvExportModal(true)
        assertTrue(viewModel.showGhgCsvExportModal.value)
        viewModel.setGhgCsvExportModal(false)
        assertFalse(viewModel.showGhgCsvExportModal.value)
    }

    @Test
    fun `test GHG scenario comparison delta calculations and trajectory assessment`() {
        val baseInputs = com.example.util.GhgCsvExporter.GhgInputParameters(
            fuelType = CarbonCalculatorEngine.FuelType.DIESEL,
            fuelQuantity = 2000.0,
            electricityKwh = 20000.0,
            gridRegion = CarbonCalculatorEngine.GridRegion.US_AVERAGE,
            transportMode = CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT,
            transportVolume = 10000.0
        )
        val baseResult = CarbonCalculatorEngine.calculateInventory(
            baseInputs.fuelType, baseInputs.fuelQuantity, baseInputs.electricityKwh,
            baseInputs.gridRegion, baseInputs.transportMode, baseInputs.transportVolume
        )
        val baselineScenario = com.example.calculator.GhgScenario(
            id = "base_1",
            name = "Baseline FY24",
            inputs = baseInputs,
            result = baseResult,
            isBaseline = true
        )

        // Proposed green scenario: switch to LPG, EU Grid, Rail transport
        val propInputs = com.example.util.GhgCsvExporter.GhgInputParameters(
            fuelType = CarbonCalculatorEngine.FuelType.LPG,
            fuelQuantity = 1000.0,
            electricityKwh = 10000.0,
            gridRegion = CarbonCalculatorEngine.GridRegion.EU_AVERAGE,
            transportMode = CarbonCalculatorEngine.TransportMode.TRAIN_COMMUTER,
            transportVolume = 5000.0
        )
        val propResult = CarbonCalculatorEngine.calculateInventory(
            propInputs.fuelType, propInputs.fuelQuantity, propInputs.electricityKwh,
            propInputs.gridRegion, propInputs.transportMode, propInputs.transportVolume
        )
        val proposedScenario = com.example.calculator.GhgScenario(
            id = "prop_1",
            name = "Decarbonized Operation",
            inputs = propInputs,
            result = propResult
        )

        val comparison = com.example.calculator.GhgScenarioComparison(
            baseline = baselineScenario,
            current = proposedScenario
        )

        // Verify reductions
        assertTrue("Proposed scenario must be a net reduction", comparison.isNetReduction)
        assertFalse("Should not be emissions increase", comparison.isEmissionsIncrease)
        assertTrue("Delta total kg must be negative", comparison.deltaTotalKg < 0)
        assertTrue("Delta total percent must be negative", comparison.deltaTotalPercent < 0)

        // Verify trajectory assessment recognizes deep decarbonization
        val assessment = comparison.mitigationTrajectoryAssessment
        assertNotNull(assessment)
        assertTrue(assessment.isNotEmpty())

        // Verify dominant driver
        val driver = comparison.dominantAbatementDriver
        assertNotNull(driver)
        assertTrue(driver.isNotEmpty())

        // Verify comparison CSV output
        val csv = comparison.generateComparisonCsv()
        assertNotNull(csv)
        assertTrue(csv.contains("GHG Scenario Comparison Report"))
        assertTrue(csv.contains("Baseline FY24"))
        assertTrue(csv.contains("Decarbonized Operation"))
        assertTrue(csv.contains("TOTAL GHG INVENTORY"))
    }

    @Test
    fun `test ViewModel scenario saving, baseline selection, and comparison lifecycle`() {
        val context = ApplicationProvider.getApplicationContext<Context>() as android.app.Application
        val viewModel = com.example.viewmodel.EdenViewModel(context)

        // Check initial seeded baseline exists
        val initialScenarios = viewModel.savedGhgScenarios.value
        assertTrue("Initial scenarios must have at least seeded baseline", initialScenarios.isNotEmpty())
        val initialBaseline = initialScenarios.find { it.isBaseline }
        assertNotNull("Must have a default baseline scenario", initialBaseline)

        // Trigger active calculation
        viewModel.calculateCarbon(
            fuel = CarbonCalculatorEngine.FuelType.PETROL,
            qty = 800.0,
            kwh = 5000.0,
            grid = CarbonCalculatorEngine.GridRegion.HIGH_RENEWABLE,
            mode = CarbonCalculatorEngine.TransportMode.ELECTRIC_VEHICLE,
            travelVol = 1200.0
        )

        // Save current calculation as new scenario
        val saved = viewModel.saveCurrentCalculationAsScenario(
            name = "Fleet Electrification Phase 1",
            description = "EV test fleet with high-renewable solar grid",
            setAsBaseline = false
        )

        assertEquals("Fleet Electrification Phase 1", saved.name)
        val scenariosAfterSave = viewModel.savedGhgScenarios.value
        assertEquals(initialScenarios.size + 1, scenariosAfterSave.size)

        // Verify comparison result generation
        val comparison = viewModel.getComparisonResult()
        assertNotNull(comparison)
        assertEquals(initialBaseline?.id, comparison?.baseline?.id)

        // Toggle comparison mode
        assertFalse(viewModel.isComparisonModeActive.value)
        viewModel.toggleComparisonMode(true)
        assertTrue(viewModel.isComparisonModeActive.value)
        viewModel.toggleComparisonMode(false)
        assertFalse(viewModel.isComparisonModeActive.value)

        // Switch baseline to the newly saved scenario
        viewModel.setBaselineScenario(saved.id)
        assertEquals(saved.id, viewModel.selectedBaselineScenarioId.value)
        val newBaselineInList = viewModel.savedGhgScenarios.value.find { it.id == saved.id }
        assertTrue(newBaselineInList?.isBaseline == true)

        // Delete scenario
        viewModel.deleteScenario(saved.id)
        val scenariosAfterDelete = viewModel.savedGhgScenarios.value
        assertNull(scenariosAfterDelete.find { it.id == saved.id })
    }

    @Test
    fun `test GHG unit converter conversions between Metric and Imperial`() {
        // Fuel conversions:
        // 1000 Litres of Diesel to Gallons
        val gal = com.example.calculator.GhgUnitConverter.convertFuelFromMetric(
            1000.0,
            CarbonCalculatorEngine.FuelType.DIESEL,
            com.example.calculator.GhgUnitSystem.IMPERIAL
        )
        // 1000 / 3.785411784 = ~264.17 gal
        assertEquals(264.17, gal, 0.05)

        // Convert back to metric
        val metricLitres = com.example.calculator.GhgUnitConverter.convertFuelToMetric(
            gal,
            CarbonCalculatorEngine.FuelType.DIESEL,
            com.example.calculator.GhgUnitSystem.IMPERIAL
        )
        assertEquals(1000.0, metricLitres, 0.05)

        // Coal / Biomass (kg <-> lbs):
        val lbs = com.example.calculator.GhgUnitConverter.convertFuelFromMetric(
            100.0,
            CarbonCalculatorEngine.FuelType.COAL,
            com.example.calculator.GhgUnitSystem.IMPERIAL
        )
        assertEquals(220.462, lbs, 0.05)

        val metricKg = com.example.calculator.GhgUnitConverter.convertFuelToMetric(
            lbs,
            CarbonCalculatorEngine.FuelType.COAL,
            com.example.calculator.GhgUnitSystem.IMPERIAL
        )
        assertEquals(100.0, metricKg, 0.05)

        // Transport conversions:
        // Road Freight (ton-km <-> ton-miles)
        val tonMiles = com.example.calculator.GhgUnitConverter.convertTransportFromMetric(
            1000.0,
            CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT,
            com.example.calculator.GhgUnitSystem.IMPERIAL
        )
        assertEquals(684.93, tonMiles, 0.05)

        val metricTonKm = com.example.calculator.GhgUnitConverter.convertTransportToMetric(
            tonMiles,
            CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT,
            com.example.calculator.GhgUnitSystem.IMPERIAL
        )
        assertEquals(1000.0, metricTonKm, 0.05)

        // Commuter Rail (passenger-km <-> passenger-miles)
        val pMiles = com.example.calculator.GhgUnitConverter.convertTransportFromMetric(
            500.0,
            CarbonCalculatorEngine.TransportMode.TRAIN_COMMUTER,
            com.example.calculator.GhgUnitSystem.IMPERIAL
        )
        assertEquals(310.686, pMiles, 0.05)

        // Display labels
        assertEquals("gallons", com.example.calculator.GhgUnitConverter.getFuelUnit(CarbonCalculatorEngine.FuelType.DIESEL, com.example.calculator.GhgUnitSystem.IMPERIAL))
        assertEquals("litres", com.example.calculator.GhgUnitConverter.getFuelUnit(CarbonCalculatorEngine.FuelType.DIESEL, com.example.calculator.GhgUnitSystem.METRIC))
        assertEquals("ton-miles", com.example.calculator.GhgUnitConverter.getTransportUnit(CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT, com.example.calculator.GhgUnitSystem.IMPERIAL))
        assertEquals("tonne-km", com.example.calculator.GhgUnitConverter.getTransportUnit(CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT, com.example.calculator.GhgUnitSystem.METRIC))
    }

    @Test
    fun `test EdenViewModel global GHG unit system state management`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.viewmodel.EdenViewModel(app)

        // Default is METRIC
        assertEquals(com.example.calculator.GhgUnitSystem.METRIC, viewModel.ghgUnitSystem.value)

        // Toggle to IMPERIAL
        viewModel.setGhgUnitSystem(com.example.calculator.GhgUnitSystem.IMPERIAL)
        assertEquals(com.example.calculator.GhgUnitSystem.IMPERIAL, viewModel.ghgUnitSystem.value)

        // Toggle back to METRIC
        viewModel.setGhgUnitSystem(com.example.calculator.GhgUnitSystem.METRIC)
        assertEquals(com.example.calculator.GhgUnitSystem.METRIC, viewModel.ghgUnitSystem.value)
    }
}
