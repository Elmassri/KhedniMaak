package com.example.pickapp

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.pickapp.databinding.ActivityRegisterScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


@Suppress("DEPRECATION")
class RegisterScreenActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityRegisterScreenBinding
    private  lateinit var database : DatabaseReference
    lateinit var imageUri: Uri
    var mAuth: FirebaseAuth? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()


        // sending data to realtime database
        binding.startApp.setOnClickListener {

            if((binding.regName.text.toString() == "")||(binding.lastname.text.toString() == "")){


                Toast.makeText(this, "Please fill your first and last name", Toast.LENGTH_SHORT).show()

            }else{

                uploadimage()
            }





        }
        //show image
        binding.regPhoto.setOnClickListener {
            selectPhoto()
        }

    }



    private fun uploadimage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Profile confirming")
        progressDialog.setCancelable(false)
        progressDialog.show()


        val filename = UUID.randomUUID().toString()
        val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")

        storageReference.putFile(imageUri).addOnSuccessListener {

            storageReference.downloadUrl.addOnSuccessListener {
                saveDataToFirebase(it.toString())

            }
        }


    }

    private fun saveDataToFirebase(photoUrl: String) {
        val name = binding.regName.text.toString()+ " "+binding.lastname.text.toString()

        val phoneNumber = mAuth?.currentUser?.phoneNumber.toString()

        database = FirebaseDatabase.getInstance().getReference("Users")
        val User=Users(name,phoneNumber,photoUrl)
        database.child(phoneNumber).setValue(User).addOnSuccessListener {

            val intent = Intent(this@RegisterScreenActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, "failed to create user", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectPhoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){

            imageUri=data?.data!!
            binding.regPhoto.setImageURI(imageUri)

        }
    }


}