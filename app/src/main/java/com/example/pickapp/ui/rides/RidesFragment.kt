package com.example.pickapp.ui.rides

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickapp.databinding.RidesFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RidesFragment : Fragment() {

    private lateinit var ridesViewModel: RidesViewModel
    private var _binding: RidesFragmentBinding? = null
    lateinit var mRecyclerView : RecyclerView
    lateinit var mDatabase: DatabaseReference
    lateinit var mAuth : FirebaseAuth

    private lateinit var myrideClass : ArrayList<MyRidesData>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ridesViewModel =
            ViewModelProvider(this).get(RidesViewModel::class.java)

        _binding = RidesFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //display data
        mRecyclerView= binding.listview as RecyclerView

        mRecyclerView.layoutManager = LinearLayoutManager(activity)

        mRecyclerView.setHasFixedSize(true)

        myrideClass= arrayListOf<MyRidesData>()
        getMyRides()




        return root
    }

    private fun getMyRides() {
        mAuth = FirebaseAuth.getInstance()
        val phoneNumber = mAuth?.currentUser?.phoneNumber.toString()
        mDatabase = FirebaseDatabase.getInstance().getReference("Users/${phoneNumber}/Rides")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myrideClass.clear()
                if(snapshot.exists()){
                    for(rideSnapshot in snapshot.children){
                        val role = rideSnapshot.child("role").value.toString()
                        val name = rideSnapshot.child("name").value.toString()
                        val sourceLocation =rideSnapshot.child("sourceLocation").value.toString()
                        val destLocation = rideSnapshot.child("destinationLocation").value.toString()
                        val sourceTime = rideSnapshot.child("sourceTime").value.toString()
                        val passenger = rideSnapshot.child("passenger").value.toString()
                        myrideClass.add(
                            MyRidesData(role,name,passenger,sourceLocation,destLocation,sourceTime))
                    }

                    mRecyclerView.adapter = MyRidesAdapter(myrideClass)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

