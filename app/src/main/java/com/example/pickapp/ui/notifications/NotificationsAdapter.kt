
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

class NotificationsAdapter(private val notifications: ArrayList<NotificationToAccept>) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {


    var mAuth: FirebaseAuth? = null
    private lateinit var dbref : DatabaseReference


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsAdapter.NotificationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.notification_list_request,parent
            ,false)

        return NotificationViewHolder(
            itemView


        )

    }


    override fun onBindViewHolder(holder: NotificationsAdapter.NotificationViewHolder, position: Int) {

        val currentItem = notifications[position]
        holder.name.text = currentItem.name
        if (currentItem.type == "Request"){
            holder.text.text = "Requested you to join your ride"
            Picasso.get().load(currentItem.imageUri).into(holder.image)
        }
        if (currentItem.type == "Invite"){
            holder.text.text = "Invited you to join his ride"
            Picasso.get().load(currentItem.imageUri).into(holder.image)
        }
        if (currentItem.type == "Accept"){
            holder.text.text = "Accepted your request"
            Picasso.get().load(currentItem.imageUri).into(holder.image)
            holder.acceptBtn.visibility = View.INVISIBLE
            holder.rejectBtn.visibility = View.INVISIBLE
        }
        if (currentItem.type == "Reject"){
            holder.text.text = "Rejected your request"
            Picasso.get().load(currentItem.imageUri).into(holder.image)
            holder.acceptBtn.visibility = View.INVISIBLE
            holder.rejectBtn.visibility = View.INVISIBLE
        }

        holder.acceptBtn.setOnClickListener {
            mAuth = FirebaseAuth.getInstance()
            dbref = FirebaseDatabase.getInstance().getReference("Users/${mAuth?.currentUser?.phoneNumber.toString()}")
            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").value.toString()
                    val imageUri = snapshot.child("imageUri").value.toString()
                    dbref = FirebaseDatabase.getInstance().getReference("Users/${currentItem.phoneNumber}/Notifications")


                    val notifications = NotificationToAccept(name,imageUri,currentItem.phoneNumber.toString(),"Accept")

                    val a = it
                    dbref.child(mAuth?.currentUser?.phoneNumber.toString()).setValue(notifications).addOnSuccessListener {

                        holder.rejectBtn.visibility = View.INVISIBLE
                        holder.acceptBtn.visibility = View.INVISIBLE
                        Toast.makeText(a.context, "This request has been accepted", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
        holder.rejectBtn.setOnClickListener {
            mAuth = FirebaseAuth.getInstance()
            dbref = FirebaseDatabase.getInstance().getReference("Users/${mAuth?.currentUser?.phoneNumber.toString()}")
            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").value.toString()
                    val imageUri = snapshot.child("imageUri").value.toString()
                    dbref = FirebaseDatabase.getInstance().getReference("Users/${currentItem.phoneNumber}/Notifications")


                    val notifications = NotificationToAccept(name,imageUri,currentItem.phoneNumber.toString(),"Reject")

                    val a = it

                    dbref.child(mAuth?.currentUser?.phoneNumber.toString()).setValue(notifications).addOnSuccessListener {
                        holder.rejectBtn.visibility = View.INVISIBLE
                        holder.acceptBtn.visibility = View.INVISIBLE
                        Toast.makeText(a.context, "This request has been rejected", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
    }




    override fun getItemCount(): Int {
        return notifications.size
    }
    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.textViewNameNotification)
        val text : TextView = itemView.findViewById(R.id.roleOfRideNotification)
        val acceptBtn : Button = itemView.findViewById(R.id.acceptBtn)
        val rejectBtn : Button = itemView.findViewById(R.id.rejecttBtn)
        val image : ImageView = itemView.findViewById(R.id.imageViewRecNotification)

    }
}