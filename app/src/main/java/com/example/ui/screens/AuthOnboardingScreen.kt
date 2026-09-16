package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.EdenViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

// The 5 mandatory institution types required by user specification:
// "after degisnation, college, school, office, ngo, minstry option then fill is necessary"
enum class InstitutionType(val displayName: String, val iconRes: String) {
    COLLEGE("College", "🎓"),
    SCHOOL("School", "🏫"),
    OFFICE("Office", "🏢"),
    NGO("NGO", "🤝"),
    MINISTRY("Ministry", "🏛️")
}

@Composable
fun AuthOnboardingScreen(
    viewModel: EdenViewModel
) {
    val context = LocalContext.current
    val isBlackAndWhite by viewModel.isBlackAndWhiteMode.collectAsState()

    // Auth screen mode: 0 = Sign Up (default for first-time app download), 1 = Sign In
    var selectedAuthMode by remember { mutableIntStateOf(0) }

    // Navigation sub-state: false = Form entry, true = OTP verification step
    var isVerifyingOtp by remember { mutableStateOf(false) }

    // Sign-Up form fields
    var fullName by remember { mutableStateOf("") }
    var designation by remember { mutableStateOf("") }
    var selectedInstitutionType by remember { mutableStateOf(InstitutionType.OFFICE) }
    var institutionName by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }

    // Sign-In form fields
    var signInContact by remember { mutableStateOf("") }

    // Form validation error state
    var validationError by remember { mutableStateOf<String?>(null) }

    // OTP verification states
    var generatedOtpCode by remember { mutableStateOf("482910") }
    var enteredOtpCode by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf<String?>(null) }
    var resendCountdown by remember { mutableIntStateOf(30) }
    var isVerifyingSpinner by remember { mutableStateOf(false) }

    // Common designations for quick selection
    val quickDesignations = listOf(
        "Student",
        "Environmental Officer",
        "Professor",
        "ESG Analyst",
        "HSE Lead",
        "Research Scholar",
        "Consultant",
        "Director"
    )

    // Countdown timer for OTP resend
    LaunchedEffect(isVerifyingOtp, resendCountdown) {
        if (isVerifyingOtp && resendCountdown > 0) {
            delay(1000)
            resendCountdown -= 1
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("auth_onboarding_scaffold"),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top App Brand Bar: Horizontal Line
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = "EDEN Logo",
                    tint = Color(0xFF0F6E43),
                    modifier = Modifier.size(34.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "EDEN",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF0F6E43).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "ENVIRONMENT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F6E43),
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp)
                    )
                }
            }

            Text(
                text = "Planetary Intelligence & Sustainability Workspace",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // SCREEN DISPLAY MODE SECTION:
            // Black & White Whole Screen Interface vs Full Color
            // + Device Welcome Sound Preview
            // ==========================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_screen_display_mode_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isBlackAndWhite) Icons.Default.Contrast else Icons.Default.Palette,
                                contentDescription = null,
                                tint = if (isBlackAndWhite) MaterialTheme.colorScheme.onSurface else Color(0xFF0F6E43),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Screen Display Mode",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isBlackAndWhite) Color(0xFF222222) else Color(0xFF0F6E43)
                        ) {
                            Text(
                                text = if (isBlackAndWhite) "B&W SCREEN" else "COLOR SCREEN",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Black & White Screen Option
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.setBlackAndWhiteMode(true) }
                                .testTag("btn_mode_bnw"),
                            shape = RoundedCornerShape(10.dp),
                            color = if (isBlackAndWhite) Color(0xFF1E1E1E) else MaterialTheme.colorScheme.surface,
                            border = if (isBlackAndWhite) androidx.compose.foundation.BorderStroke(1.5.dp, Color.White) else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Contrast,
                                    contentDescription = null,
                                    tint = if (isBlackAndWhite) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Black & White",
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isBlackAndWhite) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isBlackAndWhite) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Full Color Screen Option
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.setBlackAndWhiteMode(false) }
                                .testTag("btn_mode_color"),
                            shape = RoundedCornerShape(10.dp),
                            color = if (!isBlackAndWhite) Color(0xFF0F6E43) else MaterialTheme.colorScheme.surface,
                            border = if (!isBlackAndWhite) androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF2E7D32)) else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Palette,
                                    contentDescription = null,
                                    tint = if (!isBlackAndWhite) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Full Color",
                                    fontSize = 11.5.sp,
                                    fontWeight = if (!isBlackAndWhite) FontWeight.Bold else FontWeight.Medium,
                                    color = if (!isBlackAndWhite) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Audio Welcome Chime Test Button
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { viewModel.playWelcomeSound() }
                            .testTag("btn_test_welcome_sound"),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tap to listen to Welcome Sound (Plays on Login & Sign-Up)",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ==========================================
            // REWARD POINTS INTRODUCTION SECTION
            // ==========================================
            RewardPointsIntroCard()

            Spacer(modifier = Modifier.height(18.dp))

            if (!isVerifyingOtp) {
                // ==========================================
                // AUTH TAB ROW: Sign Up (default) vs Sign In
                // ==========================================
                TabRow(
                    selectedTabIndex = selectedAuthMode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .testTag("auth_mode_tab_row"),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Tab(
                        selected = selectedAuthMode == 0,
                        onClick = {
                            selectedAuthMode = 0
                            validationError = null
                        },
                        modifier = Modifier.testTag("auth_tab_signup")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Sign Up (New)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Tab(
                        selected = selectedAuthMode == 1,
                        onClick = {
                            selectedAuthMode = 1
                            validationError = null
                        },
                        modifier = Modifier.testTag("auth_tab_signin")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Sign In (Existing)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Validation Error Alert
                AnimatedVisibility(
                    visible = validationError != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                            .testTag("auth_validation_error_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = "Error",
                                tint = Color(0xFFC62828),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = validationError ?: "",
                                color = Color(0xFFC62828),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // ==========================================
                // SIGN UP FORM
                // ==========================================
                if (selectedAuthMode == 0) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signup_form_card"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "Create Your Environmental Account",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Fill in your professional details below to join EDEN and unlock all environmental intelligence features.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // 1. FULL NAME FIELD
                            Column {
                                Text(
                                    text = "Full Name *",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = fullName,
                                    onValueChange = {
                                        fullName = it
                                        validationError = null
                                    },
                                    placeholder = { Text("e.g., Dr. Ashish Mishra") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("auth_name_field"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            // 2. DESIGNATION FIELD
                            Column {
                                Text(
                                    text = "Designation / Role *",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = designation,
                                    onValueChange = {
                                        designation = it
                                        validationError = null
                                    },
                                    placeholder = { Text("e.g., Lead Environmental Engineer") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Work,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("auth_designation_field"),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Quick suggestion chips in a single horizontal line
                                Text(
                                    text = "Suggested roles (tap to select):",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    quickDesignations.forEach { suggestion ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (designation == suggestion) {
                                                Color(0xFF0F6E43).copy(alpha = 0.18f)
                                            } else {
                                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                            },
                                            border = if (designation == suggestion) {
                                                androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0F6E43))
                                            } else null,
                                            modifier = Modifier
                                                .clickable {
                                                    designation = suggestion
                                                    validationError = null
                                                }
                                                .testTag("chip_designation_${suggestion.replace(" ", "_")}")
                                        ) {
                                            Text(
                                                text = suggestion,
                                                fontSize = 11.sp,
                                                fontWeight = if (designation == suggestion) FontWeight.Bold else FontWeight.Normal,
                                                color = if (designation == suggestion) Color(0xFF0F6E43) else MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // 3. AFFILIATION TYPE (College, School, Office, NGO, Ministry)
                            Column {
                                Text(
                                    text = "Affiliation / Sector Type *",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Select your institution category (required)",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState())
                                        .testTag("auth_institution_type_row"),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    InstitutionType.values().forEach { type ->
                                        val isSelected = selectedInstitutionType == type
                                        Surface(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .clickable {
                                                    selectedInstitutionType = type
                                                    validationError = null
                                                }
                                                .testTag("btn_institution_type_${type.name.lowercase()}"),
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected) Color(0xFF0F6E43) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                            border = if (isSelected) {
                                                androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF0F6E43))
                                            } else {
                                                androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                            }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(vertical = 9.dp, horizontal = 14.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = type.iconRes, fontSize = 16.sp)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = type.displayName,
                                                    fontSize = 13.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 4. INSTITUTION / ORGANIZATION NAME ("then fill is necessary")
                            Column {
                                val dynamicLabel = when (selectedInstitutionType) {
                                    InstitutionType.COLLEGE -> "College / University Name *"
                                    InstitutionType.SCHOOL -> "School / Academy Name *"
                                    InstitutionType.OFFICE -> "Office / Corporation / Enterprise Name *"
                                    InstitutionType.NGO -> "NGO / Non-Profit Foundation Name *"
                                    InstitutionType.MINISTRY -> "Ministry / Government Department Name *"
                                }
                                Text(
                                    text = dynamicLabel,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = institutionName,
                                    onValueChange = {
                                        institutionName = it
                                        validationError = null
                                    },
                                    placeholder = {
                                        Text(
                                            when (selectedInstitutionType) {
                                                InstitutionType.COLLEGE -> "e.g., Stanford University / IIT Delhi"
                                                InstitutionType.SCHOOL -> "e.g., St. Xavier's International School"
                                                InstitutionType.OFFICE -> "e.g., CleanEnergy Global Corp / EDEN Labs"
                                                InstitutionType.NGO -> "e.g., World Wildlife & Climate Foundation"
                                                InstitutionType.MINISTRY -> "e.g., Ministry of Environment, Forest & Climate Change"
                                            }
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = when (selectedInstitutionType) {
                                                InstitutionType.COLLEGE, InstitutionType.SCHOOL -> Icons.Default.School
                                                InstitutionType.OFFICE -> Icons.Default.Apartment
                                                InstitutionType.NGO -> Icons.Default.Diversity3
                                                InstitutionType.MINISTRY -> Icons.Default.AccountBalance
                                            },
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("auth_institution_name_field"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            // 5. MOBILE NUMBER OR EMAIL FOR OTP VERIFICATION
                            Column {
                                Text(
                                    text = "Mobile Number or Email for OTP *",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = contactInfo,
                                    onValueChange = {
                                        contactInfo = it
                                        validationError = null
                                    },
                                    placeholder = { Text("+1 (555) 0199 or user@domain.com") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.ContactPhone,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Done
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("auth_contact_field"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // PROCEED TO OTP VERIFICATION BUTTON
                            Button(
                                onClick = {
                                    // Rigorous validation: Name, Designation, Institution Name and Contact are mandatory
                                    when {
                                        fullName.trim().isBlank() -> {
                                            validationError = "Please enter your Full Name. (Mandatory)"
                                        }
                                        designation.trim().isBlank() -> {
                                            validationError = "Please enter your Designation / Role. (Mandatory)"
                                        }
                                        institutionName.trim().isBlank() -> {
                                            validationError = "Please enter your ${selectedInstitutionType.displayName} Name. (Mandatory)"
                                        }
                                        contactInfo.trim().isBlank() -> {
                                            validationError = "Please enter your Mobile Number or Email for OTP verification."
                                        }
                                        else -> {
                                            // Generate random 6-digit OTP code for interactive verification
                                            generatedOtpCode = (100000 + Random.nextInt(900000)).toString()
                                            enteredOtpCode = ""
                                            otpError = null
                                            resendCountdown = 30
                                            isVerifyingOtp = true
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("auth_proceed_otp_btn"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F6E43))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Proceed to OTP Verification (+250 Pts)",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // ==========================================
                    // SIGN IN FORM (FOR EXISTING ACCOUNTS)
                    // ==========================================
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signin_form_card"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "Welcome Back to EDEN",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Enter your registered Mobile Number or Email to sign in and restore your 250+ Eco-Reward Points.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Column {
                                Text(
                                    text = "Registered Mobile Number or Email *",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = signInContact,
                                    onValueChange = {
                                        signInContact = it
                                        validationError = null
                                    },
                                    placeholder = { Text("imashishrawmmishra@gmail.com or +1 555-0199") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.ContactPhone,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("auth_signin_contact_field"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Button(
                                onClick = {
                                    if (signInContact.trim().isBlank()) {
                                        validationError = "Please enter your registered Mobile Number or Email to sign in."
                                    } else {
                                        contactInfo = signInContact.trim()
                                        generatedOtpCode = (100000 + Random.nextInt(900000)).toString()
                                        enteredOtpCode = ""
                                        otpError = null
                                        resendCountdown = 30
                                        isVerifyingOtp = true
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("auth_signin_proceed_btn"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F6E43))
                            ) {
                                Text(
                                    text = "Send Login OTP & Verify",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Don't have an account?",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Sign Up (+250 Pts)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F6E43),
                                    modifier = Modifier
                                        .clickable {
                                            selectedAuthMode = 0
                                            validationError = null
                                        }
                                        .testTag("switch_to_signup_link")
                                )
                            }
                        }
                    }
                }
            } else {
                // ==========================================
                // OTP VERIFICATION SCREEN
                // ==========================================
                OtpVerificationSection(
                    contact = contactInfo,
                    generatedOtp = generatedOtpCode,
                    enteredOtp = enteredOtpCode,
                    onOtpChange = {
                        enteredOtpCode = it
                        otpError = null
                    },
                    otpError = otpError,
                    resendCountdown = resendCountdown,
                    onResendCode = {
                        generatedOtpCode = (100000 + Random.nextInt(900000)).toString()
                        enteredOtpCode = ""
                        otpError = null
                        resendCountdown = 30
                        Toast.makeText(context, "New verification code dispatched!", Toast.LENGTH_SHORT).show()
                    },
                    onAutoFill = {
                        enteredOtpCode = generatedOtpCode
                        otpError = null
                    },
                    onBack = {
                        isVerifyingOtp = false
                        otpError = null
                    },
                    isVerifying = isVerifyingSpinner,
                    onVerifyAndSubmit = {
                        if (enteredOtpCode.trim() != generatedOtpCode) {
                            otpError = "Invalid verification code. Please enter the 6-digit code or tap Auto-Fill."
                        } else {
                            isVerifyingSpinner = true
                            if (selectedAuthMode == 0) {
                                // Register user with name, designation, institutionType, institutionName, and contact
                                viewModel.registerAndVerifyUser(
                                    name = fullName,
                                    designation = designation,
                                    institutionType = selectedInstitutionType.displayName,
                                    institutionName = institutionName,
                                    contact = contactInfo
                                )
                            } else {
                                // Existing user sign in
                                viewModel.signInExistingUser(
                                    contact = contactInfo,
                                    name = if (fullName.isNotBlank()) fullName else "Ashish Mishra",
                                    designation = if (designation.isNotBlank()) designation else "Lead Environmental Engineer",
                                    institutionType = selectedInstitutionType.displayName,
                                    institutionName = if (institutionName.isNotBlank()) institutionName else "EDEN Environmental Network"
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

// ==========================================
// REWARD POINTS INTRODUCTION CARD COMPONENT
// ==========================================
@Composable
private fun RewardPointsIntroCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("reward_points_intro_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA5D6A7))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF0F6E43),
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EDEN Eco-Reward Points Program",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF2E7D32)
                ) {
                    Text(
                        text = "+250 BONUS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = "Welcome Offer: Sign up with your institution affiliation & verify via OTP to receive 250 Eco-Reward Points instantly in your green wallet.",
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = Color(0xFF2E7D32)
            )

            HorizontalDivider(color = Color(0xFFC8E6C9))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RewardBenefitChip(icon = "📜", title = "Statutory EIA Dossiers")
                RewardBenefitChip(icon = "🌱", title = "Green Sentinel Badge")
                RewardBenefitChip(icon = "🔬", title = "Numerical Models")
                RewardBenefitChip(icon = "💎", title = "VIP Community Access")
            }
        }
    }
}

@Composable
private fun RewardBenefitChip(icon: String, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.7f))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(text = icon, fontSize = 11.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1B5E20)
        )
    }
}

// ==========================================
// OTP VERIFICATION SECTION
// ==========================================
@Composable
private fun OtpVerificationSection(
    contact: String,
    generatedOtp: String,
    enteredOtp: String,
    onOtpChange: (String) -> Unit,
    otpError: String?,
    resendCountdown: Int,
    onResendCode: () -> Unit,
    onAutoFill: () -> Unit,
    onBack: () -> Unit,
    isVerifying: Boolean,
    onVerifyAndSubmit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("otp_verification_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header with Back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("otp_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to form",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "OTP Verification",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // High-visibility Security Shield
            Surface(
                shape = CircleShape,
                color = Color(0xFF0F6E43).copy(alpha = 0.12f),
                modifier = Modifier.size(60.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = Color(0xFF0F6E43),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Text(
                text = "Enter 6-Digit Verification Code",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "We have sent a 6-digit secure authentication code to:\n$contact",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // SIMULATED INCOMING SMS NOTIFICATION BANNER
            // Provides direct visibility of the OTP code and a 1-tap "Auto-Fill" button for optimal UX
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF81C784)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("simulated_sms_banner")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔔", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "EDEN Verification Service (SMS/Email)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Your 6-digit one-time code is $generatedOtp. Valid for 10 minutes.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedButton(
                        onClick = onAutoFill,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .testTag("otp_auto_fill_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0F6E43))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Auto-Fill Code: $generatedOtp",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // OTP Input Field
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = enteredOtp,
                    onValueChange = {
                        if (it.length <= 6 && it.all { ch -> ch.isDigit() }) {
                            onOtpChange(it)
                        }
                    },
                    placeholder = { Text("• • • • • •", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 8.sp,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .testTag("auth_otp_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0F6E43),
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                if (otpError != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = otpError,
                        fontSize = 12.sp,
                        color = Color(0xFFC62828),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Resend Code row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (resendCountdown > 0) {
                    Text(
                        text = "Resend code in ${resendCountdown}s",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Didn't receive code? ",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Resend OTP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F6E43),
                        modifier = Modifier
                            .clickable { onResendCode() }
                            .testTag("otp_resend_button")
                    )
                }
            }

            // SUBMIT & VERIFY BUTTON
            Button(
                onClick = onVerifyAndSubmit,
                enabled = !isVerifying && enteredOtp.length >= 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("auth_verify_submit_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F6E43))
            ) {
                if (isVerifying) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Verify OTP & Enter EDEN",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
