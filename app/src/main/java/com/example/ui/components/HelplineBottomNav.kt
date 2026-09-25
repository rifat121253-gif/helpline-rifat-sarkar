package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TealOnPrimaryContainer
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryContainer
import com.example.viewmodel.HelplineScreen

@Composable
fun HelplineBottomNav(
    currentScreen: HelplineScreen,
    isBengali: Boolean,
    onNavigate: (HelplineScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        // Home
        NavigationBarItem(
            selected = currentScreen == HelplineScreen.HOME,
            onClick = { onNavigate(HelplineScreen.HOME) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = if (isBengali) "মূলপাতা" else "Home",
                    fontSize = 11.sp,
                    fontWeight = if (currentScreen == HelplineScreen.HOME) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TealPrimary,
                selectedTextColor = TealPrimary,
                indicatorColor = TealPrimaryContainer
            ),
            modifier = Modifier.testTag("nav_home_button")
        )

        // Request Form
        NavigationBarItem(
            selected = currentScreen == HelplineScreen.REQUEST_FORM,
            onClick = { onNavigate(HelplineScreen.REQUEST_FORM) },
            icon = {
                Icon(
                    imageVector = Icons.Default.PostAdd,
                    contentDescription = "Apply",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = if (isBengali) "আবেদন" else "Apply",
                    fontSize = 11.sp,
                    fontWeight = if (currentScreen == HelplineScreen.REQUEST_FORM) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TealPrimary,
                selectedTextColor = TealPrimary,
                indicatorColor = TealPrimaryContainer
            ),
            modifier = Modifier.testTag("nav_apply_button")
        )

        // Deposit / Fund
        NavigationBarItem(
            selected = currentScreen == HelplineScreen.DEPOSIT,
            onClick = { onNavigate(HelplineScreen.DEPOSIT) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Paid,
                    contentDescription = "Deposit",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = if (isBengali) "তহবিল জমা" else "Deposit",
                    fontSize = 11.sp,
                    fontWeight = if (currentScreen == HelplineScreen.DEPOSIT) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF16A34A),
                selectedTextColor = Color(0xFF16A34A),
                indicatorColor = Color(0xFFDCFCE7)
            ),
            modifier = Modifier.testTag("nav_deposit_button")
        )

        // Track
        NavigationBarItem(
            selected = currentScreen == HelplineScreen.TRACK,
            onClick = { onNavigate(HelplineScreen.TRACK) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Track",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = if (isBengali) "অবস্থা জানুন" else "Track",
                    fontSize = 11.sp,
                    fontWeight = if (currentScreen == HelplineScreen.TRACK) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TealPrimary,
                selectedTextColor = TealPrimary,
                indicatorColor = TealPrimaryContainer
            ),
            modifier = Modifier.testTag("nav_track_button")
        )

        // Admin
        NavigationBarItem(
            selected = currentScreen == HelplineScreen.ADMIN,
            onClick = { onNavigate(HelplineScreen.ADMIN) },
            icon = {
                Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = "Admin",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = if (isBengali) "রিফাত সরকার" else "Admin",
                    fontSize = 11.sp,
                    fontWeight = if (currentScreen == HelplineScreen.ADMIN) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFD97706),
                selectedTextColor = Color(0xFFD97706),
                indicatorColor = Color(0xFFFEF3C7)
            ),
            modifier = Modifier.testTag("nav_admin_button")
        )
    }
}
