package com.example.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Sync
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class RequestStatus(
    val id: String,
    val bnLabel: String,
    val enLabel: String,
    val color: Color,
    val containerColor: Color,
    val icon: ImageVector
) {
    PENDING(
        id = "PENDING",
        bnLabel = "পর্যালোচনাধীন",
        enLabel = "Under Review",
        color = Color(0xFFD97706), // Amber
        containerColor = Color(0xFFFEF3C7),
        icon = Icons.Default.HourglassTop
    ),
    APPROVED(
        id = "APPROVED",
        bnLabel = "অনুমোদিত",
        enLabel = "Approved",
        color = Color(0xFF0284C7), // Sky/Blue
        containerColor = Color(0xFFE0F2FE),
        icon = Icons.Default.PendingActions
    ),
    IN_PROGRESS(
        id = "IN_PROGRESS",
        bnLabel = "প্রক্রিয়াধীন",
        enLabel = "In Progress",
        color = Color(0xFF7C3AED), // Purple
        containerColor = Color(0xFFEDE9FE),
        icon = Icons.Default.Sync
    ),
    COMPLETED(
        id = "COMPLETED",
        bnLabel = "সাহায্য সম্পন্ন",
        enLabel = "Completed",
        color = Color(0xFF16A34A), // Emerald/Green
        containerColor = Color(0xFFDCFCE7),
        icon = Icons.Default.CheckCircle
    ),
    DECLINED(
        id = "DECLINED",
        bnLabel = "অনুরোধ বাতিল",
        enLabel = "Declined",
        color = Color(0xFFDC2626), // Red
        containerColor = Color(0xFFFEE2E2),
        icon = Icons.Default.Cancel
    );

    companion object {
        fun fromId(id: String): RequestStatus {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: PENDING
        }
    }
}
