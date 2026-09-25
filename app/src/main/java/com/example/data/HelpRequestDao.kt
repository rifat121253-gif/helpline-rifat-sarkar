package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HelpRequestDao {

    @Query("SELECT * FROM help_requests ORDER BY submittedAt DESC")
    fun getAllRequests(): Flow<List<HelpRequestEntity>>

    @Query("SELECT * FROM help_requests WHERE status = :status ORDER BY submittedAt DESC")
    fun getRequestsByStatus(status: String): Flow<List<HelpRequestEntity>>

    @Query("SELECT * FROM help_requests WHERE id = :id LIMIT 1")
    fun getRequestById(id: Long): Flow<HelpRequestEntity?>

    @Query("SELECT * FROM help_requests WHERE trackingCode = :trackingCode LIMIT 1")
    fun getRequestByTrackingCode(trackingCode: String): Flow<HelpRequestEntity?>

    @Query("""
        SELECT * FROM help_requests 
        WHERE phoneNumber LIKE '%' || :query || '%' 
           OR trackingCode LIKE '%' || :query || '%' 
           OR applicantName LIKE '%' || :query || '%'
           OR locationAddress LIKE '%' || :query || '%'
        ORDER BY submittedAt DESC
    """)
    fun searchRequests(query: String): Flow<List<HelpRequestEntity>>

    @Query("SELECT * FROM help_requests WHERE phoneNumber = :phoneNumber ORDER BY submittedAt DESC")
    fun getRequestsByPhoneNumber(phoneNumber: String): Flow<List<HelpRequestEntity>>

    @Query("SELECT COUNT(*) FROM help_requests")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: HelpRequestEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(requests: List<HelpRequestEntity>)

    @Update
    suspend fun updateRequest(request: HelpRequestEntity)

    @Delete
    suspend fun deleteRequest(request: HelpRequestEntity)

    @Query("UPDATE help_requests SET status = :status, adminNotes = :adminNotes, aidDisbursedDetails = :aidDetails, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatusAndNotes(
        id: Long,
        status: String,
        adminNotes: String,
        aidDetails: String,
        updatedAt: Long = System.currentTimeMillis()
    )
}
