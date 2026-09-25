package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.HelpCategory
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.HopeGreen
import com.example.ui.theme.TealOnPrimaryContainer
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryContainer
import com.example.viewmodel.HelplineViewModel
import com.example.viewmodel.RequestFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestFormScreen(
    formState: RequestFormState,
    isBengali: Boolean,
    viewModel: HelplineViewModel,
    onSuccessNavigateToTrack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    var showSuccessDialog by remember { mutableStateOf(false) }
    var createdTrackingCode by remember { mutableStateOf("") }

    // Android Zero-permission Photo Picker for Document/NID verification
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.updateDocumentUri(it.toString())
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onSuccessNavigateToTrack()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = HopeGreen,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = if (isBengali) "আবেদন সফলভাবে গৃহীত হয়েছে!" else "Request Submitted Successfully!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isBengali)
                            "আপনার আবেদনটি সমন্বয়কারী রিফাত সরকারের পর্যালোচনা তালিকায় যুক্ত হয়েছে। নিচের ট্র্যাকিং নম্বরটি সংরক্ষণ করুন:"
                        else
                            "Your request has been added for review by Coordinator Rifat Sarkar. Please save your tracking code:",
                        fontSize = 14.sp,
                        color = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = TealPrimaryContainer),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = createdTrackingCode,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = TealPrimary
                            )
                            TextButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(createdTrackingCode))
                                }
                            ) {
                                Text(if (isBengali) "কপি করুন" else "Copy")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isBengali)
                            "\"অবস্থা জানুন\" পেজে গিয়ে আপনার ফোন নম্বর বা এই ট্র্যাকিং নম্বর দিয়ে অগ্রগতি জানতে পারবেন।"
                        else
                            "You can check progress in the 'Track' tab anytime using your phone or tracking code.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.setTrackSearchInput(createdTrackingCode)
                        viewModel.searchTrackRequests()
                        onSuccessNavigateToTrack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text(if (isBengali) "অবস্থা দেখুন" else "Track Now")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.resetForm()
                    }
                ) {
                    Text(if (isBengali) "নতুন আবেদন" else "New Request")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Form Title & Subtitle Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(TealPrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = TealPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isBengali) "সাহায্যের আবেদন ফরম" else "Assistance Request Form",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = if (isBengali) "সরাসরি রিফাত সরকার পরিচালিত হেল্পলাইন" else "Direct Helpline operated by Rifat Sarkar",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            }
        }

        // Assistance Category Picker
        item {
            Column {
                Text(
                    text = if (isBengali) "১. সাহায্যের ধরন নির্বাচন করুন *" else "1. Select Category of Help *",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(HelpCategory.entries) { category ->
                        val isSelected = formState.category == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.updateCategory(category) },
                            label = {
                                Text(
                                    text = if (isBengali) category.bnName else category.enName,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TealPrimary,
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            ),
                            modifier = Modifier.testTag("form_category_chip_${category.id}")
                        )
                    }
                }
            }
        }

        // Personal Information Fields
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (isBengali) "২. আবেদনকারীর তথ্য" else "2. Applicant Information",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF0F172A)
                    )

                    // Applicant Name
                    OutlinedTextField(
                        value = formState.applicantName,
                        onValueChange = { viewModel.updateApplicantName(it) },
                        label = { Text(if (isBengali) "পূর্ণ নাম (Applicant Name) *" else "Full Name *") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = TealPrimary)
                        },
                        placeholder = { Text(if (isBengali) "যেমন: মোঃ কামাল হোসেন" else "e.g., Kamal Hossain") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_name_field"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Phone Number
                    OutlinedTextField(
                        value = formState.phoneNumber,
                        onValueChange = { viewModel.updatePhoneNumber(it) },
                        label = { Text(if (isBengali) "মোবাইল নম্বর (Phone Number) *" else "Phone Number *") },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = TealPrimary)
                        },
                        placeholder = { Text("01XXXXXXXXX") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_phone_field"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Location / Address
                    OutlinedTextField(
                        value = formState.locationAddress,
                        onValueChange = { viewModel.updateLocationAddress(it) },
                        label = { Text(if (isBengali) "বর্তমান ঠিকানা ও জেলা (Location) *" else "Address & District *") },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = TealPrimary)
                        },
                        placeholder = { Text(if (isBengali) "যেমন: মিরপুর-১, ঢাকা অথবা থানা ও জেলা" else "Village/Ward, Thana, District") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_address_field"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // Assistance Details Fields
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (isBengali) "৩. প্রয়োজনের বিস্তারিত বিবরণ" else "3. Details of Assistance Needed",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF0F172A)
                    )

                    // Need Description
                    OutlinedTextField(
                        value = formState.description,
                        onValueChange = { viewModel.updateDescription(it) },
                        label = { Text(if (isBengali) "কী ধরণের সাহায্য প্রয়োজন? (বর্ণনা করুন) *" else "Describe your need *") },
                        placeholder = {
                            Text(
                                if (isBengali)
                                    "আপনার বর্তমান পারিবারিক অবস্থা ও কী ধরনের সাহায্য প্রয়োজন বিস্তারিত লিখুন..."
                                else
                                    "Describe your situation and exactly what help is required..."
                            )
                        },
                        minLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_description_field"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Requested Aid Estimate (e.g. ৳5,000 or 2 blankets)
                    OutlinedTextField(
                        value = formState.requestedAidEstimate,
                        onValueChange = { viewModel.updateRequestedAidEstimate(it) },
                        label = { Text(if (isBengali) "প্রয়োজনীয় অনুদান বা পরিমাণের ধারণা (ঐচ্ছিক)" else "Estimated Aid / Quantity (Optional)") },
                        placeholder = { Text(if (isBengali) "যেমন: ৳৩,০০০ টাকা বা ২ ব্যাগ রক্ত বা ১টি কম্বল" else "e.g., ৳3000 or 2 blankets or 1 blood bag") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_aid_estimate_field"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Urgent Flag Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (formState.isUrgent) Color(0xFFFEF2F2) else Color(0xFFF8FAFC),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (formState.isUrgent) Color(0xFFDC2626) else Color(0xFF64748B),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isBengali) "জরুরি সাহায্য প্রয়োজন?" else "Is this an emergency?",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (formState.isUrgent) Color(0xFFDC2626) else Color(0xFF0F172A)
                                )
                                Text(
                                    text = if (isBengali) "চিকিৎসা বা জীবন বাঁচানোর ক্ষেত্রে" else "Critical medical or life-saving need",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                        Switch(
                            checked = formState.isUrgent,
                            onCheckedChange = { viewModel.updateIsUrgent(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFFDC2626)
                            ),
                            modifier = Modifier.testTag("form_urgent_switch")
                        )
                    }
                }
            }
        }

        // Verification System (NID / Document Upload)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AmberSecondary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = AmberSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isBengali) "৪. ভেরিফিকেশন ও পরিচয়পত্র (Verification)" else "4. Verification & Identity Proof",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = if (isBengali) "প্রতারণা রোধে ডকুমেন্ট আপলোড আবশ্যক" else "Prevents fraud and expedites review",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    // Document Type Selector Chips
                    Text(
                        text = if (isBengali) "ডকুমেন্টের ধরন:" else "Document Type:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155)
                    )

                    val docTypes = listOf(
                        "NID" to (if (isBengali) "জাতীয় পরিচয়পত্র (NID)" else "NID Card"),
                        "PRESCRIPTION" to (if (isBengali) "ডাক্তারের প্রেসক্রিপশন" else "Medical Prescription"),
                        "STUDENT_ID" to (if (isBengali) "ছাত্রত্ব সনদ / আইডি" else "Student ID"),
                        "CHAIRMAN_CERTIFICATE" to (if (isBengali) "চেয়ারম্যানের প্রত্যয়ন" else "Chairman Certificate"),
                        "OTHER" to (if (isBengali) "অন্যান্য প্রমাণপত্র" else "Other Proof")
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(docTypes) { (typeKey, typeLabel) ->
                            val isSelected = formState.documentType == typeKey
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.updateDocumentType(typeKey) },
                                label = { Text(typeLabel, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AmberSecondary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    // Document / ID number field
                    OutlinedTextField(
                        value = formState.documentDescription,
                        onValueChange = { viewModel.updateDocumentDescription(it) },
                        label = { Text(if (isBengali) "এনআইডি / কার্ড নম্বর বা ডকুমেন্টের বিবরণ" else "NID / Card # or Document Note") },
                        placeholder = { Text(if (isBengali) "যেমন: NID নং- ৮২২৩৯৪১০৯২" else "e.g., NID: 8223941092") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Photo / Document Upload Box
                    if (formState.documentUri != null) {
                        // Display attached preview
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF1F5F9))
                                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(10.dp))
                        ) {
                            AsyncImage(
                                model = formState.documentUri,
                                contentDescription = "Attached Verification Document",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                            IconButton(
                                onClick = { viewModel.updateDocumentUri(null) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(32.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove Document",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(8.dp)
                                    .background(Color(0xFF16A34A), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isBengali) "ডকুমেন্ট সংযুক্ত হয়েছে ✓" else "Document Attached ✓",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        // Upload Button & Test Sample option
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("upload_document_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0F766E),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBengali) "ছবি / NID যুক্ত করুন" else "Attach NID / Photo",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Quick sample button for effortless testing on emulator
                            OutlinedButton(
                                onClick = {
                                    // Use local vector/drawable reference as sample proof for emulator test
                                    viewModel.updateDocumentUri("sample://nid-verification-demo")
                                    if (formState.documentDescription.isBlank()) {
                                        viewModel.updateDocumentDescription("এনআইডি নং: ৯৫৮২১৩৪২০১ (যাচাইকৃত নমুনা)")
                                    }
                                },
                                modifier = Modifier
                                    .weight(0.9f)
                                    .height(52.dp)
                                    .testTag("sample_document_button"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = if (isBengali) "নমুনা NID টেস্ট" else "Sample NID Test",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Error message banner if any
        if (formState.errorMessage != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = Color(0xFFDC2626)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formState.errorMessage,
                            color = Color(0xFFB91C1C),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Big Accessible Submit Request Button
        item {
            Button(
                onClick = {
                    viewModel.submitRequest { trackingCode ->
                        createdTrackingCode = trackingCode
                        showSuccessDialog = true
                    }
                },
                enabled = !formState.isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .testTag("submit_request_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealPrimary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
            ) {
                if (formState.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isBengali) "আবেদন জমা দিন (Submit Request)" else "Submit Request Now",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
