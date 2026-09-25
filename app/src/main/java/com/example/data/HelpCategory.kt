package com.example.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.vector.ImageVector

enum class HelpCategory(
    val id: String,
    val bnName: String,
    val enName: String,
    val bnSubtitle: String,
    val enSubtitle: String,
    val icon: ImageVector,
    val defaultAidUnit: String
) {
    FINANCIAL(
        id = "FINANCIAL",
        bnName = "আর্থিক সহায়তা",
        enName = "Financial Aid",
        bnSubtitle = "জরুরি অর্থ সহায়তা ও ধার দেনা পরিশোধ",
        enSubtitle = "Emergency funds & debt relief support",
        icon = Icons.Default.Payments,
        defaultAidUnit = "টাকা (BDT)"
    ),
    MEDICAL(
        id = "MEDICAL",
        bnName = "চিকিৎসা ও ঔষধ",
        enName = "Medical & Healthcare",
        bnSubtitle = "অপারেশন, টেস্ট ও জরুরি ঔষধের জন্য সাহায্য",
        enSubtitle = "Surgeries, medical tests & vital medicines",
        icon = Icons.Default.LocalHospital,
        defaultAidUnit = "চিকিৎসা খরচ"
    ),
    FOOD(
        id = "FOOD",
        bnName = "খাদ্য সামগ্রী",
        enName = "Food & Groceries",
        bnSubtitle = "অসহায় পরিবারের জন্য চাল, ডাল ও নিত্যপণ্য",
        enSubtitle = "Monthly groceries & essential staples",
        icon = Icons.Default.LocalDining,
        defaultAidUnit = "প্যাকেট / মাস"
    ),
    EDUCATION(
        id = "EDUCATION",
        bnName = "শিক্ষা সহায়তা",
        enName = "Education Support",
        bnSubtitle = "দরিদ্র শিক্ষার্থীদের বই-খাতা, বেতন ও ফরম পূরণ",
        enSubtitle = "School tuition, books & exam fees",
        icon = Icons.Default.School,
        defaultAidUnit = "শিক্ষা অনুদান"
    ),
    WINTER_CLOTHES(
        id = "WINTER_CLOTHES",
        bnName = "শীতবস্ত্র ও পোশাক",
        enName = "Winter Clothes",
        bnSubtitle = "কম্বল, জ্যাকেট ও শিশুদের গরম কাপড়",
        enSubtitle = "Warm blankets, jackets & seasonal clothes",
        icon = Icons.Default.Checkroom,
        defaultAidUnit = "কম্বল / পিস"
    ),
    BLOOD_DONATION(
        id = "BLOOD_DONATION",
        bnName = "রক্তদান সহায়তা",
        enName = "Blood Donation",
        bnSubtitle = "জরুরি রক্তের জন্য স্বেচ্ছাসেবী রক্তদাতা অনুসন্ধান",
        enSubtitle = "Emergency blood donor matching & liaison",
        icon = Icons.Default.Bloodtype,
        defaultAidUnit = "ব্যাগ রক্ত"
    ),
    OTHER(
        id = "OTHER",
        bnName = "অন্যান্য সামাজিক সাহায্য",
        enName = "Other Social Relief",
        bnSubtitle = "ঘর মেরামত, প্রাকৃতিক দুর্যোগ বা বিশেষ প্রয়োজন",
        enSubtitle = "Shelter repair, disaster relief & special aid",
        icon = Icons.Default.Handshake,
        defaultAidUnit = "প্রয়োজনীয় সাহায্য"
    );

    companion object {
        fun fromId(id: String): HelpCategory {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: OTHER
        }
    }
}
