package com.example.pickapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DashboardViewModel : ViewModel() {
    var mAuth: FirebaseAuth? = null
    var database: DatabaseReference
    private  val _name = MutableLiveData<String>().apply {
        mAuth = FirebaseAuth.getInstance()
        val phoneNumber = mAuth?.currentUser?.phoneNumber.toString()
        database = FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber)
        database.get().addOnSuccessListener {
            value = it.child("name").value.toString()
        }

    }
    val name: LiveData<String> = _name
}