package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fund_deposits")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val depositCode: String,
    val donorName: String,
    val phoneNumber: String,
    val amount: Double,
    val method: String, // "BKASH", "NAGAD", "ROCKET", "BANK", "CASH"
    val trxId: String,
    val targetCause: String = "GENERAL", // "GENERAL", "FOOD", "WINTER_CLOTHES", "MEDICAL", "EDUCATION"
    val status: String = "VERIFIED", // "PENDING", "VERIFIED", "REJECTED"
    val isAnonymous: Boolean = false,
    val receiptNote: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
