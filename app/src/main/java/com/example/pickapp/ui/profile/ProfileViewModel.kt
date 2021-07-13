package com.example.pickapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileViewModel : ViewModel() {
    var mAuth: FirebaseAuth? = null
    var database: DatabaseReference






    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"


    }
    private  val _name = MutableLiveData<String>().apply {
       mAuth = FirebaseAuth.getInstance()
        val phoneNumber =mAuth?.currentUser?.phoneNumber.toString()
        database = FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber)
        database.get().addOnSuccessListener {
           value = it.child("name").value.toString()
        }



    }
    private  val _phonenumber = MutableLiveData<String>().apply {

        mAuth = FirebaseAuth.getInstance()
        val phoneNumber =mAuth?.currentUser?.phoneNumber.toString()
        value = phoneNumber
    }
    private  val _imageView = MutableLiveData<String>().apply {

        mAuth = FirebaseAuth.getInstance()
        val phoneNumber =mAuth?.currentUser?.phoneNumber.toString()
        database = FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber)
        database.get().addOnSuccessListener {
            value =  it.child("imageUri").value.toString()
        }
    }
    private  val _age = MutableLiveData<String>().apply {

        mAuth = FirebaseAuth.getInstance()
        val phoneNumber =mAuth?.currentUser?.phoneNumber.toString()
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(phoneNumber).get().addOnSuccessListener {
            value =  it.child("age").value.toString()
        }
    }


    val text: LiveData<String> = _text
    val userName : LiveData<String> = _name

    val userImage : LiveData<String> = _imageView
    val userphoneNumber : LiveData<String> = _phonenumber

}
