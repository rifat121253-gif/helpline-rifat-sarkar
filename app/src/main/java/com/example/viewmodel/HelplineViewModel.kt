package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DepositEntity
import com.example.data.FirestoreManager
import com.example.data.HelpCategory
import com.example.data.HelpRequestEntity
import com.example.data.HelpRequestRepository
import com.example.data.RequestStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class HelplineScreen {
    HOME,
    REQUEST_FORM,
    DEPOSIT,
    TRACK,
    ADMIN
}

data class RequestFormState(
    val applicantName: String = "",
    val phoneNumber: String = "",
    val locationAddress: String = "",
    val category: HelpCategory = HelpCategory.FOOD,
    val description: String = "",
    val isUrgent: Boolean = false,
    val documentType: String = "NID", // "NID", "PRESCRIPTION", "STUDENT_ID", "CHAIRMAN_CERTIFICATE", "OTHER"
    val documentUri: String? = null,
    val documentDescription: String = "",
    val requestedAidEstimate: String = "",
    val isSubmitting: Boolean = false,
    val submittedTrackingCode: String? = null,
    val errorMessage: String? = null
)

data class DepositFormState(
    val donorName: String = "",
    val phoneNumber: String = "",
    val amount: String = "1000",
    val method: String = "BKASH", // "BKASH", "NAGAD", "ROCKET", "BANK", "CASH"
    val trxId: String = "",
    val targetCause: String = "GENERAL", // "GENERAL", "FOOD", "WINTER_CLOTHES", "MEDICAL", "EDUCATION"
    val isAnonymous: Boolean = false,
    val receiptNote: String = "",
    val isSubmitting: Boolean = false,
    val submittedDepositCode: String? = null,
    val errorMessage: String? = null
)

class HelplineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HelpRequestRepository

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = HelpRequestRepository(database.helpRequestDao(), database.depositDao())
    }

    // Language state: true for Bengali (বাংলা), false for English
    private val _isBengali = MutableStateFlow(true)
    val isBengali: StateFlow<Boolean> = _isBengali.asStateFlow()

    fun toggleLanguage() {
        _isBengali.value = !_isBengali.value
    }

    fun setLanguage(bengali: Boolean) {
        _isBengali.value = bengali
    }

    // Current screen navigation
    private val _currentScreen = MutableStateFlow(HelplineScreen.HOME)
    val currentScreen: StateFlow<HelplineScreen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: HelplineScreen) {
        _currentScreen.value = screen
    }

    // All requests reactive stream
    val allRequests: StateFlow<List<HelpRequestEntity>> = repository.allRequests
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Admin filter states
    private val _adminStatusFilter = MutableStateFlow("ALL")
    val adminStatusFilter: StateFlow<String> = _adminStatusFilter.asStateFlow()

    private val _adminSearchQuery = MutableStateFlow("")
    val adminSearchQuery: StateFlow<String> = _adminSearchQuery.asStateFlow()

    fun setAdminStatusFilter(status: String) {
        _adminStatusFilter.value = status
    }

    fun setAdminSearchQuery(query: String) {
        _adminSearchQuery.value = query
    }

    // Filtered requests for Admin dashboard
    val filteredAdminRequests: StateFlow<List<HelpRequestEntity>> = combine(
        allRequests,
        _adminStatusFilter,
        _adminSearchQuery
    ) { requests, statusFilter, query ->
        requests.filter { item ->
            val matchesStatus = if (statusFilter == "ALL") true else item.status == statusFilter
            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                item.applicantName.contains(query, ignoreCase = true) ||
                item.phoneNumber.contains(query, ignoreCase = true) ||
                item.trackingCode.contains(query, ignoreCase = true) ||
                item.locationAddress.contains(query, ignoreCase = true) ||
                item.description.contains(query, ignoreCase = true)
            }
            matchesStatus && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Tracking screen state
    private val _trackSearchInput = MutableStateFlow("")
    val trackSearchInput: StateFlow<String> = _trackSearchInput.asStateFlow()

    private val _trackResults = MutableStateFlow<List<HelpRequestEntity>>(emptyList())
    val trackResults: StateFlow<List<HelpRequestEntity>> = _trackResults.asStateFlow()

    private val _hasSearchedTrack = MutableStateFlow(false)
    val hasSearchedTrack: StateFlow<Boolean> = _hasSearchedTrack.asStateFlow()

    fun setTrackSearchInput(input: String) {
        _trackSearchInput.value = input
    }

    fun searchTrackRequests() {
        val query = _trackSearchInput.value.trim()
        if (query.isBlank()) return

        val results = allRequests.value.filter {
            it.trackingCode.equals(query, ignoreCase = true) ||
            it.phoneNumber.contains(query)
        }
        _trackResults.value = results
        _hasSearchedTrack.value = true
    }

    // Form state for Help Request
    private val _formState = MutableStateFlow(RequestFormState())
    val formState: StateFlow<RequestFormState> = _formState.asStateFlow()

    fun openFormWithCategory(category: HelpCategory) {
        _formState.value = RequestFormState(category = category)
        _currentScreen.value = HelplineScreen.REQUEST_FORM
    }

    fun updateApplicantName(name: String) {
        _formState.value = _formState.value.copy(applicantName = name, errorMessage = null)
    }

    fun updatePhoneNumber(phone: String) {
        _formState.value = _formState.value.copy(phoneNumber = phone, errorMessage = null)
    }

    fun updateLocationAddress(address: String) {
        _formState.value = _formState.value.copy(locationAddress = address, errorMessage = null)
    }

    fun updateCategory(category: HelpCategory) {
        _formState.value = _formState.value.copy(category = category)
    }

    fun updateDescription(desc: String) {
        _formState.value = _formState.value.copy(description = desc, errorMessage = null)
    }

    fun updateIsUrgent(urgent: Boolean) {
        _formState.value = _formState.value.copy(isUrgent = urgent)
    }

    fun updateDocumentType(type: String) {
        _formState.value = _formState.value.copy(documentType = type)
    }

    fun updateDocumentUri(uri: String?) {
        _formState.value = _formState.value.copy(documentUri = uri)
    }

    fun updateDocumentDescription(desc: String) {
        _formState.value = _formState.value.copy(documentDescription = desc)
    }

    fun updateRequestedAidEstimate(estimate: String) {
        _formState.value = _formState.value.copy(requestedAidEstimate = estimate)
    }

    fun resetForm() {
        _formState.value = RequestFormState()
    }

    fun submitRequest(onSuccess: (String) -> Unit) {
        val current = _formState.value
        val isBn = _isBengali.value

        if (current.applicantName.trim().length < 3) {
            _formState.value = current.copy(
                errorMessage = if (isBn) "অনুগ্রহ করে আপনার পূর্ণ নাম লিখুন" else "Please enter your full name"
            )
            return
        }

        if (current.phoneNumber.trim().length < 10) {
            _formState.value = current.copy(
                errorMessage = if (isBn) "অনুগ্রহ করে সঠিক মোবাইল নম্বর লিখুন" else "Please enter a valid phone number"
            )
            return
        }

        if (current.locationAddress.trim().length < 5) {
            _formState.value = current.copy(
                errorMessage = if (isBn) "অনুগ্রহ করে আপনার সঠিক ঠিকানা বা জেলা লিখুন" else "Please enter your valid address or location"
            )
            return
        }

        if (current.description.trim().length < 10) {
            _formState.value = current.copy(
                errorMessage = if (isBn) "অনুগ্রহ করে আপনার সাহায্যের বিস্তারিত বিবরণ দিন" else "Please describe your need in a little more detail"
            )
            return
        }

        _formState.value = current.copy(isSubmitting = true, errorMessage = null)

        viewModelScope.launch {
            val randomSuffix = Random.nextInt(100, 999)
            val trackingCode = "AID-2026-$randomSuffix"

            val entity = HelpRequestEntity(
                trackingCode = trackingCode,
                applicantName = current.applicantName.trim(),
                phoneNumber = current.phoneNumber.trim(),
                locationAddress = current.locationAddress.trim(),
                category = current.category.id,
                description = current.description.trim(),
                isUrgent = current.isUrgent,
                documentType = current.documentType,
                documentUri = current.documentUri,
                documentDescription = current.documentDescription.trim(),
                requestedAidEstimate = current.requestedAidEstimate.trim(),
                status = RequestStatus.PENDING.id,
                adminNotes = "",
                aidDisbursedDetails = "",
                submittedAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            repository.submitRequest(entity)
            FirestoreManager.saveRequestToCloud(entity)
            _formState.value = current.copy(
                isSubmitting = false,
                submittedTrackingCode = trackingCode
            )
            onSuccess(trackingCode)
        }
    }

    // Admin detail / actions for requests
    private val _selectedRequestForAdmin = MutableStateFlow<HelpRequestEntity?>(null)
    val selectedRequestForAdmin: StateFlow<HelpRequestEntity?> = _selectedRequestForAdmin.asStateFlow()

    fun selectRequestForAdmin(request: HelpRequestEntity?) {
        _selectedRequestForAdmin.value = request
    }

    fun updateRequestStatus(requestId: Long, newStatus: RequestStatus, adminNote: String, aidDetails: String) {
        val targetRequest = allRequests.value.firstOrNull { it.id == requestId }
        viewModelScope.launch {
            repository.updateStatusAndNotes(
                id = requestId,
                status = newStatus.id,
                adminNotes = adminNote,
                aidDetails = aidDetails
            )
            targetRequest?.let {
                FirestoreManager.updateRequestStatusInCloud(
                    trackingCode = it.trackingCode,
                    status = newStatus.id,
                    adminNotes = adminNote,
                    aidDetails = aidDetails
                )
            }
            if (_selectedRequestForAdmin.value?.id == requestId) {
                _selectedRequestForAdmin.value = _selectedRequestForAdmin.value?.copy(
                    status = newStatus.id,
                    adminNotes = adminNote,
                    aidDisbursedDetails = aidDetails,
                    updatedAt = System.currentTimeMillis()
                )
            }
        }
    }

    fun deleteRequest(request: HelpRequestEntity) {
        viewModelScope.launch {
            repository.deleteRequest(request)
            if (_selectedRequestForAdmin.value?.id == request.id) {
                _selectedRequestForAdmin.value = null
            }
        }
    }

    // --- Fund Deposit & Donation System ---

    val allDeposits: StateFlow<List<DepositEntity>> = repository.allDeposits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val verifiedDeposits: StateFlow<List<DepositEntity>> = repository.verifiedDeposits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingDeposits: StateFlow<List<DepositEntity>> = repository.pendingDeposits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalVerifiedFund: StateFlow<Double> = repository.totalVerifiedFund
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 55000.0)

    private val _depositFormState = MutableStateFlow(DepositFormState())
    val depositFormState: StateFlow<DepositFormState> = _depositFormState.asStateFlow()

    fun updateDepositDonorName(name: String) {
        _depositFormState.value = _depositFormState.value.copy(donorName = name, errorMessage = null)
    }

    fun updateDepositPhone(phone: String) {
        _depositFormState.value = _depositFormState.value.copy(phoneNumber = phone, errorMessage = null)
    }

    fun updateDepositAmount(amount: String) {
        _depositFormState.value = _depositFormState.value.copy(amount = amount, errorMessage = null)
    }

    fun updateDepositMethod(method: String) {
        _depositFormState.value = _depositFormState.value.copy(method = method)
    }

    fun updateDepositTrxId(trxId: String) {
        _depositFormState.value = _depositFormState.value.copy(trxId = trxId, errorMessage = null)
    }

    fun updateDepositTargetCause(cause: String) {
        _depositFormState.value = _depositFormState.value.copy(targetCause = cause)
    }

    fun updateDepositIsAnonymous(anon: Boolean) {
        _depositFormState.value = _depositFormState.value.copy(isAnonymous = anon)
    }

    fun updateDepositNote(note: String) {
        _depositFormState.value = _depositFormState.value.copy(receiptNote = note)
    }

    fun resetDepositForm() {
        _depositFormState.value = DepositFormState()
    }

    fun submitDeposit(onSuccess: (String) -> Unit) {
        val form = _depositFormState.value
        val isBn = _isBengali.value

        val amountVal = form.amount.toDoubleOrNull()
        if (amountVal == null || amountVal <= 0) {
            _depositFormState.value = form.copy(
                errorMessage = if (isBn) "সঠিক টাকার পরিমাণ দিন (যেমন: ৫০০, ১০০০)" else "Please enter a valid deposit amount"
            )
            return
        }

        if (!form.isAnonymous && form.donorName.trim().length < 2) {
            _depositFormState.value = form.copy(
                errorMessage = if (isBn) "দাতার নাম লিখুন অথবা গোপন দান নির্বাচন করুন" else "Please enter your name or choose anonymous"
            )
            return
        }

        if (form.phoneNumber.trim().length < 10) {
            _depositFormState.value = form.copy(
                errorMessage = if (isBn) "সঠিক মোবাইল নম্বর লিখুন" else "Please enter a valid phone number"
            )
            return
        }

        if (form.trxId.trim().length < 4) {
            _depositFormState.value = form.copy(
                errorMessage = if (isBn) "ট্রানজেকশন আইডি (TrxID) লিখুন" else "Please enter your Transaction ID (TrxID)"
            )
            return
        }

        _depositFormState.value = form.copy(isSubmitting = true, errorMessage = null)

        viewModelScope.launch {
            val code = "DEP-2026-${Random.nextInt(100, 999)}"
            val deposit = DepositEntity(
                depositCode = code,
                donorName = if (form.isAnonymous) "শুভাকাঙ্ক্ষী (Anonymous)" else form.donorName.trim(),
                phoneNumber = form.phoneNumber.trim(),
                amount = amountVal,
                method = form.method,
                trxId = form.trxId.trim().uppercase(),
                targetCause = form.targetCause,
                status = "PENDING", // Pending Rifat Sarkar's review
                isAnonymous = form.isAnonymous,
                receiptNote = form.receiptNote.trim(),
                timestamp = System.currentTimeMillis()
            )

            repository.submitDeposit(deposit)
            FirestoreManager.saveDepositToCloud(deposit)

            _depositFormState.value = form.copy(
                isSubmitting = false,
                submittedDepositCode = code
            )
            onSuccess(code)
        }
    }

    fun verifyDeposit(depositId: Long, isApproved: Boolean) {
        val deposit = allDeposits.value.firstOrNull { it.id == depositId } ?: return
        val newStatus = if (isApproved) "VERIFIED" else "REJECTED"
        viewModelScope.launch {
            repository.updateDepositStatus(depositId, newStatus)
            FirestoreManager.updateDepositStatusInCloud(deposit.depositCode, newStatus)
        }
    }
}
