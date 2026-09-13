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
}
