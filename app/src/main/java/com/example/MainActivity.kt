package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Source
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AirQualityAlertDetailsModal
import com.example.ui.components.AirQualityTopBarAlertIndicator
import com.example.ui.components.AppUpdateDialog
import com.example.ui.components.DesktopNavigationRail
import com.example.ui.components.DesktopPlatformModal
import com.example.ui.components.EdenAboutModal
import com.example.ui.components.EdenSidebarDrawer
import com.example.ui.components.EdenWelcomeCelebrationDialog
import com.example.ui.components.EnvironmentalJobSearchModal
import com.example.ui.components.SignInModal
import com.example.ui.screens.AskEdenScreen
import com.example.ui.screens.AuthOnboardingScreen
import com.example.ui.screens.AiModelsStudioScreen
import com.example.ui.screens.CalculatorsScreen
import com.example.ui.screens.EsgSolutionsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.KnowledgeGraphScreen
import com.example.ui.screens.LearningScreen
import com.example.ui.screens.LiveCarbonTrackerScreen
import com.example.ui.screens.MonitoringProceduresScreen
import com.example.ui.screens.MonitoringScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ResourcesScreen
import com.example.ui.theme.EDENTheme
import com.example.viewmodel.EdenTab
import com.example.viewmodel.EdenViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: EdenViewModel = viewModel()
            val isBlackAndWhite by viewModel.isBlackAndWhiteMode.collectAsState()
            EDENTheme(isBlackAndWhite = isBlackAndWhite) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (isBlackAndWhite) {
                                Modifier.drawWithContent {
                                    val paint = Paint().apply {
                                        asFrameworkPaint().colorFilter = android.graphics.ColorMatrixColorFilter(
                                            android.graphics.ColorMatrix().apply { setSaturation(0f) }
                                        )
                                    }
                                    drawIntoCanvas { canvas ->
                                        canvas.saveLayer(Rect(0f, 0f, size.width, size.height), paint)
                                        drawContent()
                                        canvas.restore()
                                    }
                                }
                            } else Modifier
                        ),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EdenApp(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdenApp(viewModel: EdenViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsState()
    val forceOffline by viewModel.forceOffline.collectAsState()
    val userAuth by viewModel.userAuthProfile.collectAsState()
    val showAboutDialog by viewModel.showAboutDialog.collectAsState()
    val showSignInDialog by viewModel.showSignInDialog.collectAsState()
    val showUpdateDialog by viewModel.showUpdateDialog.collectAsState()
    val appUpdateInfo by viewModel.appUpdateInfo.collectAsState()
    val showAirQualityAlertDialog by viewModel.showAirQualityAlertDialog.collectAsState()
    val selectedProcedureDomain by viewModel.selectedProcedureDomain.collectAsState()
    val showJobSearchDialog by viewModel.showJobSearchDialog.collectAsState()
    val showDesktopPlatformDialog by viewModel.showDesktopPlatformDialog.collectAsState()
    val showWelcomeCelebration by viewModel.showWelcomeCelebration.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    if (showWelcomeCelebration) {
        EdenWelcomeCelebrationDialog(
            userAuth = userAuth,
            onDismiss = { viewModel.dismissWelcomeCelebration() },
            onPlayWelcomeSound = { viewModel.playWelcomeSound() }
        )
    }

    if (!userAuth.isLoggedIn) {
        // Strict gating: user is not introduced to content without sign-up or sign-in + OTP verification
        AuthOnboardingScreen(viewModel = viewModel)
    } else {
        if (showAboutDialog) {
            EdenAboutModal(onDismiss = { viewModel.setAboutDialog(false) })
        }

        if (showSignInDialog) {
            SignInModal(viewModel = viewModel, onDismiss = { viewModel.setSignInDialog(false) })
        }

        if (showUpdateDialog) {
            AppUpdateDialog(viewModel = viewModel, onDismiss = { viewModel.setUpdateDialog(false) })
        }

        if (showAirQualityAlertDialog) {
            AirQualityAlertDetailsModal(
                viewModel = viewModel,
                onDismiss = { viewModel.setAirQualityAlertDialog(false) }
            )
        }

        if (showJobSearchDialog) {
            EnvironmentalJobSearchModal(
                onDismiss = { viewModel.setJobSearchDialog(false) }
            )
        }

        if (showDesktopPlatformDialog) {
            DesktopPlatformModal(
                onDismiss = { viewModel.setDesktopPlatformDialog(false) }
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown) {
                        when (event.key) {
                            Key.One, Key.NumPad1 -> { viewModel.selectTab(EdenTab.HOME); true }
                            Key.Two, Key.NumPad2 -> { viewModel.selectTab(EdenTab.ASK_EDEN); true }
                            Key.Three, Key.NumPad3 -> { viewModel.selectTab(EdenTab.KNOWLEDGE); true }
                            Key.Four, Key.NumPad4 -> { viewModel.selectTab(EdenTab.CALCULATORS); true }
                            Key.Five, Key.NumPad5 -> { viewModel.selectTab(EdenTab.DATA); true }
                            Key.Six, Key.NumPad6 -> { viewModel.selectTab(EdenTab.MONITORING_PROCEDURES); true }
                            Key.Seven, Key.NumPad7 -> { viewModel.selectTab(EdenTab.ESG_SOLUTIONS); true }
                            Key.Eight, Key.NumPad8 -> { viewModel.selectTab(EdenTab.LEARN); true }
                            Key.M -> { viewModel.selectTab(EdenTab.AI_MODELS); true }
                            Key.Escape -> {
                                if (showDesktopPlatformDialog) viewModel.setDesktopPlatformDialog(false)
                                else if (showJobSearchDialog) viewModel.setJobSearchDialog(false)
                                else if (showAboutDialog) viewModel.setAboutDialog(false)
                                else if (showSignInDialog) viewModel.setSignInDialog(false)
                                else if (drawerState.isOpen) coroutineScope.launch { drawerState.close() }
                                true
                            }
                            Key.J -> {
                                if (event.isCtrlPressed || event.isMetaPressed) {
                                    viewModel.setJobSearchDialog(true)
                                    true
                                } else false
                            }
                            Key.D -> {
                                if (event.isCtrlPressed || event.isMetaPressed) {
                                    viewModel.setDesktopPlatformDialog(true)
                                    true
                                } else false
                            }
                            Key.B -> {
                                if (event.isCtrlPressed || event.isMetaPressed) {
                                    viewModel.toggleBlackAndWhiteMode()
                                    true
                                } else false
                            }
                            Key.O -> {
                                if (event.isCtrlPressed || event.isMetaPressed) {
                                    viewModel.toggleForceOffline(!forceOffline)
                                    true
                                } else false
                            }
                            else -> false
                        }
                    } else false
                }
        ) {
            val isDesktopOrWide = maxWidth >= 768.dp
            val isCurrentBnw by viewModel.isBlackAndWhiteMode.collectAsState()

            if (isDesktopOrWide) {
                // =========================================================================
                // DESKTOP & WIDE SCREEN WORKSTATION LAYOUT (Windows, Linux, macOS, DeX)
                // =========================================================================
                Row(modifier = Modifier.fillMaxSize()) {
                    DesktopNavigationRail(
                        currentTab = currentTab,
                        onSelectTab = { tab -> viewModel.selectTab(tab) },
                        userAuth = userAuth,
                        forceOffline = forceOffline,
                        onToggleForceOffline = { viewModel.toggleForceOffline(!forceOffline) },
                        isBlackAndWhite = isCurrentBnw,
                        onToggleBlackAndWhite = { viewModel.toggleBlackAndWhiteMode() },
                        onOpenDesktopHub = { viewModel.setDesktopPlatformDialog(true) },
                        onOpenJobSearch = { viewModel.setJobSearchDialog(true) },
                        onOpenAbout = { viewModel.setAboutDialog(true) },
                        onOpenAccount = { viewModel.setSignInDialog(true) },
                        onPlaySound = { viewModel.playWelcomeSound() }
                    )

                    Scaffold(
                        contentWindowInsets = WindowInsets.safeDrawing,
                        topBar = {
                            EdenTopBar(
                                viewModel = viewModel,
                                isDesktopOrWide = true,
                                onMenuClick = {
                                    coroutineScope.launch {
                                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                    }
                                }
                            )
                        },
                        bottomBar = {},
                        modifier = Modifier.weight(1f)
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            EdenTabContent(currentTab = currentTab, viewModel = viewModel)
                        }
                    }
                }
            } else {
                // =========================================================================
                // COMPACT MOBILE PHONE LAYOUT
                // =========================================================================
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        EdenSidebarDrawer(
                            currentTab = currentTab,
                            selectedProcedureDomain = selectedProcedureDomain,
                            onSelectTab = { tab -> viewModel.selectTab(tab) },
                            onSelectProcedureDomain = { domain -> viewModel.selectProcedureDomain(domain) },
                            onCloseDrawer = {
                                coroutineScope.launch { drawerState.close() }
                            },
                            onOpenJobSearch = {
                                viewModel.setJobSearchDialog(true)
                            },
                            onOpenDesktopHub = {
                                viewModel.setDesktopPlatformDialog(true)
                            },
                            isBlackAndWhite = isCurrentBnw,
                            onToggleBlackAndWhite = { viewModel.toggleBlackAndWhiteMode() },
                            onPlayWelcomeSound = { viewModel.playWelcomeSound() }
                        )
                    }
                ) {
                    Scaffold(
                        contentWindowInsets = WindowInsets.safeDrawing,
                        topBar = {
                            EdenTopBar(
                                viewModel = viewModel,
                                isDesktopOrWide = false,
                                onMenuClick = {
                                    coroutineScope.launch {
                                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 4.dp,
                                modifier = Modifier
                                    .border(
                                        width = 0.5.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                    )
                                    .testTag("bottom_navigation_bar")
                            ) {
                                NavigationBarItem(
                                    selected = currentTab == EdenTab.HOME,
                                    onClick = { viewModel.selectTab(EdenTab.HOME) },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                    label = { Text("Home", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentTab == EdenTab.ASK_EDEN,
                                    onClick = { viewModel.selectTab(EdenTab.ASK_EDEN) },
                                    icon = { Icon(Icons.Default.Psychology, contentDescription = "Ask EDEN") },
                                    label = { Text("Ask EDEN", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentTab == EdenTab.KNOWLEDGE,
                                    onClick = { viewModel.selectTab(EdenTab.KNOWLEDGE) },
                                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Knowledge") },
                                    label = { Text("Knowledge", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentTab == EdenTab.CALCULATORS,
                                    onClick = { viewModel.selectTab(EdenTab.CALCULATORS) },
                                    icon = { Icon(Icons.Default.Calculate, contentDescription = "Calculators") },
                                    label = { Text("Calculators", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentTab == EdenTab.DATA,
                                    onClick = { viewModel.selectTab(EdenTab.DATA) },
                                    icon = { Icon(Icons.Default.Sensors, contentDescription = "Sensors") },
                                    label = { Text("Sensors", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            EdenTabContent(currentTab = currentTab, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EdenTopBar(
    viewModel: EdenViewModel,
    isDesktopOrWide: Boolean,
    onMenuClick: () -> Unit
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val forceOffline by viewModel.forceOffline.collectAsState()
    val userAuth by viewModel.userAuthProfile.collectAsState()
    val isCurrentBnw by viewModel.isBlackAndWhiteMode.collectAsState()
    val appUpdateInfo by viewModel.appUpdateInfo.collectAsState()
    val context = LocalContext.current

    TopAppBar(
        navigationIcon = {
            if (!isDesktopOrWide) {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.testTag("top_bar_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open Monitoring Procedures & Navigation Sidebar",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { viewModel.setAboutDialog(true) }
                    .padding(vertical = 4.dp, horizontal = 2.dp)
                    .testTag("top_left_eden_brand")
            ) {
                // Eco Leaf Emblem
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = "EDEN Emblem",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isDesktopOrWide) "EDEN DESKTOP" else "EDEN",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            letterSpacing = 0.5.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(if (forceOffline) Color(0xFFC25400) else Color(0xFF0F6E43))
                        )
                        if (isDesktopOrWide) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "WIN • LINUX • MAC",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = if (forceOffline) "Offline Engine • Tap for Info" else "Online Open AI • Tap for Info",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (forceOffline) Color(0xFFC25400) else MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        actions = {
            // Real-time Air Quality Threshold Alert Status Indicator
            AirQualityTopBarAlertIndicator(
                viewModel = viewModel,
                modifier = Modifier.padding(end = 2.dp)
            )

            // Multi-Platform & Desktop Hub Direct Button
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { viewModel.setDesktopPlatformDialog(true) }
                    .padding(horizontal = 2.dp)
                    .testTag("top_bar_desktop_hub_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = "Multi-Platform & Desktop Hub",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isDesktopOrWide) "Desktop Hub [Ctrl+D]" else "Desktop Hub",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Search Environmental Jobs Direct Connect Section
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF0F6E43),
                contentColor = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { viewModel.setJobSearchDialog(true) }
                    .padding(horizontal = 2.dp)
                    .testTag("top_bar_search_jobs_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = "Search Environmental Jobs",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isDesktopOrWide) "Jobs [Ctrl+J]" else "Jobs",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Black & White Monochrome vs Full Color Mode Toggle
            IconButton(
                onClick = { viewModel.toggleBlackAndWhiteMode() },
                modifier = Modifier.testTag("top_action_theme_mode")
            ) {
                Icon(
                    imageVector = if (isCurrentBnw) Icons.Default.Palette else Icons.Default.Contrast,
                    contentDescription = if (isCurrentBnw) "Switch to Full Color Mode" else "Switch to Black & White Mode",
                    tint = if (isCurrentBnw) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Online / Offline Mode Toggle Button
            IconButton(
                onClick = { viewModel.toggleForceOffline(!forceOffline) },
                modifier = Modifier.testTag("toggle_online_offline_button")
            ) {
                Icon(
                    imageVector = if (forceOffline) Icons.Default.CloudOff else Icons.Default.CloudDone,
                    contentDescription = if (forceOffline) "Mode: Offline (Tap for Online)" else "Mode: Online (Tap for Offline)",
                    tint = if (forceOffline) Color(0xFFC25400) else Color(0xFF0F6E43)
                )
            }

            // EcoPoints & Account Pill
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { viewModel.setSignInDialog(true) }
                    .padding(horizontal = 3.dp)
                    .testTag("top_bar_account_pill")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFC25400),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userAuth.rewardPoints} pts",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            if (appUpdateInfo.isUpdateAvailable) {
                IconButton(
                    onClick = { viewModel.setUpdateDialog(true) },
                    modifier = Modifier.testTag("top_bar_update_action_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.SystemUpdate,
                        contentDescription = "New Version Available",
                        tint = Color(0xFFE65100)
                    )
                }
            }

            IconButton(
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://eden-environmental-intelligence.onrender.com"))
                        context.startActivity(intent)
                    } catch (_: Exception) {}
                },
                modifier = Modifier.testTag("top_action_live_web")
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInBrowser,
                    contentDescription = "Live Render Web Platform",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (!isDesktopOrWide) {
                IconButton(
                    onClick = { viewModel.selectTab(EdenTab.ESG_SOLUTIONS) },
                    modifier = Modifier.testTag("top_action_esg_solutions")
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "ESG & Solutions",
                        tint = if (currentTab == EdenTab.ESG_SOLUTIONS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { viewModel.selectTab(EdenTab.PROFILE) },
                    modifier = Modifier.testTag("top_action_profile")
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = if (currentTab == EdenTab.PROFILE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.border(
            width = 0.5.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    )
}

@Composable
private fun EdenTabContent(currentTab: EdenTab, viewModel: EdenViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (currentTab) {
            EdenTab.HOME -> HomeScreen(viewModel = viewModel, onNavigate = { viewModel.selectTab(it) })
            EdenTab.AI_MODELS -> AiModelsStudioScreen(viewModel = viewModel, onNavigate = { viewModel.selectTab(it) })
            EdenTab.LIVE_CARBON -> LiveCarbonTrackerScreen(viewModel = viewModel)
            EdenTab.ASK_EDEN -> AskEdenScreen(viewModel = viewModel, onNavigate = { viewModel.selectTab(it) })
            EdenTab.KNOWLEDGE -> KnowledgeGraphScreen(viewModel = viewModel)
            EdenTab.CALCULATORS -> CalculatorsScreen(viewModel = viewModel)
            EdenTab.DATA -> MonitoringScreen(viewModel = viewModel)
            EdenTab.LEARN -> LearningScreen(viewModel = viewModel)
            EdenTab.RESOURCES -> ResourcesScreen(viewModel = viewModel)
            EdenTab.PROFILE -> ProfileScreen(viewModel = viewModel)
            EdenTab.MONITORING_PROCEDURES -> MonitoringProceduresScreen(viewModel = viewModel)
            EdenTab.ESG_SOLUTIONS -> EsgSolutionsScreen(viewModel = viewModel, onNavigate = { viewModel.selectTab(it) })
        }
    }
}

