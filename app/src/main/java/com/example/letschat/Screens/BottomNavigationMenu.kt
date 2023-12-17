package com.example.letschat.Screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.letschat.DestinationScreen
import com.example.letschat.R
import com.example.letschat.navigateto

enum class BottomNavigationMenu (val icon : Int,val navDestination: DestinationScreen){
    CHATLIST(R.drawable.chat,DestinationScreen.Chatlist),
    STATUSLIST(R.drawable.status,DestinationScreen.Statuslist),
    PROFILE(R.drawable.user,DestinationScreen.Profile)
}


@Composable
fun BottomNavigationMenu(
    selectedItem :BottomNavigationMenu,
    navController : NavController
){

    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(10.dp)
        .background(color = Color.White)) {
for (item in BottomNavigationMenu.values()){
    Image(painter = painterResource(id = item.icon), contentDescription = null,
modifier = Modifier.size(40.dp).padding(3.dp).weight(1f).clickable {
    navigateto(navController,item.navDestination.route)
},
        colorFilter = if (item == selectedItem)
        ColorFilter.tint(color = Color.Black)

else
            ColorFilter.tint(color = Color.Gray)
    )


}


    }
}