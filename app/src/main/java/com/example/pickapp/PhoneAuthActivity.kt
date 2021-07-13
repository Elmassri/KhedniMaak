package com.example.pickapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneAuthActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        mAuth = FirebaseAuth.getInstance()
        val phone : Button = findViewById(R.id.button4)
        val phoneNumberInput : EditText = findViewById(R.id.phone)
        phone.setOnClickListener{
            val phoneNumber = phoneNumberInput.text.toString()
            sendVerificationCode(phoneNumber)
        }

    }
    fun sendVerificationCode(phoneNumber: String){
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private val mCallback:PhoneAuthProvider.OnVerificationStateChangedCallbacks=
        object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                val role = intent.getStringExtra("role")

                val intent = Intent(this@PhoneAuthActivity,VerifyActivity::class.java)
                intent.putExtra("code",p0)

                startActivity(intent)

            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@PhoneAuthActivity,p0.message, Toast.LENGTH_SHORT).show()
            }
        }



    }
