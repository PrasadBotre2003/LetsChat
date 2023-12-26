package com.example.letschat


import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.letschat.data.CHATS
import com.example.letschat.data.ChatData
import com.example.letschat.data.ChatUser
import com.example.letschat.data.Event
import com.example.letschat.data.USER_NODE
import com.example.letschat.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects

import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(

    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {

    var inProcessChats = mutableStateOf(false)
    var inprocess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var Signin = mutableStateOf(false)
    var userData = mutableStateOf<UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(listOf())

    init {
        val currentUser = auth.currentUser
        Signin.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

fun populateChats(){
inProcessChats.value = false
    db.collection(CHATS).where(
        Filter.or(

            Filter.equalTo("user1.userId",userData.value?.userId),
            Filter.equalTo("user2.userId",userData.value?.userId),

        )
    ).addSnapshotListener {

            value, error ->

        if (error != null) {
            handelexception(error, "error")

        }

        if (value != null) {


            chats.value = value.documents.mapNotNull {
                it.toObject<ChatData>()
            }
            inProcessChats.value = false
        }




    }


}


    fun Signup(name: String, number: String, email: String, passward: String) {
        inprocess.value = true
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or passward.isEmpty()) {
            handelexception(customMessage = "fill all fillds")
            return
        }
        inprocess.value = true
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email, passward).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Signin.value = true
                        CreateOrUpdateProfile(name, number)

                    } else {
                        handelexception(it.exception, customMessage = "SignUpFailled")
                    }
                }
            } else {
                handelexception(customMessage = "user already exist")
                inprocess.value = false
            }
        }


    }

    fun logIn(email: String, passward: String) {

        if (email.isEmpty() or passward.isEmpty()) {
            handelexception(customMessage = "enter all fillds")
            return
        } else {
            inprocess.value = true
            auth.signInWithEmailAndPassword(email, passward)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Signin.value = true
                        inprocess.value = false
                        auth.currentUser?.uid?.let {
                            getUserData(it)
                        }

                    } else {

                        handelexception(it.exception, customMessage = "login failled")
                    }
                }
        }
    }


    fun uploadProfileImage(uri: Uri) {
        uploadimage(uri) {
            CreateOrUpdateProfile(imageurl = it.toString())

        }

    }

    fun uploadimage(uri: Uri, onSuccess: (Uri) -> Unit) {
        inprocess.value = true

        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl

            result?.addOnSuccessListener(onSuccess)
            inprocess.value = false
        }
            .addOnFailureListener {
                handelexception(it, " ")
            }
    }

    fun CreateOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageurl: String? = null
    ) {
        var uid = auth.currentUser?.uid
        val userdata = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            number = number ?: userData.value?.number,
            imageUrl = imageurl ?: userData.value?.imageUrl


        )
        uid?.let {
            inprocess.value = true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener {

                if (it.exists()) {

                } else {
                    db.collection(USER_NODE).document(uid).set(userData)
                    inprocess.value = false
                    getUserData(uid)
                }

            }.addOnFailureListener {
                handelexception(it, "Cannot retrive user")
            }

        }
    }

    private fun getUserData(uid: String) {
        inprocess.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener {
                                                                   value, error ->

            if (value != null) {
                handelexception(error, "cannot retrive user")
            }
            if (value != null) {
                var user = value.toObject<UserData>()
                userData.value = user
                inprocess.value = false
              populateChats()
            }
        }

    }


    fun handelexception(exception: Exception? = null, customMessage: String) {
        Log.e("LiveChatApp", "live chat exception", exception)
        exception?.printStackTrace()
        val errormsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errormsg else customMessage
        eventMutableState.value = Event(message)
        inprocess.value = false

    }

    fun logout() {
        auth.signOut()
        Signin.value = false
        userData.value = null
        eventMutableState.value = Event("Logged Out")
    }

    //N or n
    fun onAddChat(number: String) {
        if (number.isEmpty() or !number.isDigitsOnly()) {
            handelexception(customMessage = "number contain  digit only")

        } else {
            db.collection(CHATS).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.number", number),
                        Filter.equalTo("user2.number", userData.value?.number)


                    ),
                    Filter.and(
                        Filter.equalTo("user1.number", userData.value?.number),
                        Filter.equalTo("user2.number", number)

                    )
                )


            ).get().addOnSuccessListener {
                if (it.isEmpty) {
                    db.collection(USER_NODE).whereEqualTo("number", number).get()
                        .addOnSuccessListener {
                            if (it.isEmpty) {
                                handelexception(customMessage = "number not found")
                            } else {
                                val chatPartner = it.toObjects<UserData>()[0]

                                val id = db.collection(CHATS).document().id

                                val chat = ChatData(

                                    chatId = id,
                                    ChatUser(
                                        userData.value?.userId,
                                        userData.value?.name,
                                        userData.value?.imageUrl,
                                        userData.value?.number
                                    ),
                                    ChatUser(
                                        chatPartner.userId,
                                        chatPartner.name, chatPartner.imageUrl,
                                        chatPartner.number
                                    )

                                )
                                db.collection(CHATS).document(id).set(chat)

                            }


                        }
                        .addOnFailureListener {
                    handelexception(it,"process failed")

                }

                }
                else{
                    handelexception(customMessage = "chat already exist")
                }
            }
        }


    }

}