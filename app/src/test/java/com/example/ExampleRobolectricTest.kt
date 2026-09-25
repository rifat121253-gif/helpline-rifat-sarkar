package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.DepositEntity
import com.example.data.HelpCategory
import com.example.data.RequestStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app_name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Helpline Rifat Sarkar", appName)
  }

  @Test
  fun `verify help categories and status mappings`() {
    val foodCategory = HelpCategory.fromId("FOOD")
    assertEquals(HelpCategory.FOOD, foodCategory)
    assertEquals("খাদ্য সামগ্রী", foodCategory.bnName)

    val pendingStatus = RequestStatus.fromId("PENDING")
    assertEquals(RequestStatus.PENDING, pendingStatus)
    assertEquals("পর্যালোচনাধীন", pendingStatus.bnLabel)
  }

  @Test
  fun `verify deposit entity creation`() {
    val deposit = DepositEntity(
      depositCode = "DEP-2026-101",
      donorName = "রাকিব",
      phoneNumber = "01712345678",
      amount = 5000.0,
      method = "BKASH",
      trxId = "BK99281726",
      targetCause = "WINTER_CLOTHES",
      status = "PENDING"
    )
    assertEquals("DEP-2026-101", deposit.depositCode)
    assertEquals(5000.0, deposit.amount, 0.0)
    assertEquals("BKASH", deposit.method)
  }
}
