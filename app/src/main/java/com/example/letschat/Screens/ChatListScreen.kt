package com.example.letschat.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.navigation.NavController
import com.example.letschat.LCViewModel

@Composable
fun ChatListScreen(navcontroller: NavController, vm:LCViewModel) {
    Column(modifier = Modifier.fillMaxWidth(),Arrangement.Bottom) {

      BottomNavigationMenu(selectedItem = BottomNavigationMenu.CHATLIST, navController = navcontroller)
    }
}