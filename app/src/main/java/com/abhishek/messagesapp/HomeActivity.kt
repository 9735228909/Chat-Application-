package com.abhishek.messagesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.messagesapp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userlist:ArrayList<User>
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var mdata:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth=FirebaseAuth.getInstance()
        mdata = FirebaseDatabase.getInstance().getReference()

        userlist = ArrayList()
        adapter = UserAdapter(this,userlist)

        userRecyclerView  = binding.recyclerview
        userRecyclerView.adapter = adapter

        mdata.child("User").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userlist.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser= postSnapshot.getValue(User::class.java)

                    if (auth.currentUser?.uid != currentUser?.uid){
                        userlist.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menubar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.camera->{
                Toast.makeText(this, "Camera", Toast.LENGTH_LONG).show()
                true
            }
            R.id.logout->{
                auth.signOut()
                val intent = Intent(this,LogActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else->super.onOptionsItemSelected(item)
        }
    }

}