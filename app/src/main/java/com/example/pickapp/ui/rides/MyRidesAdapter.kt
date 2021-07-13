package com.example.pickapp.ui.rides

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pickapp.MainActivity
import com.example.pickapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyRidesAdapter(private val myRidesdata:ArrayList<MyRidesData>):
    RecyclerView.Adapter<MyRidesAdapter.MyRidesViewHolder>() {

    var mAuth: FirebaseAuth? = null
    private lateinit var dbref : DatabaseReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRidesAdapter.MyRidesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.my_ride_list,parent
            ,false)

        return MyRidesViewHolder(itemView)

    }


    override fun onBindViewHolder(holder: MyRidesViewHolder, position: Int) {
        val currentItem = myRidesdata[position]
        holder.nameView.text = currentItem.name
        holder.roleView.text =currentItem.role
        holder.destination.text = currentItem.destinationLocation
        holder.source.text = currentItem.sourceLocation


        if (currentItem.role == "passenger"){
            holder.passengerText.text = ""
            holder.titleColor.setBackgroundColor(Color.parseColor("#FF846B"))

        }
        holder.passenger.text = currentItem.passenger
        holder.startTime.text = currentItem.sourceTime

        holder.dltBtn.setOnClickListener {
            mAuth = FirebaseAuth.getInstance()
            dbref = FirebaseDatabase.getInstance().getReference("Users/${mAuth?.currentUser?.phoneNumber.toString()}/Rides")
            dbref.child(currentItem.name.toString()).removeValue()
            Toast.makeText(it.context, "Ride Deleted", Toast.LENGTH_SHORT).show()
            val intent = Intent(it.context, MainActivity::class.java)
            it.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return myRidesdata.size
    }


    class MyRidesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val nameView : TextView = itemView.findViewById(R.id.textViewNameofRide)
        val roleView : TextView = itemView.findViewById(R.id.rideView)
        val passenger : TextView = itemView.findViewById(R.id.passengerNumberRide)
        val source : TextView = itemView.findViewById(R.id.srcLocationRide)
        val destination :TextView = itemView.findViewById(R.id.destLocationRide)
        val startTime : TextView = itemView.findViewById(R.id.startTimeRide)
        val passengerText : TextView = itemView.findViewById(R.id.passengerText)
        val titleColor   : LinearLayout = itemView.findViewById(R.id.titelBackground)
        val dltBtn : Button = itemView.findViewById(R.id.dltBtn)
    }
}
