package com.example.pickapp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AddRide : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ride)

        val iAmPassenger : Button = findViewById(R.id.passengerBtn)
        val iAmDriver : Button = findViewById(R.id.driverBtn)

        iAmDriver.setOnClickListener {
            val intent = Intent(this@AddRide,DriverRide::class.java)
            startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }
        iAmPassenger.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this,R.anim.fade_in)
            val intent = Intent(this@AddRide,PassengerRide::class.java)
            startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

            finish()
        }
    }
}