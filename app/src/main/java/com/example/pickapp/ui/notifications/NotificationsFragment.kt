package com.example.pickapp.ui.notifications

import NotificationsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickapp.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NotificationsFragment : Fragment() {
    var mAuth: FirebaseAuth? = null
    private lateinit var dbref : DatabaseReference
    private lateinit var rideRecyclerView: RecyclerView
    private lateinit var notificationsClass : ArrayList<NotificationToAccept>
    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        rideRecyclerView= binding.recyclerRideViewForNotificationList as RecyclerView

        rideRecyclerView.layoutManager = LinearLayoutManager(activity)

        rideRecyclerView.setHasFixedSize(true)

        notificationsClass = arrayListOf<NotificationToAccept>()

        getNotifications()

        return root

    }
    private fun getNotifications() {
        mAuth = FirebaseAuth.getInstance()
        dbref = FirebaseDatabase.getInstance().getReference("Users/${mAuth?.currentUser?.phoneNumber.toString()}/Notifications")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                notificationsClass.clear()
                if(snapshot.exists()){


                        for (details in snapshot.children) {

                            val phoneNumber = details.child("phoneNumber").value.toString()
                            val type = details.child("type").value.toString()
                            val name = details.child("name").value.toString()
                            val imageuri = details.child("imageUri").value.toString()

                            notificationsClass.add(
                                NotificationToAccept(
                                    name,
                                    imageuri,
                                    phoneNumber,
                                    type
                                )
                            )




                    }


                    rideRecyclerView.adapter = NotificationsAdapter(notificationsClass)



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