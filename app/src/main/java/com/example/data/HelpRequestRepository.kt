package com.example.data

import kotlinx.coroutines.flow.Flow

class HelpRequestRepository(
    private val dao: HelpRequestDao,
    private val depositDao: DepositDao
) {

    // Help Requests
    val allRequests: Flow<List<HelpRequestEntity>> = dao.getAllRequests()

    fun getRequestsByStatus(status: String): Flow<List<HelpRequestEntity>> =
        dao.getRequestsByStatus(status)

    fun getRequestById(id: Long): Flow<HelpRequestEntity?> =
        dao.getRequestById(id)

    fun getRequestByTrackingCode(code: String): Flow<HelpRequestEntity?> =
        dao.getRequestByTrackingCode(code)

    fun searchRequests(query: String): Flow<List<HelpRequestEntity>> =
        dao.searchRequests(query)

    fun getRequestsByPhone(phone: String): Flow<List<HelpRequestEntity>> =
        dao.getRequestsByPhoneNumber(phone)

    suspend fun submitRequest(request: HelpRequestEntity): Long =
        dao.insertRequest(request)

    suspend fun updateRequest(request: HelpRequestEntity) =
        dao.updateRequest(request)

    suspend fun deleteRequest(request: HelpRequestEntity) =
        dao.deleteRequest(request)

    suspend fun updateStatusAndNotes(
        id: Long,
        status: String,
        adminNotes: String,
        aidDetails: String
    ) = dao.updateStatusAndNotes(
        id = id,
        status = status,
        adminNotes = adminNotes,
        aidDetails = aidDetails,
        updatedAt = System.currentTimeMillis()
    )

    // Fund Deposits
    val allDeposits: Flow<List<DepositEntity>> = depositDao.getAllDeposits()
    val verifiedDeposits: Flow<List<DepositEntity>> = depositDao.getVerifiedDeposits()
    val pendingDeposits: Flow<List<DepositEntity>> = depositDao.getPendingDeposits()
    val totalVerifiedFund: Flow<Double?> = depositDao.getTotalVerifiedFund()

    suspend fun submitDeposit(deposit: DepositEntity): Long =
        depositDao.insertDeposit(deposit)

    suspend fun updateDepositStatus(id: Long, status: String) =
        depositDao.updateStatus(id, status)
}
