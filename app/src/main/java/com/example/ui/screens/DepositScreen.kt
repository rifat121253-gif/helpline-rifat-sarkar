package com.example.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DepositEntity
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.HopeGreen
import com.example.ui.theme.TealOnPrimaryContainer
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryContainer
import com.example.viewmodel.DepositFormState
import com.example.viewmodel.HelplineViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DepositScreen(
    depositFormState: DepositFormState,
    totalFund: Double,
    verifiedDeposits: List<DepositEntity>,
    isBengali: Boolean,
    viewModel: HelplineViewModel,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    var showSuccessDialog by remember { mutableStateOf(false) }
    var createdCode by remember { mutableStateOf("") }
    var copiedNotice by remember { mutableStateOf<String?>(null) }

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val numberFormatter = NumberFormat.getInstance(Locale("bn", "BD"))

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
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
                    text = if (isBengali) "ডিপোজিট সফলভাবে জমা হয়েছে!" else "Deposit Logged Successfully!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isBengali)
                            "আপনার অনুদান/ডিপোজিট নোটিফিকেশনটি সমন্বয়কারী রিফাত সরকারের কাছে পৌঁছেছে। ট্রানজেকশন আইডি যাচাইপূর্বক তহবিলে যুক্ত করা হবে।"
                        else
                            "Your deposit record has been submitted for verification by Coordinator Rifat Sarkar. Thank you for your generous contribution!",
                        fontSize = 13.sp,
                        color = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = TealPrimaryContainer),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = createdCode,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TealPrimary
                            )
                            TextButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(createdCode))
                                }
                            ) {
                                Text(if (isBengali) "কপি" else "Copy")
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.resetDepositForm()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text(if (isBengali) "ঠিক আছে" else "OK")
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
        // Total Fund Balance Transparency Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = TealPrimary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(TealPrimary, Color(0xFF004D40))
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = Color(0xFFFDE047),
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBengali) "মানবিক সহায়তা ত্রাণ তহবিল" else "Humanitarian Relief Fund",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isBengali) "স্বচ্ছ হিসাব" else "Verified",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "৳ ${String.format("%,.0f", totalFund)}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = if (isBengali) "মোট সংগৃহীত ও অনুমোদিত সহায়তা তহবিল" else "Total Collected & Verified Aid Fund",
                            fontSize = 12.sp,
                            color = Color(0xFFE0F2F1)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isBengali)
                                "সমন্বয়ে: রিফাত সরকার • প্রতিটি টাকা অসহায় মানুষের খাদ্য, ঔষধ ও শীতবস্ত্র বিতরণে ব্যয় হয়।"
                            else
                                "Coordinated by Rifat Sarkar • 100% of donations directly benefit the needy.",
                            fontSize = 11.sp,
                            color = Color(0xFFB2DFDB),
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Official Deposit Account Numbers & How to Add Money Card
        item {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBengali)
                                "মোবাইল ব্যাংকিংয়ের মাধ্যমে টাকা যুক্ত / অনুদান দিন"
                            else
                                "How to Add Money / Donate via Mobile Banking",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Text(
                        text = if (isBengali)
                            "আমাদের তহবিলে সহায়তা করতে অনুগ্রহ করে বিকাশ অথবা নগদ-এ Send Money করুন:"
                        else
                            "To add funds or support our cause through the app, please send money using Bkash or Nagad:",
                        fontSize = 12.sp,
                        color = Color(0xFF475569)
                    )

                    if (copiedNotice != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFDCFCE7), RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = copiedNotice ?: "",
                                fontSize = 12.sp,
                                color = Color(0xFF166534),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // bKash Account
                    DepositAccountRow(
                        title = "🔹 Bkash: Personal (Send Money)",
                        number = "01712125357",
                        badgeColor = Color(0xFFE2136E),
                        onCopy = {
                            clipboardManager.setText(AnnotatedString("01712125357"))
                            copiedNotice = if (isBengali) "বিকাশ নম্বর কপি হয়েছে: 01712125357" else "Copied Bkash: 01712125357"
                        }
                    )

                    // Nagad Account
                    DepositAccountRow(
                        title = "🔹 Nagad: Personal (Send Money)",
                        number = "01315901343",
                        badgeColor = Color(0xFFF7941D),
                        onCopy = {
                            clipboardManager.setText(AnnotatedString("01315901343"))
                            copiedNotice = if (isBengali) "নগদ নম্বর কপি হয়েছে: 01315901343" else "Copied Nagad: 01315901343"
                        }
                    )

                    // Next Steps Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF0FDF4))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = if (isBengali) "টাকা পাঠানোর পরবর্তী ধাপসমূহ:" else "Next Steps After Transfer:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF166534)
                            )
                            Text(
                                text = if (isBengali)
                                    "১. আপনার পেমেন্ট কনফার্মেশন মেসেজ থেকে TrxID (Transaction ID) কপি করুন।\n২. ভেরিফিকেশন সম্পন্ন করতে নিচের ফরমে TrxID ও প্রেরক নম্বরটি লিখুন।"
                                else
                                    "1. Copy the TrxID (Transaction ID) from your payment confirmation message.\n2. Enter the TrxID and your Sender Number in the app below to complete verification.",
                                fontSize = 11.sp,
                                color = Color(0xFF15803D),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Deposit / Donation Form Card
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
                        text = if (isBengali) "তহবিলে অনুদান জমা দিন (Deposit Form)" else "Submit Deposit Information",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF0F172A)
                    )

                    // Quick Amount Chips
                    Column {
                        Text(
                            text = if (isBengali) "টাকার পরিমাণ নির্বাচন করুন:" else "Select Amount (BDT):",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val quickAmounts = listOf("200", "500", "1000", "2000", "5000")
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(quickAmounts) { amt ->
                                val isSel = depositFormState.amount == amt
                                FilterChip(
                                    selected = isSel,
                                    onClick = { viewModel.updateDepositAmount(amt) },
                                    label = { Text("৳ $amt", fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TealPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    // Custom Amount Field
                    OutlinedTextField(
                        value = depositFormState.amount,
                        onValueChange = { viewModel.updateDepositAmount(it) },
                        label = { Text(if (isBengali) "টাকার পরিমাণ (BDT) *" else "Amount (BDT) *") },
                        leadingIcon = {
                            Icon(Icons.Default.Paid, contentDescription = null, tint = TealPrimary)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("deposit_amount_field"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Payment Method Chips
                    Column {
                        Text(
                            text = if (isBengali) "ডিপোজিটের মাধ্যম নির্বাচন করুন:" else "Payment Method:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val methods = listOf(
                            "BKASH" to "bKash",
                            "NAGAD" to "Nagad",
                            "ROCKET" to "Rocket",
                            "BANK" to (if (isBengali) "ব্যাংক" else "Bank"),
                            "CASH" to (if (isBengali) "নগদ ক্যাশ" else "Cash")
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(methods) { (methodKey, methodLabel) ->
                                val isSel = depositFormState.method == methodKey
                                FilterChip(
                                    selected = isSel,
                                    onClick = { viewModel.updateDepositMethod(methodKey) },
                                    label = { Text(methodLabel, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = AmberSecondary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    // Target Cause Chips
                    Column {
                        Text(
                            text = if (isBengali) "কোন খাতে ব্যয় করতে চান?" else "Dedicated Purpose:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val causes = listOf(
                            "GENERAL" to (if (isBengali) "সাধারণ তহবিল" else "General Relief"),
                            "FOOD" to (if (isBengali) "খাদ্য সামগ্রী" else "Food"),
                            "WINTER_CLOTHES" to (if (isBengali) "শীতবস্ত্র ও কম্বল" else "Winter Blankets"),
                            "MEDICAL" to (if (isBengali) "জরুরি চিকিৎসা" else "Medical"),
                            "EDUCATION" to (if (isBengali) "শিক্ষা অনুদান" else "Education")
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(causes) { (causeKey, causeLabel) ->
                                val isSel = depositFormState.targetCause == causeKey
                                FilterChip(
                                    selected = isSel,
                                    onClick = { viewModel.updateDepositTargetCause(causeKey) },
                                    label = { Text(causeLabel) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = HopeGreen,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    // Transaction ID (TrxID)
                    OutlinedTextField(
                        value = depositFormState.trxId,
                        onValueChange = { viewModel.updateDepositTrxId(it) },
                        label = { Text(if (isBengali) "ট্রানজেকশন আইডি (TrxID) *" else "Transaction ID (TrxID) *") },
                        placeholder = { Text("e.g., 9K8L2M1N") },
                        leadingIcon = {
                            Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = TealPrimary)
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("deposit_trxid_field"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Anonymous Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.updateDepositIsAnonymous(!depositFormState.isAnonymous) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = depositFormState.isAnonymous,
                            onCheckedChange = { viewModel.updateDepositIsAnonymous(it) },
                            colors = CheckboxDefaults.colors(checkedColor = TealPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBengali) "গোপন দান (তালিকায় নাম গোপন রাখুন)" else "Anonymous donation (Hide my name)",
                            fontSize = 13.sp,
                            color = Color(0xFF334155)
                        )
                    }

                    // Donor Name (disabled if anonymous)
                    if (!depositFormState.isAnonymous) {
                        OutlinedTextField(
                            value = depositFormState.donorName,
                            onValueChange = { viewModel.updateDepositDonorName(it) },
                            label = { Text(if (isBengali) "দাতার নাম (Donor Name) *" else "Donor Full Name *") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = TealPrimary)
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("deposit_name_field"),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    // Phone Number
                    OutlinedTextField(
                        value = depositFormState.phoneNumber,
                        onValueChange = { viewModel.updateDepositPhone(it) },
                        label = { Text(if (isBengali) "মোবাইল নম্বর (Phone Number) *" else "Sender Phone Number *") },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = TealPrimary)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("deposit_phone_field"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Optional Note / Dua
                    OutlinedTextField(
                        value = depositFormState.receiptNote,
                        onValueChange = { viewModel.updateDepositNote(it) },
                        label = { Text(if (isBengali) "দোয়া / মন্তব্য (ঐচ্ছিক)" else "Message / Note (Optional)") },
                        placeholder = { Text(if (isBengali) "যেমন: বাবা-মায়ের জন্য দোয়া চেয়ে..." else "e.g., Praying for wellness...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Error Message Banner
                    if (depositFormState.errorMessage != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFDC2626))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = depositFormState.errorMessage,
                                    color = Color(0xFFB91C1C),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // Submit Deposit Button
                    Button(
                        onClick = {
                            viewModel.submitDeposit { code ->
                                createdCode = code
                                showSuccessDialog = true
                            }
                        },
                        enabled = !depositFormState.isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("submit_deposit_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HopeGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (depositFormState.isSubmitting) {
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
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBengali) "ডিপোজিট জমা নিশ্চিত করুন" else "Confirm Deposit Notification",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Recent Verified Deposits Section
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBengali) "সাম্প্রতিক অনুদানকারীগণ" else "Recent Contributors",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = if (isBengali) "যাচাইকৃত তালিকা" else "Verified List",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                if (verifiedDeposits.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (isBengali) "এখনো কোনো অনুদান তালিকাভুক্ত হয়নি" else "No verified deposits yet",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                } else {
                    verifiedDeposits.forEach { dep ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(TealPrimaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VolunteerActivism,
                                            contentDescription = null,
                                            tint = TealPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = dep.donorName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF0F172A)
                                        )
                                        Text(
                                            text = "${dep.method} • ${dateFormat.format(Date(dep.timestamp))}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "+৳ ${String.format("%,.0f", dep.amount)}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        color = HopeGreen
                                    )
                                    Text(
                                        text = dep.targetCause,
                                        fontSize = 10.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DepositAccountRow(
    title: String,
    number: String,
    badgeColor: Color,
    onCopy: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = badgeColor
            )
            Text(
                text = number,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B)
            )
        }

        IconButton(
            onClick = onCopy,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy number",
                tint = TealPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
