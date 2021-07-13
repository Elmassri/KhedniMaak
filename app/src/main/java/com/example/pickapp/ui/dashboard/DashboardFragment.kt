package com.example.pickapp.ui.dashboard

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickapp.R
import com.example.pickapp.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class DashboardFragment : Fragment() {
    var mAuth: FirebaseAuth? = null
    private lateinit var dbref : DatabaseReference
    private lateinit var rideRecyclerView: RecyclerView
    private lateinit var rideClass : ArrayList<RidesData>
    private lateinit var allRides :ArrayList<AllRides>
    private lateinit var tempArrayList : ArrayList<AllRides>
    private lateinit var dashboardViewModel: DashboardViewModel

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mAuth = FirebaseAuth.getInstance()
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rideRecyclerView= binding.recyclerRideView as RecyclerView

        rideRecyclerView.layoutManager = LinearLayoutManager(activity)

        rideRecyclerView.setHasFixedSize(true)

        rideClass = arrayListOf<RidesData>()
        allRides = arrayListOf<AllRides>()
        tempArrayList= arrayListOf<AllRides>()

        tempArrayList.addAll(allRides)
        getRides()



        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar_item,menu)
        val item = menu?.findItem(R.id.itemAppBarSearch)
        val searchView = item?.actionView as SearchView


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                rideRecyclerView.adapter = DashboardAdapter(tempArrayList)

                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    allRides.forEach{
                        if (it.sourceLocation?.toLowerCase(Locale.getDefault())!!.contains(searchText)){

                            tempArrayList.add(it)
                        }
                    }
                    rideRecyclerView.adapter = DashboardAdapter(tempArrayList)
                }else{
                    tempArrayList.clear()
                    tempArrayList.addAll(allRides)
                    rideRecyclerView.adapter = DashboardAdapter(allRides)
                }


                return false
            }

        })
        return super.onPrepareOptionsMenu(menu)

    }






    private fun getRides() {
       dbref = FirebaseDatabase.getInstance().getReference("Users")
       dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                allRides.clear()

                if(snapshot.exists()){

                    for(rideSnapshot in snapshot.children){

                        for (details in rideSnapshot.child("Rides").children){
                            val phoneNumber = details.child("phoneNumber").value.toString()
                            val role = details.child("role").value.toString()
                            val name = rideSnapshot.child("name").value.toString()
                            val imageuri = rideSnapshot.child("imageUri").value.toString()
                            val sourceLocation = details.child("sourceLocation").value.toString()
                            val destLocation = details.child("destinationLocation").value.toString()
                            val sourceTime = details.child("sourceTime").value.toString()
                            val passenger = details.child("passenger").value.toString()
                            allRides.add(AllRides(role,name,passenger,sourceLocation,destLocation,sourceTime,imageuri,phoneNumber ))

                        }





                    }
                    rideRecyclerView.adapter = DashboardAdapter(allRides)



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


