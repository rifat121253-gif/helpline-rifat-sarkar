package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [HelpRequestEntity::class, DepositEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun helpRequestDao(): HelpRequestDao
    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "humanitarian_helpline_db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.helpRequestDao(), database.depositDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: HelpRequestDao, depositDao: DepositDao) {
                val now = System.currentTimeMillis()
                val oneDay = 24 * 60 * 60 * 1000L

                val seedRequests = listOf(
                    HelpRequestEntity(
                        trackingCode = "AID-2026-101",
                        applicantName = "মোঃ আবদুর রহিম",
                        phoneNumber = "01712345678",
                        locationAddress = "মিরপুর-১১, ঢাকা (দিনমজুর পরিবার)",
                        category = HelpCategory.FOOD.id,
                        description = "অসুস্থতার কারণে গত ১ মাস রিকশা চালাতে পারিনি। পরিবারে ৩টি শিশু না খেয়ে আছে, ঘরে খাবার চাল-ডাল নেই।",
                        isUrgent = true,
                        documentType = "NID",
                        documentUri = null,
                        documentDescription = "জাতীয় পরিচয়পত্র নম্বর: ৮২২৩৯৪১০৯২",
                        requestedAidEstimate = "১ মাসের খাদ্য সামগ্রী (চাল ২০ কেজি, ডাল ৫ কেজি)",
                        status = RequestStatus.IN_PROGRESS.id,
                        adminNotes = "ভেরিফিকেশন সম্পন্ন। স্থানীয় ভলান্টিয়ারের মাধ্যমে খাদ্য প্যাকেট পাঠানো হচ্ছে।",
                        aidDisbursedDetails = "১টি পূর্ণ খাদ্য প্যাকেট প্রস্তুতকৃত",
                        submittedAt = now - (2 * oneDay),
                        updatedAt = now - (oneDay)
                    ),
                    HelpRequestEntity(
                        trackingCode = "AID-2026-102",
                        applicantName = "ফাতেমা বেগম",
                        phoneNumber = "01898765432",
                        locationAddress = "কুড়িগ্রাম সদর, কুড়িগ্রাম",
                        category = HelpCategory.WINTER_CLOTHES.id,
                        description = "শীতের তীব্রতায় আমাদের জীর্ণ ঘরে বৃদ্ধা শাশুড়ি ও দুই সন্তান কাঁপছে। কোনো গরম কাপড় বা লেপ-কম্বল নেই।",
                        isUrgent = false,
                        documentType = "CHAIRMAN_CERTIFICATE",
                        documentUri = null,
                        documentDescription = "ইউনিয়ন পরিষদ চেয়ারম্যানের প্রত্যয়নপত্র সংযুক্ত",
                        requestedAidEstimate = "৩টি কম্বল ও শিশুদের শীতবস্ত্র",
                        status = RequestStatus.APPROVED.id,
                        adminNotes = "রিফাত সরকার কর্তৃক শীতবস্ত্র বিতরণ তালিকার সিরিয়ালে অন্তর্ভুক্ত করা হয়েছে।",
                        aidDisbursedDetails = "৩টি মানসম্মত গরম কম্বল বরাদ্দকৃত",
                        submittedAt = now - (3 * oneDay),
                        updatedAt = now - (2 * oneDay)
                    ),
                    HelpRequestEntity(
                        trackingCode = "AID-2026-103",
                        applicantName = "তানভীর আহমেদ",
                        phoneNumber = "01911223344",
                        locationAddress = "ঢাকা মেডিকেল কলেজ হাসপাতাল, ওয়ার্ড নং-৮",
                        category = HelpCategory.BLOOD_DONATION.id,
                        description = "আমার চাচির জরুরি জরায়ু অপারেশনের জন্য আজ রাতের মধ্যে ২ ব্যাগ 'O নেগেটিভ' (O-) রক্তের প্রয়োজন।",
                        isUrgent = true,
                        documentType = "PRESCRIPTION",
                        documentUri = null,
                        documentDescription = "ডিএমসিএইচ জরুরি রক্তের রিকুইজিশন স্লিপ",
                        requestedAidEstimate = "২ ব্যাগ O Negative রক্ত",
                        status = RequestStatus.COMPLETED.id,
                        adminNotes = "রিফাত সরকারের স্বেচ্ছাসেবী রক্তদাতা নেটওয়ার্কের মাধ্যমে ২ জন ডোনার পাঠানো হয়েছে এবং রক্তদান সম্পন্ন।",
                        aidDisbursedDetails = "২ ব্যাগ রক্ত প্রদান সম্পন্ন হয়েছে",
                        submittedAt = now - (4 * oneDay),
                        updatedAt = now - (3 * oneDay)
                    ),
                    HelpRequestEntity(
                        trackingCode = "AID-2026-104",
                        applicantName = "সালেহা আক্তার",
                        phoneNumber = "01655443322",
                        locationAddress = "সুনামগঞ্জ হাওর অঞ্চল, শান্তিগঞ্জ",
                        category = HelpCategory.MEDICAL.id,
                        description = "আমার ৭ বছরের সন্তানের শ্বাসকষ্টের ইনহেলার এবং অ্যান্টিবায়োটিক ঔষধ কেনার টাকা নেই।",
                        isUrgent = true,
                        documentType = "PRESCRIPTION",
                        documentUri = null,
                        documentDescription = "উপজেলা স্বাস্থ্য কমপ্লেক্সের ব্যবস্থাপত্র",
                        requestedAidEstimate = "৳২,৫০০ ঔষধ ক্রয়ের জন্য",
                        status = RequestStatus.PENDING.id,
                        adminNotes = "নতুন আবেদন প্রাপ্ত হয়েছে। ডকুমেন্টের সত্যতা যাচাই চলছে।",
                        aidDisbursedDetails = "",
                        submittedAt = now - (4 * 3600 * 1000L),
                        updatedAt = now - (4 * 3600 * 1000L)
                    ),
                    HelpRequestEntity(
                        trackingCode = "AID-2026-105",
                        applicantName = "হাসান মাহমুদ",
                        phoneNumber = "01533221100",
                        locationAddress = "বগুড়া সরকারি কলেজ রোড",
                        category = HelpCategory.EDUCATION.id,
                        description = "এইচএসসি পরীক্ষার টেস্ট পরীক্ষার ফি ও ব্যবহারিক বই কেনার সামর্থ্য নেই। এতিম শিক্ষার্থী।",
                        isUrgent = false,
                        documentType = "STUDENT_ID",
                        documentUri = null,
                        documentDescription = "কলেজ আইডি কার্ড নং-১৮৯৪",
                        requestedAidEstimate = "৳৪,০০০ পরীক্ষার ফি",
                        status = RequestStatus.PENDING.id,
                        adminNotes = "",
                        aidDisbursedDetails = "",
                        submittedAt = now - (2 * 3600 * 1000L),
                        updatedAt = now - (2 * 3600 * 1000L)
                    )
                )
                dao.insertAll(seedRequests)

                // Pre-seed realistic fund deposits
                val seedDeposits = listOf(
                    DepositEntity(
                        depositCode = "DEP-2026-001",
                        donorName = "তৌহিদুর রহমান",
                        phoneNumber = "01711223344",
                        amount = 10000.0,
                        method = "BKASH",
                        trxId = "BK99281726",
                        targetCause = "WINTER_CLOTHES",
                        status = "VERIFIED",
                        isAnonymous = false,
                        receiptNote = "কুড়িগ্রামের শীতবস্ত্র বিতরণ তহবিলে অবদান",
                        timestamp = now - (5 * oneDay)
                    ),
                    DepositEntity(
                        depositCode = "DEP-2026-002",
                        donorName = "শুভাকাঙ্ক্ষী",
                        phoneNumber = "01855667788",
                        amount = 25000.0,
                        method = "BANK",
                        trxId = "EBL78291038",
                        targetCause = "MEDICAL",
                        status = "VERIFIED",
                        isAnonymous = true,
                        receiptNote = "জরুরি চিকিৎসা ও অপারেশন তহবিল",
                        timestamp = now - (3 * oneDay)
                    ),
                    DepositEntity(
                        depositCode = "DEP-2026-003",
                        donorName = "ডাঃ শাহনাজ পারভীন",
                        phoneNumber = "01999887766",
                        amount = 15000.0,
                        method = "NAGAD",
                        trxId = "NG88219401",
                        targetCause = "FOOD",
                        status = "VERIFIED",
                        isAnonymous = false,
                        receiptNote = "অসহায় পরিবারের খাদ্য সাহায্য",
                        timestamp = now - (2 * oneDay)
                    ),
                    DepositEntity(
                        depositCode = "DEP-2026-004",
                        donorName = "এনামুল হক",
                        phoneNumber = "01611002233",
                        amount = 5000.0,
                        method = "BKASH",
                        trxId = "BK66209148",
                        targetCause = "GENERAL",
                        status = "PENDING",
                        isAnonymous = false,
                        receiptNote = "সাধারণ মানবিক ত্রাণ তহবিল",
                        timestamp = now - (4 * 3600 * 1000L)
                    )
                )
                depositDao.insertAll(seedDeposits)
            }
        }
    }
}
