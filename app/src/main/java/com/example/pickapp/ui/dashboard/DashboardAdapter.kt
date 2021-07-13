package com.example.pickapp.ui.dashboard

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pickapp.R
import com.example.pickapp.ui.notifications.NotificationToAccept
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.net.URLEncoder

class DashboardAdapter(private val rideList: ArrayList<AllRides>) : RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {


    var mAuth: FirebaseAuth? = null
    private lateinit var dbref : DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_layout,parent
            ,false)

        return DashboardViewHolder(
            itemView



        )

    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {

        val currentItem = rideList[position]
        holder.nameView.text = currentItem.name
        Picasso.get().load(currentItem.imageUri).into(holder.imageUri)
        holder.role.text = currentItem.role
        holder.srcTime.text = currentItem.sourceTime
        holder.srceLocationAdapter.text = currentItem.sourceLocation
        holder.destLocationAdapter.text = currentItem.destinationLocation
        holder.passengerNumber.text = currentItem.passenger


        holder.whatsappBtn.setOnClickListener{
           try {






               val phoneNumber = currentItem.phoneNumber.toString()
               val intent = Intent()
               var packageManager = it.context.packageManager
               intent.setAction(Intent.ACTION_VIEW)


               val url = "https://api.whatsapp.com/send?phone=${phoneNumber}&text=" + URLEncoder.encode("")


               intent.setPackage("com.whatsapp")
               intent.data = Uri.parse(url)
               if (intent.resolveActivity(packageManager) != null){
                   it.context.startActivity(intent)
               }else{
                   Toast.makeText(it.context, "Please install WhatsApp", Toast.LENGTH_SHORT).show()
               }
               it.context.startActivity(intent)
           }catch (e : Exception){
               Toast.makeText(it.context, "" + e.stackTrace, Toast.LENGTH_SHORT).show()
           }

        }

        if (currentItem.role =="passenger"){

            holder.passengerText.text = ""
            holder.role.setTextColor(Color.parseColor("#FF846B"))
            holder.requestBtn.setText("Invite")

        }



        holder.requestBtn.setOnClickListener {
            if(holder.requestBtn.text.toString() == "Invite"){
                mAuth = FirebaseAuth.getInstance()
                dbref = FirebaseDatabase.getInstance().getReference("Users/${mAuth?.currentUser?.phoneNumber.toString()}")
                dbref.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val name = snapshot.child("name").value.toString()
                        val imageUri = snapshot.child("imageUri").value.toString()
                        dbref = FirebaseDatabase.getInstance().getReference("Users/${currentItem.phoneNumber}/Notifications")


                        val notifications = NotificationToAccept(name,imageUri,currentItem.phoneNumber.toString(),"Invite")

                        val a = it
                        dbref.child(mAuth?.currentUser?.phoneNumber.toString()).setValue(notifications).addOnSuccessListener {
                            Toast.makeText(a.context, "Invitation sent", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            }else{
                mAuth = FirebaseAuth.getInstance()
                dbref = FirebaseDatabase.getInstance().getReference("Users/${mAuth?.currentUser?.phoneNumber.toString()}")
                dbref.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val name = snapshot.child("name").value.toString()
                        val imageUri = snapshot.child("imageUri").value.toString()
                        dbref = FirebaseDatabase.getInstance().getReference("Users/${currentItem.phoneNumber}/Notifications")


                        val notifications = NotificationToAccept(name,imageUri,currentItem.phoneNumber.toString(),"Request")

                        val a = it
                        dbref.child(mAuth?.currentUser?.phoneNumber.toString()).setValue(notifications).addOnSuccessListener {
                            Toast.makeText(a.context, "Request sent", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })


            }

        }
    }




    override fun getItemCount(): Int {
        return rideList.size
    }
    class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val imageUri :ImageView = itemView.findViewById(R.id.imageViewRec)
            val nameView : TextView = itemView.findViewById(R.id.textViewNameRec)
            val srceLocationAdapter : TextView = itemView.findViewById(R.id.srcAdapterLocationRide)
            val destLocationAdapter : TextView = itemView.findViewById(R.id.destAdapterLocationRide)
            val srcTime : TextView = itemView.findViewById(R.id.startAdapterTimeRide)
            val passengerNumber : TextView = itemView.findViewById(R.id.passengerAdapterNumberRide)
            val passengerText : TextView = itemView.findViewById(R.id.passengerAdaText)
            val role : TextView = itemView.findViewById(R.id.roleOfRide)
            val whatsappBtn : Button = itemView.findViewById(R.id.whatsappBtn)
            val requestBtn : Button = itemView.findViewById(R.id.requestBtn)



    }
}


