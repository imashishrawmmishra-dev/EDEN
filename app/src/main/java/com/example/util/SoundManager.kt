package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.ToneGenerator
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * SoundManager provides synthesized audio feedback for EDEN,
 * including a warm, harmonious ascending welcome chime played
 * when a user logs in or registers.
 */
object SoundManager {
    private const val TAG = "SoundManager"

    /**
     * Plays a pleasant 4-note ascending harmonic welcome chime:
     * C5 (523.25 Hz) -> E5 (659.25 Hz) -> G5 (783.99 Hz) -> C6 (1046.50 Hz)
     * Synthesized purely via AudioTrack (no external asset needed, 100% offline).
     */
    fun playWelcomeSound(context: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                playSynthesizedWelcomeChime()
            } catch (e: Throwable) {
                Log.w(TAG, "AudioTrack synthesis failed, falling back to ToneGenerator", e)
                fallbackToneChime()
            }
        }
    }

    private fun playSynthesizedWelcomeChime() {
        val sampleRate = 44100
        // 4 ascending harmonious frequencies: C5, E5, G5, C6
        val frequencies = doubleArrayOf(523.25, 659.25, 783.99, 1046.50)
        val noteDurationSec = 0.18
        val decayDurationSec = 0.25
        val totalDurationSec = (frequencies.size * noteDurationSec) + decayDurationSec
        val totalSamples = (sampleRate * totalDurationSec).toInt()

        val pcmBuffer = ShortArray(totalSamples)

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / sampleRate
            var sampleVal = 0.0

            for (n in frequencies.indices) {
                val noteStart = n * noteDurationSec
                if (t >= noteStart) {
                    val noteTime = t - noteStart
                    val freq = frequencies[n]

                    // Smooth exponential decay envelope
                    val envelope = exp(-noteTime * 4.5)
                    // Fundamental tone + subtle 2nd harmonic for rich acoustic warmth
                    val fundamental = sin(2.0 * PI * freq * noteTime)
                    val overtone = 0.3 * sin(2.0 * PI * (freq * 2.0) * noteTime)

                    sampleVal += (fundamental + overtone) * envelope * 0.32
                }
            }

            // Clamp and convert to 16-bit PCM short
            val clamped = sampleVal.coerceIn(-1.0, 1.0)
            pcmBuffer[i] = (clamped * Short.MAX_VALUE).toInt().toShort()
        }

        val bufferSize = pcmBuffer.size * 2 // 2 bytes per short

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val audioFormat = AudioFormat.Builder()
            .setSampleRate(sampleRate)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()

        val track = AudioTrack.Builder()
            .setAudioAttributes(audioAttributes)
            .setAudioFormat(audioFormat)
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        track.write(pcmBuffer, 0, pcmBuffer.size)
        track.play()

        // Clean up after playback ends
        Thread.sleep((totalDurationSec * 1000).toLong() + 150)
        track.stop()
        track.release()
    }

    private fun fallbackToneChime() {
        try {
            val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 90)
            toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 400)
            Thread.sleep(450)
            toneGen.release()
        } catch (ignored: Throwable) {
            // Ignore if device audio is restricted
        }
    }
}
