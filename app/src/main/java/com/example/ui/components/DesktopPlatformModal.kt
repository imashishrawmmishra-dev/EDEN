package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Window
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

private const val EDEN_WEB_URL = "https://eden-environmental-intelligence.onrender.com"

@Composable
fun DesktopPlatformModal(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Windows", "Linux", "Desktop & DeX", "Apple", "Shortcuts")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 840.dp)
                ) {
                    // Header Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Computer,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Multi-Platform & Desktop Hub",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFF0F6E43)
                                    ) {
                                        Text(
                                            text = "ACTIVE",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Ready for Windows, Linux, Desktop/DeX & Apple (macOS)",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("desktop_modal_close_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Platform Switch Tabs
                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        edgePadding = 12.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            val icon: ImageVector = when (index) {
                                0 -> Icons.Default.DesktopWindows
                                1 -> Icons.Default.Terminal
                                2 -> Icons.Default.Devices
                                3 -> Icons.Default.Laptop
                                else -> Icons.Default.Keyboard
                            }
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            )
                        }
                    }

                    // Main Scrollable Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        when (selectedTab) {
                            0 -> WindowsPlatformSection(context)
                            1 -> LinuxPlatformSection(context)
                            2 -> DesktopDexSection(context)
                            3 -> AppleMacSection(context)
                            4 -> KeyboardShortcutsSection()
                        }
                    }

                    // Bottom Quick Actions Bar
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("EDEN Web URL", EDEN_WEB_URL)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Web platform URL copied to clipboard!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy Web URL", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(EDEN_WEB_URL))
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    Toast.makeText(context, "Unable to launch browser.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F6E43))
                        ) {
                            Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open Live Web Platform", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WindowsPlatformSection(context: Context) {
    Column {
        PlatformBanner(
            title = "Windows 11 & Windows 10 Compatibility",
            badge = "WSA • Google Play Games • Native PWA",
            icon = Icons.Default.DesktopWindows,
            description = "Run EDEN directly on Windows PCs with mouse precision, full-size monitors, and responsive window snapping."
        )

        Spacer(modifier = Modifier.height(14.dp))

        MethodCard(
            stepNumber = "1",
            title = "Zero-Install Instant Windows Access (Edge / Chrome)",
            badge = "RECOMMENDED",
            summary = "Open the live EDEN platform directly in Microsoft Edge or Google Chrome on any Windows PC. You can install it as a native desktop application with full offline caching.",
            commandOrUrl = EDEN_WEB_URL,
            buttonText = "Launch on Windows",
            onAction = {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(EDEN_WEB_URL))
                    context.startActivity(intent)
                } catch (_: Exception) {}
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        MethodCard(
            stepNumber = "2",
            title = "Windows Subsystem for Android (WSA) / Sideload",
            badge = "NATIVE APK",
            summary = "Windows 11 natively executes this APK package using the Windows Subsystem for Android virtualization layer. The app manifest includes full support for keyboard, mouse, and freeform resizing.",
            commandOrUrl = "wsaclient.exe /install com.aistudio.eden.envintel.apk",
            buttonText = "Copy WSA Command",
            onAction = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("WSA Command", "wsaclient.exe /install com.aistudio.eden.envintel.apk"))
                Toast.makeText(context, "WSA command copied!", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        MethodCard(
            stepNumber = "3",
            title = "Google Play Games for PC / Bluestacks / LDPlayer",
            badge = "EMULATOR & GAMING RIGS",
            summary = "Compatible with Google Play Games for PC, LDPlayer, and BlueStacks 5. Supports DirectX and Vulkan graphics pipelines with zero lag.",
            commandOrUrl = "Resolution: Up to 4K • 60+ FPS • Full Keyboard Controls",
            buttonText = "Configured & Ready",
            onAction = {}
        )
    }
}

@Composable
private fun LinuxPlatformSection(context: Context) {
    Column {
        PlatformBanner(
            title = "Linux Distribution Support",
            badge = "Waydroid • Anbox • Android-x86 • ChromeOS",
            icon = Icons.Default.Terminal,
            description = "Complete compatibility with modern Linux distributions (Ubuntu, Fedora, Arch Linux, Debian, Pop!_OS, and ChromeOS)."
        )

        Spacer(modifier = Modifier.height(14.dp))

        MethodCard(
            stepNumber = "1",
            title = "Waydroid Native Container Execution",
            badge = "FASTEST ON LINUX",
            summary = "Waydroid uses Linux LXC containers and Wayland direct rendering for near-zero overhead performance. Install the APK into your Waydroid session directly via terminal.",
            commandOrUrl = "waydroid app install com.aistudio.eden.envintel.apk",
            buttonText = "Copy Waydroid Command",
            onAction = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Waydroid Command", "waydroid app install com.aistudio.eden.envintel.apk"))
                Toast.makeText(context, "Waydroid command copied!", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        MethodCard(
            stepNumber = "2",
            title = "Linux Desktop PWA (Firefox / Chromium)",
            badge = "LIGHTWEIGHT",
            summary = "Run the desktop optimized environmental intelligence suite in native Linux browser environments with hardware accelerated WebGL and local storage.",
            commandOrUrl = EDEN_WEB_URL,
            buttonText = "Open in Linux Browser",
            onAction = {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(EDEN_WEB_URL))
                    context.startActivity(intent)
                } catch (_: Exception) {}
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        MethodCard(
            stepNumber = "3",
            title = "Android-x86 / Bliss OS / ChromeOS Linux",
            badge = "CONTAINER & BARE-METAL",
            summary = "Full touchless mouse and keyboard features are declared in the manifest (<uses-feature android:name='android.hardware.touchscreen' android:required='false'/>). Runs without touchscreens.",
            commandOrUrl = "Touchscreen: Optional • Mouse: Active • Freeform: Enabled",
            buttonText = "Verified Compatible",
            onAction = {}
        )
    }
}

@Composable
private fun DesktopDexSection(context: Context) {
    Column {
        PlatformBanner(
            title = "Desktop Mode & Samsung DeX",
            badge = "Adaptive Rail • Multi-Window • Resizable",
            icon = Icons.Default.Devices,
            description = "Connect your phone or tablet to any external monitor or TV via HDMI/USB-C or wireless DeX to experience a full workstation desktop UI."
        )

        Spacer(modifier = Modifier.height(14.dp))

        FeatureHighlightCard(
            title = "Adaptive Navigation Rail & Persistent Sidebar",
            description = "When running on wide screens (>= 720dp), EDEN automatically shifts into Desktop Mode with a vertical navigation rail on the left, giving full horizontal breathing space to data tables and calculators."
        )

        Spacer(modifier = Modifier.height(10.dp))

        FeatureHighlightCard(
            title = "Freeform Window Resizing & Snap",
            description = "Declared with android:resizeableActivity='true' and android.allow_multiple_resumed_activities=true. You can float EDEN side-by-side with Excel, GIS, or laboratory spreadsheets."
        )

        Spacer(modifier = Modifier.height(10.dp))

        FeatureHighlightCard(
            title = "Full Physical Mouse & Scroll Wheel",
            description = "Hover states, mouse wheel table scrolling, right-click context safety, and smooth trackpad panning work seamlessly on Motorola Ready For and Samsung DeX."
        )
    }
}

@Composable
private fun AppleMacSection(context: Context) {
    Column {
        PlatformBanner(
            title = "Apple macOS & iOS Integration",
            badge = "Apple Silicon M1-M4 • Safari PWA • Mac Dock",
            icon = Icons.Default.Laptop,
            description = "Run EDEN seamlessly across MacBook, Mac mini, iMac, Mac Studio, iPad, and iPhone."
        )

        Spacer(modifier = Modifier.height(14.dp))

        MethodCard(
            stepNumber = "1",
            title = "macOS Safari Native Web App (Add to Dock)",
            badge = "ZERO-INSTALL FOR MAC",
            summary = "On macOS Sonoma or Sequoia, open the EDEN Web Platform in Safari and click 'File > Add to Dock'. EDEN runs in its own dedicated Mac window with Command shortcuts and Mac menu bar integration!",
            commandOrUrl = EDEN_WEB_URL,
            buttonText = "Launch on Mac",
            onAction = {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(EDEN_WEB_URL))
                    context.startActivity(intent)
                } catch (_: Exception) {}
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        MethodCard(
            stepNumber = "2",
            title = "Apple Silicon Direct & Android Studio Desktop",
            badge = "M1 / M2 / M3 / M4",
            summary = "Hardware virtualized on Apple ARM architecture with complete GPU acceleration and Retina display crispness.",
            commandOrUrl = "Architecture: ARM64 / Universal • Retina Scaled",
            buttonText = "Native ARM64 Ready",
            onAction = {}
        )

        Spacer(modifier = Modifier.height(10.dp))

        MethodCard(
            stepNumber = "3",
            title = "iOS & iPadOS Add to Home Screen",
            badge = "IPAD & IPHONE",
            summary = "On iPadOS, EDEN adapts to Stage Manager and Split View side-by-side multitasking with Magic Keyboard and trackpad support.",
            commandOrUrl = "Share > Add to Home Screen for instant offline app",
            buttonText = "iPad Stage Manager Ready",
            onAction = {}
        )
    }
}

@Composable
private fun KeyboardShortcutsSection() {
    Column {
        PlatformBanner(
            title = "Desktop Physical Keyboard Shortcuts",
            badge = "Windows • Linux • macOS • DeX",
            icon = Icons.Default.Keyboard,
            description = "Navigate EDEN at lightning speed with these integrated keyboard shortcuts on any desktop or physical keyboard."
        )

        Spacer(modifier = Modifier.height(14.dp))

        val shortcuts = listOf(
            Triple("1", "Home Dashboard", "View live environmental metrics & alerts"),
            Triple("2", "Ask EDEN AI", "Instant question & answer climate engine"),
            Triple("3", "Knowledge Graph", "Standards & scientific taxonomy explorer"),
            Triple("4", "Calculators", "Scope 1-3 & Stack Flow emissions"),
            Triple("5", "Multi-Domain Sensors", "Air, Water, Noise, and Soil telemetry"),
            Triple("6", "Procedures & FDS", "SOPs, test methods & printable PDF forms"),
            Triple("7", "ESG & Solutions Hub", "20 Domains and OTP decarbonization kits"),
            Triple("8", "Profile & Network", "Account, sectors, and EcoPoints"),
            Triple("Ctrl / Cmd + J", "Search Jobs", "Direct link to environmental careers"),
            Triple("Ctrl / Cmd + D", "Desktop Hub", "Open this multi-platform modal"),
            Triple("Ctrl / Cmd + B", "B&W / Color", "Toggle high-contrast monochrome mode"),
            Triple("Ctrl / Cmd + O", "Online / Offline", "Toggle Open AI vs Offline Engine"),
            Triple("Esc", "Close / Back", "Dismiss open dialogs or sidebar drawer")
        )

        shortcuts.forEach { (key, name, desc) ->
            ShortcutRow(key = key, action = name, description = desc)
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun PlatformBanner(
    title: String,
    badge: String,
    icon: ImageVector,
    description: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun MethodCard(
    stepNumber: String,
    title: String,
    badge: String,
    summary: String,
    commandOrUrl: String,
    buttonText: String,
    onAction: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = stepNumber, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF0F6E43).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = badge,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F6E43),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = summary, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = commandOrUrl,
                        fontSize = 10.5.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onAction,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.AutoMirrored.Filled.Launch, contentDescription = null, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = buttonText, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun FeatureHighlightCard(title: String, description: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF0F6E43).copy(alpha = 0.2f),
                modifier = Modifier.size(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF0F6E43), modifier = Modifier.size(14.dp))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun ShortcutRow(key: String, action: String, description: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Text(
                    text = key,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = action, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = description, fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
