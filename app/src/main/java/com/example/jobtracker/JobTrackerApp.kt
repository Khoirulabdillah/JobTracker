package com.example.jobtracker

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jobtracker.data.JobApplication
import com.example.jobtracker.ui.DetailScreen
import com.example.jobtracker.ui.FormScreen
import com.example.jobtracker.ui.JobViewModel
import com.example.jobtracker.ui.MainScreen
import kotlinx.coroutines.launch

@Composable
fun JobTrackerApp(vm: JobViewModel = viewModel()) {
    val nav = rememberNavController()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    NavHost(navController = nav, startDestination = "main") {

        composable("main") {
            MainScreen(
                vm = vm,
                snackbarHostState = snackbar,
                onAdd = { nav.navigate("form/0") },
                onOpen = { id -> nav.navigate("detail/$id") }
            )
        }

        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong("id") ?: 0L
            val job by vm.observe(id).collectAsStateWithLifecycle(initialValue = null)
            DetailScreen(
                job = job,
                snackbarHostState = snackbar,
                onBack = { nav.popBackStack() },
                onEdit = { nav.navigate("form/${it.id}") },
                onDelete = { deleted ->
                    vm.delete(deleted)
                    nav.popBackStack()
                    scope.launch {
                        val result = snackbar.showSnackbar(
                            message = "Lamaran dihapus",
                            actionLabel = "Urungkan",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) vm.restore(deleted)
                    }
                },
                onStatusChange = { j, s -> vm.save(j.copy(status = s)) }
            )
        }

        composable(
            route = "form/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong("id") ?: 0L
            var initial by remember { mutableStateOf<JobApplication?>(null) }
            var loaded by remember { mutableStateOf(id == 0L) }
            LaunchedEffect(id) {
                if (id != 0L) {
                    initial = vm.get(id)
                    loaded = true
                }
            }
            if (loaded) {
                FormScreen(
                    initial = initial,
                    onSave = { job ->
                        vm.save(job)
                        nav.popBackStack()
                        scope.launch { snackbar.showSnackbar("Lamaran disimpan") }
                    },
                    onBack = { nav.popBackStack() }
                )
            }
        }
    }
}
