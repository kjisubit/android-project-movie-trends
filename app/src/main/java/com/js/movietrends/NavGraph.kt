import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.js.movietrends.domain.core.Constants
import com.js.movietrends.presentation.features.intro.IntroScreen
import com.js.movietrends.presentation.features.main.MainScreen


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Constants.INTRO_ROUTE,
    ) {
        composable(Constants.INTRO_ROUTE) {
            IntroScreen(navController = navController)
        }
        composable(Constants.MAIN_ROUTE) {
            MainScreen()
        }
    }
}
