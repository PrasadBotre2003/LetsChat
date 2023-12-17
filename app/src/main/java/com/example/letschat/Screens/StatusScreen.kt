package com.example.letschat.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.letschat.LCViewModel

@Composable
fun StatusScreen(navcontroller: NavController, vm:LCViewModel) {
    Column(modifier = Modifier.fillMaxWidth(), Arrangement.Bottom) {

        BottomNavigationMenu(selectedItem = BottomNavigationMenu.STATUSLIST, navController = navcontroller)
    }
}