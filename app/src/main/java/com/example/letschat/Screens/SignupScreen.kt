package com.example.letschat.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.letschat.CheckSignedIn

import com.example.letschat.CommonProgress
import com.example.letschat.DestinationScreen
import com.example.letschat.LCViewModel
import com.example.letschat.R
import com.example.letschat.navigateto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navcontroller:NavController,vm:LCViewModel) {

    CheckSignedIn(vm = vm, navcontroller)

    //navcontroller = Navcontroller ..error
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val namestate = remember {
                mutableStateOf(TextFieldValue())
            }
            val Numbernamestate = remember {
                mutableStateOf(TextFieldValue())
            }


            val emailnamestate = remember {
                mutableStateOf(TextFieldValue())
            }


            val passnamestate = remember {
                mutableStateOf(TextFieldValue())
            }
            val Focus = LocalFocusManager.current


            Image(
                painter = painterResource(id = R.drawable.chat), contentDescription = null,
                modifier = Modifier
                    .width(160.dp)
                    .padding(top = 1.dp)
                    .padding(10.dp)

            )

            Text(
                text = "Sign Up",
                fontSize = 30.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.W400,
                modifier = Modifier.padding(bottom = 30.dp),


                )
            OutlinedTextField(
                value = namestate.value, onValueChange = {

                    namestate.value = it
                },
                label = { Text(text = "Name") },
                modifier = Modifier.padding(5.dp)
            )


            OutlinedTextField(
                value = Numbernamestate.value, onValueChange = {

                    Numbernamestate.value = it
                },
                label = { Text(text = "Number") },
                modifier = Modifier.padding(5.dp)
            )


            OutlinedTextField(
                value = emailnamestate.value, onValueChange = {

                    emailnamestate.value = it
                },
                label = { Text(text = "Email") },
                modifier = Modifier.padding(5.dp)
            )



            OutlinedTextField(
                value = passnamestate.value, onValueChange = {

                    passnamestate.value = it
                },
                label = { Text(text = "Passward") },
                modifier = Modifier.padding(10.dp)
            )

            Button(
                onClick = {
                    vm.Signup(
                        name = namestate.value.text,
                       number =  Numbernamestate.value.text,
                       email =  emailnamestate.value.text,
                      passward =  passnamestate.value.text,
                    )
                },
                modifier = Modifier.padding(5.dp),

                ) {
                Text(text = "Sign Up")


            }
            Text(text = "Already  a User ? Go to Login--->",
                color = Color.LightGray,
                modifier = Modifier
                    .padding(30.dp)
                    .clickable {
                        navigateto(navcontroller, DestinationScreen.Login.route)
                    }
            )
        }
    }

    if (vm.inprocess.value) {
        CommonProgress()
    }
}
