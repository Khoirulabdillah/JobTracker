package com.example.jobtracker.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MainScreen(
    vm: JobViewModel,
    snackbarHostState: SnackbarHostState,
    onAdd: () -> Unit,
    onOpen: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val allJobs by vm.all.collectAsStateWithLifecycle()
    val filteredJobs by vm.filtered.collectAsStateWithLifecycle()
    val query by vm.query.collectAsStateWithLifecycle()
    val filter by vm.filter.collectAsStateWithLifecycle()

    // Tombol back di tab Daftar kembali ke Beranda, bukan langsung keluar aplikasi
    BackHandler(enabled = selectedTab == 1) { selectedTab = 0 }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                shape = RoundedCornerShape(20.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 6.dp
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah lamaran")
            }
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                val itemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = null
                        )
                    },
                    label = { Text("Beranda") },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            if (selectedTab == 1) Icons.Filled.Work else Icons.Outlined.Work,
                            contentDescription = null
                        )
                    },
                    label = { Text("Daftar") },
                    colors = itemColors
                )
            }
        }
    ) { innerPadding ->
        // statusBarsPadding: tanpa top bar, konten tidak boleh tertutup status bar (edge-to-edge)
        val contentModifier = Modifier
            .padding(innerPadding)
            .statusBarsPadding()

        if (selectedTab == 0) {
            DashboardScreen(
                jobs = allJobs,
                onOpen = onOpen,
                onSeeAll = {
                    vm.filter.value = null
                    selectedTab = 1
                },
                onStatusClick = { status ->
                    vm.filter.value = status
                    selectedTab = 1
                },
                onAdd = onAdd,
                modifier = contentModifier
            )
        } else {
            ListScreen(
                allJobs = allJobs,
                jobs = filteredJobs,
                query = query,
                onQueryChange = { vm.query.value = it },
                filter = filter,
                onFilterChange = { vm.filter.value = it },
                onOpen = onOpen,
                modifier = contentModifier
            )
        }
    }
}