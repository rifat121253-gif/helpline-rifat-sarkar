package com.example.data

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

object FirestoreManager {
    private const val TAG = "FirestoreManager"
    const val COLLECTION_REQUESTS = "humanitarian_requests"
    const val COLLECTION_DEPOSITS = "fund_deposits"

    private var _firestoreClient: FirebaseFirestore? = null

    val client: FirebaseFirestore?
        get() = _firestoreClient

    fun initialize(context: Context) {
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            _firestoreClient = FirebaseFirestore.getInstance()
            Log.i(TAG, "Firebase Firestore client initialized successfully for humanitarian helpline.")
        } catch (e: Exception) {
            Log.w(TAG, "Firestore initialization warning: ${e.message}")
        }
    }

    /**
     * Stores or updates a humanitarian aid request in Cloud Firestore.
     */
    fun saveRequestToCloud(
        request: HelpRequestEntity,
        onSuccess: (() -> Unit)? = null,
        onFailure: ((Exception) -> Unit)? = null
    ) {
        val firestore = _firestoreClient ?: run {
            Log.w(TAG, "Firestore client not initialized or offline.")
            onFailure?.invoke(IllegalStateException("Firestore client not initialized"))
            return
        }

        val requestData = hashMapOf(
            "id" to request.id,
            "trackingCode" to request.trackingCode,
            "applicantName" to request.applicantName,
            "phoneNumber" to request.phoneNumber,
            "locationAddress" to request.locationAddress,
            "category" to request.category,
            "description" to request.description,
            "isUrgent" to request.isUrgent,
            "documentType" to request.documentType,
            "documentDescription" to (request.documentDescription ?: ""),
            "requestedAidEstimate" to request.requestedAidEstimate,
            "status" to request.status,
            "adminNotes" to request.adminNotes,
            "aidDisbursedDetails" to request.aidDisbursedDetails,
            "coordinator" to "Rifat Sarkar",
            "submittedAt" to request.submittedAt,
            "updatedAt" to request.updatedAt
        )

        firestore.collection(COLLECTION_REQUESTS)
            .document(request.trackingCode)
            .set(requestData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Request ${request.trackingCode} successfully synced to Firestore.")
                onSuccess?.invoke()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error syncing request ${request.trackingCode} to Firestore: ${e.message}")
                onFailure?.invoke(e)
            }
    }

    /**
     * Updates status and coordinator remarks in Cloud Firestore.
     */
    fun updateRequestStatusInCloud(
        trackingCode: String,
        status: String,
        adminNotes: String,
        aidDetails: String
    ) {
        val firestore = _firestoreClient ?: return

        val updates = mapOf(
            "status" to status,
            "adminNotes" to adminNotes,
            "aidDisbursedDetails" to aidDetails,
            "updatedAt" to System.currentTimeMillis()
        )

        firestore.collection(COLLECTION_REQUESTS)
            .document(trackingCode)
            .update(updates)
            .addOnSuccessListener {
                Log.d(TAG, "Status updated for $trackingCode in Firestore.")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Could not update status in Firestore: ${e.message}")
            }
    }

    /**
     * Stores or updates a fund deposit / donation in Cloud Firestore.
     */
    fun saveDepositToCloud(
        deposit: DepositEntity,
        onSuccess: (() -> Unit)? = null,
        onFailure: ((Exception) -> Unit)? = null
    ) {
        val firestore = _firestoreClient ?: run {
            onFailure?.invoke(IllegalStateException("Firestore client not initialized"))
            return
        }

        val depositData = hashMapOf(
            "depositCode" to deposit.depositCode,
            "donorName" to if (deposit.isAnonymous) "শুভাকাঙ্ক্ষী (Anonymous)" else deposit.donorName,
            "phoneNumber" to deposit.phoneNumber,
            "amount" to deposit.amount,
            "method" to deposit.method,
            "trxId" to deposit.trxId,
            "targetCause" to deposit.targetCause,
            "status" to deposit.status,
            "isAnonymous" to deposit.isAnonymous,
            "receiptNote" to deposit.receiptNote,
            "coordinator" to "Rifat Sarkar",
            "timestamp" to deposit.timestamp
        )

        firestore.collection(COLLECTION_DEPOSITS)
            .document(deposit.depositCode)
            .set(depositData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Deposit ${deposit.depositCode} synced to Firestore.")
                onSuccess?.invoke()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error syncing deposit to Firestore: ${e.message}")
                onFailure?.invoke(e)
            }
    }

    /**
     * Updates status for a deposit in Cloud Firestore.
     */
    fun updateDepositStatusInCloud(depositCode: String, status: String) {
        val firestore = _firestoreClient ?: return
        firestore.collection(COLLECTION_DEPOSITS)
            .document(depositCode)
            .update("status", status)
            .addOnSuccessListener {
                Log.d(TAG, "Deposit $depositCode status updated in Firestore.")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Failed to update deposit in Firestore: ${e.message}")
            }
    }
}
