package com.example.letschat


import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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

    init {

    }

    var inprocess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var Signin = mutableStateOf(false)
    var userData = mutableStateOf<UserData?>(null)
    fun Signup(name:String ,number: String,email:String ,passward:String ){
        inprocess.value = true
  auth.createUserWithEmailAndPassword(email,passward).addOnCompleteListener {
      if(it.isSuccessful){
          Signin.value= true
          CreateOrUpdateProfile(name,number)

      }else{
           handelexception(it.exception, customMessage = "SignUpFailled")
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


