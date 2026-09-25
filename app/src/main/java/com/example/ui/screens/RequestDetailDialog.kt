package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
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
fun RequestDetailDialog(
    request: HelpRequestEntity,
    isBengali: Boolean,
    onDismiss: () -> Unit,
    onUpdateStatusAndNotes: (Long, RequestStatus, String, String) -> Unit,
    onDeleteRequest: (HelpRequestEntity) -> Unit
) {
    val context = LocalContext.current
    val categoryEnum = HelpCategory.fromId(request.category)
    val dateFormat = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault())

    var selectedStatus by remember { mutableStateOf(RequestStatus.fromId(request.status)) }
    var adminNoteText by remember { mutableStateOf(request.adminNotes) }
    var aidDetailsText by remember { mutableStateOf(request.aidDisbursedDetails) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = request.trackingCode,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = TealPrimary
                        )
                        Text(
                            text = dateFormat.format(Date(request.submittedAt)),
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Urgent Banner if applicable
                if (request.isUrgent) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEE2E2))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBengali) "জরুরি সাহায্য প্রার্থী (Urgent Case)" else "Emergency Priority Case",
                                color = Color(0xFFB91C1C),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Applicant Information Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = TealPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = request.applicantName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = request.phoneNumber,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            // Call Button
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:${request.phoneNumber}")
                                    }
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isBengali) "কল দিন" else "Call", fontSize = 12.sp)
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = request.locationAddress,
                                fontSize = 13.sp,
                                color = Color(0xFF334155)
                            )
                        }
                    }
                }

                // Category & Description
                Column {
                    Text(
                        text = (if (isBengali) "ক্যাটেগরি: " else "Category: ") + if (isBengali) categoryEnum.bnName else categoryEnum.enName,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = request.description,
                        fontSize = 13.sp,
                        color = Color(0xFF1E293B),
                        lineHeight = 18.sp
                    )

                    if (request.requestedAidEstimate.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = (if (isBengali) "প্রয়োজনীয় অনুদান: " else "Requested Aid: ") + request.requestedAidEstimate,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AmberSecondary
                        )
                    }
                }

                // Document Verification Proof Section
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Badge, contentDescription = null, tint = AmberSecondary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBengali) "পরিচয়পত্র ও ভেরিফিকেশন প্রমাণ" else "Identity & Verification Proof",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Text(
                            text = (if (isBengali) "ডকুমেন্ট: " else "Document Type: ") + request.documentType,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569)
                        )

                        if (!request.documentDescription.isNullOrBlank()) {
                            Text(
                                text = request.documentDescription,
                                fontSize = 12.sp,
                                color = Color(0xFF334155)
                            )
                        }

                        if (!request.documentUri.isNullOrBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                            ) {
                                AsyncImage(
                                    model = request.documentUri,
                                    contentDescription = "Document preview",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        } else {
                            Text(
                                text = if (isBengali) "ডকুমেন্ট নম্বর উল্লিখিত আছে (ফাইল আপলোড করা হয়নি)" else "ID number referenced, photo not uploaded",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                // Action: Change Status
                Column {
                    Text(
                        text = if (isBengali) "আবেদনের অবস্থা পরিবর্তন করুন:" else "Change Request Status:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            RequestStatus.PENDING,
                            RequestStatus.APPROVED,
                            RequestStatus.IN_PROGRESS
                        ).forEach { status ->
                            val isSel = selectedStatus == status
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedStatus = status },
                                label = { Text(if (isBengali) status.bnLabel else status.enLabel, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = status.color,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            RequestStatus.COMPLETED,
                            RequestStatus.DECLINED
                        ).forEach { status ->
                            val isSel = selectedStatus == status
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedStatus = status },
                                label = { Text(if (isBengali) status.bnLabel else status.enLabel, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = status.color,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Admin Note Input
                OutlinedTextField(
                    value = adminNoteText,
                    onValueChange = { adminNoteText = it },
                    label = { Text(if (isBengali) "সমন্বয়কারী রিফাত সরকারের মন্তব্য" else "Coordinator Admin Note") },
                    placeholder = { Text(if (isBengali) "যেমন: ভেরিফিকেশন সম্পন্ন, bKash বা ভলান্টিয়ার পাঠানো হয়েছে" else "e.g., Verified, fund disbursed via bKash") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_note_field"),
                    shape = RoundedCornerShape(10.dp)
                )

                // Aid details (e.g. ৳5,000 sent)
                OutlinedTextField(
                    value = aidDetailsText,
                    onValueChange = { aidDetailsText = it },
                    label = { Text(if (isBengali) "প্রদত্ত সহায়তার বিবরণ (ঐচ্ছিক)" else "Aid Provided Details") },
                    placeholder = { Text(if (isBengali) "যেমন: ৳৫,০০০ টাকা প্রদান করা হয়েছে" else "e.g., ৳5000 sent via bKash") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                // Action buttons: Save & Delete
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onDeleteRequest(request)
                            onDismiss()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                    }

                    Button(
                        onClick = {
                            onUpdateStatusAndNotes(request.id, selectedStatus, adminNoteText, aidDetailsText)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("save_admin_status_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isBengali) "আপডেট সংরক্ষণ করুন" else "Save Changes", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
