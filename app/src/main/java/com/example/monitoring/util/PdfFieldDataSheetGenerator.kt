package com.example.monitoring.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.monitoring.model.FdsInstrumentInfo
import com.example.monitoring.model.FdsProjectInfo
import com.example.monitoring.model.FieldDataSheetTemplate
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object PdfFieldDataSheetGenerator {

    /**
     * Generates a high-quality, standardized printable A4 PDF Field Data Sheet
     */
    fun generatePdfFile(
        context: Context,
        template: FieldDataSheetTemplate,
        projectInfo: FdsProjectInfo = template.defaultProjectInfo,
        instrumentInfo: FdsInstrumentInfo = template.defaultInstrumentInfo
    ): File {
        val exportDir = File(context.cacheDir, "exports").apply { if (!exists()) mkdirs() }
        val filename = "FDS_${template.domain.id.uppercase()}_${System.currentTimeMillis()}.pdf"
        val pdfFile = File(exportDir, filename)

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Standard A4 points
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        drawFieldDataSheet(canvas, template, projectInfo, instrumentInfo)

        document.finishPage(page)

        try {
            FileOutputStream(pdfFile).use { out ->
                document.writeTo(out)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            document.close()
        }

        return pdfFile
    }

    private fun drawFieldDataSheet(
        canvas: Canvas,
        template: FieldDataSheetTemplate,
        projectInfo: FdsProjectInfo,
        instrumentInfo: FdsInstrumentInfo
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Background
        canvas.drawColor(Color.WHITE)

        val margin = 28f
        val pageWidth = 595f
        val contentWidth = pageWidth - (margin * 2)

        // Top Corporate Banner Header
        paint.color = Color.parseColor("#0F6E43") // Forest green brand color
        canvas.drawRect(margin, margin, margin + contentWidth, margin + 28f, paint)

        paint.color = Color.WHITE
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 10f
        canvas.drawText("EDEN ENVIRONMENTAL QUALITY MANAGEMENT SYSTEM (QMS)", margin + 8f, margin + 18f, paint)

        paint.textSize = 8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("ISO/IEC 17025:2017 & USEPA QAPP COMPLIANT", margin + contentWidth - 210f, margin + 18f, paint)

        var currentY = margin + 36f

        // Document Title
        paint.color = Color.parseColor("#1B1B1F")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 12f
        canvas.drawText(template.formTitle, margin, currentY + 12f, paint)

        paint.textSize = 8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = Color.parseColor("#49454F")
        canvas.drawText("Doc Ref: ${template.documentNumber} | Applicable: ${template.domain.isoStandard} | ${template.domain.usepaStandard}", margin, currentY + 24f, paint)

        currentY += 32f

        // Section 1: Project & Site Identification (Box with borders)
        drawSectionHeader(canvas, paint, margin, currentY, contentWidth, "1. PROJECT & FACILITY IDENTIFICATION")
        currentY += 16f

        paint.style = Paint.Style.STROKE
        paint.color = Color.parseColor("#CCCCCC")
        paint.strokeWidth = 0.8f
        canvas.drawRect(margin, currentY, margin + contentWidth, currentY + 48f, paint)
        paint.style = Paint.Style.FILL

        paint.textSize = 7.5f
        paint.color = Color.BLACK

        // Row 1
        drawLabelValue(canvas, paint, margin + 6f, currentY + 12f, "Facility:", projectInfo.facilityName, 180f)
        drawLabelValue(canvas, paint, margin + 200f, currentY + 12f, "Station ID:", projectInfo.stationId, 160f)
        drawLabelValue(canvas, paint, margin + 370f, currentY + 12f, "Date:", projectInfo.monitoringDate, 150f)

        // Row 2
        drawLabelValue(canvas, paint, margin + 6f, currentY + 26f, "Location:", projectInfo.siteLocation, 260f)
        drawLabelValue(canvas, paint, margin + 280f, currentY + 26f, "Weather:", projectInfo.weatherCondition, 240f)

        // Row 3
        drawLabelValue(canvas, paint, margin + 6f, currentY + 40f, "Lead Chemist:", projectInfo.technicianName, 260f)
        drawLabelValue(canvas, paint, margin + 280f, currentY + 40f, "Ambient:", "Temp: ${projectInfo.ambientTemp}°C | Press: ${projectInfo.barometricPressure} mmHg | RH: ${projectInfo.relativeHumidity}%", 240f)

        currentY += 56f

        // Section 2: Instrument Details & Calibration Status
        drawSectionHeader(canvas, paint, margin, currentY, contentWidth, "2. CERTIFIED INSTRUMENTATION & CALIBRATION VERIFICATION")
        currentY += 16f

        paint.style = Paint.Style.STROKE
        paint.color = Color.parseColor("#CCCCCC")
        canvas.drawRect(margin, currentY, margin + contentWidth, currentY + 36f, paint)
        paint.style = Paint.Style.FILL

        drawLabelValue(canvas, paint, margin + 6f, currentY + 12f, "Instrument Make/Model:", instrumentInfo.instrumentModel, 240f)
        drawLabelValue(canvas, paint, margin + 260f, currentY + 12f, "Serial No:", instrumentInfo.serialNumber, 120f)
        drawLabelValue(canvas, paint, margin + 390f, currentY + 12f, "Valid Until:", instrumentInfo.calibrationValidUntil, 130f)

        drawLabelValue(canvas, paint, margin + 6f, currentY + 26f, "Pre-Cal Check:", instrumentInfo.preCalReading, 180f)
        drawLabelValue(canvas, paint, margin + 200f, currentY + 26f, "Post-Cal Check:", instrumentInfo.postCalReading, 180f)
        drawLabelValue(canvas, paint, margin + 390f, currentY + 26f, "Calibration Drift:", instrumentInfo.calibrationDrift, 130f)

        currentY += 44f

        // Section 3: Pre-Sampling Verification & Leak Check Protocols
        drawSectionHeader(canvas, paint, margin, currentY, contentWidth, "3. PRE-SAMPLING QUALITY CHECKS & LEAK TEST VERIFICATION")
        currentY += 16f

        paint.style = Paint.Style.STROKE
        paint.color = Color.parseColor("#CCCCCC")
        val checklistHeight = (template.preSamplingChecklist.size * 12f) + 6f
        canvas.drawRect(margin, currentY, margin + contentWidth, currentY + checklistHeight, paint)
        paint.style = Paint.Style.FILL

        template.preSamplingChecklist.forEachIndexed { index, check ->
            val checkY = currentY + 10f + (index * 12f)
            // Draw checkbox square
            paint.style = Paint.Style.STROKE
            paint.color = Color.parseColor("#0F6E43")
            canvas.drawRect(margin + 6f, checkY - 7f, margin + 14f, checkY + 1f, paint)
            // Checkmark
            paint.style = Paint.Style.FILL
            paint.textSize = 7f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("✓", margin + 7.5f, checkY - 0.5f, paint)

            paint.color = Color.BLACK
            paint.textSize = 7f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(check, margin + 20f, checkY, paint)
        }

        currentY += checklistHeight + 8f

        // Section 4: Sampling Run Data Table
        drawSectionHeader(canvas, paint, margin, currentY, contentWidth, "4. SAMPLING RUN FIELD MEASUREMENTS & SENSOR LOGS")
        currentY += 16f

        val colCount = template.runTableHeaders.size
        val colWidth = contentWidth / colCount
        val rowHeight = 14f

        // Table Header
        paint.color = Color.parseColor("#E8F5E9")
        canvas.drawRect(margin, currentY, margin + contentWidth, currentY + rowHeight, paint)

        paint.style = Paint.Style.STROKE
        paint.color = Color.parseColor("#999999")
        canvas.drawRect(margin, currentY, margin + contentWidth, currentY + rowHeight, paint)

        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#1B1B1F")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 6.5f

        template.runTableHeaders.forEachIndexed { colIdx, header ->
            val colX = margin + (colIdx * colWidth) + 4f
            canvas.drawText(header, colX, currentY + 10f, paint)
        }
        currentY += rowHeight

        // Table Rows
        template.sampleRunData.forEachIndexed { rowIdx, rowData ->
            val rowY = currentY + (rowIdx * rowHeight)

            if (rowIdx % 2 == 1) {
                paint.color = Color.parseColor("#F9F9F9")
                canvas.drawRect(margin, rowY, margin + contentWidth, rowY + rowHeight, paint)
            }

            paint.style = Paint.Style.STROKE
            paint.color = Color.parseColor("#DDDDDD")
            canvas.drawRect(margin, rowY, margin + contentWidth, rowY + rowHeight, paint)

            paint.style = Paint.Style.FILL
            paint.color = Color.BLACK
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.textSize = 6.5f

            val cells = listOf(rowData.pointOrTime, rowData.param1, rowData.param2, rowData.param3, rowData.param4, rowData.remarks)
            cells.take(colCount).forEachIndexed { cellIdx, cellVal ->
                val cellX = margin + (cellIdx * colWidth) + 4f
                canvas.drawText(cellVal, cellX, rowY + 9.5f, paint)
            }
        }
        currentY += (template.sampleRunData.size * rowHeight) + 8f

        // Section 5: Mathematical Equations & Derived Results
        drawSectionHeader(canvas, paint, margin, currentY, contentWidth, "5. FIELD CALCULATIONS & ANALYTICAL DERIVATIONS")
        currentY += 16f

        val calcHeight = (template.calculations.size * 11f) + 8f
        paint.style = Paint.Style.STROKE
        paint.color = Color.parseColor("#CCCCCC")
        canvas.drawRect(margin, currentY, margin + contentWidth, currentY + calcHeight, paint)
        paint.style = Paint.Style.FILL

        template.calculations.forEachIndexed { idx, (label, formula) ->
            val calcY = currentY + 9f + (idx * 11f)
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 6.8f
            paint.color = Color.parseColor("#333333")
            canvas.drawText("• $label:", margin + 6f, calcY, paint)

            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#004D40")
            canvas.drawText(formula, margin + 175f, calcY, paint)
        }

        currentY += calcHeight + 8f

        // Section 6: Regulatory Compliance Verdict Banner
        paint.color = if (template.isCompliant) Color.parseColor("#E8F5E9") else Color.parseColor("#FFEBEE")
        val verdictRect = RectF(margin, currentY, margin + contentWidth, currentY + 34f)
        canvas.drawRoundRect(verdictRect, 4f, 4f, paint)

        paint.style = Paint.Style.STROKE
        paint.color = if (template.isCompliant) Color.parseColor("#2E7D32") else Color.parseColor("#C62828")
        canvas.drawRoundRect(verdictRect, 4f, 4f, paint)
        paint.style = Paint.Style.FILL

        paint.color = if (template.isCompliant) Color.parseColor("#1B5E20") else Color.parseColor("#B71C1C")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 8.5f
        canvas.drawText("REGULATORY COMPLIANCE VERDICT: ${if (template.isCompliant) "PASSED - FULLY COMPLIANT" else "WARNING / EXCEEDED"}", margin + 10f, currentY + 12f, paint)

        paint.textSize = 7f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = Color.parseColor("#1B1B1F")
        canvas.drawText("Measured Value: ${template.measuredResult}", margin + 10f, currentY + 22f, paint)
        canvas.drawText(template.regulatoryVerdict, margin + 10f, currentY + 30f, paint)

        currentY += 42f

        // Section 7: Chain of Custody & Authorized Sign-Off
        drawSectionHeader(canvas, paint, margin, currentY, contentWidth, "6. CHAIN OF CUSTODY & STATUTORY AUTHORIZATION SIGN-OFF")
        currentY += 16f

        paint.style = Paint.Style.STROKE
        paint.color = Color.parseColor("#CCCCCC")
        canvas.drawRect(margin, currentY, margin + contentWidth, currentY + 44f, paint)
        // Middle divider
        canvas.drawLine(margin + (contentWidth / 2), currentY, margin + (contentWidth / 2), currentY + 44f, paint)
        paint.style = Paint.Style.FILL

        // Left: Sampling Chemist
        paint.textSize = 7f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = Color.BLACK
        canvas.drawText("Field Chemist / Sampling Officer:", margin + 8f, currentY + 11f, paint)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Name: ${projectInfo.technicianName}", margin + 8f, currentY + 22f, paint)
        canvas.drawText("Signature: __________________________  Date: ${projectInfo.monitoringDate}", margin + 8f, currentY + 36f, paint)

        // Right: QA/QC Reviewer
        val rightX = margin + (contentWidth / 2) + 8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("QA/QC Reviewer / Lab Technical Manager:", rightX, currentY + 11f, paint)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Name: ${projectInfo.reviewerName}", rightX, currentY + 22f, paint)
        canvas.drawText("Signature: __________________________  Date: ${projectInfo.monitoringDate}", rightX, currentY + 36f, paint)

        // Footer
        paint.color = Color.parseColor("#777777")
        paint.textSize = 6.5f
        canvas.drawText("EDEN Environmental Intelligence System • Official Field Data Sheet • Retain for 5 Years under ISO 17025 Section 8.4", margin, 820f, paint)
        canvas.drawText("Page 1 of 1", margin + contentWidth - 40f, 820f, paint)
    }

    private fun drawSectionHeader(canvas: Canvas, paint: Paint, x: Float, y: Float, width: Float, title: String) {
        paint.color = Color.parseColor("#ECEFF1")
        canvas.drawRect(x, y, x + width, y + 14f, paint)

        paint.color = Color.parseColor("#263238")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 7.5f
        canvas.drawText(title, x + 6f, y + 10f, paint)
    }

    private fun drawLabelValue(canvas: Canvas, paint: Paint, x: Float, y: Float, label: String, value: String, maxValWidth: Float) {
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = Color.parseColor("#37474F")
        canvas.drawText(label, x, y, paint)

        val labelWidth = paint.measureText(label) + 3f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = Color.BLACK

        // Truncate if too long
        var displayValue = value
        if (paint.measureText(displayValue) > maxValWidth) {
            while (displayValue.length > 3 && paint.measureText("$displayValue...") > maxValWidth) {
                displayValue = displayValue.dropLast(1)
            }
            displayValue = "$displayValue..."
        }

        canvas.drawText(displayValue, x + labelWidth, y, paint)
    }

    /**
     * Triggers native Android print spooler (allowing printing to any Wi-Fi/Bluetooth printer or Save as PDF)
     */
    fun printPdf(context: Context, pdfFile: File, jobName: String = "Field_Data_Sheet_FDS") {
        try {
            val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
            if (printManager == null) {
                Toast.makeText(context, "Print service is unavailable on this device", Toast.LENGTH_SHORT).show()
                return
            }

            val printAdapter = object : PrintDocumentAdapter() {
                override fun onLayout(
                    oldAttributes: PrintAttributes?,
                    newAttributes: PrintAttributes?,
                    cancellationSignal: CancellationSignal?,
                    callback: LayoutResultCallback?,
                    extras: Bundle?
                ) {
                    if (cancellationSignal?.isCanceled == true) {
                        callback?.onLayoutCancelled()
                        return
                    }

                    val info = PrintDocumentInfo.Builder(pdfFile.name)
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(1)
                        .build()

                    callback?.onLayoutFinished(info, true)
                }

                override fun onWrite(
                    pages: Array<out PageRange>?,
                    destination: ParcelFileDescriptor?,
                    cancellationSignal: CancellationSignal?,
                    callback: WriteResultCallback?
                ) {
                    if (destination == null) {
                        callback?.onWriteFailed("Destination descriptor is null")
                        return
                    }

                    var input: FileInputStream? = null
                    var output: FileOutputStream? = null

                    try {
                        input = FileInputStream(pdfFile)
                        output = FileOutputStream(destination.fileDescriptor)

                        val buf = ByteArray(16384)
                        var bytesRead: Int
                        while (input.read(buf).also { bytesRead = it } >= 0) {
                            if (cancellationSignal?.isCanceled == true) {
                                callback?.onWriteCancelled()
                                return
                            }
                            output.write(buf, 0, bytesRead)
                        }

                        callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                    } catch (e: Exception) {
                        callback?.onWriteFailed(e.message)
                    } finally {
                        try {
                            input?.close()
                            output?.close()
                        } catch (_: IOException) {}
                    }
                }
            }

            printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to initiate printing: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Shares the generated PDF file via Android's native share sheet
     */
    fun sharePdf(context: Context, pdfFile: File) {
        try {
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", pdfFile)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "EDEN Environmental Field Data Sheet: ${pdfFile.name}")
                putExtra(Intent.EXTRA_TEXT, "Attached is the standardized ISO/USEPA environmental Field Data Sheet (FDS) generated by EDEN Environmental Intelligence.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share Field Data Sheet PDF"))
        } catch (e: Exception) {
            Toast.makeText(context, "Could not share PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Opens the generated PDF in an external viewer app
     */
    fun openPdf(context: Context, pdfFile: File) {
        try {
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", pdfFile)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Open PDF Field Data Sheet"))
        } catch (e: Exception) {
            // Fallback to sharing if no PDF viewer installed
            sharePdf(context, pdfFile)
        }
    }
}
