package com.example.pickapp.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pickapp.LandingScreen
import com.example.pickapp.databinding.ProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*

class ProfileFragment : Fragment() {

    var mAuth: FirebaseAuth? = null
    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: ProfileFragmentBinding? = null
    lateinit var database: DatabaseReference
    lateinit var imageUri: Uri
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val logout: Button = binding.exitBtn

        binding.etPhoneNumber.setText(mAuth?.currentUser?.phoneNumber.toString())
        val name: TextView = binding.etLastName

        profileViewModel.userName.observe(viewLifecycleOwner, Observer {


            name.text = it

        })
        imageUri= Uri.parse(profileViewModel.userImage.toString())
        print(imageUri.toString())

        profileViewModel.userImage.observe(viewLifecycleOwner, Observer {

            val url = it
            Picasso.get().load(url).into(binding.profileImage)

        })
        profileViewModel.userphoneNumber.observe(viewLifecycleOwner, Observer {


            binding.etPhoneNumber.setText(it)

        })
        binding.profileImage.setOnClickListener {
            selectPhoto()
        }
        binding.updateBtn.setOnClickListener {
            if(binding.etLastName.text.toString() == ""){
                print(imageUri.toString())

                Toast.makeText(activity, "Name could not be empty", Toast.LENGTH_SHORT).show()

            }else if (imageUri.toString() == profileViewModel.userImage.toString()){
                val phoneNumber = mAuth?.currentUser?.phoneNumber.toString()
                database = FirebaseDatabase.getInstance().getReference("Users")
                val user= mapOf<String,String>(
                    "name" to binding.etLastName.text.toString(),

                    

                )
                database.child(phoneNumber).updateChildren(user).addOnSuccessListener {


                }.addOnFailureListener{
                    Toast.makeText(activity, "failed to create user", Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(activity, "name changed", Toast.LENGTH_SHORT).show()

            }else{
                uploadimage()
                Toast.makeText(activity, "Profile updated", Toast.LENGTH_SHORT).show()
            }

        }


        logout.setOnClickListener {

            //val phoneNumber = mAuth?.currentUser?.phoneNumber.toString()
            //deleteUserFromDataBase(phoneNumber)
            mAuth!!.signOut()
            activity?.let {
                val intent = Intent(it, LandingScreen::class.java)
                it.startActivity(intent)
                it.finish()
            }
        }

        return root
    }

    private fun deleteUserFromDataBase(phoneNumber : String?) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child("${phoneNumber}").removeValue()
    }


    private fun uploadimage() {
        
        


        val filename = UUID.randomUUID().toString()
        val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")

        storageReference.putFile(imageUri).addOnSuccessListener {

            storageReference.downloadUrl.addOnSuccessListener {
                saveDataToFirebase(it.toString())
            }
        }


    }

    private fun saveDataToFirebase(photoUrl: String) {
        val name = binding.etLastName.text.toString()

        val phoneNumber = mAuth?.currentUser?.phoneNumber.toString()

        database = FirebaseDatabase.getInstance().getReference("Users")
        val user= mapOf<String,String>(
            "name" to binding.etLastName.text.toString(),
            "imageUri" to photoUrl


        )
        database.child(phoneNumber).updateChildren(user).addOnSuccessListener {


        }.addOnFailureListener{
            Toast.makeText(activity, "failed to create user", Toast.LENGTH_SHORT).show()
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

        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK){

            imageUri=data?.data!!
            binding.profileImage.setImageURI(imageUri)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}