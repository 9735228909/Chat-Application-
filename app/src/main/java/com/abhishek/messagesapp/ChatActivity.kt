package com.abhishek.messagesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.messagesapp.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messagerAdapter: MessageAdapter
    private lateinit var mdata:DatabaseReference

    var recevierRoom :String?= null
    var sendRoom:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val name = intent.getStringExtra("name")
        val recevierUid = intent.getStringExtra("uid")

        val senderUid= FirebaseAuth.getInstance().currentUser?.uid
        mdata = FirebaseDatabase.getInstance().getReference()

        sendRoom = recevierUid + senderUid
        recevierRoom = senderUid + recevierUid
        supportActionBar?.title=name

        messageList = ArrayList()
        messagerAdapter = MessageAdapter(this,messageList)
        chatRecyclerView = binding.chatrecyclerview
        chatRecyclerView.adapter = messagerAdapter


        mdata.child("chat").child(sendRoom!!).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children){
                        val message  = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messagerAdapter.notifyDataSetChanged()


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        binding.send.setOnClickListener {

            val message = binding.edtmessagebox.text.toString()
            val messageObject = Message(message,senderUid!!)

            mdata.child("chat").child(sendRoom!!).child("message").push().setValue(messageObject)
                .addOnSuccessListener {
                    mdata.child("chat").child(recevierRoom!!).child("message").push().setValue(messageObject)
                }
            binding.edtmessagebox.text.clear()
        }


    }
}