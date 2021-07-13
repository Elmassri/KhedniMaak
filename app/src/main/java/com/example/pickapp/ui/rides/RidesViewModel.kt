package com.example.pickapp.ui.rides

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RidesViewModel : ViewModel() {
    lateinit var mAuth : FirebaseAuth
    lateinit var database : DatabaseReference
    private val _text = MutableLiveData<String>().apply {
        value = "This is Rides Fragment"
    }


    private  val _name = MutableLiveData<String>().apply {
        mAuth = FirebaseAuth.getInstance()
        val phoneNumber = mAuth?.currentUser?.phoneNumber.toString()
        database = FirebaseDatabase.getInstance().getReference("Users/${phoneNumber}/Rides").child("gym")
        database.get().addOnSuccessListener {
            value = it.child("sourceLocation").value.toString()
        }
    }
    val text: LiveData<String> = _name
}