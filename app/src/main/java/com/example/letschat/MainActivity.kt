package com.example.letschat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.letschat.Screens.ChatScreen

import com.example.letschat.Screens.LoginScreen
import com.example.letschat.Screens.SignupScreen
import com.example.letschat.ui.theme.LetschatTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var route : String ){
object Signup:DestinationScreen ("signup")
    object Login:DestinationScreen ("login")
    object Profile:DestinationScreen ("profile")
    object Chatlist:DestinationScreen ("chatlist")
    object SingelChat:DestinationScreen("singleChat/{chatId}"){
        fun createRoute(id:String) = "singleChat//$id"
    }

    object Statuslist:DestinationScreen ("statusList")
    object SingelStatus:DestinationScreen("singelStatus/{userID}"){
        fun createRoute(userID:String) = "singelStatus//$userID"
    }

}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LetschatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
            ChatAppnavigation()
                }
            }
        }
    }
}
@Composable
fun ChatAppnavigation() {
    val navController = rememberNavController()
    var vm = hiltViewModel<LCViewModel>()
    NavHost(navController = navController, startDestination = DestinationScreen.Signup.route) {
        composable(DestinationScreen.Signup.route) {
            SignupScreen(navController, vm = vm)
        }

          composable(DestinationScreen.Login.route) {
            LoginScreen(navController,vm)
          }
        composable(DestinationScreen.Chatlist.route) {
            ChatScreen()
        }
        }

    }




