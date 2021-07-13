package com.example.pickapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LandingScreen : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_screen)

        mAuth = FirebaseAuth.getInstance()
        val continueBtn: Button = findViewById(R.id.landingContinue)

        continueBtn.setOnClickListener {
            val intent = Intent(this@LandingScreen, PhoneAuthActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
    // SATRT INTENT DIRECTLY IF THE USER WAS SIGNEDin
    override fun onStart() {
        super.onStart()
        if (mAuth?.currentUser != null) {
            val intent = Intent(this@LandingScreen, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }
}
