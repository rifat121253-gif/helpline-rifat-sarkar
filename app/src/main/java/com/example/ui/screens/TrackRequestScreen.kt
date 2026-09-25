package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HelpCategory
import com.example.data.HelpRequestEntity
import com.example.data.RequestStatus
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.HopeGreen
import com.example.ui.theme.TealOnPrimaryContainer
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TrackRequestScreen(
    searchInput: String,
    onSearchInputChange: (String) -> Unit,
    onSearch: () -> Unit,
    hasSearched: Boolean,
    results: List<HelpRequestEntity>,
    isBengali: Boolean,
    onSelectSample: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (isBengali) "আবেদনের সর্বশেষ অবস্থা জানুন" else "Track Your Request Status",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = if (isBengali)
                            "আপনার মোবাইল নম্বর বা ট্র্যাকিং কোড (যেমন AID-2026-101) লিখে অনুসন্ধান করুন।"
                        else
                            "Enter your phone number or tracking code (e.g. AID-2026-101) to search.",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchInput,
                            onValueChange = onSearchInputChange,
                            placeholder = {
                                Text(if (isBengali) "ফোন বা কোড লিখুন" else "Phone / Tracking Code")
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = TealPrimary)
                            },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("track_search_input_field"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Button(
                            onClick = onSearch,
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("track_search_submit_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(if (isBengali) "খুঁজুন" else "Search", fontWeight = FontWeight.Bold)
                        }
                    }

                    // Sample search suggestions
                    Column {
                        Text(
                            text = if (isBengali) "পরীক্ষামূলক অনুসন্ধান:" else "Quick Test Searches:",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val samples = listOf("01712345678", "AID-2026-101", "AID-2026-102", "01655443322")
                            items(samples) { sample ->
                                SuggestionChip(
                                    onClick = { onSelectSample(sample) },
                                    label = { Text(sample, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Search Results Section
        if (hasSearched && results.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FindInPage,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isBengali) "কোনো আবেদন পাওয়া যায়নি" else "No Requests Found",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF334155)
                        )
                        Text(
                            text = if (isBengali)
                                "অনুগ্রহ করে নম্বর বা ট্র্যাকিং কোডটি আবার মিলিয়ে দেখুন।"
                            else
                                "Please verify the phone number or tracking code.",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        } else if (results.isNotEmpty()) {
            items(results) { request ->
                RequestStatusTrackingCard(
                    request = request,
                    isBengali = isBengali,
                    formattedDate = dateFormat.format(Date(request.submittedAt))
                )
            }
        } else {
            // Placeholder hint before searching
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (isBengali)
                                "আবেদন জমা দেওয়ার পর সমন্বয়কারী রিফাত সরকার প্রতিটি আবেদন ব্যক্তিগতভাবে যাচাই করেন। স্বচ্ছতার সাথে নিয়মিত আপডেট এখানে দেখতে পাবেন।"
                            else
                                "Every application is reviewed personally by Coordinator Rifat Sarkar. Updates and transparency notes appear here in real-time.",
                            fontSize = 12.sp,
                            color = Color(0xFF475569),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RequestStatusTrackingCard(
    request: HelpRequestEntity,
    isBengali: Boolean,
    formattedDate: String
) {
    val statusEnum = RequestStatus.fromId(request.status)
    val categoryEnum = HelpCategory.fromId(request.category)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with Tracking code & Status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = request.trackingCode,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = TealPrimary
                    )
                    Text(
                        text = formattedDate,
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusEnum.containerColor)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = statusEnum.icon,
                            contentDescription = null,
                            tint = statusEnum.color,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isBengali) statusEnum.bnLabel else statusEnum.enLabel,
                            color = statusEnum.color,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Applicant & Category info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (isBengali) "আবেদনকারী:" else "Applicant:",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = request.applicantName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (isBengali) "সাহায্যের ধরন:" else "Category:",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = if (isBengali) categoryEnum.bnName else categoryEnum.enName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TealPrimary
                    )
                }
            }

            // Description
            Text(
                text = request.description,
                fontSize = 13.sp,
                color = Color(0xFF334155),
                lineHeight = 18.sp
            )

            // Progress Timeline
            StatusTimeline(currentStatus = statusEnum, isBengali = isBengali)

            // Admin Review Notes from Rifat Sarkar
            if (request.adminNotes.isNotBlank() || request.aidDisbursedDetails.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = if (isBengali) "সমন্বয়কারী রিফাত সরকারের মন্তব্য:" else "Note from Coordinator Rifat Sarkar:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF92400E)
                        )
                        if (request.adminNotes.isNotBlank()) {
                            Text(
                                text = request.adminNotes,
                                fontSize = 12.sp,
                                color = Color(0xFF78350F)
                            )
                        }
                        if (request.aidDisbursedDetails.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = (if (isBengali) "প্রদত্ত সাহায্য: " else "Aid Provided: ") + request.aidDisbursedDetails,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Color(0xFF166534)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusTimeline(
    currentStatus: RequestStatus,
    isBengali: Boolean
) {
    val steps = listOf(
        RequestStatus.PENDING to (if (isBengali) "জমা দেওয়া" else "Submitted"),
        RequestStatus.APPROVED to (if (isBengali) "অনুমোদিত" else "Approved"),
        RequestStatus.IN_PROGRESS to (if (isBengali) "প্রক্রিয়াধীন" else "In Progress"),
        RequestStatus.COMPLETED to (if (isBengali) "সম্পন্ন" else "Delivered")
    )

    val currentStepIndex = when (currentStatus) {
        RequestStatus.PENDING -> 0
        RequestStatus.APPROVED -> 1
        RequestStatus.IN_PROGRESS -> 2
        RequestStatus.COMPLETED -> 3
        RequestStatus.DECLINED -> 0
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, (status, label) ->
            val isPassed = index <= currentStepIndex && currentStatus != RequestStatus.DECLINED
            val isCurrent = index == currentStepIndex

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            if (isPassed) TealPrimary else Color(0xFFE2E8F0)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPassed) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF94A3B8))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isPassed) TealPrimary else Color(0xFF64748B)
                )
            }
        }
    }
}
