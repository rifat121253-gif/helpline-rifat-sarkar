package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Query("SELECT * FROM fund_deposits ORDER BY timestamp DESC")
    fun getAllDeposits(): Flow<List<DepositEntity>>

    @Query("SELECT * FROM fund_deposits WHERE status = 'VERIFIED' ORDER BY timestamp DESC")
    fun getVerifiedDeposits(): Flow<List<DepositEntity>>

    @Query("SELECT * FROM fund_deposits WHERE status = 'PENDING' ORDER BY timestamp DESC")
    fun getPendingDeposits(): Flow<List<DepositEntity>>

    @Query("SELECT SUM(amount) FROM fund_deposits WHERE status = 'VERIFIED'")
    fun getTotalVerifiedFund(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeposit(deposit: DepositEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(deposits: List<DepositEntity>)

    @Update
    suspend fun updateDeposit(deposit: DepositEntity)

    @Delete
    suspend fun deleteDeposit(deposit: DepositEntity)

    @Query("UPDATE fund_deposits SET status = :newStatus WHERE id = :id")
    suspend fun updateStatus(id: Long, newStatus: String)
}
