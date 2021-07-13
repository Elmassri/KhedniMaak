package com.example.pickapp

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pickapp.databinding.ActivityDriverRideBinding
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class DriverRide : AppCompatActivity() {
    private  lateinit var binding: ActivityDriverRideBinding
    private  lateinit var database : DatabaseReference
    lateinit var placesClient: PlacesClient
    val AUTOCOMPLETE_REQUEST_CODE = 1
    val AUTOCOMPLETE_REQUEST_CODE_Destination = 2

    var placesFields = Arrays.asList(Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS,


        )
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDriverRideBinding.inflate(layoutInflater)

        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        // places api
        initPlaces()

        // Initialize the AutocompleteSupportFragment.
        binding.etSource.setOnClickListener {


            // Set the fields to specify which types of place data to
            // return after the user has made a selection.


            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placesFields)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

        }
        binding.etDestination.setOnClickListener {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.


            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placesFields)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_Destination)
        }
            //timePicker onClick
        binding.btnSource.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{ view: TimePicker?, hourOfDay: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY,hourOfDay)
                cal.set(Calendar.MINUTE,minute)

                binding.btnSource.setText(SimpleDateFormat("HH:mm").format(cal.time))

            }
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),false).show()
        }


        binding.DriverBtnRide.setOnClickListener {
            saveDataToFirebase()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        val text =binding.etSource
                        text.setText(place.address.toString())

                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)

                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_Destination) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        val text =binding.etDestination
                        text.setText(place.address.toString())

                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)

                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initPlaces() {
        Places.initialize(this,getString(R.string.places_api))
        placesClient = Places.createClient(this)
    }

    private fun saveDataToFirebase() {
        if ((binding.etDestination.text == null) || (binding.etRideName.text == null) || (binding.etSource.text == null) || (binding.btnSource.text == null )|| (binding.etPassengers.text == null)){


            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }else{
        val name = binding.etRideName.text.toString()
        val sourceLocation = binding.etSource.text.toString()
        val destinationLocation = binding.etDestination.text.toString()
        val numberOfPassenger = binding.etPassengers.text.toString()

        val sourceTime = binding.btnSource.text.toString()

        val phoneNumber = mAuth?.currentUser?.phoneNumber.toString()

        database = FirebaseDatabase.getInstance().getReference("Users/${phoneNumber}/Rides")
        val rides=Rides("driver",name,sourceLocation,destinationLocation,sourceTime,numberOfPassenger,phoneNumber)
        database.child(name).setValue(rides).addOnSuccessListener {
            Toast.makeText(this, "New ride was added", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@DriverRide,MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, "failed to create user", Toast.LENGTH_SHORT).show()
        }
    }}

}