package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "help_requests")
data class HelpRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val trackingCode: String,
    val applicantName: String,
    val phoneNumber: String,
    val locationAddress: String,
    val category: String, // Matches HelpCategory.id
    val description: String,
    val isUrgent: Boolean = false,
    val documentType: String = "NID", // "NID", "PRESCRIPTION", "STUDENT_ID", "CHAIRMAN_CERTIFICATE", "OTHER", "NONE"
    val documentUri: String? = null,
    val documentDescription: String? = null,
    val requestedAidEstimate: String = "",
    val status: String = RequestStatus.PENDING.id,
    val adminNotes: String = "",
    val aidDisbursedDetails: String = "",
    val submittedAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
