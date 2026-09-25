package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DepositEntity
import com.example.data.HelpCategory
import com.example.data.HelpRequestEntity
import com.example.data.RequestStatus
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.HopeGreen
import com.example.ui.theme.TealOnPrimaryContainer
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryContainer
import com.example.viewmodel.HelplineViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    allRequests: List<HelpRequestEntity>,
    filteredRequests: List<HelpRequestEntity>,
    statusFilter: String,
    searchQuery: String,
    selectedRequest: HelpRequestEntity?,
    allDeposits: List<DepositEntity>,
    totalFund: Double,
    isBengali: Boolean,
    viewModel: HelplineViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    var selectedAdminTab by remember { mutableIntStateOf(0) } // 0: Aid Requests, 1: Fund Deposits

    // Detail Dialog when a request is clicked
    if (selectedRequest != null) {
        RequestDetailDialog(
            request = selectedRequest,
            isBengali = isBengali,
            onDismiss = { viewModel.selectRequestForAdmin(null) },
            onUpdateStatusAndNotes = { id, status, notes, aidDetails ->
                viewModel.updateRequestStatus(id, status, notes, aidDetails)
            },
            onDeleteRequest = { request ->
                viewModel.deleteRequest(request)
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Admin Banner Card for Rifat Sarkar
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E293B)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD97706)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isBengali) "রিফাত সরকার (সমন্বয়কারী)" else "Rifat Sarkar (Coordinator)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = "Verified Admin",
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = if (isBengali)
                                "মানবিক সহায়তা ও তহবিল ডিপোজিট নিয়ন্ত্রণ প্যানেল"
                            else
                                "Aid Applications & Fund Deposit Console",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        // Section Tabs: Aid Requests vs Fund Deposits
        item {
            val pendingDepositsCount = allDeposits.count { it.status == "PENDING" }
            TabRow(
                selectedTabIndex = selectedAdminTab,
                containerColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedAdminTab]),
                        color = TealPrimary
                    )
                }
            ) {
                Tab(
                    selected = selectedAdminTab == 0,
                    onClick = { selectedAdminTab = 0 },
                    text = {
                        Text(
                            text = if (isBengali) "সাহায্যের আবেদন (${allRequests.size})" else "Aid Requests (${allRequests.size})",
                            fontWeight = if (selectedAdminTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedAdminTab == 0) TealPrimary else Color(0xFF64748B)
                        )
                    }
                )

                Tab(
                    selected = selectedAdminTab == 1,
                    onClick = { selectedAdminTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isBengali) "তহবিল ডিপোজিট" else "Fund Deposits",
                                fontWeight = if (selectedAdminTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedAdminTab == 1) TealPrimary else Color(0xFF64748B)
                            )
                            if (pendingDepositsCount > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color(0xFFDC2626))
                                        .padding(horizontal = 6.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = pendingDepositsCount.toString(),
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }

        if (selectedAdminTab == 0) {
            // --- TAB 0: AID REQUESTS ---

            // Stats Overview Row
            item {
                val pendingCount = allRequests.count { it.status == RequestStatus.PENDING.id }
                val approvedCount = allRequests.count { it.status == RequestStatus.APPROVED.id }
                val inProgressCount = allRequests.count { it.status == RequestStatus.IN_PROGRESS.id }
                val completedCount = allRequests.count { it.status == RequestStatus.COMPLETED.id }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = if (isBengali) "মোট আবেদন" else "Total",
                        count = allRequests.size.toString(),
                        color = Color(0xFF0F172A)
                    )
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = if (isBengali) "পর্যালোচনা" else "Pending",
                        count = pendingCount.toString(),
                        color = Color(0xFFD97706)
                    )
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = if (isBengali) "চলমান" else "In Progress",
                        count = inProgressCount.toString(),
                        color = Color(0xFF7C3AED)
                    )
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = if (isBengali) "সম্পন্ন" else "Completed",
                        count = completedCount.toString(),
                        color = Color(0xFF16A34A)
                    )
                }
            }

            // Search Field
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setAdminSearchQuery(it) },
                    placeholder = {
                        Text(if (isBengali) "নাম, ফোন, ট্র্যাকিং কোড বা ঠিকানা দিয়ে খুঁজুন..." else "Search by name, phone, code or area...")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TealPrimary)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_search_field"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Status Filter Chips
            item {
                val filterOptions = listOf(
                    "ALL" to (if (isBengali) "সকল (${allRequests.size})" else "All (${allRequests.size})"),
                    RequestStatus.PENDING.id to (if (isBengali) "পর্যালোচনাধীন" else "Pending"),
                    RequestStatus.APPROVED.id to (if (isBengali) "অনুমোদিত" else "Approved"),
                    RequestStatus.IN_PROGRESS.id to (if (isBengali) "চলমান" else "In Progress"),
                    RequestStatus.COMPLETED.id to (if (isBengali) "সম্পন্ন" else "Completed"),
                    RequestStatus.DECLINED.id to (if (isBengali) "বাতিল" else "Declined")
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterOptions) { (key, label) ->
                        val isSelected = statusFilter == key
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setAdminStatusFilter(key) },
                            label = { Text(label, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TealPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("admin_filter_$key")
                        )
                    }
                }
            }

            // Requests List Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBengali) "আবেদনের তালিকা (${filteredRequests.size})" else "Applications List (${filteredRequests.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = if (isBengali) "ক্লিক করে অনুমোদন করুন" else "Tap card for full review",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // Request Item Cards
            if (filteredRequests.isEmpty()) {
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
                            Text(
                                text = if (isBengali) "এই ফিল্টারে কোনো আবেদন নেই" else "No applications in this filter",
                                color = Color(0xFF64748B),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            } else {
                items(filteredRequests) { req ->
                    AdminRequestCard(
                        request = req,
                        isBengali = isBengali,
                        formattedDate = dateFormat.format(Date(req.submittedAt)),
                        onClick = { viewModel.selectRequestForAdmin(req) },
                        onQuickApprove = {
                            viewModel.updateRequestStatus(
                                req.id,
                                RequestStatus.APPROVED,
                                adminNote = if (isBengali) "রিফাত সরকার কর্তৃক অনুমোদিত হয়েছে।" else "Approved by Rifat Sarkar.",
                                aidDetails = req.aidDisbursedDetails
                            )
                        },
                        onQuickComplete = {
                            viewModel.updateRequestStatus(
                                req.id,
                                RequestStatus.COMPLETED,
                                adminNote = if (isBengali) "সাহায্য সফলভাবে পৌঁছে দেওয়া হয়েছে।" else "Assistance successfully delivered.",
                                aidDetails = req.aidDisbursedDetails.ifBlank { "সহায়তা প্রদান সম্পন্ন" }
                            )
                        },
                        onQuickDecline = {
                            viewModel.updateRequestStatus(
                                req.id,
                                RequestStatus.DECLINED,
                                adminNote = if (isBengali) "যাচাইকরণে অসঙ্গতি পাওয়া গেছে বা সামর্থ্য অনুযায়ী সহায়তা সম্ভব হয়নি।" else "Could not verify or assistance unavailable.",
                                aidDetails = ""
                            )
                        }
                    )
                }
            }

        } else {
            // --- TAB 1: FUND DEPOSITS MANAGEMENT ---

            // Fund Summary Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isBengali) "মোট অনুমোদিত তহবিল ব্যালেন্স" else "Total Verified Fund Balance",
                                fontSize = 12.sp,
                                color = Color(0xFF065F46)
                            )
                            Text(
                                text = "৳ ${String.format("%,.0f", totalFund)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF047857)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFD1FAE5))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${allDeposits.size} Deposits",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF065F46)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = if (isBengali) "ডিপোজিট ও অনুদান নোটিফিকেশন তালিকা" else "Deposit Verification Queue",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1E293B)
                )
            }

            if (allDeposits.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isBengali) "কোনো ডিপোজিট পাওয়া যায়নি" else "No deposit records found",
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            } else {
                items(allDeposits) { deposit ->
                    AdminDepositCard(
                        deposit = deposit,
                        isBengali = isBengali,
                        onVerify = { viewModel.verifyDeposit(deposit.id, true) },
                        onReject = { viewModel.verifyDeposit(deposit.id, false) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminDepositCard(
    deposit: DepositEntity,
    isBengali: Boolean,
    onVerify: () -> Unit,
    onReject: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val isPending = deposit.status == "PENDING"
    val isVerified = deposit.status == "VERIFIED"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = deposit.depositCode,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TealPrimary
                    )
                    Text(
                        text = dateFormat.format(Date(deposit.timestamp)),
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (deposit.status) {
                                "VERIFIED" -> Color(0xFFDCFCE7)
                                "PENDING" -> Color(0xFFFEF3C7)
                                else -> Color(0xFFFEE2E2)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = when (deposit.status) {
                            "VERIFIED" -> if (isBengali) "অনুমোদিত ও তহবিলে যুক্ত ✓" else "Verified ✓"
                            "PENDING" -> if (isBengali) "যাচাই অপেক্ষমান" else "Pending"
                            else -> if (isBengali) "বাতিল" else "Rejected"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (deposit.status) {
                            "VERIFIED" -> Color(0xFF16A34A)
                            "PENDING" -> Color(0xFFD97706)
                            else -> Color(0xFFDC2626)
                        }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = deposit.donorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "মোবাইল: ${deposit.phoneNumber} • TrxID: ${deposit.trxId}",
                        fontSize = 12.sp,
                        color = Color(0xFF334155),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = "৳ ${String.format("%,.0f", deposit.amount)}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = if (isVerified) HopeGreen else Color(0xFF0F172A)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "মেথড: ${deposit.method}",
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(TealPrimaryContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "খাত: ${deposit.targetCause}",
                        fontSize = 11.sp,
                        color = TealOnPrimaryContainer
                    )
                }
            }

            if (deposit.receiptNote.isNotBlank()) {
                Text(
                    text = "মন্তব্য: ${deposit.receiptNote}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            // Verification Actions for Rifat Sarkar
            if (isPending) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onVerify,
                        colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isBengali) "যাচাই ও তহবিলে যোগ" else "Verify & Add to Fund", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = onReject,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isBengali) "বাতিল" else "Reject", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminStatCard(
    modifier: Modifier = Modifier,
    title: String,
    count: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 10.sp,
                color = Color(0xFF64748B),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun AdminRequestCard(
    request: HelpRequestEntity,
    isBengali: Boolean,
    formattedDate: String,
    onClick: () -> Unit,
    onQuickApprove: () -> Unit,
    onQuickComplete: () -> Unit,
    onQuickDecline: () -> Unit
) {
    val statusEnum = RequestStatus.fromId(request.status)
    val categoryEnum = HelpCategory.fromId(request.category)
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("admin_request_card_${request.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: Code, Urgency, Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = request.trackingCode,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = TealPrimary
                    )
                    if (request.isUrgent) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFFEE2E2))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isBengali) "জরুরি" else "Urgent",
                                color = Color(0xFFDC2626),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusEnum.containerColor)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isBengali) statusEnum.bnLabel else statusEnum.enLabel,
                        color = statusEnum.color,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Applicant Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = request.applicantName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "${request.phoneNumber} • ${request.locationAddress}",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1
                    )
                }

                // Call Icon
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${request.phoneNumber}")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .size(34.dp)
                        .background(Color(0xFFE0F2FE), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call applicant",
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Category & Need summary
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(TealPrimaryContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isBengali) categoryEnum.bnName else categoryEnum.enName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TealOnPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formattedDate,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Text(
                text = request.description,
                fontSize = 12.sp,
                color = Color(0xFF334155),
                maxLines = 2
            )

            // Document info badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "ডকুমেন্ট: ${request.documentType}",
                        fontSize = 10.sp,
                        color = Color(0xFF475569)
                    )
                }

                if (request.adminNotes.isNotBlank()) {
                    Text(
                        text = "মন্তব্য: ${request.adminNotes}",
                        fontSize = 11.sp,
                        color = Color(0xFF92400E),
                        maxLines = 1
                    )
                }
            }

            // Quick Actions Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (request.status != RequestStatus.APPROVED.id && request.status != RequestStatus.COMPLETED.id) {
                    Button(
                        onClick = onQuickApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isBengali) "অনুমোদন" else "Approve", fontSize = 11.sp)
                    }
                }

                if (request.status != RequestStatus.COMPLETED.id) {
                    Button(
                        onClick = onQuickComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                    ) {
                        Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isBengali) "সম্পন্ন" else "Complete", fontSize = 11.sp)
                    }
                }

                if (request.status != RequestStatus.DECLINED.id) {
                    OutlinedButton(
                        onClick = onQuickDecline,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(if (isBengali) "বাতিল" else "Decline", fontSize = 11.sp)
                    }
                }

                OutlinedButton(
                    onClick = onClick,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isBengali) "ডিটেইলস" else "Details", fontSize = 11.sp)
                }
            }
        }
    }
}
