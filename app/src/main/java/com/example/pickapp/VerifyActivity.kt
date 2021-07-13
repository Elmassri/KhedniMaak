package com.example.pickapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerifyActivity : AppCompatActivity() {
    var mAuth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        mAuth= FirebaseAuth.getInstance()

        val code = intent.getStringExtra("code")

        val codeInput : EditText = findViewById(R.id.codeInput)
        val verifyNumber : Button = findViewById(R.id.verifyBtn)
        verifyNumber.setOnClickListener {
            verifyCode(code.toString(),codeInput.text.toString())
        }
    }
    private fun verifyCode(authCode:String,entredCode:String){
        val credential = PhoneAuthProvider.getCredential(authCode,entredCode)
        signInWithCredentials(credential)

    }
    private  fun signInWithCredentials(credentials: PhoneAuthCredential){

        mAuth!!.signInWithCredential(credentials)
            .addOnCompleteListener {
                if(it.isSuccessful){

                    //send code progress
                    val intent = Intent(this@VerifyActivity,RegisterScreenActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()



                }

            }

    }
    }
