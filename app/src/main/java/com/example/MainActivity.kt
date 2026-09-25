package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.HelplineBottomNav
import com.example.ui.components.HelplineHeader
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.DepositScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.RequestFormScreen
import com.example.ui.screens.TrackRequestScreen
import com.example.ui.theme.HelplineTheme
import com.example.viewmodel.HelplineScreen
import com.example.viewmodel.HelplineViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelplineTheme {
                HelplineApp()
            }
        }
    }
}

@Composable
fun HelplineApp(viewModel: HelplineViewModel = viewModel()) {
    val isBengali by viewModel.isBengali.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val allRequests by viewModel.allRequests.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    // Deposit states
    val depositFormState by viewModel.depositFormState.collectAsStateWithLifecycle()
    val allDeposits by viewModel.allDeposits.collectAsStateWithLifecycle()
    val verifiedDeposits by viewModel.verifiedDeposits.collectAsStateWithLifecycle()
    val totalVerifiedFund by viewModel.totalVerifiedFund.collectAsStateWithLifecycle()

    // Admin states
    val filteredAdminRequests by viewModel.filteredAdminRequests.collectAsStateWithLifecycle()
    val adminStatusFilter by viewModel.adminStatusFilter.collectAsStateWithLifecycle()
    val adminSearchQuery by viewModel.adminSearchQuery.collectAsStateWithLifecycle()
    val selectedRequestForAdmin by viewModel.selectedRequestForAdmin.collectAsStateWithLifecycle()

    // Tracking states
    val trackSearchInput by viewModel.trackSearchInput.collectAsStateWithLifecycle()
    val trackResults by viewModel.trackResults.collectAsStateWithLifecycle()
    val hasSearchedTrack by viewModel.hasSearchedTrack.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HelplineHeader(
                isBengali = isBengali,
                onToggleLanguage = { viewModel.toggleLanguage() }
            )
        },
        bottomBar = {
            HelplineBottomNav(
                currentScreen = currentScreen,
                isBengali = isBengali,
                onNavigate = { screen -> viewModel.navigateTo(screen) }
            )
        }
    ) { innerPadding ->
        when (currentScreen) {
            HelplineScreen.HOME -> {
                HomeScreen(
                    isBengali = isBengali,
                    onNavigate = { screen -> viewModel.navigateTo(screen) },
                    onSelectCategory = { category ->
                        viewModel.openFormWithCategory(category)
                    },
                    totalRequestsCount = allRequests.size,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            HelplineScreen.REQUEST_FORM -> {
                RequestFormScreen(
                    formState = formState,
                    isBengali = isBengali,
                    viewModel = viewModel,
                    onSuccessNavigateToTrack = {
                        viewModel.navigateTo(HelplineScreen.TRACK)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            HelplineScreen.DEPOSIT -> {
                DepositScreen(
                    depositFormState = depositFormState,
                    totalFund = totalVerifiedFund,
                    verifiedDeposits = verifiedDeposits,
                    isBengali = isBengali,
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            HelplineScreen.TRACK -> {
                TrackRequestScreen(
                    searchInput = trackSearchInput,
                    onSearchInputChange = { viewModel.setTrackSearchInput(it) },
                    onSearch = { viewModel.searchTrackRequests() },
                    hasSearched = hasSearchedTrack,
                    results = trackResults,
                    isBengali = isBengali,
                    onSelectSample = { sample ->
                        viewModel.setTrackSearchInput(sample)
                        viewModel.searchTrackRequests()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            HelplineScreen.ADMIN -> {
                AdminDashboardScreen(
                    allRequests = allRequests,
                    filteredRequests = filteredAdminRequests,
                    statusFilter = adminStatusFilter,
                    searchQuery = adminSearchQuery,
                    selectedRequest = selectedRequestForAdmin,
                    allDeposits = allDeposits,
                    totalFund = totalVerifiedFund,
                    isBengali = isBengali,
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
