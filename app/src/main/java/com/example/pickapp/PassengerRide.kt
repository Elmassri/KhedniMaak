package com.example.pickapp

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pickapp.databinding.ActivityPassengerRideBinding
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

class PassengerRide : AppCompatActivity() {
    private lateinit var binding: ActivityPassengerRideBinding
    private  lateinit var database : DatabaseReference
    lateinit var placesClient: PlacesClient
    val AUTOCOMPLETE_REQUEST_CODE = 1
    val AUTOCOMPLETE_REQUEST_CODE_Destination = 2

    var placesFields = Arrays.asList(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS,

        )
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPassengerRideBinding.inflate(layoutInflater)

        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        // places api
        initPlaces()

        // Initialize the AutocompleteSupportFragment.
        binding.etPassengersource.setOnClickListener {



            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placesFields)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

        }
        binding.etPassengerDestination.setOnClickListener {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.


            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placesFields)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_Destination)
        }
        //timePicker onClick
        binding.btnPassengerSource.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{ view: TimePicker?, hourOfDay: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY,hourOfDay)
                cal.set(Calendar.MINUTE,minute)

                binding.btnPassengerSource.setText(SimpleDateFormat("HH:mm").format(cal.time))

            }
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),false).show()
        }

        binding.btnAddPassengerRide.setOnClickListener {
            saveDataToFirebase()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        val text =binding.etPassengersource
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
                        val text =binding.etPassengerDestination
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
        //SAVE DATA FOR FIREBASE+lOGIN
    private fun saveDataToFirebase() {
        if ((binding.etPassengerRideName.text.toString() == "") || (binding.etPassengerDestination.text.toString() == "") || (binding.etPassengersource.text.toString() == "") || (binding.btnPassengerSource.text.toString() ==""))
        {


            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }else{
        val name = binding.etPassengerRideName.text.toString()
        val sourceLocation = binding.etPassengersource.text.toString()
        val destinationLocation = binding.etPassengerDestination.text.toString()
        val sourceTime = binding.btnPassengerSource.text.toString()

        val phoneNumber = mAuth?.currentUser?.phoneNumber.toString()

        database = FirebaseDatabase.getInstance().getReference("Users/${phoneNumber}/Rides")
        val rides=Rides("passenger",name,sourceLocation,destinationLocation,sourceTime,"",phoneNumber)
        database.child(name).setValue(rides).addOnSuccessListener {
            Toast.makeText(this, "New ride was added", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@PassengerRide,MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, "failed to create user", Toast.LENGTH_SHORT).show()
        }
    }}
}