package com.zeroknowledgeinteractive.codevault.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// These constants are the route names used by Navigation Compose.
const val ADD_FORM = "AddForm"
const val EDIT_FORM = "EditForm"
const val SNIPPET_LIST_SCREEN = "SnippetListScreen"
const val SNIPPET_DETAIL = "SnippetDetail"
const val SETTINGS_SCREEN = "SettingsScreen"

@Composable
fun AppNav(
    innerPadding: PaddingValues,
    themeViewModel: ThemeViewModel
) {

    // rememberNavController keeps the same controller across recompositions of this composable.
    val navController = rememberNavController()
    // LocalContext gives access to the current Android Context inside Compose code.
    val context = LocalContext.current
    val dao = AppDatabase.getDatabase(context).snippetDao()
    val repository = SnippetRepository(dao)
    // viewModel(...) creates or reuses the ViewModel tied to this navigation graph.
    val snippetViewModel: SnippetViewModel = viewModel(
        factory = SnippetViewModelFactory(repository)
    )

    // NavHost is the container that swaps composable screens based on the current route.
    NavHost(
        navController = navController,
        startDestination = SNIPPET_LIST_SCREEN,
        modifier = Modifier.padding(innerPadding)
    ) {

        // Register the add form screen under the ADD_FORM route name.
        composable(ADD_FORM) {
            AddFormScreen(
                navController = navController,
                viewModel = snippetViewModel
            )
        }

        composable(
            route = "$EDIT_FORM/{snippetId}",
            arguments = listOf(
                navArgument("snippetId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val snippetId = backStackEntry.arguments?.getInt("snippetId") ?: return@composable

            AddFormScreen(
                navController = navController,
                viewModel = snippetViewModel,
                snippetId = snippetId
            )
        }

        // Register the list screen as the first screen the app shows.
        composable(SNIPPET_LIST_SCREEN) {
            SnippetListScreen(
                navController = navController,
                viewModel = snippetViewModel
            )
        }
        // Register the detail screen and define that it requires an Int argument called snippetId.
        composable(
            route = "$SNIPPET_DETAIL/{snippetId}",
            arguments = listOf(
                navArgument("snippetId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // Read the route argument that was passed when the user tapped a card.
            val snippetId = backStackEntry.arguments?.getInt("snippetId") ?: return@composable

            SnippetDetail(
                navController = navController,
                viewModel = snippetViewModel,
                snippetId = snippetId
            )
        }

        composable(SETTINGS_SCREEN) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                themeViewModel = themeViewModel
            )
        }

    }
}
