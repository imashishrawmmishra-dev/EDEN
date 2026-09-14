package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.calculator.AirCalculatorEngine
import com.example.calculator.CarbonCalculatorEngine
import com.example.calculator.WaterCalculatorEngine
import org.junit.Assert.assertEquals
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
        assertEquals("🌿 Net-Zero Champion", updated.badgeTitle)
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
}
