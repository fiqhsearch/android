package com.fiqhsearcher.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fiqhsearcher.fiqh.Madhhab
import com.fiqhsearcher.screen.browse.BrowseAllSections
import com.fiqhsearcher.screen.browse.BrowseSection
import com.fiqhsearcher.screen.browse.BrowseTopic
import com.fiqhsearcher.screen.browse.add.NewIssue
import com.fiqhsearcher.screen.browse.add.NewSection
import com.fiqhsearcher.screen.browse.add.NewTopic
import com.fiqhsearcher.screen.home.HomeScreen
import com.fiqhsearcher.screen.home.SearchOption
import com.fiqhsearcher.screen.login.LoginScreen
import com.fiqhsearcher.screen.search.SearchScreen
import com.fiqhsearcher.screen.settings.SettingsScreen

val queryArg = navArgument("query") { type = NavType.StringType }
val madhhabArg = navArgument("madhhab") { type = NavType.StringType }
val sectionId = navArgument("sectionId") { type = NavType.IntType }
val option = navArgument("option") { type = NavType.StringType }
val topicId = navArgument("topicId") { type = NavType.IntType }

@Composable
fun SearcherNavigator() {
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = "home") {
        composable("home") {
            HomeScreen(navigator = controller)
        }
        composable("settings") {
            SettingsScreen(navigator = controller)
        }
        composable("search/{query}/{option}", listOf(queryArg)) {
            val query = it.arguments?.getString("query") ?: return@composable
            val option = it.arguments?.getString("option") ?: return@composable
            SearchScreen(
                navigator = controller,
                query = query,
                searchOption = SearchOption.valueOf(option)
            )
        }
        composable("browseAllSections") {
            LtrProvider {
                BrowseAllSections(navigator = controller)
            }
        }
        composable("browse/{madhhab}/{sectionId}", listOf(madhhabArg, sectionId)) {
            val madhhab = it.arguments?.getString("madhhab") ?: return@composable
            val sectionId = it.arguments?.getInt("sectionId") ?: return@composable
            LtrProvider {
                BrowseSection(
                    madhhab = Madhhab.valueOf(madhhab),
                    id = sectionId,
                    navigator = controller
                )
            }
        }
        composable(
            "topics/{madhhab}/{sectionId}/{topicId}",
            listOf(madhhabArg, sectionId, topicId)
        ) {
            val madhhab = it.arguments?.getString("madhhab") ?: return@composable
            val sectionId = it.arguments?.getInt("sectionId") ?: return@composable
            val topicId = it.arguments?.getInt("topicId") ?: return@composable
            LtrProvider {
                BrowseTopic(Madhhab.valueOf(madhhab), sectionId, topicId, controller)
            }
        }
        composable("newSection") {
            LtrProvider {
                NewSection(controller)
            }
        }
        composable("newTopic/{sectionId}", listOf(sectionId)) {
            val sectionId = it.arguments?.getInt("sectionId") ?: return@composable
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                NewTopic(controller, sectionId)
            }
        }
        composable("newIssue/{sectionId}/{topicId}", listOf(sectionId, topicId)) {
            val sectionId = it.arguments?.getInt("sectionId") ?: return@composable
            val topicId = it.arguments?.getInt("topicId") ?: return@composable
            LtrProvider {
                NewIssue(controller, sectionId, topicId)
            }
        }
        composable("login") {
            LtrProvider {
                LoginScreen(navigator = controller)
            }
        }
    }
}

@Composable
fun LtrProvider(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        content()
    }
}