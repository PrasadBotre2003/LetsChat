package com.example.letschat.Screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.letschat.CommonDivider
import com.example.letschat.CommonImage
import com.example.letschat.CommonProgressBar
import com.example.letschat.DestinationScreen
import com.example.letschat.LCViewModel
import com.example.letschat.navigateto


@Composable

fun ProfileScreen(navcontroller: NavController, vm: LCViewModel) {

val inprogress = vm.inprocess.value
    if(inprogress){
       CommonProgressBar()
    }
    else {
        val userData = vm.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name ?: "")
        }
        var number by rememberSaveable {
            mutableStateOf(userData?.number ?: "")
        }






        Column(modifier = Modifier.fillMaxWidth()) {
            ProfileContent(modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
                vm = vm,
                name = name,
                number = number,
                onNamechange = {name=it},
                onNumberchange = {number=it},
                onSave = {
                         vm.CreateOrUpdateProfile(name = name, number = number)
                },
                onBack = {
                         navigateto(navcontroller, route = DestinationScreen.Chatlist.route)
                },
                onLogout = {
                    vm.logout()
                    navigateto(navcontroller, route = DestinationScreen.Login.route)
                }
                )
            BottomNavigationMenu(selectedItem = BottomNavigationMenu.PROFILE, navController = navcontroller)
        }

    }


}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent( modifier: Modifier,
    vm:LCViewModel,
                   name:String,
                   number: String,
                    onNamechange:(String)->Unit,
                  onNumberchange:(String)->Unit,

                    onBack :()->Unit,
                   onSave :()->Unit,
                   onLogout:()->Unit,
                  ){
    Column {
        val imageUrl = vm.userData.value?.imageUrl
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(text = "Back", Modifier.clickable { onBack.invoke() })


            Text(text = "Save", Modifier.clickable { onSave.invoke() })

        }
        CommonDivider()
            ProfileImage(imageUrl = imageUrl,vm = vm )
         CommonDivider()
        }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "name", modifier = Modifier.width(100.dp))
                TextField(value = name, onValueChange = onNamechange,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    )
                )
 
            }



            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "number", modifier = Modifier.width(100.dp))
                TextField(value = number, onValueChange = onNumberchange,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    )
                )

            }

     CommonDivider()
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                horizontalArrangement = Arrangement.Center){
                Text(text = "Logout", modifier = Modifier.clickable { onLogout.invoke() })

            }

        }



@Composable
fun ProfileImage(imageUrl : String?,vm : LCViewModel){

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent())
    {
        uri ->
        uri?.let {
            vm.uploadProfileImage(uri)
        }
    }


Box(modifier = Modifier.height(intrinsicSize =
 IntrinsicSize.Min)){

        Column(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable {
                launcher.launch("image/*")
            }, horizontalAlignment = Alignment.CenterHorizontally) {
            Card (shape = CircleShape, modifier = Modifier
                .padding(10.dp)
                .size(200.dp)){
                CommonImage(data = imageUrl)

            }
            Text(text = "Change profile picture")

        }
    if(vm.inprocess.value){
        CommonProgressBar()

    }
    }

}