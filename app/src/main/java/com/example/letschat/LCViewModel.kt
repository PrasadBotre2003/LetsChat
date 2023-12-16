package com.example.letschat


import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.letschat.data.Event
import com.example.letschat.data.USER_NODE
import com.example.letschat.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.text.MessageFormat
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class LCViewModel @Inject constructor(

    val auth :FirebaseAuth,
    val  db : FirebaseFirestore
) :ViewModel() {

    var inprocess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var Signin = mutableStateOf(false)
    var userData = mutableStateOf<UserData?>(null)

    init {
val currentUser = auth.currentUser
       Signin.value  = currentUser !=null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun Signup(name:String ,number: String,email:String ,passward:String ){
        inprocess.value = true
  if(name.isEmpty() or number.isEmpty() or email.isEmpty() or passward.isEmpty()){
      handelexception(customMessage = "fill all fillds")
      return
  }
        inprocess.value = true
        db.collection(USER_NODE).whereEqualTo("number",number).get().addOnSuccessListener {
            if(it.isEmpty){
                auth.createUserWithEmailAndPassword(email,passward).addOnCompleteListener {
                    if(it.isSuccessful){
                        Signin.value= true
                        CreateOrUpdateProfile(name,number)

                    }else{
                        handelexception(it.exception, customMessage = "SignUpFailled")
                    }
                }
            }else{
                handelexception(customMessage = "user already exist")
                inprocess.value = false
            }
        }



    }

    fun logIn(email: String,passward: String){

        if(email.isEmpty() or passward.isEmpty()){
            handelexception(customMessage = "enter all fillds")
            return
        }
        else{
            inprocess.value = true
            auth.signInWithEmailAndPassword(email,passward)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Signin.value = true
                        inprocess.value = false
                        auth.currentUser?.uid?.let {
                            getUserData(it)
                        }

                    }
                    else{

                       /* val text = "Hello toast!"
                        val duration = Toast.LENGTH_SHORT

                        val toast = Toast.makeText("", text, duration) // in Activity
                        toast.show()*/
                        handelexception(it.exception, customMessage = "login failled")
                    }
                }
        }
    }

   fun CreateOrUpdateProfile(name:String ?= null,number:String ?= null,imageurl: String?=null){
  var uid = auth.currentUser?.uid
       val userdata  = UserData(
           userId =  uid,
           name =name?:userData.value?.name,
           number = number?:userData.value?.number,
           imageUrl = imageurl?:userData.value?.imageUrl



       )
       uid?.let {
    inprocess.value = true
           db.collection(USER_NODE).document(uid).get().addOnSuccessListener {

      if (it.exists()){
          //update user data
      }
else{
          db.collection(USER_NODE).document(uid).set(userData)
          inprocess.value = false
          getUserData(uid);
      }

           }.addOnFailureListener{
               handelexception(it,"Cannot retrive user")
           }

       }
    }

    private fun getUserData(uid: String) {
inprocess.value = true
db.collection(USER_NODE).document(uid).addSnapshotListener{
    value, error->

    if(value != null){
        handelexception(error,"cannot retrive user")
    }
    if(value != null){
var user = value.toObject<UserData>()
        userData.value = user
        inprocess.value = false
    }
}

    }


    fun handelexception(exception: Exception? =null,customMessage: String){
        Log.e("LiveChatApp","live chat exception",exception)
        exception?.printStackTrace()
        val errormsg = exception?.localizedMessage?:""
        val message = if(customMessage.isNullOrEmpty()) errormsg else customMessage
        eventMutableState.value =Event(message)
        inprocess.value = false

    }
}


